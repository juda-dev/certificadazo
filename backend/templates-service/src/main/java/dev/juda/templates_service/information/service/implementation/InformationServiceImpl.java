package dev.juda.templates_service.information.service.implementation;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClient;

import dev.juda.templates_service.information.persistence.entity.Information;
import dev.juda.templates_service.information.persistence.repository.InformationRepository;
import dev.juda.templates_service.information.presentation.dto.in.InformationAiResponse;
import dev.juda.templates_service.information.presentation.dto.in.UserFullNameView;
import dev.juda.templates_service.information.presentation.dto.out.NameAndFieldsTemplate;
import dev.juda.templates_service.information.presentation.dto.request.ReadInformationRequest;
import dev.juda.templates_service.information.presentation.dto.response.InformationResponse;
import dev.juda.templates_service.information.presentation.dto.response.ReadInformationResponse;
import dev.juda.templates_service.information.service.exception.InconsistentFieldsException;
import dev.juda.templates_service.information.service.exception.InformationNotFoundException;
import dev.juda.templates_service.information.service.interfaces.InformationService;
import dev.juda.templates_service.template.persistence.repository.TemplateRepository;
import dev.juda.templates_service.template.service.exception.TemplateNotFoundException;

@Service
public class InformationServiceImpl implements InformationService {

    private final InformationRepository informationRepository;
    private final TemplateRepository templateRepository;
    private final RestClient restClient;

    public InformationServiceImpl(InformationRepository informationRepository,
            @Qualifier("usersRestClient") RestClient restClient, TemplateRepository templateRepository) {
        this.informationRepository = informationRepository;
        this.templateRepository = templateRepository;
        this.restClient = restClient;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.CREATED)
    public Set<InformationResponse> create(Set<InformationAiResponse> req) {
        Set<InformationResponse> response = new HashSet<>();
        NameAndFieldsTemplate nameAndFieldsTemplate = templateRepository
                .findNameAndFieldsById(req.iterator().next().templateId())
                .orElseThrow(TemplateNotFoundException::new);
        req.forEach(r -> {
            String userFullName = fetchUserFullName(r.userId()).fullName();

            response.add(persistInformation(null, r, nameAndFieldsTemplate, userFullName));
        });

        return response;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(InformationId id) {
        Information information = informationRepository.findById(id).orElseThrow(InformationNotFoundException::new);

        informationRepository.delete(information);
    }

    @Override
    @Transactional(readOnly = true)
    public ReadInformationResponse read(ReadInformationRequest req) {
        InformationId informationId = new InformationId(req.templateId(), req.userId());

        return ReadInformationResponse.from(
                informationRepository.findById(informationId).orElseThrow(InformationNotFoundException::new));
    }

    @Override
    @Transactional
    public InformationResponse update(InformationId id, InformationAiResponse req) {
        if (!informationRepository.existsById(id)) {
            throw new InformationNotFoundException();
        }

        String userFullName = fetchUserFullName(req.userId()).fullName();
        NameAndFieldsTemplate nameAndFieldsTemplate = templateRepository.findNameAndFieldsById(req.templateId())
                .orElseThrow(TemplateNotFoundException::new);

        return persistInformation(id, req, nameAndFieldsTemplate, userFullName);
    }

    private UserFullNameView fetchUserFullName(UUID userId) {
        return restClient.get()
                .uri("/user-fullname-view/{id}", userId)
                .retrieve()
                .body(UserFullNameView.class);
    }

    private InformationResponse persistInformation(
            InformationId informationId, InformationAiResponse req,
            NameAndFieldsTemplate nameAndFieldsTemplate, String userFullName) {

        if (!nameAndFieldsTemplate.fields().equals(req.data().keySet())) {
            throw new InconsistentFieldsException();
        }

        if (!templateRepository.existsById(req.templateId())) {
            throw new TemplateNotFoundException();
        }

        Information information = (informationId != null)
                ? informationRepository.findById(informationId).orElseThrow(InformationNotFoundException::new)
                : new Information(new InformationId(req.templateId(), req.userId()));

        information.setData(req.data());

        Information saved = informationRepository.save(information);

        return new InformationResponse(
                userFullName,
                nameAndFieldsTemplate.name(),
                saved.getData());
    }

}

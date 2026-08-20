package dev.juda.templates_service.information.service.implementation;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;
import dev.juda.templates_service.information.persistence.entity.Information;
import dev.juda.templates_service.information.persistence.repository.InformationRepository;
import dev.juda.templates_service.information.presentation.dto.in.InformationAiResponse;
import dev.juda.templates_service.information.presentation.dto.in.UserFullNameView;
import dev.juda.templates_service.information.presentation.dto.out.NameAndFieldsTemplate;
import dev.juda.templates_service.information.presentation.dto.response.InformationResponse;
import dev.juda.templates_service.information.presentation.dto.response.ReadInformationResponse;
import dev.juda.templates_service.information.service.exception.InconsistentFieldsException;
import dev.juda.templates_service.information.service.exception.InformationNotFoundException;
import dev.juda.templates_service.information.service.interfaces.InformationService;
import dev.juda.templates_service.template.persistence.entity.Template;
import dev.juda.templates_service.template.persistence.repository.TemplateRepository;
import dev.juda.templates_service.template.service.exception.TemplateNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClient;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class InformationServiceImpl implements InformationService {

    private final InformationRepository informationRepository;
    private final TemplateRepository templateRepository;
    private final RestClient restClient;
    private static final Logger LOG = LoggerFactory.getLogger(InformationServiceImpl.class);

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
        LOG.trace("Initiating information persistence");
        Set<InformationResponse> response = new HashSet<>();
        NameAndFieldsTemplate nameAndFieldsTemplate = templateRepository
                .findNameAndFieldsById(req.iterator().next().templateId())
                .orElseThrow(TemplateNotFoundException::new);
        req.forEach(r -> {
            String userFullName = fetchUserFullName(r.userId()).fullName();

            response.add(persistInformation(null, r, nameAndFieldsTemplate, userFullName));
        });

        LOG.info("Persistent information");
        return response;
    }

    @Override
    @Transactional
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(InformationId id) {
        LOG.trace("Starting data deletion");
        Information information = informationRepository.findById(id).orElseThrow(InformationNotFoundException::new);

        informationRepository.delete(information);
        LOG.info("Data deletion complete");
    }

    @Override
    @Transactional(readOnly = true)
    public ReadInformationResponse read(UUID userId, UUID templateId) {
        InformationId informationId = new InformationId(templateId, userId);
        Information information = informationRepository.findById(informationId)
                .orElseThrow(InformationNotFoundException::new);
        Template template = templateRepository.findById(templateId).get();

        return ReadInformationResponse.from(information, template);
    }

    @Override
    @Transactional
    public InformationResponse update(InformationId id, InformationAiResponse req) {
        LOG.trace("Starting information update");
        if (!informationRepository.existsById(id)) {
            LOG.warn("Information not found");
            throw new InformationNotFoundException();
        }

        String userFullName = fetchUserFullName(req.userId()).fullName();
        NameAndFieldsTemplate nameAndFieldsTemplate = templateRepository.findNameAndFieldsById(req.templateId())
                .orElseThrow(TemplateNotFoundException::new);

        LOG.info("Updated information");
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
        LOG.trace("Initiating information persistence");

        if (!nameAndFieldsTemplate.fields().equals(req.data().keySet())) {
            LOG.error("Invalid information request");
            throw new InconsistentFieldsException();
        }

        if (!templateRepository.existsById(req.templateId())) {
            LOG.warn("Template not found");
            throw new TemplateNotFoundException();
        }

        Information information = (informationId != null)
                ? informationRepository.findById(informationId).orElseThrow(InformationNotFoundException::new)
                : new Information(new InformationId(req.templateId(), req.userId()));

        information.setData(req.data());

        Information saved = informationRepository.save(information);

        LOG.info("Persistent information");
        return new InformationResponse(
                userFullName,
                nameAndFieldsTemplate.name(),
                saved.getData());
    }

}

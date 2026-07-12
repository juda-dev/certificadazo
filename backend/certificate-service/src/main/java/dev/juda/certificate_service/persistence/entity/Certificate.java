package dev.juda.certificate_service.persistence.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "certificates")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, unique = true)
    private String src;

    @Column(nullable = false, name = "issue_date", updatable = false)
    private LocalDateTime issueDate;

    @Column(nullable = false, name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(nullable = false, name = "user_id")
    private UUID userId;

    @Column(nullable = false, name = "template_id")
    private UUID templateId;

    public Certificate() {
    }

    public Certificate(String code, String src, UUID userId, UUID templateId) {
        this.code = code;
        this.src = src;
        this.userId = userId;
        this.templateId = templateId;
    }

    @PrePersist
    public void prePersist() {
        this.issueDate = LocalDateTime.now();
        this.expirationDate = LocalDateTime.now().plusMonths(1);
    }

    public boolean isItExpired() {
        return LocalDateTime.now().isAfter(expirationDate);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getTemplateId() {
        return templateId;
    }

    public void setTemplateId(UUID templateId) {
        this.templateId = templateId;
    }

}

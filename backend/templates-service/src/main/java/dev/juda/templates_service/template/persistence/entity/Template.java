package dev.juda.templates_service.template.persistence.entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String desing;

    @Column(name = "department_id", nullable = false)
    private UUID departmentId;

    @Column(name = "preview_src", nullable = false, unique = true)
    private String previewSrc;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Set<String> fields;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "images_src", columnDefinition = "json")
    private Map<String, Object> imagesSrc;

    public Template() {
    }

    public Template(String name, String desing, UUID departmentId, String previewSrc, Set<String> fields,
            Map<String, Object> imagesSrc) {
        this.name = name;
        this.desing = desing;
        this.departmentId = departmentId;
        this.previewSrc = previewSrc;
        this.fields = fields;
        this.imagesSrc = imagesSrc;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesing() {
        return desing;
    }

    public void setDesing(String desing) {
        this.desing = desing;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public Set<String> getFields() {
        return fields;
    }

    public void setFields(Set<String> fields) {
        this.fields = fields;
    }

    public String getPreviewSrc() {
        return previewSrc;
    }

    public void setPreviewSrc(String previewSrc) {
        this.previewSrc = previewSrc;
    }

    public Map<String, Object> getImagesSrc() {
        return imagesSrc;
    }

    public void setImagesSrc(Map<String, Object> imagesSrc) {
        this.imagesSrc = imagesSrc;
    }

}

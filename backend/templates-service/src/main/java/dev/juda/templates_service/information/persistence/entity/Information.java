package dev.juda.templates_service.information.persistence.entity;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import dev.juda.templates_service.information.persistence.embeddable.InformationId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "information")
public class Information {

    @EmbeddedId
    private InformationId id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> data;

    public Information() {
    }

    public Information(InformationId id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
    }

    public InformationId getId() {
        return id;
    }

    public void setId(InformationId id) {
        this.id = id;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

}

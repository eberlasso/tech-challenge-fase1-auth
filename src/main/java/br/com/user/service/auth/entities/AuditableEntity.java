package br.com.user.service.auth.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Base class for entities that require automated auditing.
 * This class provides tracking for creation and update timestamps,
 * fulfilling the mandatory requirements for tracking the last change date.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AuditableEntity {
    /**
     * Stores the date and time when the record was first created.
     */
    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    /**
     * Stores the date and time of the last modification.
     * Maps to the mandatory requirement for registration of the last change date.
     */
    @LastModifiedDate
    @Column(name = "data_ultima_alteracao")
    private LocalDateTime dataUltimaAlteracao;
}

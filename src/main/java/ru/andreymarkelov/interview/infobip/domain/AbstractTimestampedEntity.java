package ru.andreymarkelov.interview.infobip.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

@MappedSuperclass
abstract class AbstractTimestampedEntity extends AbstractEntity {
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)", updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3)", updatable = false)
    private Instant lastModifiedDate;

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @PrePersist
    private void onCreate() {
        Instant now = Instant.now();
        this.setCreatedDate(now);
        this.setLastModifiedDate(now);
    }

    @PreUpdate
    private void onPersist() {
        this.setLastModifiedDate(Instant.now());
    }
}

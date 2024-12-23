package com.example.eatpick.common.domain.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @Column(nullable = false)
    @CreatedDate
    private String createdDate;

    @LastModifiedDate
    private String modifiedDate;

    @PrePersist
    public void onPrePersist() {
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @PreUpdate
    public void onPreUpdate() {
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}

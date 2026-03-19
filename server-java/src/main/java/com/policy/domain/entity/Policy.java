package com.policy.domain.entity;

import com.policy.domain.common.BaseTimeEntity;
import com.policy.domain.enums.PolicyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "policies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Policy extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "target_age_min")
    private Integer targetAgeMin;

    @Column(name = "target_age_max")
    private Integer targetAgeMax;

    @Column(name = "income_criteria", length = 100)
    private String incomeCriteria;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "support_amount", columnDefinition = "TEXT")
    private String supportAmount;

    @Column(name = "apply_start")
    private LocalDate applyStart;

    @Column(name = "apply_end")
    private LocalDate applyEnd;

    @Column(name = "apply_url", columnDefinition = "TEXT")
    private String applyUrl;

    @Column(name = "source_site", length = 50)
    private String sourceSite;

    @Column(name = "summary", length = 200)
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PolicyStatus status;

    @Column(name = "source_url_hash", nullable = false, unique = true, length = 255)
    private String sourceUrlHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Admin reviewedBy;

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    public Policy(
            String title,
            String category,
            Integer targetAgeMin,
            Integer targetAgeMax,
            String incomeCriteria,
            String region,
            String supportAmount,
            LocalDate applyStart,
            LocalDate applyEnd,
            String applyUrl,
            String sourceSite,
            String summary,
            String sourceUrlHash
    ) {
        this.title = title;
        this.category = category;
        this.targetAgeMin = targetAgeMin;
        this.targetAgeMax = targetAgeMax;
        this.incomeCriteria = incomeCriteria;
        this.region = region;
        this.supportAmount = supportAmount;
        this.applyStart = applyStart;
        this.applyEnd = applyEnd;
        this.applyUrl = applyUrl;
        this.sourceSite = sourceSite;
        this.summary = summary;
        this.sourceUrlHash = sourceUrlHash;
        this.status = PolicyStatus.PENDING;
    }

    public void changeStatus(PolicyStatus status, Admin reviewer) {
        this.status = status;
        this.reviewedBy = reviewer;
        this.reviewedAt = OffsetDateTime.now();
    }
}

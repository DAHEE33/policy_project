package com.policy.domain.entity;

import com.policy.domain.enums.ReviewStatus;
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
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "policy_reviews")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PolicyReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "approval_tips", columnDefinition = "TEXT")
    private String approvalTips;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReviewStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private Admin reviewedBy;

    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    public PolicyReview(Policy policy, String approvalTips) {
        this.policy = policy;
        this.approvalTips = approvalTips;
        this.status = ReviewStatus.PENDING;
    }

    public void changeStatus(ReviewStatus status, Admin reviewer) {
        this.status = status;
        this.reviewedBy = reviewer;
        this.reviewedAt = OffsetDateTime.now();
    }
}

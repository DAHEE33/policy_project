package com.policy.domain.entity;

import com.policy.domain.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "policy_qna")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PolicyQna extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "is_anonymous", nullable = false)
    private boolean anonymous;

    @Column(name = "anonymous_nickname", length = 255)
    private String anonymousNickname;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "likes_count", nullable = false)
    private int likesCount;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private Admin deletedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public PolicyQna(
            Policy policy,
            User user,
            boolean anonymous,
            String anonymousNickname,
            String content
    ) {
        this.policy = policy;
        this.user = user;
        this.anonymous = anonymous;
        this.anonymousNickname = anonymousNickname;
        this.content = content;
        this.likesCount = 0;
        this.deleted = false;
    }

    public void increaseLikeCount() {
        this.likesCount += 1;
    }

    public void decreaseLikeCount() {
        this.likesCount = Math.max(0, this.likesCount - 1);
    }

    public void softDelete(Admin admin) {
        this.deleted = true;
        this.deletedBy = admin;
        this.deletedAt = OffsetDateTime.now();
    }
}

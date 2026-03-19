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
@Table(name = "qna_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qna_id", nullable = false)
    private PolicyQna qna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by")
    private Admin deletedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public QnaComment(PolicyQna qna, User user, String content) {
        this.qna = qna;
        this.user = user;
        this.content = content;
        this.deleted = false;
    }

    public void softDelete(Admin admin) {
        this.deleted = true;
        this.deletedBy = admin;
        this.deletedAt = OffsetDateTime.now();
    }
}

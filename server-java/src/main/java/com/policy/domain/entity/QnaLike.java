package com.policy.domain.entity;

import com.policy.domain.entity.id.QnaLikeId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "qna_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaLike {

    @EmbeddedId
    private QnaLikeId id;

    @MapsId("qnaId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qna_id", nullable = false)
    private PolicyQna qna;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public QnaLike(PolicyQna qna, User user) {
        this.qna = qna;
        this.user = user;
        this.id = new QnaLikeId(qna.getId(), user.getId());
    }
}

package com.policy.domain.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaLikeId implements Serializable {

    @Column(name = "qna_id")
    private Long qnaId;

    @Column(name = "user_id")
    private Long userId;

    public QnaLikeId(Long qnaId, Long userId) {
        this.qnaId = qnaId;
        this.userId = userId;
    }
}

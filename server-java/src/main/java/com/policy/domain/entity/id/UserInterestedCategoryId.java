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
public class UserInterestedCategoryId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "category", length = 50)
    private String category;

    public UserInterestedCategoryId(Long userId, String category) {
        this.userId = userId;
        this.category = category;
    }
}

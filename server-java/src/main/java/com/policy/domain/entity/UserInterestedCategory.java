package com.policy.domain.entity;

import com.policy.domain.entity.id.UserInterestedCategoryId;
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
@Table(name = "user_interested_categories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInterestedCategory {

    @EmbeddedId
    private UserInterestedCategoryId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public UserInterestedCategory(User user, String category) {
        this.user = user;
        this.id = new UserInterestedCategoryId(user.getId(), category);
    }

    public String getCategory() {
        return id.getCategory();
    }
}

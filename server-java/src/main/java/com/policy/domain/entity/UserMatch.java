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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "user_matches",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_matches_user_policy", columnNames = {"user_id", "policy_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMatch extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    @Column(name = "match_score", nullable = false)
    private Integer matchScore;

    public UserMatch(User user, Policy policy, Integer matchScore) {
        this.user = user;
        this.policy = policy;
        this.matchScore = matchScore;
    }

    public void updateMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }
}

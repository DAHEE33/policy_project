package com.policy.domain.entity;

import com.policy.domain.common.BaseTimeEntity;
import com.policy.domain.converter.Aes256Converter;
import com.policy.domain.enums.SubscriptionTier;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "oauth_provider", length = 50)
    private String oauthProvider;

    @Column(name = "oauth_id", length = 255)
    private String oauthId;

    @Column(name = "birth_year")
    private Integer birthYear;

    @Column(name = "job_status", length = 50)
    private String jobStatus;

    @Convert(converter = Aes256Converter.class)
    @Column(name = "income_bracket", length = 255)
    private String incomeBracket;

    @Convert(converter = Aes256Converter.class)
    @Column(name = "region", length = 255)
    private String region;

    @Column(name = "housing_type", length = 50)
    private String housingType;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_tier", nullable = false, length = 20)
    private SubscriptionTier subscriptionTier;

    @Column(name = "is_banned", nullable = false)
    private boolean banned;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banned_by")
    private Admin bannedBy;

    @Column(name = "banned_at")
    private OffsetDateTime bannedAt;

    public User(
            String email,
            String passwordHash,
            String oauthProvider,
            String oauthId,
            Integer birthYear,
            String jobStatus,
            String incomeBracket,
            String region,
            String housingType,
            SubscriptionTier subscriptionTier
    ) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
        this.birthYear = birthYear;
        this.jobStatus = jobStatus;
        this.incomeBracket = incomeBracket;
        this.region = region;
        this.housingType = housingType;
        this.subscriptionTier = subscriptionTier == null ? SubscriptionTier.FREE : subscriptionTier;
        this.banned = false;
    }

    public void ban(Admin admin) {
        this.banned = true;
        this.bannedBy = admin;
        this.bannedAt = OffsetDateTime.now();
    }

    public void unban() {
        this.banned = false;
        this.bannedBy = null;
        this.bannedAt = null;
    }

    public void upgradeSubscription(SubscriptionTier tier) {
        this.subscriptionTier = tier;
    }
}

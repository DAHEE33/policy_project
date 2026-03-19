package com.policy.domain.entity;

import com.policy.domain.enums.NotificationType;
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
import jakarta.persistence.UniqueConstraint;
import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "notification_logs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_notification_logs_match_type", columnNames = {"match_id", "noti_type"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private UserMatch match;

    @Enumerated(EnumType.STRING)
    @Column(name = "noti_type", nullable = false, length = 20)
    private NotificationType notiType;

    @Column(name = "sent_at", nullable = false)
    private OffsetDateTime sentAt;

    public NotificationLog(UserMatch match, NotificationType notiType, OffsetDateTime sentAt) {
        this.match = match;
        this.notiType = notiType;
        this.sentAt = sentAt == null ? OffsetDateTime.now() : sentAt;
    }
}

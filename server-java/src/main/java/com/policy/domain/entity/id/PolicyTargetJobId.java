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
public class PolicyTargetJobId implements Serializable {

    @Column(name = "policy_id")
    private Long policyId;

    @Column(name = "job_status", length = 50)
    private String jobStatus;

    public PolicyTargetJobId(Long policyId, String jobStatus) {
        this.policyId = policyId;
        this.jobStatus = jobStatus;
    }
}

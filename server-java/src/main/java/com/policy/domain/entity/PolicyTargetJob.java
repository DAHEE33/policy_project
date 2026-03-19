package com.policy.domain.entity;

import com.policy.domain.entity.id.PolicyTargetJobId;
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
@Table(name = "policy_target_jobs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PolicyTargetJob {

    @EmbeddedId
    private PolicyTargetJobId id;

    @MapsId("policyId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "policy_id", nullable = false)
    private Policy policy;

    public PolicyTargetJob(Policy policy, String jobStatus) {
        this.policy = policy;
        this.id = new PolicyTargetJobId(policy.getId(), jobStatus);
    }

    public String getJobStatus() {
        return id.getJobStatus();
    }
}

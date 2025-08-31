package com.example.carins.service;

import com.example.carins.repo.InsurancePolicyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class PolicyExpireLogger {
    private final InsurancePolicyRepository policyRepository;

    // Track which policies we’ve already logged
    private final Set<Long> loggedPolicies = new HashSet<>();

    public PolicyExpireLogger(InsurancePolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    // Run every minute
    @Scheduled(cron = "0 */1 * * * *")
    public void logExpiredPolicies() {
        LocalDate today = LocalDate.now();

        // Find policies where endDate = today (just expired at midnight)
        var expiredToday = policyRepository.findByEndDate(today);

        expiredToday.forEach(policy -> {
            if (!loggedPolicies.contains(policy.getId())) {
                log.info("Policy {} for car {} expired on {}",
                        policy.getId(),
                        policy.getCar().getId(),
                        policy.getEndDate());
                loggedPolicies.add(policy.getId());
            }
            else {
                log.info("No policies are expired");
            }
        });
    }
}

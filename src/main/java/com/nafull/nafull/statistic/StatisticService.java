package com.nafull.nafull.statistic;

import com.nafull.nafull.statistic.data.ServiceStatistic;
import com.nafull.nafull.statistic.data.UserStatistic;
import com.nafull.nafull.user.DefaultUser;
import com.nafull.nafull.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final UserService userService;
    private final DefaultUser defaultUser;

    public ServiceStatistic calculateService() {
        Long totalButterflyEffectCount = userService.calculateUserTotalSpreadCount(defaultUser.getId());
        Long totalUserCount = userService.countAll();

        return new ServiceStatistic(
            totalButterflyEffectCount,
            totalUserCount
        );
    }

    public UserStatistic calculateUser(UUID userId) {
        return userService.calculateStatistics(userId);
    }
}

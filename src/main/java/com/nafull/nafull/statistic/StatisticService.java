package com.nafull.nafull.statistic;

import com.nafull.nafull.statistic.data.ServiceStatistic;
import com.nafull.nafull.statistic.data.UserStatistic;
import com.nafull.nafull.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatisticService {
    private final UserService userService;

    public ServiceStatistic calculateService() {
        return new ServiceStatistic(10L, 34L);
    }

    public UserStatistic calculateUser(UUID userId) {
        return userService.calculateStatistics(userId);
    }
}

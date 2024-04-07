package com.nafull.nafull.statistic;

import com.nafull.nafull.letter.LetterRepository;
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
    private final LetterRepository letterRepository;

    public ServiceStatistic calculateService() {
        Long totalButterflyEffectCount = letterRepository.count();//userService.calculateUserTotalSpreadCount(defaultUser.getId());
        Long totalUserCount = userService.countAll();

        return new ServiceStatistic(
            totalUserCount,
            totalButterflyEffectCount
        );
    }

    public UserStatistic calculateUser(UUID userId) {
        return userService.calculateStatistics(userId);
    }
}

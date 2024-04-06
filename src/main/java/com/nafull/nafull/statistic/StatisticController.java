package com.nafull.nafull.statistic;

import com.nafull.nafull.statistic.data.UserStatistic;
import com.nafull.nafull.statistic.data.ServiceStatistic;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/statistics")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/service")
    public ServiceStatistic calculateService() {
        return statisticService.calculateService();
    }

    @GetMapping("/users/{userId}")
    public UserStatistic calculateUser(
        @PathVariable UUID userId
    ) {
        return statisticService.calculateUser(userId);
    }
}

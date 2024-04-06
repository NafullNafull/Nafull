package com.nafull.nafull.statistic;

import com.nafull.nafull.statistic.data.ServiceStatistic;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticController {
    @GetMapping()
    public ServiceStatistic calculateStatistics() {
        return new ServiceStatistic(10L, 34L);
    }
}

package com.practice.statsserver.service;

import com.practice.statsdto.dto.EndpointHit;
import com.practice.statsdto.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void addHit(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start,LocalDateTime end,List<String> uris, Boolean unique);
}

package com.practice.statsserver.service;

import com.practice.statsdto.dto.EndpointHit;
import com.practice.statsdto.dto.ViewStats;
import com.practice.statsserver.entity.Hit;
import com.practice.statsserver.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void addHit(EndpointHit endpointHit) {
        statsRepository.save(toEntity(endpointHit));
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)){
            throw new IllegalStateException("Дата начала должна быть раньше даты завершения");
        }
        if (uris == null || uris.isEmpty()){
            if (unique){
                return statsRepository.findStatsUnique(start,end);
            }else {
                return statsRepository.findAllStats(start,end);
            }
        } else {
            if (unique) {
                return statsRepository.findStatsUnique(start, end, uris);
            } else {
                return statsRepository.findAllStats(start, end, uris);
            }
        }
    }

    private Hit toEntity(EndpointHit endpointHit) {
        Hit hit = new Hit();
        hit.setApp(endpointHit.getApp());
        hit.setIp(endpointHit.getIp());
        hit.setUri(endpointHit.getUri());
        hit.setTimestamp(endpointHit.getTimestamp());

        return hit;
    }
}

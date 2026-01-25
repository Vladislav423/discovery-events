package com.practice.statsserver.repository;

import com.practice.statsdto.dto.ViewStats;
import com.practice.statsserver.entity.Hit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Integer> {
    @Query("select new com.practice.statsdto.dto.ViewStats(h.app,h.uri,count(h.ip)) " +
            "from Hit h where h.timestamp between ?1 and ?2 " +
            "group by h.app,h.uri order by count(h.ip) desc")
    List<ViewStats> findAllStats(LocalDateTime start, LocalDateTime end);

    @Query("select new com.practice.statsdto.dto.ViewStats(h.app,h.uri,count(distinct h.ip)) " +
            "from Hit h where h.timestamp between ?1 and ?2 " +
            "group by h.app,h.uri order by count(distinct h.ip) desc")
    List<ViewStats> findStatsUnique(LocalDateTime start, LocalDateTime end);

    @Query("select new com.practice.statsdto.dto.ViewStats(h.app,h.uri,count(h.ip)) " +
            "from Hit h where h.timestamp between ?1 and ?2 and h.uri in ?3 " +
            "group by h.app,h.uri order by count(h.ip) desc")
    List<ViewStats> findAllStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new com.practice.statsdto.dto.ViewStats(h.app,h.uri,count(distinct h.ip)) " +
            "from Hit h where h.timestamp between ?1 and ?2 and h.uri in ?3 " +
            "group by h.app,h.uri order by count(distinct h.ip) desc")
    List<ViewStats> findStatsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);


}

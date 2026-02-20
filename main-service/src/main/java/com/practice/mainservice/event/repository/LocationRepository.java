package com.practice.mainservice.event.repository;

import com.practice.mainservice.event.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location,Long> {
}

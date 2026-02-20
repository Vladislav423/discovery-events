package com.practice.mainservice.compilation.repository;

import com.practice.mainservice.compilation.entity.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {
    Page<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}

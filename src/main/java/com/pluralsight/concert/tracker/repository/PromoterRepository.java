package com.pluralsight.concert.tracker.repository;

import com.pluralsight.concert.tracker.models.Promoter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PromoterRepository extends JpaRepository<Promoter, Integer> {
    List<Promoter> findByNameContainingIgnoreCase(String name);
}

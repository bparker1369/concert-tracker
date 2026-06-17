package com.pluralsight.concert.tracker.repository;

import com.pluralsight.concert.tracker.models.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {
}
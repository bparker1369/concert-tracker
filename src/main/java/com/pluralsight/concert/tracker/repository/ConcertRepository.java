package com.pluralsight.concert.tracker.repository;

import com.pluralsight.concert.tracker.models.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {
    List<Concert> findByTicketPriceLessThanEqualAndYearGreaterThanEqual(double price, int year);
}
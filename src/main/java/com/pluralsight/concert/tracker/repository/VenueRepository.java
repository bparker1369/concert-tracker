package com.pluralsight.concert.tracker.repository;

import com.pluralsight.concert.tracker.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Integer> {
    List<Venue> findByCity(String city);
    List<Venue> findByNameContainingIgnoreCase(String name);
    List<Venue> findByCapacityGreaterThanEqual(int capacity);
}

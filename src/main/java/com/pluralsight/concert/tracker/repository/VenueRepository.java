package com.pluralsight.concert.tracker.repository;

import com.pluralsight.concert.tracker.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Integer> {
}
package com.pluralsight.concert.tracker.repository;

import com.pluralsight.concert.tracker.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
}
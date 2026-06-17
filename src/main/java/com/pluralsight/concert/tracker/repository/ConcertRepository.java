package com.pluralsight.concert.tracker.repository;

import com.pluralsight.concert.tracker.models.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Integer> {
    List<Concert> findByTicketPriceLessThanEqualAndYearGreaterThanEqual(double price, int year);
    @Query("SELECT c.venue.name, SUM(c.ticketPrice * c.ticketsSold) FROM Concert c GROUP BY c.venue.name")
    List<Object[]> getRevenuePerVenue();

    @Query("SELECT c.venue.name, COUNT(c) FROM Concert c GROUP BY c.venue.name ORDER BY COUNT(c) DESC")
    List<Object[]> getBusiestVenue();

    @Query("SELECT c.artist.name, COUNT(c) FROM Concert c GROUP BY c.artist.name ORDER BY COUNT(c) DESC")
    List<Object[]> getBusiestArtist();

    @Query("SELECT c.year, AVG(c.ticketPrice) FROM Concert c GROUP BY c.year ORDER BY c.year")
    List<Object[]> getAvgPriceByYear();
    List<Concert> findByYear(int year);
    List<Concert> findByArtist_NameContainingIgnoreCase(String name);
    List<Concert> findByVenue_NameIgnoreCase(String name);
    List<Concert> findByVenue_CityIgnoreCase(String city);
    List<Concert> findByTicketPriceLessThanEqual(double price);
    List<Concert> findByTicketPriceBetween(double min, double max);
}
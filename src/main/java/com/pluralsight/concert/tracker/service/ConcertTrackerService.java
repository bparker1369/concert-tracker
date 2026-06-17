package com.pluralsight.concert.tracker.service;

import com.pluralsight.concert.tracker.models.*;
import com.pluralsight.concert.tracker.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConcertTrackerService {

    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    private final PromoterRepository promoterRepository;
    private final ConcertRepository concertRepository;

    public ConcertTrackerService(VenueRepository venueRepository,
                                 ArtistRepository artistRepository,
                                 PromoterRepository promoterRepository,
                                 ConcertRepository concertRepository) {
        this.venueRepository = venueRepository;
        this.artistRepository = artistRepository;
        this.promoterRepository = promoterRepository;
        this.concertRepository = concertRepository;
    }

    // Venue methods
    public List<Venue> getAllVenues() { return venueRepository.findAll(); }
    public Optional<Venue> getVenueById(int id) { return venueRepository.findById(id); }
    public Venue saveVenue(Venue venue) { return venueRepository.save(venue); }
    public void deleteVenue(int id) { venueRepository.deleteById(id); }

    // Artist methods
    public List<Artist> getAllArtists() { return artistRepository.findAll(); }
    public Optional<Artist> getArtistById(int id) { return artistRepository.findById(id); }
    public Artist saveArtist(Artist artist) { return artistRepository.save(artist); }
    public void deleteArtist(int id) { artistRepository.deleteById(id); }

    // Promoter methods
    public List<Promoter> getAllPromoters() { return promoterRepository.findAll(); }
    public Optional<Promoter> getPromoterById(int id) { return promoterRepository.findById(id); }
    public Promoter savePromoter(Promoter promoter) { return promoterRepository.save(promoter); }
    public void deletePromoter(int id) { promoterRepository.deleteById(id); }

    // Concert methods
    public List<Concert> getAllConcerts() { return concertRepository.findAll(); }
    public Optional<Concert> getConcertById(int id) { return concertRepository.findById(id); }
    public Concert saveConcert(Concert concert) { return concertRepository.save(concert); }
    public void deleteConcert(int id) { concertRepository.deleteById(id); }
    public Concert updateConcert(Concert concert) {
        return concertRepository.save(concert);
    }
    public List<Venue> findVenuesByCity(String city) {
        return venueRepository.findByCity(city);
    }

    public List<Venue> findVenuesByName(String name) {
        return venueRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Venue> findVenuesByMinCapacity(int capacity) {
        return venueRepository.findByCapacityGreaterThanEqual(capacity);
    }

    public List<Artist> findArtistsByGenre(String genre) {
        return artistRepository.findByGenreIgnoreCase(genre);
    }

    public List<Artist> findArtistsByName(String name) {
        return artistRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Promoter> findPromotersByName(String name) {
        return promoterRepository.findByNameContainingIgnoreCase(name);
    }
}
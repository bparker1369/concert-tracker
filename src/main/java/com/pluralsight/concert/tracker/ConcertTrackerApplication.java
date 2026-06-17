package com.pluralsight.concert.tracker;

import com.pluralsight.concert.tracker.models.*;
import com.pluralsight.concert.tracker.service.ConcertTrackerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.pluralsight.concert.tracker.repository.ConcertRepository;

import java.util.Optional;
import java.util.Scanner;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ConcertTrackerApplication implements CommandLineRunner {

    private final ConcertTrackerService service;
    private final Scanner scanner = new Scanner(System.in);

    public ConcertTrackerApplication(ConcertTrackerService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(ConcertTrackerApplication.class, args);
    }

    @Override
    public void run(String... args) {
        seedData();
        mainMenu();
    }

    private void seedData() {
        if (service.getAllConcerts().isEmpty()) {
            Venue v1 = new Venue(); v1.setName("Scotiabank Arena"); v1.setCity("Toronto"); v1.setCapacity(20000);
            Venue v2 = new Venue(); v2.setName("Staples Center"); v2.setCity("Los Angeles"); v2.setCapacity(18000);
            Venue v3 = new Venue(); v3.setName("United Center"); v3.setCity("Chicago"); v3.setCapacity(22000);
            service.saveVenue(v1); service.saveVenue(v2); service.saveVenue(v3);

            Artist a1 = new Artist(); a1.setName("Taylor Swift"); a1.setGenre("Pop");
            Artist a2 = new Artist(); a2.setName("Drake"); a2.setGenre("Hip-Hop");
            Artist a3 = new Artist(); a3.setName("The Beatles"); a3.setGenre("Rock");
            service.saveArtist(a1); service.saveArtist(a2); service.saveArtist(a3);

            Promoter p1 = new Promoter(); p1.setName("Live Nation");
            Promoter p2 = new Promoter(); p2.setName("AEG Presents");
            service.savePromoter(p1); service.savePromoter(p2);

            Concert c1 = new Concert(); c1.setYear(2024); c1.setTicketPrice(150.00); c1.setTicketsSold(18000);
            c1.setArtist(a1); c1.setVenue(v1); c1.setPromoter(p1);
            service.saveConcert(c1);

            Concert c2 = new Concert(); c2.setYear(2023); c2.setTicketPrice(120.00); c2.setTicketsSold(15000);
            c2.setArtist(a2); c2.setVenue(v2); c2.setPromoter(p2);
            service.saveConcert(c2);

            Concert c3 = new Concert(); c3.setYear(2024); c3.setTicketPrice(200.00); c3.setTicketsSold(22000);
            c3.setArtist(a3); c3.setVenue(v3); c3.setPromoter(p1);
            service.saveConcert(c3);

            System.out.println("Starter data loaded!");
        }
    }

    private void mainMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n=== CONCERT TRACKER ===");
            System.out.println("1) Concerts");
            System.out.println("2) Search Concerts");
            System.out.println("3) Artists");
            System.out.println("4) Venues");
            System.out.println("5) Promoters");
            System.out.println("6) Reports");
            System.out.println("0) Quit");
            System.out.print("Choose: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1 -> concertsMenu();
                case 2 -> searchMenu();
                case 3 -> artistsMenu();
                case 4 -> venuesMenu();
                case 5 -> promotersMenu();
                case 6 -> reportsMenu();
                case 0 -> System.out.println("Goodbye!");
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void concertsMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n=== CONCERTS ===");
            System.out.println("1) List all concerts");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1 -> listAllConcerts();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void listAllConcerts() {
        List<Concert> concerts = service.getAllConcerts();
        if (concerts.isEmpty()) {
            System.out.println("No concerts found.");
        } else {
            System.out.println("\n--- All Concerts ---");
            for (Concert c : concerts) {
                System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " (" + c.getYear() + ")");
            }
        }
    }

    private void viewConcertById() {
        System.out.print("Enter concert ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Concert> concert = service.getConcertById(id);
            if (concert.isPresent()) {
                Concert c = concert.get();
                System.out.println("\n--- Concert Details ---");
                System.out.println("ID: " + c.getId());
                System.out.println("Artist: " + c.getArtist().getName());
                System.out.println("Venue: " + c.getVenue().getName());
                System.out.println("Promoter: " + c.getPromoter().getName());
                System.out.println("Year: " + c.getYear());
                System.out.println("Ticket Price: $" + c.getTicketPrice());
                System.out.println("Tickets Sold: " + c.getTicketsSold());
            } else {
                System.out.println("No concert found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid ID.");
        }
    }

    private void addConcert() {
        System.out.println("\n--- Artists ---");
        service.getAllArtists().forEach(a -> System.out.println(a.getId() + ") " + a.getName()));
        System.out.print("Choose artist ID: ");
        int artistId = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("\n--- Venues ---");
        service.getAllVenues().forEach(v -> System.out.println(v.getId() + ") " + v.getName()));
        System.out.print("Choose venue ID: ");
        int venueId = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("\n--- Promoters ---");
        service.getAllPromoters().forEach(p -> System.out.println(p.getId() + ") " + p.getName()));
        System.out.print("Choose promoter ID: ");
        int promoterId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Year: ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Ticket Price: ");
        double price = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Tickets Sold: ");
        int ticketsSold = Integer.parseInt(scanner.nextLine().trim());

        var artist = service.getArtistById(artistId);
        var venue = service.getVenueById(venueId);
        var promoter = service.getPromoterById(promoterId);

        if (artist.isEmpty() || venue.isEmpty() || promoter.isEmpty()) {
            System.out.println("Invalid artist, venue, or promoter ID.");
            return;
        }

        if (price < 0 || ticketsSold < 0) {
            System.out.println("Price and tickets sold cannot be negative.");
            return;
        }

        if (ticketsSold > venue.get().getCapacity()) {
            System.out.println("Tickets sold cannot exceed venue capacity of " + venue.get().getCapacity());
            return;
        }

        Concert c = new Concert();
        c.setArtist(artist.get());
        c.setVenue(venue.get());
        c.setPromoter(promoter.get());
        c.setYear(year);
        c.setTicketPrice(price);
        c.setTicketsSold(ticketsSold);
        service.saveConcert(c);
        System.out.println("Concert added!");
    }

    private void updateTicketPrice() {
        System.out.print("Enter concert ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Concert> concert = service.getConcertById(id);
            if (concert.isPresent()) {
                System.out.print("New ticket price: ");
                double price = Double.parseDouble(scanner.nextLine().trim());
                if (price < 0) {
                    System.out.println("Price cannot be negative.");
                    return;
                }
                Concert c = concert.get();
                c.setTicketPrice(price);
                service.updateConcert(c);
                System.out.println("Ticket price updated!");
            } else {
                System.out.println("No concert found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void updateTicketsSold() {
        System.out.print("Enter concert ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Concert> concert = service.getConcertById(id);
            if (concert.isPresent()) {
                Concert c = concert.get();
                System.out.print("New tickets sold: ");
                int ticketsSold = Integer.parseInt(scanner.nextLine().trim());
                if (ticketsSold < 0) {
                    System.out.println("Tickets sold cannot be negative.");
                    return;
                }
                if (ticketsSold > c.getVenue().getCapacity()) {
                    System.out.println("Tickets sold cannot exceed venue capacity of " + c.getVenue().getCapacity());
                    return;
                }
                c.setTicketsSold(ticketsSold);
                service.updateConcert(c);
                System.out.println("Tickets sold updated!");
            } else {
                System.out.println("No concert found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void deleteConcert() {
        System.out.print("Enter concert ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Concert> concert = service.getConcertById(id);
            if (concert.isPresent()) {
                service.deleteConcert(id);
                System.out.println("Concert deleted!");
            } else {
                System.out.println("No concert found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid ID.");
        }
    }
    private void artistsMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n=== ARTISTS ===");
            System.out.println("1) List all artists");
            System.out.println("2) Add an artist");
            System.out.println("3) Find by genre");
            System.out.println("4) Find by name");
            System.out.println("5) Update genre");
            System.out.println("6) Delete an artist");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1 -> listAllArtists();
                case 2 -> addArtist();
                case 3 -> findArtistsByGenre();
                case 4 -> findArtistsByName();
                case 5 -> updateArtistGenre();
                case 6 -> deleteArtist();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    private void listAllArtists() {
        List<Artist> artists = service.getAllArtists();
        if (artists.isEmpty()) {
            System.out.println("No artists found.");
        } else {
            System.out.println("\n--- All Artists ---");
            for (Artist a : artists) {
                System.out.println("ID: " + a.getId() + " | " + a.getName() + " | " + a.getGenre());
            }
        }
    }

    private void addArtist() {
        System.out.print("Artist name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Genre: ");
        String genre = scanner.nextLine().trim();
        Artist a = new Artist();
        a.setName(name);
        a.setGenre(genre);
        service.saveArtist(a);
        System.out.println("Artist added!");
    }

    private void findArtistsByGenre() {
        System.out.print("Enter genre: ");
        String genre = scanner.nextLine().trim();
        List<Artist> artists = service.findArtistsByGenre(genre);
        if (artists.isEmpty()) {
            System.out.println("No artists found with that genre.");
        } else {
            for (Artist a : artists) {
                System.out.println("ID: " + a.getId() + " | " + a.getName() + " | " + a.getGenre());
            }
        }
    }

    private void findArtistsByName() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().trim();
        List<Artist> artists = service.findArtistsByName(name);
        if (artists.isEmpty()) {
            System.out.println("No artists found with that name.");
        } else {
            for (Artist a : artists) {
                System.out.println("ID: " + a.getId() + " | " + a.getName() + " | " + a.getGenre());
            }
        }
    }

    private void updateArtistGenre() {
        System.out.print("Enter artist ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Artist> artist = service.getArtistById(id);
            if (artist.isPresent()) {
                System.out.print("New genre: ");
                String genre = scanner.nextLine().trim();
                Artist a = artist.get();
                a.setGenre(genre);
                service.saveArtist(a);
                System.out.println("Genre updated!");
            } else {
                System.out.println("No artist found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void deleteArtist() {
        System.out.print("Enter artist ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Artist> artist = service.getArtistById(id);
            if (artist.isPresent()) {
                service.deleteArtist(id);
                System.out.println("Artist deleted!");
            } else {
                System.out.println("No artist found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }
    private void searchMenu() {

    }

    private void promotersMenu() {

    }

    private void reportsMenu() {

    }

    private void venuesMenu() {

    }
}
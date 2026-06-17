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
            System.out.println("2) View concert by ID");
            System.out.println("3) Add a concert");
            System.out.println("4) Update ticket price");
            System.out.println("5) Update tickets sold");
            System.out.println("6) Delete a concert");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1 -> listAllConcerts();
                case 2 -> viewConcertById();
                case 3 -> addConcert();
                case 4 -> updateTicketPrice();
                case 5 -> updateTicketsSold();
                case 6 -> deleteConcert();
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
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n=== SEARCH CONCERTS ===");
            System.out.println("1) By year");
            System.out.println("2) By artist");
            System.out.println("3) By venue");
            System.out.println("4) By city");
            System.out.println("5) By maximum price");
            System.out.println("6) By price range");
            System.out.println("7) Advanced search");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1 -> searchByYear();
                case 2 -> searchByArtist();
                case 3 -> searchByVenue();
                case 4 -> searchByCity();
                case 5 -> searchByMaxPrice();
                case 6 -> searchByPriceRange();
                case 7 -> searchAdvanced();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice, try again.");
            }
        }

    }
    private void searchByYear() {
        System.out.print("Enter year: ");
        try {
            int year = Integer.parseInt(scanner.nextLine().trim());
            List<Concert> concerts = service.findConcertsByYear(year);
            if (concerts.isEmpty()) {
                System.out.println("No concerts found for that year.");
            } else {
                for (Concert c : concerts) {
                    System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " (" + c.getYear() + ")");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void searchByArtist() {
        System.out.print("Enter artist name: ");
        String name = scanner.nextLine().trim();
        List<Concert> concerts = service.findConcertsByArtist(name);
        if (concerts.isEmpty()) {
            System.out.println("No concerts found for that artist.");
        } else {
            for (Concert c : concerts) {
                System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " (" + c.getYear() + ")");
            }
        }
    }

    private void searchByVenue() {
        System.out.print("Enter venue name: ");
        String name = scanner.nextLine().trim();
        List<Concert> concerts = service.findConcertsByVenue(name);
        if (concerts.isEmpty()) {
            System.out.println("No concerts found for that venue.");
        } else {
            for (Concert c : concerts) {
                System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " (" + c.getYear() + ")");
            }
        }
    }

    private void searchByCity() {
        System.out.print("Enter city: ");
        String city = scanner.nextLine().trim();
        List<Concert> concerts = service.findConcertsByCity(city);
        if (concerts.isEmpty()) {
            System.out.println("No concerts found in that city.");
        } else {
            for (Concert c : concerts) {
                System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " (" + c.getYear() + ")");
            }
        }
    }

    private void searchByMaxPrice() {
        System.out.print("Enter maximum price: ");
        try {
            double price = Double.parseDouble(scanner.nextLine().trim());
            List<Concert> concerts = service.findConcertsByMaxPrice(price);
            if (concerts.isEmpty()) {
                System.out.println("No concerts found at or below that price.");
            } else {
                for (Concert c : concerts) {
                    System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " | $" + c.getTicketPrice());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void searchByPriceRange() {
        System.out.print("Enter minimum price: ");
        try {
            double min = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter maximum price: ");
            double max = Double.parseDouble(scanner.nextLine().trim());
            List<Concert> concerts = service.findConcertsByPriceRange(min, max);
            if (concerts.isEmpty()) {
                System.out.println("No concerts found in that price range.");
            } else {
                for (Concert c : concerts) {
                    System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " | $" + c.getTicketPrice());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void searchAdvanced() {
        System.out.print("Enter maximum price: ");
        try {
            double price = Double.parseDouble(scanner.nextLine().trim());
            System.out.print("Enter earliest year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            List<Concert> concerts = service.findConcertsByMaxPriceAndYear(price, year);
            if (concerts.isEmpty()) {
                System.out.println("No concerts found matching those criteria.");
            } else {
                for (Concert c : concerts) {
                    System.out.println("ID: " + c.getId() + " | " + c.getArtist().getName() + " at " + c.getVenue().getName() + " | $" + c.getTicketPrice() + " (" + c.getYear() + ")");
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void promotersMenu() {
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n=== PROMOTERS ===");
            System.out.println("1) List all promoters");
            System.out.println("2) Add a promoter");
            System.out.println("3) Find by name");
            System.out.println("4) Delete a promoter");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                choice = -1;
            }
            switch (choice) {
                case 1 -> listAllPromoters();
                case 2 -> addPromoter();
                case 3 -> findPromotersByName();
                case 4 -> deletePromoter();
                case 0 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice, try again.");
            }
        }

    }
    private void listAllPromoters() {
        List<Promoter> promoters = service.getAllPromoters();
        if (promoters.isEmpty()) {
            System.out.println("No promoters found.");
        } else {
            System.out.println("\n--- All Promoters ---");
            for (Promoter p : promoters) {
                System.out.println("ID: " + p.getId() + " | " + p.getName());
            }
        }
    }

    private void addPromoter() {
        System.out.print("Promoter name: ");
        String name = scanner.nextLine().trim();
        Promoter p = new Promoter();
        p.setName(name);
        service.savePromoter(p);
        System.out.println("Promoter added!");
    }

    private void findPromotersByName() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().trim();
        List<Promoter> promoters = service.findPromotersByName(name);
        if (promoters.isEmpty()) {
            System.out.println("No promoters found with that name.");
        } else {
            for (Promoter p : promoters) {
                System.out.println("ID: " + p.getId() + " | " + p.getName());
            }
        }
    }

    private void deletePromoter() {
        System.out.print("Enter promoter ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Promoter> promoter = service.getPromoterById(id);
            if (promoter.isPresent()) {
                service.deletePromoter(id);
                System.out.println("Promoter deleted!");
            } else {
                System.out.println("No promoter found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void reportsMenu() {
            int choice = -1;
            while (choice != 0) {
                System.out.println("\n=== REPORTS ===");
                System.out.println("1) Revenue per venue");
                System.out.println("2) Busiest venue and artist");
                System.out.println("3) Average ticket price by year");
                System.out.println("4) Capacity report");
                System.out.println("0) Back");
                System.out.print("Choose: ");
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (Exception e) {
                    choice = -1;
                }
                switch (choice) {
                    case 1 -> revenuePerVenue();
                    case 2 -> busiestVenueAndArtist();
                    case 3 -> avgPriceByYear();
                    case 4 -> capacityReport();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid choice, try again.");
                }
            }
    }
    private void revenuePerVenue() {
        List<Object[]> results = service.getRevenuePerVenue();
        if (results.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("\n--- Revenue Per Venue ---");
            for (Object[] row : results) {
                System.out.printf("%-30s $%,.2f%n", row[0], row[1]);
            }
        }
    }

    private void busiestVenueAndArtist() {
        Object[] venue = service.getBusiestVenue();
        Object[] artist = service.getBusiestArtist();
        System.out.println("\n--- Busiest Venue & Artist ---");
        if (venue != null) {
            System.out.println("Busiest Venue: " + venue[0] + " (" + venue[1] + " concerts)");
        }
        if (artist != null) {
            System.out.println("Busiest Artist: " + artist[0] + " (" + artist[1] + " concerts)");
        }
    }

    private void avgPriceByYear() {
        List<Object[]> results = service.getAvgPriceByYear();
        if (results.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("\n--- Average Ticket Price By Year ---");
            for (Object[] row : results) {
                System.out.printf("Year: %s | Avg Price: $%,.2f%n", row[0], row[1]);
            }
        }
    }

    private void capacityReport() {
        List<Concert> concerts = service.getAllConcerts();
        if (concerts.isEmpty()) {
            System.out.println("No concerts found.");
        } else {
            System.out.println("\n--- Capacity Report ---");
            for (Concert c : concerts) {
                int capacity = c.getVenue().getCapacity();
                int sold = c.getTicketsSold();
                double percent = (double) sold / capacity * 100;
                String status = sold >= capacity ? " *** SOLD OUT ***" : "";
                System.out.printf("%-20s at %-25s | %d/%d (%.1f%%)%s%n",
                        c.getArtist().getName(),
                        c.getVenue().getName(),
                        sold, capacity, percent, status);
            }
        }
    }

    private void venuesMenu() {
            int choice = -1;
            while (choice != 0) {
            System.out.println("\n=== VENUES ===");
            System.out.println("1) List all venues");
            System.out.println("2) Add a venue");
            System.out.println("3) Find by city");
            System.out.println("4) Find by name");
            System.out.println("5) Find by minimum capacity");
            System.out.println("6) Update capacity");
            System.out.println("7) Delete a venue");
            System.out.println("0) Back");
            System.out.print("Choose: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                } catch (Exception e) {
                    choice = -1;
                }
                switch (choice) {
                    case 1 -> listAllVenues();
                    case 2 -> addVenue();
                    case 3 -> findVenuesByCity();
                    case 4 -> findVenuesByName();
                    case 5 -> findVenuesByMinCapacity();
                    case 6 -> updateVenueCapacity();
                    case 7 -> deleteVenue();
                    case 0 -> System.out.println("Returning to main menu...");
                    default -> System.out.println("Invalid choice, try again.");
                }
            }
    }
    private void findVenuesByCity() {
        System.out.print("Enter city: ");
        String city = scanner.nextLine().trim();
        List<Venue> venues = service.findVenuesByCity(city);
        if (venues.isEmpty()) {
            System.out.println("No venues found in " + city);
        } else {
            for (Venue v : venues) {
                System.out.println("ID: " + v.getId() + " | " + v.getName() + " | Capacity: " + v.getCapacity());
            }
        }
    }

    private void findVenuesByName() {
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().trim();
        List<Venue> venues = service.findVenuesByName(name);
        if (venues.isEmpty()) {
            System.out.println("No venues found with that name.");
        } else {
            for (Venue v : venues) {
                System.out.println("ID: " + v.getId() + " | " + v.getName() + " | " + v.getCity());
            }
        }
    }

    private void findVenuesByMinCapacity() {
        System.out.print("Enter minimum capacity: ");
        try {
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            List<Venue> venues = service.findVenuesByMinCapacity(capacity);
            if (venues.isEmpty()) {
                System.out.println("No venues found with that capacity.");
            } else {
                for (Venue v : venues) {
                    System.out.println("ID: " + v.getId() + " | " + v.getName() + " | Capacity: " + v.getCapacity());
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void updateVenueCapacity() {
        System.out.print("Enter venue ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Venue> venue = service.getVenueById(id);
            if (venue.isPresent()) {
                System.out.print("New capacity: ");
                int capacity = Integer.parseInt(scanner.nextLine().trim());
                if (capacity < 0) {
                    System.out.println("Capacity cannot be negative.");
                    return;
                }
                Venue v = venue.get();
                v.setCapacity(capacity);
                service.saveVenue(v);
                System.out.println("Capacity updated!");
            } else {
                System.out.println("No venue found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void deleteVenue() {
        System.out.print("Enter venue ID to delete: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            Optional<Venue> venue = service.getVenueById(id);
            if (venue.isPresent()) {
                service.deleteVenue(id);
                System.out.println("Venue deleted!");
            } else {
                System.out.println("No venue found with that ID.");
            }
        } catch (Exception e) {
            System.out.println("Invalid input.");
        }
    }

    private void listAllVenues() {
        List<Venue> venues = service.getAllVenues();
        if (venues.isEmpty()) {
            System.out.println("No venues found.");
        } else {
            System.out.println("\n--- All Venues ---");
            for (Venue v : venues) {
                System.out.println("ID: " + v.getId() + " | " + v.getName() + " | " + v.getCity() + " | Capacity: " + v.getCapacity());
            }
        }
    }

    private void addVenue() {
        System.out.print("Venue name: ");
        String name = scanner.nextLine().trim();
        System.out.print("City: ");
        String city = scanner.nextLine().trim();
        System.out.print("Capacity: ");
        try {
            int capacity = Integer.parseInt(scanner.nextLine().trim());
            if (capacity < 0) {
                System.out.println("Capacity cannot be negative.");
                return;
            }
            Venue v = new Venue();
            v.setName(name);
            v.setCity(city);
            v.setCapacity(capacity);
            service.saveVenue(v);
            System.out.println("Venue added!");
        } catch (Exception e) {
            System.out.println("Invalid capacity.");
        }
    }
}
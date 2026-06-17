package com.pluralsight.concert.tracker;

import com.pluralsight.concert.tracker.models.*;
import com.pluralsight.concert.tracker.service.ConcertTrackerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
}
package librarymanagementsystem.reservation;

import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;
import librarymanagementsystem.notifications.NotificationObserver;
import librarymanagementsystem.notifications.PatronNotificationObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Reservation system for the library.
 * This demonstrates the Observer pattern with the notification system.
 */
public class ReservationSystem {
    private static final Logger logger = Logger.getLogger(ReservationSystem.class.getName());
    
    // Maps ISBN to list of patrons who have reserved the book
    private Map<String, List<Patron>> reservations;
    // Maps ISBN to list of observers to notify when book becomes available
    private Map<String, List<NotificationObserver>> notificationObservers;
    
    public ReservationSystem() {
        this.reservations = new HashMap<>();
        this.notificationObservers = new HashMap<>();
    }
    
    /**
     * Reserves a book for a patron.
     *
     * @param isbn The ISBN of the book to reserve
     * @param patron The patron who wants to reserve the book
     * @return true if reservation was successful, false otherwise
     */
    public boolean reserveBook(String isbn, Patron patron) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to reserve book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (patron == null) {
            logger.warning("Attempted to reserve book for null patron");
            throw new IllegalArgumentException("Patron cannot be null");
        }
        
        // Initialize lists if they don't exist
        reservations.putIfAbsent(isbn, new ArrayList<>());
        notificationObservers.putIfAbsent(isbn, new ArrayList<>());
        
        // Check if patron already has a reservation for this book
        if (reservations.get(isbn).contains(patron)) {
            logger.info("Patron " + patron.getName() + " already has a reservation for book " + isbn);
            return false;
        }
        
        // Add reservation
        reservations.get(isbn).add(patron);
        
        // Add observer for notifications
        NotificationObserver observer = new PatronNotificationObserver(patron);
        notificationObservers.get(isbn).add(observer);
        
        logger.info("Book " + isbn + " reserved for patron " + patron.getName());
        return true;
    }
    
    /**
     * Notifies patrons when a reserved book becomes available.
     *
     * @param isbn The ISBN of the book that became available
     */
    public void notifyBookAvailable(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to notify for book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        List<NotificationObserver> observers = notificationObservers.get(isbn);
        if (observers == null || observers.isEmpty()) {
            logger.info("No reservations for book " + isbn);
            return;
        }
        
        // Notify all observers
        for (NotificationObserver observer : observers) {
            observer.update("The book you reserved is now available!");
        }
        
        // Clear reservations and observers for this book
        reservations.remove(isbn);
        notificationObservers.remove(isbn);
        
        logger.info("Notified patrons that book " + isbn + " is available");
    }
    
    /**
     * Checks if a book is reserved.
     *
     * @param isbn The ISBN of the book to check
     * @return true if the book is reserved, false otherwise
     */
    public boolean isBookReserved(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }
        
        List<Patron> patrons = reservations.get(isbn);
        return patrons != null && !patrons.isEmpty();
    }
    
    /**
     * Gets the number of reservations for a book.
     *
     * @param isbn The ISBN of the book
     * @return The number of reservations
     */
    public int getReservationCount(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return 0;
        }
        
        List<Patron> patrons = reservations.get(isbn);
        return patrons != null ? patrons.size() : 0;
    }
}
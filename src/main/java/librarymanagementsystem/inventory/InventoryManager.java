package librarymanagementsystem.inventory;

import librarymanagementsystem.model.Book;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Manages library inventory including tracking available and borrowed books.
 * This class follows the Single Responsibility Principle by focusing only on inventory management.
 */
public class InventoryManager {
    private static final Logger logger = Logger.getLogger(InventoryManager.class.getName());
    
    // Maps ISBN to list of patron IDs who have borrowed the book
    private Map<String, List<String>> borrowedBooks;
    // Maps patron ID to list of ISBNs they have borrowed
    private Map<String, List<String>> patronBorrowedBooks;
    
    /**
     * Constructor initializes the inventory tracking data structures.
     */
    public InventoryManager() {
        this.borrowedBooks = new HashMap<>();
        this.patronBorrowedBooks = new HashMap<>();
        logger.info("Inventory manager initialized");
    }
    
    /**
     * Initializes inventory tracking for a new book.
     *
     * @param isbn The ISBN of the book to initialize
     */
    public void initializeBookInventory(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to initialize inventory for null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        borrowedBooks.putIfAbsent(isbn, new ArrayList<>());
        logger.info("Initialized inventory for book with ISBN: " + isbn);
    }
    
    /**
     * Removes inventory tracking for a book.
     *
     * @param isbn The ISBN of the book to remove from inventory
     */
    public void removeBookFromInventory(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to remove inventory for null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        borrowedBooks.remove(isbn);
        logger.info("Removed inventory for book with ISBN: " + isbn);
    }
    
    /**
     * Records a book checkout in the inventory system.
     *
     * @param isbn The ISBN of the book being checked out
     * @param patronId The ID of the patron checking out the book
     * @return true if checkout was recorded successfully, false otherwise
     */
    public boolean recordCheckout(String isbn, String patronId) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to record checkout with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (patronId == null || patronId.isEmpty()) {
            logger.warning("Attempted to record checkout with null or empty patron ID");
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        // Initialize if not already present
        borrowedBooks.putIfAbsent(isbn, new ArrayList<>());
        patronBorrowedBooks.putIfAbsent(patronId, new ArrayList<>());
        
        // Check if book is already borrowed
        if (!borrowedBooks.get(isbn).isEmpty()) {
            logger.info("Book with ISBN " + isbn + " is already borrowed");
            return false;
        }
        
        // Record the checkout
        borrowedBooks.get(isbn).add(patronId);
        patronBorrowedBooks.get(patronId).add(isbn);
        logger.info("Recorded checkout of book " + isbn + " by patron " + patronId);
        return true;
    }
    
    /**
     * Records a book return in the inventory system.
     *
     * @param isbn The ISBN of the book being returned
     * @param patronId The ID of the patron returning the book
     * @return true if return was recorded successfully, false otherwise
     */
    public boolean recordReturn(String isbn, String patronId) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to record return with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (patronId == null || patronId.isEmpty()) {
            logger.warning("Attempted to record return with null or empty patron ID");
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        List<String> borrowedBy = borrowedBooks.get(isbn);
        if (borrowedBy == null || !borrowedBy.contains(patronId)) {
            logger.warning("Patron " + patronId + " has not borrowed book with ISBN " + isbn);
            return false;
        }
        
        // Record the return
        borrowedBy.remove(patronId);
        List<String> patronBooks = patronBorrowedBooks.get(patronId);
        if (patronBooks != null) {
            patronBooks.remove(isbn);
        }
        
        logger.info("Recorded return of book " + isbn + " by patron " + patronId);
        return true;
    }
    
    /**
     * Checks if a book is available (not borrowed by anyone).
     *
     * @param isbn The ISBN of the book to check
     * @return true if the book is available, false otherwise
     */
    public boolean isBookAvailable(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }
        
        List<String> borrowedBy = borrowedBooks.get(isbn);
        return borrowedBy != null && borrowedBy.isEmpty();
    }
    
    /**
     * Gets the number of copies available for a book.
     * In this simple implementation, a book is either available (1) or not (0).
     *
     * @param isbn The ISBN of the book
     * @return 1 if available, 0 if borrowed
     */
    public int getAvailableCount(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return 0;
        }
        
        return isBookAvailable(isbn) ? 1 : 0;
    }
    
    /**
     * Gets all currently borrowed books.
     *
     * @param allBooks Map of all books in the library (ISBN -> Book)
     * @return List of borrowed books
     */
    public List<Book> getBorrowedBooks(Map<String, Book> allBooks) {
        List<Book> borrowed = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : borrowedBooks.entrySet()) {
            if (!entry.getValue().isEmpty() && allBooks.containsKey(entry.getKey())) {
                borrowed.add(allBooks.get(entry.getKey()));
            }
        }
        return borrowed;
    }
    
    /**
     * Gets all currently available books.
     *
     * @param allBooks Map of all books in the library (ISBN -> Book)
     * @return List of available books
     */
    public List<Book> getAvailableBooks(Map<String, Book> allBooks) {
        List<Book> available = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : borrowedBooks.entrySet()) {
            if (entry.getValue().isEmpty() && allBooks.containsKey(entry.getKey())) {
                available.add(allBooks.get(entry.getKey()));
            }
        }
        return available;
    }
    
    /**
     * Gets the IDs of patrons who have borrowed a specific book.
     *
     * @param isbn The ISBN of the book
     * @return List of patron IDs
     */
    public List<String> getBorrowers(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> borrowers = borrowedBooks.get(isbn);
        return borrowers != null ? new ArrayList<>(borrowers) : new ArrayList<>();
    }
    
    /**
     * Gets the ISBNs of books borrowed by a specific patron.
     *
     * @param patronId The ID of the patron
     * @return List of ISBNs
     */
    public List<String> getBorrowedBooksByPatron(String patronId) {
        if (patronId == null || patronId.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<String> books = patronBorrowedBooks.get(patronId);
        return books != null ? new ArrayList<>(books) : new ArrayList<>();
    }
}
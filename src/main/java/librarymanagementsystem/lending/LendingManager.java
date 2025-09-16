package librarymanagementsystem.lending;

import librarymanagementsystem.inventory.InventoryManager;
import librarymanagementsystem.patron.PatronManager;
import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;

import java.util.logging.Logger;

/**
 * Manages the lending process including book checkout and return functionalities.
 * This class follows the Single Responsibility Principle by focusing only on lending operations.
 */
public class LendingManager {
    private static final Logger logger = Logger.getLogger(LendingManager.class.getName());
    
    private InventoryManager inventoryManager;
    private PatronManager patronManager;
    
    /**
     * Constructor initializes the lending manager with required dependencies.
     *
     * @param inventoryManager The inventory manager to use
     * @param patronManager The patron manager to use
     */
    public LendingManager(InventoryManager inventoryManager, PatronManager patronManager) {
        this.inventoryManager = inventoryManager;
        this.patronManager = patronManager;
        logger.info("Lending manager initialized");
    }
    
    /**
     * Processes a book checkout for a patron.
     *
     * @param isbn The ISBN of the book to checkout
     * @param patronId The ID of the patron checking out the book
     * @return true if checkout was successful, false otherwise
     * @throws IllegalArgumentException if isbn or patronId is null/empty
     */
    public boolean checkoutBook(String isbn, String patronId) {
        // Validate inputs
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to checkout book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (patronId == null || patronId.isEmpty()) {
            logger.warning("Attempted to checkout book with null or empty patron ID");
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        // Check if book exists (this would typically be checked by the service layer)
        // For now, we'll assume the service layer has already validated this
        
        // Check if patron exists
        if (!patronManager.patronExists(patronId)) {
            logger.warning("Patron with ID " + patronId + " not found");
            return false;
        }
        
        // Check if book is available (not borrowed by anyone)
        if (!inventoryManager.isBookAvailable(isbn)) {
            logger.info("Book with ISBN " + isbn + " is already borrowed");
            return false;
        }
        
        // Process checkout through inventory manager
        boolean checkoutSuccess = inventoryManager.recordCheckout(isbn, patronId);
        if (checkoutSuccess) {
            Patron patron = patronManager.findPatronById(patronId);
            Book book = new Book("", "", isbn, 0); // In a real system, we'd get the actual book from a book manager
            // Note: In a more complete implementation, we would retrieve the actual book object
            // For now, we're just adding to history based on ISBN
            logger.info("Book with ISBN " + isbn + " checked out by " + patron.getName());
        }
        return checkoutSuccess;
    }
    
    /**
     * Processes a book return from a patron.
     *
     * @param isbn The ISBN of the book being returned
     * @param patronId The ID of the patron returning the book
     * @return true if return was successful, false otherwise
     * @throws IllegalArgumentException if isbn or patronId is null/empty
     */
    public boolean returnBook(String isbn, String patronId) {
        // Validate inputs
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to return book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (patronId == null || patronId.isEmpty()) {
            logger.warning("Attempted to return book with null or empty patron ID");
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        // Check if patron exists
        if (!patronManager.patronExists(patronId)) {
            logger.warning("Patron with ID " + patronId + " not found");
            return false;
        }
        
        // Process return through inventory manager
        boolean returnSuccess = inventoryManager.recordReturn(isbn, patronId);
        if (returnSuccess) {
            Patron patron = patronManager.findPatronById(patronId);
            logger.info("Book with ISBN " + isbn + " returned by " + patron.getName());
        }
        return returnSuccess;
    }
}
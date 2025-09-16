package librarymanagementsystem.patron;

import librarymanagementsystem.model.Patron;
import librarymanagementsystem.model.Book;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Manages library patrons including adding, updating, and tracking borrowing history.
 * This class follows the Single Responsibility Principle by focusing only on patron management.
 */
public class PatronManager {
    private static final Logger logger = Logger.getLogger(PatronManager.class.getName());
    
    // Map of patrons by patron ID
    private Map<String, Patron> patrons;
    
    /**
     * Constructor initializes the patron data structure.
     */
    public PatronManager() {
        this.patrons = new HashMap<>();
        logger.info("Patron manager initialized");
    }
    
    /**
     * Adds a new patron to the system.
     *
     * @param patron The patron to add
     * @throws IllegalArgumentException if patron is null or already exists
     */
    public void addPatron(Patron patron) {
        if (patron == null) {
            logger.warning("Attempted to add null patron");
            throw new IllegalArgumentException("Patron cannot be null");
        }
        
        if (patrons.containsKey(patron.getPatronId())) {
            logger.warning("Patron with ID " + patron.getPatronId() + " already exists");
            throw new IllegalArgumentException("Patron with ID " + patron.getPatronId() + " already exists");
        }
        
        patrons.put(patron.getPatronId(), patron);
        logger.info("Added patron: " + patron.getName());
    }
    
    /**
     * Updates an existing patron's information.
     *
     * @param patronId The ID of the patron to update
     * @param updatedPatron The updated patron information
     * @throws IllegalArgumentException if patronId is null/empty or patron doesn't exist
     */
    public void updatePatron(String patronId, Patron updatedPatron) {
        if (patronId == null || patronId.isEmpty()) {
            logger.warning("Attempted to update patron with null or empty ID");
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        if (updatedPatron == null) {
            logger.warning("Attempted to update with null patron");
            throw new IllegalArgumentException("Updated patron cannot be null");
        }
        
        if (!patrons.containsKey(patronId)) {
            logger.warning("Patron with ID " + patronId + " not found");
            throw new IllegalArgumentException("Patron with ID " + patronId + " not found");
        }
        
        patrons.put(patronId, updatedPatron);
        logger.info("Updated patron with ID: " + patronId);
    }
    
    /**
     * Finds a patron by their ID.
     *
     * @param patronId The ID of the patron to find
     * @return The patron with the given ID, or null if not found
     * @throws IllegalArgumentException if patronId is null or empty
     */
    public Patron findPatronById(String patronId) {
        if (patronId == null || patronId.isEmpty()) {
            logger.warning("Attempted to find patron with null or empty ID");
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        return patrons.get(patronId);
    }
    
    /**
     * Gets all patrons in the system.
     *
     * @return A list of all patrons
     */
    public List<Patron> getAllPatrons() {
        return new ArrayList<>(patrons.values());
    }
    
    /**
     * Checks if a patron exists in the system.
     *
     * @param patronId The ID of the patron to check
     * @return true if the patron exists, false otherwise
     */
    public boolean patronExists(String patronId) {
        if (patronId == null || patronId.isEmpty()) {
            return false;
        }
        
        return patrons.containsKey(patronId);
    }
    
    /**
     * Adds a book to a patron's borrowing history.
     *
     * @param patronId The ID of the patron
     * @param book The book to add to borrowing history
     * @throws IllegalArgumentException if patronId is null/empty or book is null
     */
    public void addToBorrowingHistory(String patronId, Book book) {
        if (patronId == null || patronId.isEmpty()) {
            logger.warning("Attempted to add to borrowing history with null or empty patron ID");
            throw new IllegalArgumentException("Patron ID cannot be null or empty");
        }
        
        if (book == null) {
            logger.warning("Attempted to add null book to borrowing history");
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        Patron patron = patrons.get(patronId);
        if (patron == null) {
            logger.warning("Patron with ID " + patronId + " not found");
            throw new IllegalArgumentException("Patron with ID " + patronId + " not found");
        }
        
        patron.addToBorrowingHistory(book);
        logger.info("Added book '" + book.getTitle() + "' to borrowing history of patron " + patron.getName());
    }
}
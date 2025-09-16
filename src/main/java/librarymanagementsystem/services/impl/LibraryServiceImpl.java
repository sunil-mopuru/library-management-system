package librarymanagementsystem.services.impl;

import librarymanagementsystem.inventory.InventoryManager;
import librarymanagementsystem.patron.PatronManager;
import librarymanagementsystem.lending.LendingManager;
import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;
import librarymanagementsystem.services.LibraryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of the LibraryService interface.
 * This class demonstrates several SOLID principles:
 * - Single Responsibility Principle: Focuses only on library operations
 * - Open/Closed Principle: Can be extended without modification
 * - Dependency Inversion Principle: Depends on abstractions (LibraryService interface)
 */
public class LibraryServiceImpl implements LibraryService {
    private static final Logger logger = Logger.getLogger(LibraryServiceImpl.class.getName());
    
    // Using maps for efficient lookups - demonstrating appropriate data structure usage
    private Map<String, Book> books; // ISBN -> Book
    private InventoryManager inventoryManager; // Manages book availability and borrowing
    private PatronManager patronManager; // Manages patrons
    private LendingManager lendingManager; // Manages lending operations
    
    /**
     * Constructor initializes the data structures.
     */
    public LibraryServiceImpl() {
        this.books = new HashMap<>();
        this.inventoryManager = new InventoryManager();
        this.patronManager = new PatronManager();
        this.lendingManager = new LendingManager(inventoryManager, patronManager);
        logger.info("Library service initialized");
    }
    
    // Book Management
    
    @Override
    public void addBook(Book book) {
        if (book == null) {
            logger.warning("Attempted to add null book");
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        if (books.containsKey(book.getIsbn())) {
            logger.warning("Book with ISBN " + book.getIsbn() + " already exists");
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        books.put(book.getIsbn(), book);
        inventoryManager.initializeBookInventory(book.getIsbn());
        logger.info("Added book: " + book.getTitle());
    }
    
    @Override
    public void removeBook(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to remove book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (!books.containsKey(isbn)) {
            logger.warning("Book with ISBN " + isbn + " not found");
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found");
        }
        
        // Check if book is currently borrowed
        if (!inventoryManager.isBookAvailable(isbn)) {
            logger.warning("Cannot remove book with ISBN " + isbn + " as it is currently borrowed");
            throw new IllegalStateException("Cannot remove book as it is currently borrowed");
        }
        
        books.remove(isbn);
        inventoryManager.removeBookFromInventory(isbn);
        logger.info("Removed book with ISBN: " + isbn);
    }
    
    @Override
    public void updateBook(String isbn, Book updatedBook) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to update book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (updatedBook == null) {
            logger.warning("Attempted to update with null book");
            throw new IllegalArgumentException("Updated book cannot be null");
        }
        
        if (!books.containsKey(isbn)) {
            logger.warning("Book with ISBN " + isbn + " not found");
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found");
        }
        
        books.put(isbn, updatedBook);
        logger.info("Updated book with ISBN: " + isbn);
    }
    
    @Override
    public Book findBookByIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to find book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        return books.get(isbn);
    }
    
    @Override
    public List<Book> findBooksByTitle(String title) {
        if (title == null || title.isEmpty()) {
            logger.warning("Attempted to find books with null or empty title");
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        
        return books.values().stream()
                .filter(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> findBooksByAuthor(String author) {
        if (author == null || author.isEmpty()) {
            logger.warning("Attempted to find books with null or empty author");
            throw new IllegalArgumentException("Author cannot be null or empty");
        }
        
        return books.values().stream()
                .filter(book -> book.getAuthor().toLowerCase().contains(author.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    // Patron Management
    
    @Override
    public void addPatron(Patron patron) {
        patronManager.addPatron(patron);
    }
    
    @Override
    public void updatePatron(String patronId, Patron updatedPatron) {
        patronManager.updatePatron(patronId, updatedPatron);
    }
    
    @Override
    public Patron findPatronById(String patronId) {
        return patronManager.findPatronById(patronId);
    }
    
    @Override
    public List<Patron> getAllPatrons() {
        return patronManager.getAllPatrons();
    }
    
    // Lending Process
    
    @Override
    public boolean checkoutBook(String isbn, String patronId) {
        // First, we need to add the book to the patron's borrowing history
        // This is a bit of a design challenge since we need to get the actual book object
        Book book = books.get(isbn);
        if (book != null) {
            Patron patron = patronManager.findPatronById(patronId);
            if (patron != null) {
                patron.addToBorrowingHistory(book);
            }
        }
        
        // Then process the checkout through the lending manager
        return lendingManager.checkoutBook(isbn, patronId);
    }
    
    @Override
    public boolean returnBook(String isbn, String patronId) {
        return lendingManager.returnBook(isbn, patronId);
    }
    
    // Inventory Management
    
    @Override
    public int getAvailableBookCount(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            logger.warning("Attempted to get available count for book with null or empty ISBN");
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        if (!books.containsKey(isbn)) {
            logger.warning("Book with ISBN " + isbn + " not found");
            return 0;
        }
        
        return inventoryManager.getAvailableCount(isbn);
    }
    
    @Override
    public List<Book> getAvailableBooks() {
        return inventoryManager.getAvailableBooks(books);
    }
    
    @Override
    public List<Book> getBorrowedBooks() {
        return inventoryManager.getBorrowedBooks(books);
    }
}
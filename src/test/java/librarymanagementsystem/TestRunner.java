package librarymanagementsystem;

import librarymanagementsystem.inventory.InventoryManager;
import librarymanagementsystem.patron.PatronManager;
import librarymanagementsystem.lending.LendingManager;
import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;
import librarymanagementsystem.services.LibraryService;
import librarymanagementsystem.services.impl.LibraryServiceImpl;

/**
 * Simple test runner to demonstrate testing.
 * In a real project, we would use a testing framework like JUnit.
 */
public class TestRunner {
    public static void main(String[] args) {
        System.out.println("Running tests for Library Management System...");
        
        // Run Book tests
        runBookTests();
        
        // Run LibraryService tests
        runLibraryServiceTests();
        
        // Run InventoryManager tests
        runInventoryManagerTests();
        
        // Run PatronManager tests
        runPatronManagerTests();
        
        // Run LendingManager tests
        runLendingManagerTests();
        
        System.out.println("All tests completed.");
    }
    
    private static void runBookTests() {
        System.out.println("\n=== Running Book Tests ===");
        
        // Test book creation
        Book book = new Book("Test Title", "Test Author", "123-456-789", 2023);
        assert book.getTitle().equals("Test Title") : "Book title mismatch";
        assert book.getAuthor().equals("Test Author") : "Book author mismatch";
        assert book.getIsbn().equals("123-456-789") : "Book ISBN mismatch";
        assert book.getPublicationYear() == 2023 : "Book publication year mismatch";
        System.out.println("Book creation test passed.");
        
        // Test book equality
        Book book2 = new Book("Test Title", "Test Author", "123-456-789", 2023);
        assert book.equals(book2) : "Books with same ISBN should be equal";
        System.out.println("Book equality test passed.");
        
        System.out.println("All Book tests passed.");
    }
    
    private static void runLibraryServiceTests() {
        System.out.println("\n=== Running LibraryService Tests ===");
        
        LibraryService libraryService = new LibraryServiceImpl();
        Book testBook = new Book("Test Book", "Test Author", "123-456-789", 2023);
        Patron testPatron = new Patron("Test Patron", "test@example.com", "123-456-7890", "P001");
        
        // Test add book
        libraryService.addBook(testBook);
        Book foundBook = libraryService.findBookByIsbn("123-456-789");
        assert foundBook != null : "Book should be found after adding";
        assert foundBook.equals(testBook) : "Found book should match added book";
        System.out.println("Add book test passed.");
        
        // Test add patron
        libraryService.addPatron(testPatron);
        Patron foundPatron = libraryService.findPatronById("P001");
        assert foundPatron != null : "Patron should be found after adding";
        assert foundPatron.equals(testPatron) : "Found patron should match added patron";
        System.out.println("Add patron test passed.");
        
        // Test checkout book
        boolean checkoutResult = libraryService.checkoutBook("123-456-789", "P001");
        assert checkoutResult : "Checkout should succeed";
        assert libraryService.getBorrowedBooks().size() == 1 : "Should have one borrowed book";
        System.out.println("Checkout book test passed.");
        
        // Test return book
        boolean returnResult = libraryService.returnBook("123-456-789", "P001");
        assert returnResult : "Return should succeed";
        assert libraryService.getBorrowedBooks().size() == 0 : "Should have no borrowed books after return";
        assert libraryService.getAvailableBooks().size() == 1 : "Should have one available book after return";
        System.out.println("Return book test passed.");
        
        System.out.println("All LibraryService tests passed.");
    }
    
    private static void runInventoryManagerTests() {
        System.out.println("\n=== Running InventoryManager Tests ===");
        
        InventoryManager inventoryManager = new InventoryManager();
        inventoryManager.initializeBookInventory("123-456-789");
        
        // Test book availability
        assert inventoryManager.isBookAvailable("123-456-789") : "Book should be available initially";
        System.out.println("Book availability test passed.");
        
        // Test checkout recording
        boolean checkoutSuccess = inventoryManager.recordCheckout("123-456-789", "P001");
        assert checkoutSuccess : "Checkout recording should succeed";
        assert !inventoryManager.isBookAvailable("123-456-789") : "Book should not be available after checkout";
        System.out.println("Checkout recording test passed.");
        
        // Test return recording
        boolean returnSuccess = inventoryManager.recordReturn("123-456-789", "P001");
        assert returnSuccess : "Return recording should succeed";
        assert inventoryManager.isBookAvailable("123-456-789") : "Book should be available after return";
        System.out.println("Return recording test passed.");
        
        System.out.println("All InventoryManager tests passed.");
    }
    
    private static void runPatronManagerTests() {
        System.out.println("\n=== Running PatronManager Tests ===");
        
        PatronManager patronManager = new PatronManager();
        Patron testPatron = new Patron("Test Patron", "test@example.com", "123-456-7890", "P001");
        
        // Test add patron
        patronManager.addPatron(testPatron);
        assert patronManager.patronExists("P001") : "Patron should exist after adding";
        System.out.println("Add patron test passed.");
        
        // Test find patron
        Patron foundPatron = patronManager.findPatronById("P001");
        assert foundPatron != null : "Patron should be found";
        assert foundPatron.equals(testPatron) : "Found patron should match added patron";
        System.out.println("Find patron test passed.");
        
        // Test update patron
        Patron updatedPatron = new Patron("Updated Patron", "updated@example.com", "098-765-4321", "P001");
        patronManager.updatePatron("P001", updatedPatron);
        Patron foundUpdatedPatron = patronManager.findPatronById("P001");
        assert foundUpdatedPatron.getName().equals("Updated Patron") : "Patron name should be updated";
        System.out.println("Update patron test passed.");
        
        System.out.println("All PatronManager tests passed.");
    }
    
    private static void runLendingManagerTests() {
        System.out.println("\n=== Running LendingManager Tests ===");
        
        InventoryManager inventoryManager = new InventoryManager();
        PatronManager patronManager = new PatronManager();
        LendingManager lendingManager = new LendingManager(inventoryManager, patronManager);
        
        // Set up test data
        inventoryManager.initializeBookInventory("123-456-789");
        Patron testPatron = new Patron("Test Patron", "test@example.com", "123-456-7890", "P001");
        patronManager.addPatron(testPatron);
        
        // Test checkout
        boolean checkoutSuccess = lendingManager.checkoutBook("123-456-789", "P001");
        assert checkoutSuccess : "Checkout should succeed";
        assert !inventoryManager.isBookAvailable("123-456-789") : "Book should not be available after checkout";
        System.out.println("Checkout test passed.");
        
        // Test return
        boolean returnSuccess = lendingManager.returnBook("123-456-789", "P001");
        assert returnSuccess : "Return should succeed";
        assert inventoryManager.isBookAvailable("123-456-789") : "Book should be available after return";
        System.out.println("Return test passed.");
        
        System.out.println("All LendingManager tests passed.");
    }
}
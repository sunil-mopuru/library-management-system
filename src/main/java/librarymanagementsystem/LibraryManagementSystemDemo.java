package librarymanagementsystem;

import librarymanagementsystem.branches.LibraryBranch;
import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;
import librarymanagementsystem.recommendations.RecommendationSystem;
import librarymanagementsystem.reservation.ReservationSystem;
import librarymanagementsystem.services.LibraryService;
import librarymanagementsystem.services.impl.LibraryServiceImpl;
import librarymanagementsystem.utils.BookFactory;

import java.util.List;
import java.util.logging.Logger;

/**
 * Demonstration of the Library Management System.
 */
public class LibraryManagementSystemDemo {
    private static final Logger logger = Logger.getLogger(LibraryManagementSystemDemo.class.getName());
    
    public static void main(String[] args) {
        System.out.println("=== Library Management System Demo ===\n");
        
        // Create library service
        LibraryService libraryService = new LibraryServiceImpl();
        
        // Create reservation system
        ReservationSystem reservationSystem = new ReservationSystem();
        
        // Create recommendation system
        RecommendationSystem recommendationSystem = new RecommendationSystem();
        
        // Create books using factory pattern
        System.out.println("1. Creating books using Factory pattern...");
        Book book1 = BookFactory.createBook("The God of Small Things", "Arundhati Roy", "978-0-679-45732-2", 1997, BookFactory.BookType.FICTION);
        Book book2 = BookFactory.createBook("A Suitable Boy", "Vikram Seth", "978-0-06-055755-8", 1993, BookFactory.BookType.FICTION);
        Book book3 = BookFactory.createBook("The Wonder That Was India", "A.L. Basham", "978-81-292-0194-3", 1954, BookFactory.BookType.NON_FICTION);
        Book book4 = BookFactory.createBook("Midnight's Children", "Salman Rushdie", "978-0-09-957831-6", 1981, BookFactory.BookType.FICTION);
        
        // Add books to library
        try {
            libraryService.addBook(book1);
            libraryService.addBook(book2);
            libraryService.addBook(book3);
            libraryService.addBook(book4);
            System.out.println("Books added successfully.\n");
        } catch (Exception e) {
            System.err.println("Error adding books: " + e.getMessage());
        }
        
        System.out.println("2. Creating patrons...");
        Patron patron1 = new Patron("Aarav Patel", "aarav@example.com", "555-1234", "P001");
        Patron patron2 = new Patron("Priya Sharma", "priya@example.com", "555-5678", "P002");
        
        // Add patrons to library
        try {
            libraryService.addPatron(patron1);
            libraryService.addPatron(patron2);
            System.out.println("Patrons added successfully.\n");
        } catch (Exception e) {
            System.err.println("Error adding patrons: " + e.getMessage());
        }
        
        // Search for books
        System.out.println("3. Searching for books...");
        List<Book> godOfSmallThingsBooks = libraryService.findBooksByTitle("God of Small Things");
        System.out.println("Books with 'God of Small Things' in title: " + godOfSmallThingsBooks.size());
        
        List<Book> rushdieBooks = libraryService.findBooksByAuthor("Salman Rushdie");
        System.out.println("Books by Salman Rushdie: " + rushdieBooks.size());
        System.out.println();
        
        // Checkout books
        System.out.println("4. Checking out books...");
        boolean checkout1 = libraryService.checkoutBook("978-0-679-45732-2", "P001"); // Aarav checks out The God of Small Things
        System.out.println("Aarav checking out 'The God of Small Things': " + (checkout1 ? "Success" : "Failed"));
        
        boolean checkout2 = libraryService.checkoutBook("978-0-09-957831-6", "P002"); // Priya checks out Midnight's Children
        System.out.println("Priya checking out 'Midnight's Children': " + (checkout2 ? "Success" : "Failed"));
        
        // Try to checkout already borrowed book
        boolean checkout3 = libraryService.checkoutBook("978-0-679-45732-2", "P002"); // Priya tries to check out The God of Small Things
        System.out.println("Priya trying to check out 'The God of Small Things' (already borrowed): " + (checkout3 ? "Success" : "Failed"));
        System.out.println();
        
        // Show available and borrowed books
        System.out.println("5. Library inventory status...");
        List<Book> availableBooks = libraryService.getAvailableBooks();
        System.out.println("Available books: " + availableBooks.size());
        
        List<Book> borrowedBooks = libraryService.getBorrowedBooks();
        System.out.println("Borrowed books: " + borrowedBooks.size());
        System.out.println();
        
        // Return a book
        System.out.println("6. Returning books...");
        boolean return1 = libraryService.returnBook("978-0-679-45732-2", "P001"); // Aarav returns The God of Small Things
        System.out.println("Aarav returning 'The God of Small Things': " + (return1 ? "Success" : "Failed"));
        System.out.println();
        
        // Show updated inventory
        System.out.println("7. Updated library inventory...");
        availableBooks = libraryService.getAvailableBooks();
        System.out.println("Available books: " + availableBooks.size());
        
        borrowedBooks = libraryService.getBorrowedBooks();
        System.out.println("Borrowed books: " + borrowedBooks.size());
        System.out.println();
        
        // Demonstrate reservation system
        System.out.println("8. Reservation system...");
        // Priya reserves The God of Small Things
        boolean reserved = reservationSystem.reserveBook("978-0-679-45732-2", patron2);
        System.out.println("Priya reserving 'The God of Small Things': " + (reserved ? "Success" : "Failed"));
        
        // Aarav tries to borrow the reserved book
        boolean checkout4 = libraryService.checkoutBook("978-0-679-45732-2", "P001");
        System.out.println("Aarav trying to check out 'The God of Small Things' (reserved by Priya): " + (checkout4 ? "Success" : "Failed"));
        
        // Book becomes available again
        reservationSystem.notifyBookAvailable("978-0-679-45732-2");
        System.out.println();
        
        // Demonstrate recommendation system
        System.out.println("9. Recommendation system...");
        // Update preferences based on borrowing history
        recommendationSystem.updatePreferences(patron1);
        recommendationSystem.updatePreferences(patron2);
        
        // Generate recommendations for Aarav
        List<Book> recommendations = recommendationSystem.generateRecommendations(patron1, libraryService, 3);
        System.out.println("Recommendations for Aarav:");
        for (Book book : recommendations) {
            System.out.println("  - " + book.getTitle() + " by " + book.getAuthor());
        }
        System.out.println();
        
        // Demonstrate multi-branch system
        System.out.println("10. Multi-branch system...");
        LibraryBranch branch1 = new LibraryBranch("B001", "Mumbai Central Branch", "123 Marine Drive");
        LibraryBranch branch2 = new LibraryBranch("B002", "Delhi Branch", "456 Connaught Place");
        
        // Add some books to branch 1
        Book branchBook1 = new Book("The White Tiger", "Aravind Adiga", "978-1-4165-6259-6", 2008);
        Book branchBook2 = new Book("The Inheritance of Loss", "Kiran Desai", "978-0-307-26316-5", 2006);
        
        branch1.getLibraryService().addBook(branchBook1);
        branch1.getLibraryService().addBook(branchBook2);
        
        System.out.println("Branch 1 books: " + branch1.getLibraryService().getAllBooks().size());
        System.out.println("Branch 2 books: " + branch2.getLibraryService().getAllBooks().size());
        
        // Transfer a book from branch 1 to branch 2
        boolean transferred = branch1.transferBook(branchBook1, branch2);
        System.out.println("Transferring 'The White Tiger' from Mumbai Central Branch to Delhi Branch: " + (transferred ? "Success" : "Failed"));
        
        System.out.println("Branch 1 books after transfer: " + branch1.getLibraryService().getAllBooks().size());
        System.out.println("Branch 2 books after transfer: " + branch2.getLibraryService().getAllBooks().size());
        
        System.out.println("\n=== Demo completed ===");
    }
}
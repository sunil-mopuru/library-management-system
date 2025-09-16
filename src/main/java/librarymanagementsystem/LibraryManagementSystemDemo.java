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
        Book book1 = BookFactory.createBook("The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5", 1925, BookFactory.BookType.FICTION);
        Book book2 = BookFactory.createBook("To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4", 1960, BookFactory.BookType.FICTION);
        Book book3 = BookFactory.createBook("A Brief History of Time", "Stephen Hawking", "978-0-553-38016-3", 1988, BookFactory.BookType.NON_FICTION);
        Book book4 = BookFactory.createBook("1984", "George Orwell", "978-0-452-28423-4", 1949, BookFactory.BookType.FICTION);
        
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
        
        // Create patrons
        System.out.println("2. Creating patrons...");
        Patron patron1 = new Patron("Alice Johnson", "alice@example.com", "555-1234", "P001");
        Patron patron2 = new Patron("Bob Smith", "bob@example.com", "555-5678", "P002");
        
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
        List<Book> gatsbyBooks = libraryService.findBooksByTitle("Great Gatsby");
        System.out.println("Books with 'Great Gatsby' in title: " + gatsbyBooks.size());
        
        List<Book> fictionBooks = libraryService.findBooksByAuthor("George Orwell");
        System.out.println("Books by George Orwell: " + fictionBooks.size());
        System.out.println();
        
        // Checkout books
        System.out.println("4. Checking out books...");
        boolean checkout1 = libraryService.checkoutBook("978-0-7432-7356-5", "P001"); // Alice checks out The Great Gatsby
        System.out.println("Alice checking out 'The Great Gatsby': " + (checkout1 ? "Success" : "Failed"));
        
        boolean checkout2 = libraryService.checkoutBook("978-0-452-28423-4", "P002"); // Bob checks out 1984
        System.out.println("Bob checking out '1984': " + (checkout2 ? "Success" : "Failed"));
        
        // Try to checkout already borrowed book
        boolean checkout3 = libraryService.checkoutBook("978-0-7432-7356-5", "P002"); // Bob tries to check out The Great Gatsby
        System.out.println("Bob trying to check out 'The Great Gatsby' (already borrowed): " + (checkout3 ? "Success" : "Failed"));
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
        boolean return1 = libraryService.returnBook("978-0-7432-7356-5", "P001"); // Alice returns The Great Gatsby
        System.out.println("Alice returning 'The Great Gatsby': " + (return1 ? "Success" : "Failed"));
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
        // Bob reserves The Great Gatsby
        boolean reserved = reservationSystem.reserveBook("978-0-7432-7356-5", patron2);
        System.out.println("Bob reserving 'The Great Gatsby': " + (reserved ? "Success" : "Failed"));
        
        // Alice tries to borrow the reserved book
        boolean checkout4 = libraryService.checkoutBook("978-0-7432-7356-5", "P001");
        System.out.println("Alice trying to check out 'The Great Gatsby' (reserved by Bob): " + (checkout4 ? "Success" : "Failed"));
        
        // Book becomes available again
        reservationSystem.notifyBookAvailable("978-0-7432-7356-5");
        System.out.println();
        
        // Demonstrate recommendation system
        System.out.println("9. Recommendation system...");
        // Update preferences based on borrowing history
        recommendationSystem.updatePreferences(patron1);
        recommendationSystem.updatePreferences(patron2);
        
        // Generate recommendations for Alice
        List<Book> recommendations = recommendationSystem.generateRecommendations(patron1, libraryService, 3);
        System.out.println("Recommendations for Alice:");
        for (Book book : recommendations) {
            System.out.println("  - " + book.getTitle() + " by " + book.getAuthor());
        }
        System.out.println();
        
        // Demonstrate multi-branch system
        System.out.println("10. Multi-branch system...");
        LibraryBranch branch1 = new LibraryBranch("B001", "Downtown Branch", "123 Main St");
        LibraryBranch branch2 = new LibraryBranch("B002", "Uptown Branch", "456 Oak Ave");
        
        // Add some books to branch 1
        Book branchBook1 = new Book("The Catcher in the Rye", "J.D. Salinger", "978-0-316-76948-0", 1951);
        Book branchBook2 = new Book("Pride and Prejudice", "Jane Austen", "978-0-14-143951-8", 1813);
        
        branch1.getLibraryService().addBook(branchBook1);
        branch1.getLibraryService().addBook(branchBook2);
        
        System.out.println("Branch 1 books: " + branch1.getLibraryService().getAllBooks().size());
        System.out.println("Branch 2 books: " + branch2.getLibraryService().getAllBooks().size());
        
        // Transfer a book from branch 1 to branch 2
        boolean transferred = branch1.transferBook(branchBook1, branch2);
        System.out.println("Transferring 'The Catcher in the Rye' from Branch 1 to Branch 2: " + (transferred ? "Success" : "Failed"));
        
        System.out.println("Branch 1 books after transfer: " + branch1.getLibraryService().getAllBooks().size());
        System.out.println("Branch 2 books after transfer: " + branch2.getLibraryService().getAllBooks().size());
        
        System.out.println("\n=== Demo completed ===");
    }
}
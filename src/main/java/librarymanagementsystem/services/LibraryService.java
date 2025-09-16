package librarymanagementsystem.services;

import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;
import java.util.List;

/**
 * Interface defining the core operations for a library management system.
 * This demonstrates the Interface Segregation Principle (ISP) from SOLID.
 */
public interface LibraryService {
    
    // Book Management
    void addBook(Book book);
    void removeBook(String isbn);
    void updateBook(String isbn, Book updatedBook);
    Book findBookByIsbn(String isbn);
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByAuthor(String author);
    List<Book> getAllBooks();
    
    // Patron Management
    void addPatron(Patron patron);
    void updatePatron(String patronId, Patron updatedPatron);
    Patron findPatronById(String patronId);
    List<Patron> getAllPatrons();
    
    // Lending Process
    boolean checkoutBook(String isbn, String patronId);
    boolean returnBook(String isbn, String patronId);
    
    // Inventory Management
    int getAvailableBookCount(String isbn);
    List<Book> getAvailableBooks();
    List<Book> getBorrowedBooks();
}
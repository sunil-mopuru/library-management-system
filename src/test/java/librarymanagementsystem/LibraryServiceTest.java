package librarymanagementsystem;

import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;
import librarymanagementsystem.services.LibraryService;
import librarymanagementsystem.services.impl.LibraryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Unit tests for the LibraryService implementation.
 */
public class LibraryServiceTest {
    
    private LibraryService libraryService;
    private Book testBook;
    private Patron testPatron;
    
    @BeforeEach
    public void setUp() {
        libraryService = new LibraryServiceImpl();
        testBook = new Book("Test Book", "Test Author", "123-456-789", 2023);
        testPatron = new Patron("Test Patron", "test@example.com", "123-456-7890", "P001");
    }
    
    @Test
    public void testAddBook() {
        // When
        libraryService.addBook(testBook);
        
        // Then
        Book foundBook = libraryService.findBookByIsbn("123-456-789");
        assertNotNull(foundBook);
        assertEquals(testBook, foundBook);
    }
    
    @Test
    public void testAddDuplicateBook() {
        // Given
        libraryService.addBook(testBook);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            libraryService.addBook(testBook);
        });
    }
    
    @Test
    public void testRemoveBook() {
        // Given
        libraryService.addBook(testBook);
        
        // When
        libraryService.removeBook("123-456-789");
        
        // Then
        Book foundBook = libraryService.findBookByIsbn("123-456-789");
        assertNull(foundBook);
    }
    
    @Test
    public void testRemoveNonExistentBook() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            libraryService.removeBook("non-existent-isbn");
        });
    }
    
    @Test
    public void testAddPatron() {
        // When
        libraryService.addPatron(testPatron);
        
        // Then
        Patron foundPatron = libraryService.findPatronById("P001");
        assertNotNull(foundPatron);
        assertEquals(testPatron, foundPatron);
    }
    
    @Test
    public void testCheckoutBook() {
        // Given
        libraryService.addBook(testBook);
        libraryService.addPatron(testPatron);
        
        // When
        boolean result = libraryService.checkoutBook("123-456-789", "P001");
        
        // Then
        assertTrue(result);
        List<Book> borrowedBooks = libraryService.getBorrowedBooks();
        assertEquals(1, borrowedBooks.size());
        assertEquals(testBook, borrowedBooks.get(0));
    }
    
    @Test
    public void testReturnBook() {
        // Given
        libraryService.addBook(testBook);
        libraryService.addPatron(testPatron);
        libraryService.checkoutBook("123-456-789", "P001");
        
        // When
        boolean result = libraryService.returnBook("123-456-789", "P001");
        
        // Then
        assertTrue(result);
        List<Book> borrowedBooks = libraryService.getBorrowedBooks();
        assertEquals(0, borrowedBooks.size());
        List<Book> availableBooks = libraryService.getAvailableBooks();
        assertEquals(1, availableBooks.size());
    }
    
    @Test
    public void testFindBooksByAuthor() {
        // Given
        Book book1 = new Book("Book 1", "Author A", "ISBN1", 2023);
        Book book2 = new Book("Book 2", "Author A", "ISBN2", 2023);
        Book book3 = new Book("Book 3", "Author B", "ISBN3", 2023);
        
        libraryService.addBook(book1);
        libraryService.addBook(book2);
        libraryService.addBook(book3);
        
        // When
        List<Book> booksByAuthorA = libraryService.findBooksByAuthor("Author A");
        
        // Then
        assertEquals(2, booksByAuthorA.size());
        assertTrue(booksByAuthorA.contains(book1));
        assertTrue(booksByAuthorA.contains(book2));
    }
}
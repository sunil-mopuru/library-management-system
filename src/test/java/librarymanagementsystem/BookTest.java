package librarymanagementsystem;

import librarymanagementsystem.model.Book;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Book class.
 */
public class BookTest {
    
    @Test
    public void testBookCreation() {
        // Given
        String title = "Test Book";
        String author = "Test Author";
        String isbn = "123-456-789";
        int publicationYear = 2023;
        
        // When
        Book book = new Book(title, author, isbn, publicationYear);
        
        // Then
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(isbn, book.getIsbn());
        assertEquals(publicationYear, book.getPublicationYear());
    }
    
    @Test
    public void testBookEquality() {
        // Given
        Book book1 = new Book("Title", "Author", "ISBN", 2023);
        Book book2 = new Book("Title", "Author", "ISBN", 2023);
        Book book3 = new Book("Different Title", "Author", "ISBN", 2023);
        
        // When & Then
        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
        assertEquals(book1, book3); // Should be equal because ISBN is the same
    }
    
    @Test
    public void testBookSetters() {
        // Given
        Book book = new Book("Original Title", "Original Author", "ISBN", 2023);
        
        // When
        book.setTitle("New Title");
        book.setAuthor("New Author");
        book.setIsbn("New ISBN");
        book.setPublicationYear(2024);
        
        // Then
        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
        assertEquals("New ISBN", book.getIsbn());
        assertEquals(2024, book.getPublicationYear());
    }
}
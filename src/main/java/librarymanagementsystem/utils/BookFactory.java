package librarymanagementsystem.utils;

import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.FictionBook;
import librarymanagementsystem.model.NonFictionBook;
import librarymanagementsystem.model.ReferenceBook;

/**
 * Factory pattern implementation for creating different types of books.
 * This demonstrates the Factory design pattern and helps with object creation.
 */
public class BookFactory {
    
    /**
     * Creates a book based on its type.
     *
     * @param title The title of the book
     * @param author The author of the book
     * @param isbn The ISBN of the book
     * @param publicationYear The publication year of the book
     * @param type The type of book to create
     * @return A new Book instance of the specified type
     */
    public static Book createBook(String title, String author, String isbn, int publicationYear, BookType type) {
        switch (type) {
            case FICTION:
                return new FictionBook(title, author, isbn, publicationYear);
            case NON_FICTION:
                return new NonFictionBook(title, author, isbn, publicationYear);
            case REFERENCE:
                return new ReferenceBook(title, author, isbn, publicationYear);
            default:
                return new Book(title, author, isbn, publicationYear);
        }
    }
    
    /**
     * Enum representing different types of books.
     */
    public enum BookType {
        FICTION,
        NON_FICTION,
        REFERENCE,
        GENERAL
    }
}
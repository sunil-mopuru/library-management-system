package librarymanagementsystem.model;

/**
 * Represents a non-fiction book in the library.
 * This demonstrates inheritance from the base Book class.
 */
public class NonFictionBook extends Book {
    private String subject;
    
    public NonFictionBook(String title, String author, String isbn, int publicationYear) {
        super(title, author, isbn, publicationYear);
        this.subject = "Non-Fiction";
    }
    
    public String getSubject() {
        return subject;
    }
    
    @Override
    public String toString() {
        return super.toString().replace("Book{", "NonFictionBook{") +
                ", subject='" + subject + '\'' +
                '}';
    }
}
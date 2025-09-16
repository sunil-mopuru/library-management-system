package librarymanagementsystem.model;

/**
 * Represents a reference book in the library.
 * This demonstrates inheritance from the base Book class.
 * Reference books typically cannot be borrowed.
 */
public class ReferenceBook extends Book {
    private boolean isReferenceOnly;
    
    public ReferenceBook(String title, String author, String isbn, int publicationYear) {
        super(title, author, isbn, publicationYear);
        this.isReferenceOnly = true;
    }
    
    public boolean isReferenceOnly() {
        return isReferenceOnly;
    }
    
    @Override
    public String toString() {
        return super.toString().replace("Book{", "ReferenceBook{") +
                ", isReferenceOnly=" + isReferenceOnly +
                '}';
    }
}
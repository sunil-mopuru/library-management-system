package librarymanagementsystem.model;

/**
 * Represents a fiction book in the library.
 * This demonstrates inheritance from the base Book class.
 */
public class FictionBook extends Book {
    private String genre;
    
    public FictionBook(String title, String author, String isbn, int publicationYear) {
        super(title, author, isbn, publicationYear);
        this.genre = "Fiction";
    }
    
    public String getGenre() {
        return genre;
    }
    
    @Override
    public String toString() {
        return super.toString().replace("Book{", "FictionBook{") +
                ", genre='" + genre + '\'' +
                '}';
    }
}
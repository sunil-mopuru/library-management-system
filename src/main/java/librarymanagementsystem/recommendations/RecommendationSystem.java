package librarymanagementsystem.recommendations;

import librarymanagementsystem.model.Book;
import librarymanagementsystem.model.Patron;
import librarymanagementsystem.services.LibraryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Recommendation system based on patron borrowing history.
 * This demonstrates appropriate use of data structures and algorithms.
 */
public class RecommendationSystem {
    private Map<String, List<String>> authorPreferences; // Patron ID -> Authors they've borrowed
    private Map<String, List<String>> bookPreferences;   // Patron ID -> Books they've borrowed
    
    public RecommendationSystem() {
        this.authorPreferences = new HashMap<>();
        this.bookPreferences = new HashMap<>();
    }
    
    /**
     * Updates the recommendation system with a patron's borrowing history.
     *
     * @param patron The patron whose history to update
     */
    public void updatePreferences(Patron patron) {
        if (patron == null) {
            return;
        }
        
        String patronId = patron.getPatronId();
        List<Book> borrowingHistory = patron.getBorrowingHistory();
        
        // Initialize lists if they don't exist
        authorPreferences.putIfAbsent(patronId, new ArrayList<>());
        bookPreferences.putIfAbsent(patronId, new ArrayList<>());
        
        // Update preferences based on borrowing history
        for (Book book : borrowingHistory) {
            if (!authorPreferences.get(patronId).contains(book.getAuthor())) {
                authorPreferences.get(patronId).add(book.getAuthor());
            }
            
            if (!bookPreferences.get(patronId).contains(book.getIsbn())) {
                bookPreferences.get(patronId).add(book.getIsbn());
            }
        }
    }
    
    /**
     * Generates book recommendations for a patron.
     *
     * @param patron The patron to generate recommendations for
     * @param libraryService The library service to search for books
     * @param maxRecommendations The maximum number of recommendations to return
     * @return A list of recommended books
     */
    public List<Book> generateRecommendations(Patron patron, LibraryService libraryService, int maxRecommendations) {
        if (patron == null || libraryService == null || maxRecommendations <= 0) {
            return new ArrayList<>();
        }
        
        String patronId = patron.getPatronId();
        List<Book> recommendations = new ArrayList<>();
        
        // Get the patron's preferences
        List<String> preferredAuthors = authorPreferences.getOrDefault(patronId, new ArrayList<>());
        List<String> borrowedBooks = bookPreferences.getOrDefault(patronId, new ArrayList<>());
        
        // Get all books from the library
        List<Book> allBooks = libraryService.getAllBooks();
        
        // First, recommend books by the same authors
        for (String author : preferredAuthors) {
            List<Book> booksByAuthor = allBooks.stream()
                    .filter(book -> book.getAuthor().equals(author))
                    .filter(book -> !borrowedBooks.contains(book.getIsbn())) // Don't recommend already borrowed books
                    .collect(Collectors.toList());
            
            for (Book book : booksByAuthor) {
                if (recommendations.size() < maxRecommendations && !recommendations.contains(book)) {
                    recommendations.add(book);
                }
            }
        }
        
        // If we still need more recommendations, add some popular books
        if (recommendations.size() < maxRecommendations) {
            // In a real system, we might track popularity by borrowing frequency
            // For now, we'll just add some books that haven't been borrowed by this patron
            for (Book book : allBooks) {
                if (recommendations.size() >= maxRecommendations) {
                    break;
                }
                
                if (!borrowedBooks.contains(book.getIsbn()) && !recommendations.contains(book)) {
                    recommendations.add(book);
                }
            }
        }
        
        return recommendations;
    }
}
package librarymanagementsystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a library patron/member.
 * This class demonstrates encapsulation by keeping fields private and providing controlled access.
 */
public class Patron {
    private String name;
    private String email;
    private String phoneNumber;
    private String patronId;
    private List<Book> borrowingHistory;
    
    /**
     * Constructor for creating a new patron.
     *
     * @param name The name of the patron
     * @param email The email of the patron
     * @param phoneNumber The phone number of the patron
     * @param patronId The unique identifier for the patron
     */
    public Patron(String name, String email, String phoneNumber, String patronId) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.patronId = patronId;
        this.borrowingHistory = new ArrayList<>();
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public String getPatronId() {
        return patronId;
    }
    
    public List<Book> getBorrowingHistory() {
        return new ArrayList<>(borrowingHistory); // Return a copy to maintain encapsulation
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * Adds a book to the patron's borrowing history.
     *
     * @param book The book to add to the borrowing history
     */
    public void addToBorrowingHistory(Book book) {
        if (!borrowingHistory.contains(book)) {
            borrowingHistory.add(book);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(patronId, patron.patronId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }
    
    @Override
    public String toString() {
        return "Patron{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", patronId='" + patronId + '\'' +
                ", borrowingHistory=" + borrowingHistory.size() + " books" +
                '}';
    }
}
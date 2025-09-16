package librarymanagementsystem.branches;

import librarymanagementsystem.model.Book;
import librarymanagementsystem.services.impl.LibraryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a library branch.
 * This supports the multi-branch requirement.
 */
public class LibraryBranch {
    private String branchId;
    private String branchName;
    private String location;
    private LibraryServiceImpl libraryService;
    
    public LibraryBranch(String branchId, String branchName, String location) {
        this.branchId = branchId;
        this.branchName = branchName;
        this.location = location;
        this.libraryService = new LibraryServiceImpl();
    }
    
    // Getters
    public String getBranchId() {
        return branchId;
    }
    
    public String getBranchName() {
        return branchName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public LibraryServiceImpl getLibraryService() {
        return libraryService;
    }
    
    /**
     * Transfers a book to another branch.
     *
     * @param book The book to transfer
     * @param targetBranch The target branch
     * @return true if transfer was successful, false otherwise
     */
    public boolean transferBook(Book book, LibraryBranch targetBranch) {
        if (book == null || targetBranch == null) {
            return false;
        }
        
        // Check if book exists in this branch
        Book existingBook = libraryService.findBookByIsbn(book.getIsbn());
        if (existingBook == null) {
            return false;
        }
        
        // Add to target branch
        try {
            targetBranch.getLibraryService().addBook(book);
        } catch (IllegalArgumentException e) {
            // Book might already exist in target branch
            return false;
        }
        
        // Remove from this branch
        try {
            libraryService.removeBook(book.getIsbn());
        } catch (Exception e) {
            // Rollback - remove from target branch if removal failed
            targetBranch.getLibraryService().removeBook(book.getIsbn());
            return false;
        }
        
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryBranch that = (LibraryBranch) o;
        return Objects.equals(branchId, that.branchId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(branchId);
    }
    
    @Override
    public String toString() {
        return "LibraryBranch{" +
                "branchId='" + branchId + '\'' +
                ", branchName='" + branchName + '\'' +
                ", location='" + location + '\'' +
                ", bookCount=" + libraryService.getAllBooks().size() +
                '}';
    }
}
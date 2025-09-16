package librarymanagementsystem.notifications;

/**
 * Observer interface for the notification system.
 * This demonstrates the Observer design pattern.
 */
public interface NotificationObserver {
    void update(String message);
}
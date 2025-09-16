package librarymanagementsystem.notifications;

import librarymanagementsystem.model.Patron;
import librarymanagementsystem.notifications.NotificationObserver;

import java.util.logging.Logger;

/**
 * Concrete observer implementation for patron notifications.
 * This demonstrates the Observer design pattern.
 */
public class PatronNotificationObserver implements NotificationObserver {
    private static final Logger logger = Logger.getLogger(PatronNotificationObserver.class.getName());
    
    private Patron patron;
    
    public PatronNotificationObserver(Patron patron) {
        this.patron = patron;
    }
    
    @Override
    public void update(String message) {
        // In a real system, this might send an email or SMS
        logger.info("Notification sent to " + patron.getName() + ": " + message);
        System.out.println("NOTIFICATION for " + patron.getName() + ": " + message);
    }
}
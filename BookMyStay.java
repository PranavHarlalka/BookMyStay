import java.util.ArrayList;
import java.util.List;

/**
 * ========================================================
 * MAIN CLASS - BookMyStay
 * ========================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class demonstrates how
 * confirmed bookings are stored
 * and reported.
 *
 * The system maintains an ordered
 * audit trail of reservations.
 *
 * @version 8.0
 */
public class BookMyStay {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Initialize booking history
        BookingHistory bookingHistory = new BookingHistory();

        // Simulate confirmed reservations (from UC6)
        bookingHistory.addReservation(new Reservation("Abhi", "Single"));
        bookingHistory.addReservation(new Reservation("Subha", "Double"));
        bookingHistory.addReservation(new Reservation("Vanmathi", "Suite"));

        System.out.println("============================================");
        System.out.println("         Booking History Report             ");
        System.out.println("============================================\n");

        // Generate report using report service
        BookingReportService reportService = new BookingReportService();
        reportService.generateReport(bookingHistory);
    }
}

/**
 * ============================================================================
 * CLASS - BookingHistory
 * ============================================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class maintains a record of
 * confirmed reservations.
 *
 * It provides ordered storage for
 * historical and reporting purposes.
 *
 * @version 8.0
 */
class BookingHistory {

    /** List that stores confirmed reservations. */
    private List<Reservation> confirmedReservations;

    /**
     * Initializes an empty booking history.
     */
    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    /**
     * Adds a confirmed reservation
     * to booking history.
     *
     * @param reservation confirmed booking
     */
    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    /**
     * Returns all confirmed reservations.
     *
     * @return list of reservations
     */
    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

/**
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class generates reports
 * from booking history data.
 *
 * Reporting logic is separated
 * from data storage.
 *
 * @version 8.0
 */
class BookingReportService {

    /**
     * Displays a summary report
     * of all confirmed bookings.
     *
     * @param history booking history
     */
    public void generateReport(BookingHistory history) {
        List<Reservation> reservations = history.getConfirmedReservations();

        if (reservations.isEmpty()) {
            System.out.println("No confirmed bookings found.");
            return;
        }

        for (Reservation res : reservations) {
            System.out.println("Guest: " + res.getGuestName()
                    + ", Room Type: " + res.getRoomType());
        }

        System.out.println("\nTotal Bookings: " + reservations.size());
    }
}

/**
 * Use Case 5: Booking Request (FIFO)
 *
 * Description:
 * This class represents a booking request
 * made by a guest.
 *
 * @version 5.0
 */
class Reservation {

    /** Name of the guest making the booking. */
    private String guestName;

    /** Requested room type. */
    private String roomType;

    /**
     * Creates a new booking request.
     *
     * @param guestName Name of the guest
     * @param roomType  requested room type
     */
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    /** @return guest name */
    public String getGuestName() { return guestName; }

    /** @return requested room type */
    public String getRoomType() { return roomType; }
}
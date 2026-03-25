import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * ========================================================
 * MAIN CLASS - BookMyStay
 * ========================================================
 *
 * Use Case 10: Booking Cancellation
 *
 * Description:
 * This class demonstrates how confirmed
 * bookings can be cancelled safely.
 *
 * Inventory is restored and rollback
 * history is maintained.
 *
 * @version 10.0
 */
public class BookMyStay {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        // Initialize inventory (Single starts at 5)
        RoomInventory inventory = new RoomInventory();

        // Initialize cancellation service
        CancellationService cancellationService = new CancellationService();

        // Simulate a confirmed booking being registered
        cancellationService.registerBooking("Single-1", "Single");

        System.out.println("============================================");
        System.out.println("           Booking Cancellation             ");
        System.out.println("============================================\n");

        // Cancel the booking
        cancellationService.cancelBooking("Single-1", inventory);

        // Show rollback history (LIFO order)
        System.out.println();
        cancellationService.showRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getRoomAvailability().get("Single"));
    }
}

/**
 * ============================================================================
 * CLASS - CancellationService
 * ============================================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class is responsible for handling
 * booking cancellations.
 *
 * It ensures that:
 * - Cancelled room IDs are tracked
 * - Inventory is restored correctly
 * - Invalid cancellations are prevented
 *
 * A stack is used to model rollback behavior.
 *
 * @version 10.0
 */
class CancellationService {

    /** Stack to track recently released room IDs (LIFO rollback). */
    private Stack<String> rollbackStack;

    /** Maps reservation ID to room type. */
    private Map<String, String> reservationRoomTypeMap;

    /**
     * Initializes cancellation tracking structures.
     */
    public CancellationService() {
        rollbackStack = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking.
     *
     * This method simulates storing confirmation
     * data that will later be required for cancellation.
     *
     * @param reservationId confirmed reservation ID
     * @param roomType      allocated room type
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels a confirmed booking and
     * restores inventory safely.
     *
     * @param reservationId reservation to cancel
     * @param inventory     centralized room inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        // Validate reservation exists
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation ID '"
                    + reservationId + "' not found or already cancelled.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        // Push released room ID to rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory count
        Map<String, Integer> availability = inventory.getRoomAvailability();
        int currentCount = availability.getOrDefault(roomType, 0);
        inventory.updateAvailability(roomType, currentCount + 1);

        // Remove from active reservations
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: "
                + roomType);
    }

    /**
     * Displays recently cancelled reservations.
     *
     * This method helps visualize rollback order.
     */
    public void showRollbackHistory() {
        System.out.println("Rollback History (Most Recent First):");

        if (rollbackStack.isEmpty()) {
            System.out.println("No cancellations recorded.");
            return;
        }

        // Iterate stack from top (most recent) to bottom
        Stack<String> temp = (Stack<String>) rollbackStack.clone();
        while (!temp.isEmpty()) {
            System.out.println("Released Reservation ID: " + temp.pop());
        }
    }
}

/**
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * This class acts as the single source of truth
 * for room availability in the hotel.
 *
 * @version 3.0
 */
class RoomInventory {

    /**
     * Stores available room count for each room type.
     *
     * Key   -> Room type name
     * Value -> Available room count
     */
    private Map<String, Integer> roomAvailability;

    /**
     * Constructor initializes the inventory
     * with default availability values.
     */
    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    /**
     * Initializes room availability data.
     */
    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    /**
     * Returns the current availability map.
     *
     * @return map of room type to available count
     */
    public Map<String, Integer> getRoomAvailability() {
        return roomAvailability;
    }

    /**
     * Updates availability for a specific room type.
     *
     * @param roomType the room type to update
     * @param count    new availability count
     */
    public void updateAvailability(String roomType, int count) {
        roomAvailability.put(roomType, count);
    }
}
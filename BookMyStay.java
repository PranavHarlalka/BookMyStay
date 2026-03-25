import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ========================================================
 * MAIN CLASS - BookMyStay
 * ========================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class demonstrates how system state
 * can be restored after an application restart.
 *
 * Inventory data is loaded from a file
 * before any booking operations occur.
 *
 * @version 12.0
 */
public class BookMyStay {

    /**
     * Application entry point.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("============================================");
        System.out.println("              System Recovery               ");
        System.out.println("============================================\n");

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistenceService = new FilePersistenceService();

        // Attempt to load saved inventory on startup
        persistenceService.loadInventory(inventory, filePath);

        // Display current inventory state
        System.out.println("\nCurrent Inventory:");
        Map<String, Integer> availability = inventory.getRoomAvailability();
        System.out.println("Single: " + availability.get("Single"));
        System.out.println("Double: " + availability.get("Double"));
        System.out.println("Suite: " + availability.get("Suite"));

        // Save current inventory to file
        persistenceService.saveInventory(inventory, filePath);
    }
}

/**
 * ============================================================================
 * CLASS - FilePersistenceService
 * ============================================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * Description:
 * This class is responsible for persisting
 * critical system state to a plain text file.
 *
 * It supports:
 * - Saving room inventory state
 * - Restoring inventory on system startup
 *
 * No database or serialization framework
 * is used in this use case.
 *
 * @version 12.0
 */
class FilePersistenceService {

    /**
     * Saves room inventory state to a file.
     *
     * Each line follows the format:
     * roomType=availableCount
     *
     * @param inventory centralized room inventory
     * @param filePath  path to persistence file
     */
    public void saveInventory(RoomInventory inventory, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : inventory.getRoomAvailability().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
            System.out.println("Inventory saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    /**
     * Loads room inventory state from a file.
     *
     * @param inventory centralized room inventory
     * @param filePath  path to persistence file
     */
    public void loadInventory(RoomInventory inventory, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean dataLoaded = false;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String roomType = parts[0].trim();
                    int count = Integer.parseInt(parts[1].trim());
                    inventory.updateAvailability(roomType, count);
                    dataLoaded = true;
                }
            }

            if (dataLoaded) {
                System.out.println("Inventory restored successfully from file.");
            } else {
                System.out.println("No valid inventory data found. Starting fresh.");
            }

        } catch (IOException e) {
            System.out.println("No valid inventory data found. Starting fresh.");
        }
    }
}

/**
 * Use Case 3: Centralized Room Inventory Management
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
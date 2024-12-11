
package logger;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A utility class that handles loading and saving configuration data to and from a file in JSON format.
 * Uses Gson for JSON serialization and deserialization.
 */
public class FileHandler {
    private static final Logger logger = LogManager.getLogger(FileHandler.class);
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * Default constructor for FileHandler.
     * Provides utilities for managing configuration files.
     */
    public FileHandler(){};


    /**
     * Saves the current configuration to a specified file in JSON format.
     *
     * @param filename The name of the file to save the configuration data.
     */
    public void saveToFile(String filename) {
        Gson gson = new Gson();
        String json = gson.toJson(this);

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
        } catch (IOException e) {
            System.out.println("Error Occurred while saving " + e.getMessage());
        }
    }

    /**
     * Loads configuration data from a specified file in JSON format.
     * Updates the current instance with the loaded configuration values.
     *
     * @param filename The name of the file from which the configuration data should be loaded.
     */
    public void loadFromFile(String filename) {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(filename)) {
            FileHandler loadedParams = gson.fromJson(reader, FileHandler.class);

            this.totalTickets = loadedParams.totalTickets;
            this.ticketReleaseRate = loadedParams.ticketReleaseRate;
            this.customerRetrievalRate = loadedParams.customerRetrievalRate;
            this.maxTicketCapacity = loadedParams.maxTicketCapacity;
        } catch (IOException e) {
            logger.error("Error Occurred while loading: " + e.getMessage());
        }
    }

    /**
     * Gets the total number of tickets.
     *
     * @return The total number of tickets.
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Sets the total number of tickets.
     *
     * @param totalTickets The total number of tickets to be set.
     */
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    /**
     * Gets the ticket release rate (the rate at which tickets are released).
     *
     * @return The ticket release rate.
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Sets the ticket release rate.
     *
     * @param ticketReleaseRate The ticket release rate to be set.
     */
    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Gets the customer retrieval rate (the rate at which customers retrieve tickets).
     *
     * @return The customer retrieval rate.
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Sets the customer retrieval rate.
     *
     * @param customerRetrievalRate The customer retrieval rate to be set.
     */
    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    /**
     * Gets the maximum ticket capacity.
     *
     * @return The maximum ticket capacity.
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Sets the maximum ticket capacity.
     *
     * @param maxTicketCapacity The maximum ticket capacity to be set.
     */
    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }
}
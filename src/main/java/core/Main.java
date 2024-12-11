package core;
import java.util.*;
import config.Configuration;
import threads.Vendor;
import threads.Customer;


/**
 * The main class that manages the ticketing system.
 * It initializes the ticket pool and manages vendor and customer threads.
 */
public class Main{
    /**
     * Default constructor for Main.
     * Initializes the main control mechanisms for the application.
     */
    public Main() {};

    /**
     * Flag to control vendor threads. Set to true to signal threads to stop.
     */
    public static boolean vendorThreads = false;
    /**
     * Flag to control customer threads. Set to true to signal threads to stop.
     */
    public static boolean customerThreads = false;


    /**
     * The entry point of the program.
     * Initializes the configuration, creates threads, and starts the ticketing process.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        // Create a Configuration object to load ticketing parameters.
        Configuration config = new Configuration();
        System.out.println("Welcome to Ticketing System!");
        System.out.println("==================================================");
        config.config_loader();
        int customerRetrievalRate = config.getCustomerRetrievalRate();
        int maxTicketCapacity = config.getMaxTicketCapacity();
        int totalTickets = config.getTotalTickets();
        int ticketReleaseRate = config.getTicketReleaseRate();

        // Initialize the shared TicketPool
        TicketPool ticketPool = new TicketPool(totalTickets, maxTicketCapacity);

        // Create and start vendor threads
        int vendor_count = config.vendCustom_configuration("Vendor");
        int customer_count = config.vendCustom_configuration("Customer");
        System.out.println("Press Enter to stop all threads...");

        for (int i = 0; i < vendor_count; i++) {
            Thread vendor_thread = new Thread(new Vendor(ticketPool, ticketReleaseRate),"Vendor "+(i+1));
            vendor_thread.start();
        }

        for (int i = 0; i < customer_count; i++) {
            Thread custormer_thread = new Thread(new Customer(ticketPool, customerRetrievalRate),"Customer "+(i+1));
            custormer_thread.start();
        }

        Scanner getInput = new Scanner(System.in);
        getInput.nextLine(); // Wait for Enter input
        vendorThreads = true;// Set it to true to stop the threads. Threads
        customerThreads = true;

        System.out.println("All threads stopped. Exiting program.");

    }
}
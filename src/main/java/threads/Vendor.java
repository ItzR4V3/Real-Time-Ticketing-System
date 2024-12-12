package threads;
import core.TicketingSystem;
import core.TicketPool;
/**
 * A thread that adds tickets to the pool at a specific rate.
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate; // Tickets added per second

    /**
     * Creates a new Vendor.
     *
     * @param ticketPool        the ticket pool to update
     * @param ticketReleaseRate the number of tickets to add per second
     */
    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    /**
     * Runs the vendor thread, adding tickets at regular intervals.
     */
    @Override
    public void run() {
        try {
            while (!TicketingSystem.vendorThreads) {
                ticketPool.addTickets(ticketReleaseRate); // Add tickets at the vendor rate
                Thread.sleep(1000); // Wait for 1 second before adding more

            }
        } catch (InterruptedException e) {
            System.out.println("Vendor Thread interrupted");
        }
    }
}
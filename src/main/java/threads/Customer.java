package threads;
import core.TicketingSystem;
import core.TicketPool;

/**
 * A thread that removes tickets from the pool at a specific rate.
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketRetrievalRate; // Tickets purchased per second


    /**
     * Creates a new Customer.
     *
     * @param ticketPool         the ticket pool to update
     * @param ticketRetrievalRate the number of tickets to purchase per second
     */
    public Customer(TicketPool ticketPool, int ticketRetrievalRate) {
        this.ticketPool = ticketPool;
        this.ticketRetrievalRate = ticketRetrievalRate;
    }

    /**
     * Runs the customer thread, purchasing tickets at regular intervals.
     */
    @Override
    public void run() {

        try {
            while (!TicketingSystem.customerThreads) {
                ticketPool.removeTickets(ticketRetrievalRate); // Remove tickets at the customer rate
                Thread.sleep(1000); // Wait for 1 second before buying more
            }
        } catch (InterruptedException e) {
            System.out.println("Customer Thread interrupted");
        }
    }
}

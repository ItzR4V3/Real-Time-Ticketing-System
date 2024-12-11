package core;

import logger.TableLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A class representing a pool of tickets for an event.
 * Manages the addition and removal of tickets, ensuring thread safety.
 * Vendor threads add tickets to the pool, and customer threads remove tickets.
 * This class synchronizes access to the ticket pool.
 */
public class TicketPool {
    private static final Logger logger = LogManager.getLogger(TicketPool.class);
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int maxTicketCapacity; // Maximum tickets for the event
    private int totalTicketsSold;       // Counter for tickets sold
    private int nextTicketNumber;       // Tracks the next ticket number to add
    private TableLogger tableLogger;

    /**
     * Sets the TableLogger instance for logging purposes.
     *
     * @param tableLogger The TableLogger to be used for logging.
     */
    public void setTableLogger(TableLogger tableLogger) {
        this.tableLogger = tableLogger;
    }

    /**
     * Constructor that initializes the ticket pool with a specified number of tickets.
     * The pool is filled with ticket numbers starting from 1 up to the specified totalTickets.
     *
     * @param totalTickets The total number of tickets to initialize in the pool.
     * @param maxTicketCapacity The maximum capacity of tickets in the pool.
     */
    public TicketPool(int totalTickets, int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
        this.totalTicketsSold = 0;
        this.nextTicketNumber = totalTickets + 1;

        // Initialize the pool with `initialTickets`
        for (int i = 1; i <= totalTickets; i++) {
            tickets.add(i);
        }
    }

    /**
     * Adds tickets to the pool, up to the specified ticketReleaseRate.
     * The method ensures that the pool does not exceed the maximum ticket capacity.
     * If the capacity is reached, it stops adding tickets and notifies vendor threads.
     *
     * @param ticketReleaseRate The number of tickets to add to the pool.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized void addTickets(int ticketReleaseRate) throws InterruptedException {
        // Prevent exceeding maxTicketCapacity
        while (tickets.size() + totalTicketsSold >= maxTicketCapacity) {
            TicketingSystem.vendorThreads = true;
            notifyAll();
            logger.info("Vendor Threads Stopped");
            if (tableLogger != null) {
                tableLogger.logMessage("Vendor Threads Stopped");
            }
            return;
        }

        // Calculate how many tickets can actually be added
        int ticketsToAdd = Math.min(ticketReleaseRate, maxTicketCapacity - totalTicketsSold - tickets.size());
        for (int i = 0; i < ticketsToAdd; i++) {
            tickets.add(nextTicketNumber++);
        }

        // Log a single consolidated message
        if (ticketsToAdd > 0) {
            if (tableLogger != null) {
                tableLogger.logMessage(Thread.currentThread().getName() + " added " + ticketsToAdd + " tickets. Total " +
                        "tickets in pool: " + tickets.size());
            }
            logger.info(Thread.currentThread().getName() + " added " + ticketsToAdd + " tickets. Total " +
                    "tickets in pool: " + tickets.size());
        }
        notifyAll(); // Notify waiting customers
    }

    /**
     * Removes tickets from the pool, up to the specified ticketRetrievalRate.
     * If there are insufficient tickets, the method waits for tickets to be added by the vendor threads.
     * When the maximum ticket capacity is reached, it stops customer threads from removing tickets.
     *
     * @param ticketRetrievalRate The number of tickets to remove from the pool.
     * @throws InterruptedException If the thread is interrupted while waiting.
     */
    public synchronized void removeTickets(int ticketRetrievalRate) throws InterruptedException {
        while (tickets.size() < ticketRetrievalRate && totalTicketsSold <= maxTicketCapacity) {
            if (TicketingSystem.vendorThreads && !tickets.isEmpty()) {
                int ticketsToBuy = tickets.size();

                for (int i = 0; i < ticketsToBuy; i++) {
                    tickets.poll();
                    totalTicketsSold++;
                }
                if (tableLogger != null) {
                    tableLogger.logMessage(Thread.currentThread().getName() + " bought " + ticketsToBuy + " tickets. Tickets " +
                            "remaining in pool: " + tickets.size()+". Total Tickets Sold: "+totalTicketsSold);
                }
                logger.info(Thread.currentThread().getName() + " bought " + ticketsToBuy + " tickets. Tickets " +
                        "remaining in pool: " + tickets.size()+". Total Tickets Sold: "+totalTicketsSold);

                return;
            }
            if (totalTicketsSold == maxTicketCapacity && TicketingSystem.vendorThreads) {
                TicketingSystem.customerThreads = true;
                logger.info("Customer Threads Stopped");
                if (tableLogger != null) {
                    tableLogger.logMessage("Customer Threads Stopped");
                }
            }
            wait();
        }

        // Calculate how many tickets can actually be bought
        int ticketsToBuy = Math.min(ticketRetrievalRate, tickets.size());
        for (int i = 0; i < ticketsToBuy; i++) {
            tickets.poll();
            totalTicketsSold++;
        }

        // Log a single consolidated message
        if (ticketsToBuy > 0) {
            if (tableLogger != null) {
                tableLogger.logMessage(Thread.currentThread().getName() + " bought " + ticketsToBuy + " tickets. Tickets " +
                        "remaining in pool: " + tickets.size()+". Total Tickets Sold: "+totalTicketsSold);
            }
            logger.info(Thread.currentThread().getName() + " bought " + ticketsToBuy + " tickets. Tickets " +
                    "remaining in pool: " + tickets.size()+". Total Tickets Sold: "+totalTicketsSold);
        }
        notifyAll(); // Notify waiting vendors
    }
}

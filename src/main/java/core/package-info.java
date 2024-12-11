/**
 * This package contains core components of the Real-Time Event Ticketing System.
 * It includes the main class for running the ticketing simulation and managing thread execution.
 *
 * <p>Classes in this package:
 * <ul>
 *   <li>{@link core.TicketingSystem} - The main class that starts the event ticketing simulation. It initializes configuration, creates and starts vendor and customer threads, and handles stopping threads based on user input.</li>
 *   <li>{@link core.TicketPool} - A class responsible for managing the synchronized pool of tickets. It ensures thread safety while adding and removing tickets, maintaining counters for tickets sold, and tracking the next available ticket number. This class is crucial for the overall operation of the ticketing system, ensuring proper ticket allocation and preventing issues like exceeding ticket capacity.</li>
 * </ul>
 *
 * @author Raveen Gamachchige
 * @version 1.0
 */
package core;

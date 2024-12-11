/**
 * This package contains thread implementations for managing ticket operations.
 * It includes threads for vendors and customers that interact with the ticket pool.
 *
 * <p>Classes in this package:
 * <ul>
 *   <li>{@link threads.Vendor} - A thread that simulates a vendor adding tickets to the pool at a specified release rate. It runs concurrently with customer threads and ensures that tickets are released as specified.</li>
 *   <li>{@link threads.Customer} - A thread that simulates a customer retrieving tickets from the pool at a specified retrieval rate. It interacts with the pool to simulate customers purchasing tickets until the pool is empty or the operation ends.</li>
 * </ul>
 *
 * @author Raveen Gamachchige
 * @version 1.0
 */
package threads;

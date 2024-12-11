package config;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Scanner;

import logger.FileHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class handles the configuration of the ticketing system.
 * It allows the user to input configuration parameters or load them from a file.
 */
public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    Scanner getInput = new Scanner(System.in);
    FileHandler handler = new FileHandler();
    private static final Logger logger = LogManager.getLogger(Configuration.class);

    /**
     * Default constructor for the Configuration class.
     * Initializes the configuration parameters for the ticketing system.
     */
    public Configuration() {}

    /**
     * Gets the total number of tickets.
     *
     * @return totalTickets the total number of tickets
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Gets the ticket release rate.
     *
     * @return ticketReleaseRate the rate at which tickets are released
     */
    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    /**
     * Gets the customer retrieval rate.
     *
     * @return customerRetrievalRate the rate at which customers retrieve tickets
     */
    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    /**
     * Gets the maximum ticket capacity.
     *
     * @return maxTicketCapacity the maximum capacity of the ticket pool
     */
    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }


    /**
     * Configures the system parameters by prompting the user for input.
     * Parameters include total tickets, ticket release rate, customer retrieval rate, and max ticket capacity.
     */
    public void parameter_configuration() {
        System.out.println("Enter the Configuration Settings ");
        maxTicketCapacity = parameter_errorhandling("Max Ticket Capacity: ", 0, 200);
        ticketReleaseRate = parameter_errorhandling("Ticket Release Rate: ", 0, 20);
        customerRetrievalRate = parameter_errorhandling("Customer Retrieval Rate: ", 0, 10);
        totalTickets = parameter_errorhandling("Total Ticket Count: ", 0, 100);

        while (true) {
            try {
                System.out.println("\nCurrent Configuration Settings: ");
                System.out.println("1) Max Ticket Capacity: " + maxTicketCapacity);
                System.out.println("2) Ticket Release Rate: " + ticketReleaseRate);
                System.out.println("3) Customer Retrieval Rate: " + customerRetrievalRate);
                System.out.println("4 )Total Tickets: " + totalTickets);

                System.out.print("Would you like to change any? Yes(y) or No(n): ");
                String config_input = getInput.next();
                if (config_input.equals("y")) {
                    System.out.print("Which Parameter would you like to change? ");
                    int parameter_input = getInput.nextInt();

                    switch (parameter_input) {
                        case 1:
                            maxTicketCapacity = parameter_errorhandling("Max Ticket Capacity: ", 0, 100);
                            break;
                        case 2:
                            ticketReleaseRate = parameter_errorhandling("Ticket Release Rate: ", 0, 20);
                            break;
                        case 3:
                            customerRetrievalRate = parameter_errorhandling("Customer Retrieval Rate: ", 0, 20);
                            break;
                        case 4:
                            totalTickets = parameter_errorhandling("Total Ticket Count: ", 0, 100);
                            break;
                        default:
                            System.out.println("Invalid input! Please enter again!");
                    }

                } else if (config_input.equals("n")) {
                    break;
                } else {
                    System.out.println("Invalid input! Please enter again! (Yes(y) or No(n))!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entered input type is invalid! Please enter again!");
                getInput.nextLine();
            }
        }
    }


    /**
     * Handles input validation for parameters, ensuring values are within a valid range.
     *
     * @param configure_parameter the name of the parameter
     * @param start_range the minimum acceptable value
     * @param end_range the maximum acceptable value
     * @return the valid input value
     */
    public int parameter_errorhandling(String configure_parameter, int start_range, int end_range) {
        while (true) {
            int parameter_input;
            try {
                System.out.print("Enter the " + configure_parameter);
                parameter_input = getInput.nextInt();
                if (parameter_input > start_range && parameter_input <= end_range) {
                    return parameter_input;
                } else {
                    System.out.println("Invalid input! Please enter and Integer between " + start_range + " and " + end_range);
                }
            } catch (InputMismatchException e) {
                logger.error("Entered input type is invalid! Please enter an Integer!");
                getInput.nextLine();
            }
        }
    }

    /**
     * Configures the number of vendor or customer threads based on user input.
     *
     * @param threadName either "Vendor" or "Customer"
     * @return the number of threads to be created
     */
    public int vendCustom_configuration(String threadName) {
        while (true) {
            int user_input;
            try {
                System.out.print("Enter the number of " + threadName + ": ");
                user_input = getInput.nextInt();
                if (user_input >= 1 && user_input <= 5) {
                    return user_input;
                }
                else {
                    System.out.println("Invalid input! Please enter a number between 1 and 5!");
                }
            } catch (InputMismatchException e) {
                logger.error("Entered input type is invalid! Please enter an Integer between 1-5!");
                getInput.nextLine();
            }
        }
    }

    /**
     * Loads configuration settings from a file, or asks the user to enter new settings if the file doesn't exist.
     * Saves the configuration to a file if the user chooses to.
     */
    public void config_loader() {
        String configFilePath = "config.json";

        File configFile = new File(configFilePath);

        if (configFile.exists()) {
            if (configFile.length() > 0) {
                logger.info("Configuration file exists.");
                while (true) {
                    try {
                        System.out.print("Configuration file found. Would you like to load it? (Yes(y) / No(n)): ");
                        String loadResponse = getInput.next();
                        if (loadResponse.equalsIgnoreCase("y")) {
                            handler.loadFromFile("config.json"); // Load configuration from file
                            // Set the loaded configuration values into the current instance
                            this.totalTickets = handler.getTotalTickets();
                            this.ticketReleaseRate = handler.getTicketReleaseRate();
                            this.customerRetrievalRate = handler.getCustomerRetrievalRate();
                            this.maxTicketCapacity = handler.getMaxTicketCapacity();
                            logger.info("Configuration Loaded! \nMax Ticket Capacity: " + maxTicketCapacity+": \nTicket Release " +
                                    "Rate: "+ticketReleaseRate+" \nCustomer Retrieval Rate: "+customerRetrievalRate+" \nTotal Tickets: "+totalTickets);
                            break;
                        } else if (loadResponse.equals("n")) {
                            parameter_configuration();  // Ask for new parameters
                            System.out.print("Would you like to save these settings? (Yes(y) / No(n)): ");
                            String saveResponse = getInput.next();
                            if (saveResponse.equalsIgnoreCase("y")) {
                                handler.setCustomerRetrievalRate(customerRetrievalRate);
                                handler.setMaxTicketCapacity(maxTicketCapacity);
                                handler.setTotalTickets(totalTickets);
                                handler.setTicketReleaseRate(ticketReleaseRate);
                                handler.saveToFile("config.json");  // Save configuration to file
                                logger.info("Config Saved Successfully!");
                            }
                            break;
                        } else {
                            System.out.println("Invalid input! Please enter 'y' or 'n'.");
                        }
                    } catch (InputMismatchException e) {
                        logger.error("Invalid input! Please enter 'y' or 'n'.");
                        getInput.nextLine(); // Consume the invalid input
                    }
                }
            }

        } else {
            logger.info("Configuration file does not exist.");
            parameter_configuration();  // Ask user for parameters

            while (true) {
                try {
                    System.out.print("Would you like to save these settings? (Yes(y) / No(n)): ");
                    String saveResponse = getInput.nextLine();
                    if (saveResponse.equals("y")) {
                        handler.setCustomerRetrievalRate(customerRetrievalRate);
                        handler.setMaxTicketCapacity(maxTicketCapacity);
                        handler.setTotalTickets(totalTickets);
                        handler.setTicketReleaseRate(ticketReleaseRate);
                        handler.saveToFile("config.json"); // Save configuration to file
                        logger.info("Config Saved Successfully!");
                        break;
                    } else if (saveResponse.equals("n")) {
                        break;
                    } else {
                        System.out.println("Invalid input! Please enter 'y' or 'n'.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input! Please enter 'y' or 'n'.");
                    getInput.nextLine(); // Consume the invalid input
                }
            }

        }
        logger.info("Parameters Loaded Successfully!");
        logger.info("System is ready to Start\n");
    }
}
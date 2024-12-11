package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import core.Main;
import logger.TableLogger;
import core.TicketPool;
import threads.Vendor;
import threads.Customer;
import logger.FileHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

/**
 * This class represents the user interface of the Real-Time Ticketing System.
 * It provides a graphical interface for configuring the system, saving/loading configurations,
 * starting and stopping vendor and customer threads, and displaying logs.
 */
public class JavaFxInterface extends Application {

    /**
     * Default constructor for JavaFxInterface.
     * Initializes the JavaFX application components.
     */
    public JavaFxInterface() {};

    private int totalTickets;
    private int customerRetrievalRate;
    private int maxCapacity;
    private int ticketReleaseRate;
    private int vendorCount;
    private int customerCount;
    private boolean parametersConfigured = false;
    private static final Logger logger = LogManager.getLogger(JavaFxInterface.class);

    private TicketPool ticketPool;
    private List<Thread> threads = new ArrayList<>();
    private TableLogger tableLogger;

    /**
     * Clears the status label after a short delay.
     *
     * @param label the label to clear
     */
    private void clearLabelAfterDelay(Label label) {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> label.setText(""));
        pause.play();
    }

    /**
     * Initializes the JavaFX application and sets up the user interface components.
     *
     * @param primaryStage the primary stage for the JavaFX application
     */
    @Override
    public void start(Stage primaryStage) {
        // Create labels and input fields
        Label totalTicketsLabel = new Label("Total Tickets (1-200):");
        TextField totalTicketsField = new TextField();
        totalTicketsField.setPromptText("Total Tickets count must be higher than 0 and lower than Max Ticket Capacity");

        Label ticketReleaseRateLabel = new Label("Ticket Release Rate (1-5):");
        TextField ticketReleaseRateField = new TextField();
        ticketReleaseRateField.setPromptText("Ticket Release Rate must be between 1 and 5");

        Label customerRetrievalRateLabel = new Label("Customer Retrieval Rate (1-5):");
        TextField customerRetrievalRateField = new TextField();
        customerRetrievalRateField.setPromptText("Customer Retrieval Rate must be between 1 and 5");

        Label maxCapacityLabel = new Label("Max Ticket Capacity (1-200):");
        TextField maxCapacityField = new TextField();
        maxCapacityField.setPromptText("Max Ticket Capacity must be between 1 and 200");

        // Submit button
        Button submitButton = new Button("Submit");
        Label statusLabel = new Label();

        // Save and Load Configurations Buttons
        Button saveConfigButton = new Button("Save Configuration");
        Button loadConfigButton = new Button("Load Configuration");
        Label loadStatusLabel = new Label();

        // Start/Stop Buttons for Threads
        Button startButton = new Button("Start Threads");
        Button stopButton = new Button("Stop Threads");
        Button resetButton = new Button("Reset");

        Label vendorCountLabel = new Label("Vendor Count (1-5):");
        TextField vendorCountField = new TextField();
        vendorCountField.setPromptText("Vendor Count must be between 1 and 5");

        Label customerCountLabel = new Label("Customer Count (1-5):");
        TextField customerCountField = new TextField();
        customerCountField.setPromptText("Customer Count must be between 1 and 5");

        // Create Table for logs
        ListView<String> logsTable = new ListView<>();
        logsTable.setPrefWidth(800);
        logsTable.setPlaceholder(new Label("No logs available"));

        //System Status Label
        Label systemStatusLabel = new Label("System Not Running");

        // Initialize LoggerHelper with logsTable
        tableLogger = new TableLogger(logsTable);
        tableLogger.logMessage("Application started!");

        // Submit Button Action to Validate user inputs
        submitButton.setOnAction(e -> {
            try {
                // Parse and validate input
                totalTickets = Integer.parseInt(totalTicketsField.getText());
                ticketReleaseRate = Integer.parseInt(ticketReleaseRateField.getText());
                customerRetrievalRate = Integer.parseInt(customerRetrievalRateField.getText());
                maxCapacity = Integer.parseInt(maxCapacityField.getText());

                if (totalTickets >= 0 && totalTickets <= maxCapacity && ticketReleaseRate > 0 && ticketReleaseRate <= 5 &&
                        customerRetrievalRate > 0 && customerRetrievalRate <= 5 && maxCapacity > 0 && maxCapacity <= 200) {

                    ticketPool = new TicketPool(totalTickets, maxCapacity);
                    ticketPool.setTableLogger(tableLogger);

                    statusLabel.setText("Parameters successfully submitted!");
                    tableLogger.logMessage("Parameters successfully submitted!");
                    logger.info("Parameters successfully submitted!"+"\nTotal Tickets: " + totalTickets+"\nTicket" +
                            " Release Rate: " + ticketReleaseRate+ "\nCustomer Retrieval Rate: " + customerRetrievalRate+
                            "\nMax Capacity: " + maxCapacity);
                    tableLogger.logMessage("Total Tickets: " + totalTickets);
                    tableLogger.logMessage("Ticket Release Rate: " + ticketReleaseRate);
                    tableLogger.logMessage("Customer Retrieval Rate: " + customerRetrievalRate);
                    tableLogger.logMessage("Max Capacity: " + maxCapacity);
                    parametersConfigured = true;
                } else {
                    statusLabel.setText("All values must match the required ranges.");
                }

            } catch (NumberFormatException ex) {
                statusLabel.setText("Error: Please enter valid numbers.");
            } catch (InputMismatchException exception) {
                statusLabel.setText("Entered input type is invalid");
            }
            clearLabelAfterDelay(statusLabel);
        });

        saveConfigButton.setOnAction(e -> {
            if (parametersConfigured) {
                try {
                    FileHandler fileHandler = new FileHandler();
                    fileHandler.setTotalTickets(totalTickets);
                    fileHandler.setTicketReleaseRate(ticketReleaseRate);
                    fileHandler.setCustomerRetrievalRate(customerRetrievalRate);
                    fileHandler.setMaxTicketCapacity(maxCapacity);
                    fileHandler.saveToFile("config.json");

                    statusLabel.setText("Configuration saved successfully!");
                } catch (Exception ex) {
                    statusLabel.setText("Error saving configuration.");
                }
            }
            else {
                statusLabel.setText("Submit Parameters First!");
            }
            clearLabelAfterDelay(statusLabel);
        });

        loadConfigButton.setOnAction(e -> {
            FileHandler fileHandler = new FileHandler();
            File configFile = new File("config.json");
            if (configFile.exists()) {
                try {
                    fileHandler.loadFromFile("config.json");

                    totalTickets = fileHandler.getTotalTickets();
                    ticketReleaseRate = fileHandler.getTicketReleaseRate();
                    customerRetrievalRate = fileHandler.getCustomerRetrievalRate();
                    maxCapacity = fileHandler.getMaxTicketCapacity();

                    totalTicketsField.setText(String.valueOf(totalTickets));
                    ticketReleaseRateField.setText(String.valueOf(ticketReleaseRate));
                    customerRetrievalRateField.setText(String.valueOf(customerRetrievalRate));
                    maxCapacityField.setText(String.valueOf(maxCapacity));

                    loadStatusLabel.setText("Configuration loaded successfully!");

                } catch (Exception ex) {
                    loadStatusLabel.setText("Error: Could not load configuration.");
                }
            } else {
                loadStatusLabel.setText("No configuration file found.");
            }
            clearLabelAfterDelay(loadStatusLabel);
        });

        startButton.setOnAction(e -> {
            if (parametersConfigured) {
                try {
                    vendorCount = Integer.parseInt(vendorCountField.getText());
                    customerCount = Integer.parseInt(customerCountField.getText());

                    if (vendorCount > 0 && vendorCount <= 5 && customerCount > 0 && customerCount <= 5 && parametersConfigured) {
                        for (int i = 0; i < vendorCount; i++) {
                            Thread vendorThread = new Thread(new Vendor(ticketPool, ticketReleaseRate), "Vendor " + (i + 1));
                            threads.add(vendorThread);
                            vendorThread.start();
                        }

                        for (int i = 0; i < customerCount; i++) {
                            Thread customerThread = new Thread(new Customer(ticketPool, customerRetrievalRate), "Customer " + (i + 1));
                            threads.add(customerThread);
                            customerThread.start();
                        }
                        statusLabel.setText("Threads started successfully!");
                        //Main.vendorThreads = true;
                        //Main.customerThreads = true;
                        systemStatusLabel.setText("System Running! ");
                    }
                    else {
                        loadStatusLabel.setText("Parameters not configured correctly");
                    }
                }
                catch (NumberFormatException ex) {
                    loadStatusLabel.setText("Parameters not configured correctly.");
                }
            }
            else {
                loadStatusLabel.setText("Submit Parameters First!");
            }
            clearLabelAfterDelay(loadStatusLabel);
        });

        stopButton.setOnAction(e -> {
            for (Thread thread : threads) {
                thread.interrupt(); // Signal threads to stop
            }
            threads.clear(); // Clearing the list

            statusLabel.setText("Threads stopped successfully!");
            systemStatusLabel.setText("System Stopped!");
            clearLabelAfterDelay(statusLabel);
        });

        resetButton.setOnAction(e -> {
            logsTable.getItems().clear();
            totalTicketsField.setText("");
            ticketReleaseRateField.setText("");
            customerRetrievalRateField.setText("");
            maxCapacityField.setText("");
            vendorCountField.setText("");
            customerCountField.setText("");
            Main.customerThreads = false;
            Main.vendorThreads = false;
            for (Thread thread : threads) {
                thread.interrupt(); // Signal threads to stop
            }
            threads.clear(); // Clearing the list
            //ticketPool = new TicketPool(totalTickets, maxCapacity); // Reinitialize
            systemStatusLabel.setText("System Terminated!");
        });

        // Layout for Buttons
        HBox buttonsLayout = new HBox(10, saveConfigButton, loadConfigButton);
        buttonsLayout.setAlignment(Pos.CENTER);

        HBox threadButtons = new HBox(10, startButton, stopButton, resetButton);
        threadButtons.setAlignment(Pos.CENTER);

        HBox functionButtons = new HBox(10,startButton, stopButton, resetButton);
        functionButtons.setSpacing(20);
        functionButtons.setPadding(new Insets(10));
        functionButtons.setAlignment(Pos.CENTER);

        VBox parameterFieldsLayout = new VBox(10, totalTicketsLabel, totalTicketsField,
                ticketReleaseRateLabel, ticketReleaseRateField,
                customerRetrievalRateLabel, customerRetrievalRateField,
                maxCapacityLabel, maxCapacityField,
                submitButton,
                statusLabel,
                buttonsLayout,
                loadStatusLabel,
                vendorCountLabel, vendorCountField,
                customerCountLabel, customerCountField,
                functionButtons);
        parameterFieldsLayout.setAlignment(Pos.TOP_CENTER);
        parameterFieldsLayout.setMinWidth(300);

        // Create VBox for the ListView (right side)
        VBox logsLayout = new VBox(10, new Label("Logs:"), logsTable);
        logsLayout.setAlignment(Pos.CENTER_LEFT);
        logsLayout.setMaxWidth(700);

        // Create an HBox for the left and right side components
        HBox mainLayout = new HBox(20, parameterFieldsLayout, logsLayout);

        //VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 1000, 600);

        primaryStage.setTitle("Real-Time Ticketing System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Entry point for the JavaFX application.
     * Launches the user interface for the Real-Time Ticketing System.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}

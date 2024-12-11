package logger;

import javafx.application.Platform;
import javafx.scene.control.ListView;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A logger class that handles logging messages with timestamps to a ListView.
 * This class is used for displaying logs in Listview on JavaFX application.
 */
public class TableLogger {
    private final ListView<String> logsList;
    private  DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH: mm: ss");

    /**
     * Constructs a TableLogger with a specified ListView for displaying logs.
     *
     * @param logsList The ListView component where the logs will be displayed.
     */
    public TableLogger(ListView<String> logsList) {
        this.logsList = logsList;
    }

    /**
     * Logs a message with a specified log level and adds it to the ListView with a timestamp.
     * This method is thread-safe and runs on the JavaFX Application thread.
     *
     * @param message The log message to be displayed.
     */
    public void logMessage(String message) {

        String timestamp = LocalDateTime.now().format(dateTime);
        String formattedLog = String.format("[%s] INFO: %s", timestamp, message);

        Platform.runLater(() -> {
            logsList.getItems().add(formattedLog);
            // Automatically scroll to the bottom
            logsList.scrollTo(logsList.getItems().size() - 1);
        });
    }


}

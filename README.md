
# Real-Time Ticketing Application

An Real-Time event ticketing simulation system that designed to simulate the ticketing process of events.

# Prerequisites and Libraries

- JDK (21 or Later preffered) 
- JavaFx 23.0.1
- Gson 2.11.0
- Log4j 2.17.1

# How to Run using and IDE

1. Clone The github repository
https://github.com/ItzR4V3/Real-Time-Ticketing-System

2. Open the project using Intellij

3. (Java CLI) Run the TicketingSystem.java

4. (JavaFx) Create a new Run/Debug Configuration with vm options added as

--module-path javafx-sdk-21.0.5\lib --add-modules javafx.controls,javafx.fxml

5. Set the Class name ui.JavaFxInterface

6. Run the Custom Configuration

# How to Run using the Console 

1. Run 'mvn clean install' command in the project directory to compile the project

2. Run 'mvn javafx:run' command to run the JavaFx Application

# System Flow

1. Input the main parameters first to configure the system properly

2. User can save the four parameter configuration if want for any future quicky use

3. Use submit button to configure the system with the typed paramters

4. Enter Vendor and Customer count to Start the system

5. Start thread button will start the threads based on the user input. Stop threads will interupt the threads and clear threads.

6. Reset Buttom will clear the threads and reset every parameter. 
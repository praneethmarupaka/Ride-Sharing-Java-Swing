import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

// Represents an individual ride
class Ride {
    String origin;
    String destination;
    double fare;

    public Ride(String origin, String destination, double fare) {
        this.origin = origin;
        this.destination = destination;
        this.fare = fare;
    }
}

// Main application class
public class RideSharingApp {

    private List<Ride> rides;
    private JTextArea rideInfoTextArea;
    private JTextField currentLocationTextField;
    private JTextField dropLocationTextField;
    private JTextField distanceTextField;
    private JLabel fareLabel;
    private JComboBox<String> vehicleTypeComboBox;
    private JComboBox<Integer> numberOfPersonsComboBox;
    private double currentFare;
    private int numberOfPersons;

    public RideSharingApp() {
        rides = new ArrayList<>();
        createAndShowGUI();
    }

    // Add a new ride to the list
    public void addRide(String origin, String destination, double fare) {
        Ride ride = new Ride(origin, destination, fare);
        rides.add(ride);
        updateRideInfo();
    }

    // Refresh the ride info display area
    public void updateRideInfo() {
        StringBuilder rideInfo = new StringBuilder("Available Rides:\n");
        for (int i = 0; i < rides.size(); i++) {
            Ride ride = rides.get(i);
            rideInfo.append("Ride ").append(i + 1).append(": ")
                    .append(ride.origin).append(" to ").append(ride.destination)
                    .append(", Fare: \u20b9").append((int) ride.fare).append("\n");
        }
        rideInfoTextArea.setText(rideInfo.toString());
    }

    // Build and display the GUI
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Ride Sharing App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);

        currentLocationTextField = new JTextField();
        dropLocationTextField    = new JTextField();
        distanceTextField        = new JTextField();
        fareLabel                = new JLabel("Fare: \u20b90");

        JButton calculateFareButton = new JButton("Calculate Fare");
        JButton confirmRideButton   = new JButton("Confirm Ride");

        Font buttonFont = new Font("Arial", Font.BOLD, 20);
        Font labelFont  = new Font("Arial", Font.PLAIN, 18);

        calculateFareButton.setFont(buttonFont);
        confirmRideButton.setFont(buttonFont);
        fareLabel.setFont(labelFont);

        calculateFareButton.setBackground(new Color(0, 153, 76));     // Green
        calculateFareButton.setForeground(Color.WHITE);
        calculateFareButton.setBorder(BorderFactory.createRaisedBevelBorder());

        confirmRideButton.setBackground(new Color(255, 69, 0));       // Red-Orange
        confirmRideButton.setForeground(Color.WHITE);
        confirmRideButton.setBorder(BorderFactory.createLoweredBevelBorder());

        vehicleTypeComboBox = new JComboBox<>(new String[]{"Bike", "Car"});
        vehicleTypeComboBox.setFont(labelFont);

        numberOfPersonsComboBox = new JComboBox<>();
        numberOfPersonsComboBox.setFont(labelFont);

        rideInfoTextArea = new JTextArea();
        rideInfoTextArea.setFont(new Font("Arial", Font.PLAIN, 18));
        rideInfoTextArea.setEditable(false);

        // Action listeners
        vehicleTypeComboBox.addActionListener(e -> updatePassengerOptions());
        calculateFareButton.addActionListener(e -> calculateFare());
        confirmRideButton.addActionListener(e -> confirmRide());

        // Form panel
        JPanel panel = new JPanel(new GridLayout(13, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("Current Location:"));
        panel.add(currentLocationTextField);
        panel.add(new JLabel("Drop Location:"));
        panel.add(dropLocationTextField);
        panel.add(new JLabel("Distance (in km):"));
        panel.add(distanceTextField);
        panel.add(new JLabel("Vehicle Type:"));
        panel.add(vehicleTypeComboBox);
        panel.add(new JLabel("Number of Persons:"));
        panel.add(numberOfPersonsComboBox);
        panel.add(calculateFareButton);
        panel.add(fareLabel);
        panel.add(confirmRideButton);

        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(rideInfoTextArea), BorderLayout.CENTER);

        updatePassengerOptions();
        frame.setVisible(true);
    }

    // Update the number of passengers based on vehicle type
    private void updatePassengerOptions() {
        String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();
        numberOfPersonsComboBox.removeAllItems();

        if ("Bike".equals(vehicleType)) {
            // Bike: 1 or 2 persons
            numberOfPersonsComboBox.addItem(1);
            numberOfPersonsComboBox.addItem(2);
        } else if ("Car".equals(vehicleType)) {
            // Car: 3 to 5 persons
            for (int i = 3; i <= 5; i++) {
                numberOfPersonsComboBox.addItem(i);
            }
        }
    }

    // Calculate and display the fare
    private void calculateFare() {
        String vehicleType = (String) vehicleTypeComboBox.getSelectedItem();
        double farePerKm = 0;

        if ("Bike".equals(vehicleType)) {
            farePerKm = 5.0;   // ₹5 per km
        } else if ("Car".equals(vehicleType)) {
            farePerKm = 10.0;  // ₹10 per km
        }

        numberOfPersons = (int) numberOfPersonsComboBox.getSelectedItem();
        double distance;

        try {
            distance = Double.parseDouble(distanceTextField.getText().trim());
            if (distance <= 0) {
                throw new IllegalArgumentException("Distance must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid distance.");
            return;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            return;
        }

        // Fare = farePerKm * distance * numberOfPersons
        currentFare = farePerKm * distance * numberOfPersons;
        fareLabel.setText("Fare: \u20b9" + (int) currentFare);
    }

    // Confirm the ride and add it to the list
    private void confirmRide() {
        String currentLocation = currentLocationTextField.getText().trim();
        String dropLocation    = dropLocationTextField.getText().trim();

        if (!currentLocation.isEmpty() && !dropLocation.isEmpty()) {
            if (currentFare <= 0) {
                JOptionPane.showMessageDialog(null, "Please calculate the fare first.");
                return;
            }
            addRide(currentLocation, dropLocation, currentFare);
            JOptionPane.showMessageDialog(null, "Ride confirmed! Fare: \u20b9" + (int) currentFare);
        } else {
            JOptionPane.showMessageDialog(null, "Please enter both current and drop locations.");
        }
    }

    // Entry point
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RideSharingApp());
    }
}

import javax.swing.*;
import java.awt.*;

/**
 * MixiesAppFrame is the main application window for the Mixies Ice Cream System.
 * 
 * It manages:
 * - Navigation between different panels using tabs
 * - Displaying employee information
 * - Providing access to employee, manager, and kiosk features
 * 
 * Tabs include:
 * - Employee Orders (order creation and management)
 * - Orders (viewing existing orders)
 * - Manager (restricted access for managers only)
 * - Kiosk Menu (customer-facing flavor selection)
 */
public class MixiesAppFrame extends JFrame {

    // Service layer used for all business logic and data access
    private final MixiesService service;

    // Tabbed pane for switching between different views
    private final JTabbedPane tabs;

    private final OrdersPanel ordersPanel;

    /**
     * Constructor initializes the main application frame and UI components.
     */
    public MixiesAppFrame(Employee loggedInEmployee) {
        this.service = new MixiesService();
        this.tabs = new JTabbedPane();
        this.ordersPanel = new OrdersPanel(service);

        tabs.addTab("Employee Orders", new EmployeeOrderPanel(service, loggedInEmployee));
        tabs.addTab("Orders", ordersPanel);
        tabs.addTab("Kiosk Menu", new KioskPanel(service, loggedInEmployee, ordersPanel));

        // Frame settings
        setTitle("Mixies Ice Cream System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    
        // Button to access manager panel
        JButton managerAccessButton = new JButton("Manager Access");
        managerAccessButton.addActionListener(e -> openManagerAccess());

        // Label displaying logged-in employee info
        JLabel loggedInLabel = new JLabel(
                "Logged in: " + loggedInEmployee.getEmployeeName() +
                " (" + loggedInEmployee.getEmployeeRole() + ")"
        );

        // Top panel containing user info and action buttons
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(loggedInLabel, BorderLayout.WEST);

        // Right side of the top panel for buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(managerAccessButton);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // Add components to frame
        add(topPanel, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    /**
     * Handles manager access by prompting for an employee ID
     * and verifying if the employee has manager privileges.
     * If valid, opens or switches to the Manager tab.
     */
    private void openManagerAccess() {
        String input = JOptionPane.showInputDialog(this, "Enter employee ID:");

        // Cancel or empty input
        if (input == null || input.isBlank()) {
            return;
        }

        try {
            int employeeID = Integer.parseInt(input.trim());
            Employee manager = service.getEmployeeById(employeeID);

            // Check if employee exists
            if (manager == null) {
                JOptionPane.showMessageDialog(this, "Employee ID not found.");
                return;
            }

            // Check if employee is actually a manager
            if (manager.getEmployeeRole() != employeeRoles.MANAGER) {
                JOptionPane.showMessageDialog(this, "Access denied. Employee is not a manager.");
                return;
            }

            // Check if Manager tab already exists
            int existingTabIndex = tabs.indexOfTab("Manager");

            if (existingTabIndex == -1) {
                // Create new Manager tab
                tabs.addTab("Manager", new ManagerPanel(service, manager));
                tabs.setSelectedIndex(tabs.getTabCount() - 1);
            } else {
                // Switch to existing Manager tab
                tabs.setSelectedIndex(existingTabIndex);
            }

        } catch (NumberFormatException ex) {
            // Handle invalid number input
            JOptionPane.showMessageDialog(this, "Invalid employee ID.");
        }
    }
}
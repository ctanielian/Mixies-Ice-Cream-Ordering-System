import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * OrdersPanel displays a list of all orders in the system.
 * 
 * It allows the user to:
 * - View all orders with their ID, total, and status
 * - Refresh the order list
 * - Open a detailed view of a selected order
 * 
 * This panel interacts with the MixiesService to retrieve
 * and display order data.
 */
public class OrdersPanel extends JPanel {

    // Service layer used to fetch order data
    private final MixiesService service;

    // Table model for displaying orders (non-editable)
    private final DefaultTableModel ordersTableModel = new DefaultTableModel(
            new Object[] { "Order ID", "Total", "Status" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Prevent editing table cells
        }
    };

    // Table displaying all orders
    private final JTable ordersTable = new JTable(ordersTableModel);

    /**
     * Constructor initializes UI components and event handlers.
     */
    public OrdersPanel(MixiesService service) {
        this.service = service;

        // Set layout for the panel
        setLayout(new BorderLayout());

        // Buttons for refreshing and viewing order details
        JButton refreshButton = new JButton("Refresh Orders");
        JButton viewDetailsButton = new JButton("View Order Details");

        // Top panel for buttons
        JPanel top = new JPanel();
        top.add(refreshButton);
        top.add(viewDetailsButton);

        // Add components to panel
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(ordersTable), BorderLayout.CENTER);

        // Button actions
        refreshButton.addActionListener(e -> refreshOrders());
        viewDetailsButton.addActionListener(e -> viewSelectedOrder());

        // Load initial data
        refreshOrders();
    }

    /**
     * Reloads all orders from the service and updates the table.
     */
    public void refreshOrders() {

        // Clear existing rows
        ordersTableModel.setRowCount(0);

        // Fetch all orders
        List<Order> orders = service.getAllOrders();

        // Populate table with order data
        for (Order order : orders) {
            ordersTableModel.addRow(new Object[] {
                    order.getOrderID(),
                    order.getTotal(),
                    order.getOrderStatus()
            });
        }
    }

    /**
     * Opens a dialog showing details for the selected order.
     */
    private void viewSelectedOrder() {

        // Get selected row
        int selectedRow = ordersTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select an order first.");
            return;
        }

        // Get order ID from selected row
        int orderID = (int) ordersTableModel.getValueAt(selectedRow, 0);

        // Open OrderDetailsDialog for the selected order
        new OrderDetailsDialog(
                (Frame) SwingUtilities.getWindowAncestor(this),
                service,
                orderID
        ).setVisible(true);

        // Refresh orders after closing dialog (in case changes were made)
        refreshOrders();
    }
}
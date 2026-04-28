// this is the topping panel, with scoops/cones/toppings
// customize after ice cream flavor
// a go back to menu button
// and a add to cart button

import javax.swing.*;
import java.awt.*;

public class IceCreamCustomizationPanel extends JPanel {

    // Service layer for database operations
    private final MixiesService service;

    // Shared kiosk session (stores selected flavor and order ID)
    private final KioskSession session;

    // Navigator for switching screens
    private final KioskNavigator navigator;

    // Label to display selected flavor
    private final JLabel flavorLabel = new JLabel("Selected Flavor: None", SwingConstants.CENTER);

    public IceCreamCustomizationPanel(MixiesService service, KioskSession session, KioskNavigator navigator) {
        this.service = service;
        this.session = session;
        this.navigator = navigator;

        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Customize Ice Cream", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));

        // Back button
        JButton backButton = new JButton("Back to Menu");
        backButton.addActionListener(e -> navigator.showMenu());

        // Add to cart button
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.addActionListener(e -> addToCart());

        // Bottom panel for buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(backButton);
        bottomPanel.add(addToCartButton);

        // Center panel layout
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(flavorLabel);

        // Add components
        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Updates the displayed flavor when panel becomes visible
    private void refreshSelectedFlavor() {
        IceCreamFlavor flavor = session.getSelectedFlavor();

        if (flavor == null) {
            flavorLabel.setText("Selected Flavor: None");
        } else {
            flavorLabel.setText("Selected Flavor: " + flavor.getFlavorName());
        }
    }

    // Runs whenever panel is shown
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (visible) {
            refreshSelectedFlavor();
        }
    }

    // Temporary add-to-cart logic
    private void addToCart() {

        IceCreamFlavor flavor = session.getSelectedFlavor();

        if (flavor == null) {
            JOptionPane.showMessageDialog(this, "No flavor selected.");
            return;
        }

        int orderID = session.getCurrentOrderID();

        if (orderID == -1) {
            JOptionPane.showMessageDialog(this, "No active order.");
            return;
        }

        int scoops = 1;

        int orderItemID = service.addOrderItem(
                orderID,
                flavor.getFlavorID(),
                scoops
        );

        if (orderItemID == -1) {
            JOptionPane.showMessageDialog(this, "Could not add item.");
            return;
        }

        JOptionPane.showMessageDialog(this, "Item added to cart.");

        // Navigate to cart after adding
        navigator.showCart();
    }
}
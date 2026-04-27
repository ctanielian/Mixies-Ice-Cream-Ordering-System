import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * KioskPanel represents the customer-facing kiosk UI.
 * 
 * It allows users to:
 * - Start an order
 * - Browse flavors
 * - Customize items (scoops + toppings)
 * - View cart and checkout
 * - Complete the order with an optional tip
 */
public class KioskPanel extends JPanel {

    // Service layer for business logic
    private final MixiesService service;

    // Employee creating the order (kiosk user)
    private final Employee employee;

    // Card layout to switch between different screens
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    // Current order state
    private int currentOrderID = -1;
    private IceCreamFlavor selectedFlavor;
    private int selectedScoops = 1;
    private final List<Topping> selectedToppings = new ArrayList<>();
    private double selectedTip = 0.0;

    // Reference to orders panel (to refresh after checkout)
    private final OrdersPanel ordersPanel;

    // Checkout UI components
    private final DefaultListModel<String> checkoutModel = new DefaultListModel<>();
    private final JList<String> checkoutList = new JList<>(checkoutModel);

    private final JLabel subtotalLabel = new JLabel("Subtotal: $0.00");
    private final JLabel tipLabel = new JLabel("Tip: $0.00");
    private final JLabel totalLabel = new JLabel("Total: $0.00");

    /**
     * Constructor initializes all screens and sets default view.
     */
    public KioskPanel(MixiesService service, Employee employee, OrdersPanel ordersPanel) {
        this.service = service;
        this.employee = employee;
        this.ordersPanel = ordersPanel;

        setLayout(new BorderLayout());

        // Add all screens to card layout
        cardPanel.add(buildWelcomeScreen(), "welcome");
        cardPanel.add(buildMenuScreen(), "menu");
        cardPanel.add(buildCustomizeScreen(), "customize");
        cardPanel.add(buildCheckoutScreen(), "checkout");

        add(cardPanel, BorderLayout.CENTER);

        // Show welcome screen first
        cardLayout.show(cardPanel, "welcome");
    }

    /**
     * Builds the initial welcome screen.
     */
    private JPanel buildWelcomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Welcome to Mixies", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JButton startButton = new JButton("Start Order");
        startButton.setPreferredSize(new Dimension(180, 60));

        // Start a new order and move to menu screen
        startButton.addActionListener(e -> {
            if (currentOrderID == -1) {
                currentOrderID = service.createOrder(employee, 0.0, 0.0);
            }
            cardLayout.show(cardPanel, "menu");
        });

        JPanel center = new JPanel();
        center.add(startButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Loads and scales an image for a given flavor.
     */
    private ImageIcon loadFlavorImage(String flavorName) {
        try {
            String fileName = flavorName.toLowerCase().replaceAll("\\s+", "") + ".png";
            String path = "src/images/" + fileName;

            ImageIcon originalIcon = new ImageIcon(path);

            // Only scale if image exists
            if (originalIcon.getIconWidth() > 0) {
                Image scaled = originalIcon.getImage()
                        .getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            } else {
                System.out.println("Image not found: " + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Builds the menu screen displaying all available flavors.
     */
    private JPanel buildMenuScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel header = new JLabel("Mixies Ice Cream", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 22));

        JButton checkoutButton = new JButton("Cart / Checkout");

        // Navigate to checkout screen
        checkoutButton.addActionListener(e -> {
            refreshCheckout();
            cardLayout.show(cardPanel, "checkout");
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.CENTER);
        top.add(checkoutButton, BorderLayout.EAST);

        JPanel grid = new JPanel(new GridLayout(0, 3, 20, 20));
        grid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Create a card for each available flavor
        for (IceCreamFlavor flavor : service.getAllFlavors()) {
            if (!flavor.isOutOfStock() &&
                    !"Unavailable".equalsIgnoreCase(flavor.getAvailabilityStatus())) {

                JPanel flavorCard = new JPanel(new BorderLayout());
                flavorCard.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                flavorCard.setPreferredSize(new Dimension(180, 220));
                flavorCard.setBackground(Color.WHITE);

                JLabel nameLabel = new JLabel(flavor.getFlavorName(), SwingConstants.CENTER);

                ImageIcon icon = loadFlavorImage(flavor.getFlavorName());
                JLabel imageLabel = new JLabel();
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

                if (icon != null) {
                    imageLabel.setIcon(icon);
                } else {
                    imageLabel.setText("No Image");
                }

                JButton flavorButton = new JButton("Customize");

                // Select flavor and go to customization screen
                flavorButton.addActionListener(e -> {
                    selectedFlavor = flavor;
                    selectedScoops = 1;
                    selectedToppings.clear();
                    cardLayout.show(cardPanel, "customize");
                });

                flavorCard.add(nameLabel, BorderLayout.NORTH);
                flavorCard.add(imageLabel, BorderLayout.CENTER);
                flavorCard.add(flavorButton, BorderLayout.SOUTH);

                grid.add(flavorCard);
            }
        }

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(grid), BorderLayout.CENTER);
        return panel;
    }

    /**
     * Builds the customization screen for scoops and toppings.
     */
    private JPanel buildCustomizeScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Customize Item", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Scoop selection
        JPanel scoopPanel = new JPanel();
        scoopPanel.setLayout(new GridLayout(0, 1));
        scoopPanel.setBorder(BorderFactory.createTitledBorder("Scoops"));

        JRadioButton one = new JRadioButton("1");
        JRadioButton two = new JRadioButton("2");
        JRadioButton three = new JRadioButton("3");
        ButtonGroup scoopGroup = new ButtonGroup();
        scoopGroup.add(one);
        scoopGroup.add(two);
        scoopGroup.add(three);
        one.setSelected(true);

        one.addActionListener(e -> selectedScoops = 1);
        two.addActionListener(e -> selectedScoops = 2);
        three.addActionListener(e -> selectedScoops = 3);

        scoopPanel.add(one);
        scoopPanel.add(two);
        scoopPanel.add(three);

        // Toppings selection
        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));

        for (Topping topping : service.getAllToppings()) {
            JToggleButton tBtn = new JToggleButton(topping.getToppingName());

            // Add/remove topping from selection
            tBtn.addActionListener(e -> {
                if (tBtn.isSelected()) {
                    selectedToppings.add(topping);
                } else {
                    selectedToppings.remove(topping);
                }
            });

            toppingsPanel.add(tBtn);
        }

        // Add customized item to order
        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(e -> addCustomizedItem());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "menu"));

        JPanel bottom = new JPanel();
        bottom.add(backButton);
        bottom.add(addItemButton);

        content.add(scoopPanel);
        content.add(Box.createVerticalStrut(20));
        content.add(toppingsPanel);

        panel.add(title, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Builds the checkout screen displaying items, totals, and tip options.
     */
    private JPanel buildCheckoutScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Checkout", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        // Tip selection panel
        JPanel tipPanel = new JPanel();
        tipPanel.setBorder(BorderFactory.createTitledBorder("Tip"));

        JRadioButton zero = new JRadioButton("0%");
        JRadioButton five = new JRadioButton("5%");
        JRadioButton ten = new JRadioButton("10%");
        JRadioButton customTipButton = new JRadioButton("Custom:");
        ButtonGroup tipGroup = new ButtonGroup();
        tipGroup.add(customTipButton);
        tipGroup.add(zero);
        tipGroup.add(five);
        tipGroup.add(ten);
        zero.setSelected(true);
        
        // Custom tip as an option with text field that updates tip percentage when text changed using listener
        JTextField customTipField = new JTextField(5);
        customTipField.setText("0");
        customTipField.addActionListener(e -> {
            try {
                double customPercent = Double.parseDouble(customTipField.getText()) / 100.0;
                //Clamp custom percent to 0% to 100%
                if (customPercent < 0) customPercent = 0;
                if (customPercent > 1) customPercent = 1;
                setTipPercent(customPercent);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Enter a valid number for custom tip.");
            }
        });

        zero.addActionListener(e -> setTipPercent(0.0));
        five.addActionListener(e -> setTipPercent(0.05));
        ten.addActionListener(e -> setTipPercent(0.10));

        tipPanel.add(customTipButton);
        tipPanel.add(customTipField);
        tipPanel.add(zero);
        tipPanel.add(five);
        tipPanel.add(ten);

        // Totals display
        JPanel totalsPanel = new JPanel(new GridLayout(1, 3));
        totalsPanel.add(subtotalLabel);
        totalsPanel.add(tipLabel);
        totalsPanel.add(totalLabel);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "menu"));

        JButton completeButton = new JButton("Create Order");
        completeButton.addActionListener(e -> completeOrder());

        JPanel bottom = new JPanel();
        bottom.add(backButton);
        bottom.add(completeButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(checkoutList), BorderLayout.CENTER);
        panel.add(tipPanel, BorderLayout.WEST);

        // Fix overlapping by putting totals and buttons in a separate panel at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(totalsPanel, BorderLayout.CENTER);
        bottomPanel.add(bottom, BorderLayout.SOUTH);
        panel.add(bottomPanel, BorderLayout.PAGE_END);

        return panel;
    }

    /**
     * Adds the currently customized item to the order.
     */
    private void addCustomizedItem() {
        if (selectedFlavor == null || currentOrderID == -1) {
            return;
        }

        int orderItemID = service.addOrderItem(currentOrderID, selectedFlavor.getFlavorID(), selectedScoops);

        if (orderItemID == -1) {
            JOptionPane.showMessageDialog(this, "Could not add item.");
            return;
        }

        // Add selected toppings
        for (Topping topping : selectedToppings) {
            service.addOrderItemTopping(orderItemID, topping.getToppingID(), 1);
        }

        JOptionPane.showMessageDialog(this, "Item added.");

        refreshCheckout();
        cardLayout.show(cardPanel, "menu");
    }

    /**
     * Refreshes the checkout list and recalculates totals.
     */
    private void refreshCheckout() {
        checkoutModel.clear();

        if (currentOrderID == -1) {
            subtotalLabel.setText("Subtotal: $0.00");
            tipLabel.setText("Tip: $0.00");
            totalLabel.setText("Total: $0.00");
            return;
        }

        List<OrderItem> items = service.getOrderItemsForOrder(currentOrderID);
        double subtotal = 0.0;

        for (OrderItem item : items) {
            double itemTotal = service.getDisplayedOrderItemCost(item);
            subtotal += itemTotal;

            checkoutModel.addElement(
                    item.getFlavor().getFlavorName() +
                            " x" + item.getQuantity() +
                            " - $" + String.format("%.2f", itemTotal));
        }

        double tipAmount = subtotal * selectedTip;
        double total = subtotal + tipAmount;

        subtotalLabel.setText("Subtotal: $" + String.format("%.2f", subtotal));
        tipLabel.setText("Tip: $" + String.format("%.2f", tipAmount));
        totalLabel.setText("Total: $" + String.format("%.2f", total));
    }

    /**
     * Sets tip percentage and updates totals.
     */
    private void setTipPercent(double percent) {
        selectedTip = percent;
        refreshCheckout();
    }

    /**
     * Finalizes the order and saves it to the database.
     */
    private void completeOrder() {
        if (currentOrderID == -1) {
            JOptionPane.showMessageDialog(this, "No active order.");
            return;
        }

        List<OrderItem> items = service.getOrderItemsForOrder(currentOrderID);

        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one item first.");
            return;
        }

        double subtotal = 0.0;
        for (OrderItem item : items) {
            subtotal += service.getDisplayedOrderItemCost(item);
        }

        double tipAmount = subtotal * selectedTip;

        // Save tip and recalculate total
        service.updateOrderTip(currentOrderID, tipAmount);
        service.refreshOrderTotal(currentOrderID);

        boolean ok = service.concludeOrder(currentOrderID);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Order created.");
            ordersPanel.refreshOrders();

            // Reset state for next order
            currentOrderID = -1;
            selectedFlavor = null;
            selectedScoops = 1;
            selectedToppings.clear();
            selectedTip = 0.0;
            checkoutModel.clear();

            refreshCheckout();
            cardLayout.show(cardPanel, "welcome");
        } else {
            JOptionPane.showMessageDialog(this, "Could not complete order.");
        }
    }
}
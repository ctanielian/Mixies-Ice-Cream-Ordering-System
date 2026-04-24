import javax.swing.*;
import java.awt.*;

/**
 * IceCreamFlavorPanel displays all available ice cream flavors
 * in a scrollable grid layout. Each flavor is shown as a card
 * containing an image, name, and an "Add to Order" button.
 *
 * This panel retrieves flavor data from the MixiesService
 * and dynamically builds the UI based on available flavors.
 */
public class IceCreamFlavorPanel extends JPanel {

    // Service used to fetch flavor data
    public final MixiesService service;
    
    /**
     * Constructor initializes the panel layout and populates
     * it with flavor cards.
     */
    public IceCreamFlavorPanel(MixiesService service) {
        this.service = service;
        
        // Set main layout and background color
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Panel that will hold all flavor cards in a grid
        JPanel flavorGrid = new JPanel(new GridBagLayout());
        flavorGrid.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        flavorGrid.setBackground(new Color(245, 245, 245));
        
        // Constraints for positioning cards in the grid
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        int col = 0;
        int row = 0;
        int maxCol = 3; // Maximum number of columns per row

        // Fetch flavors from the service and create cards
        for (IceCreamFlavor flavor : service.getAllFlavors()) {

            // Skip flavors that are marked unavailable
            if (!flavor.getAvailabilityStatus().equalsIgnoreCase("Unavailable")) {
                
                gbc.gridx = col;
                gbc.gridy = row;
                
                // Add a flavor card to the grid
                flavorGrid.add(createFlavorCard(flavor), gbc);

                col++;

                // Move to next row after reaching max columns
                if (col >= maxCol) {
                    col = 0;
                    row++;
                }
            }
        }

        // Add vertical space at the bottom so items align nicely
        gbc.gridx = 0;
        gbc.gridy = row + 1;
        gbc.weighty = 1.0;
        flavorGrid.add(Box.createVerticalGlue(), gbc);

        // Wrap grid in a scroll pane for vertical scrolling
        JScrollPane scrollPane = new JScrollPane(flavorGrid);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add scroll pane to the main panel
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Creates a visual card for a single ice cream flavor.
     * Each card includes:
     * - Flavor name
     * - Flavor image
     * - "Add to Order" button
     */
    private JPanel createFlavorCard(IceCreamFlavor flavor) {

        // Panel representing one flavor card
        JPanel card = new JPanel(new BorderLayout());
        
        // Set card size and styling
        card.setPreferredSize(new Dimension(180, 200));
        card.setMinimumSize(new Dimension(180, 200));
        card.setMaximumSize(new Dimension(180, 200));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Load flavor image
        ImageIcon icon = null;
        try {
            // Build image file path based on flavor name
            String path = "src/images/" + flavor.getFlavorName()
                .toLowerCase()
                .replaceAll(" ", "") + ".png";
            
            // Scale image to fit the card
            Image img = new ImageIcon(path).getImage()
                    .getScaledInstance(150, 150, Image.SCALE_SMOOTH);

            if (img != null) {
                icon = new ImageIcon(img);
            } else {
                // Debug message if image is missing
                System.out.println("Image not found for flavor: " + flavor.getFlavorName());
            }
        } catch (Exception e) {
            // Use empty placeholder if loading fails
            icon = new ImageIcon();
        }

        // Label to display the image
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Label to display the flavor name
        JLabel nameLabel = new JLabel(flavor.getFlavorName(), SwingConstants.CENTER);
        
        // Button to add flavor to order
        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(e -> {

            // Check stock before adding
            if (flavor.getStockLevel() > 0) {
                // Placeholder for actual order logic
                // CustomOrderItem.addItem(flavor);

                JOptionPane.showMessageDialog(this, 
                    flavor.getFlavorName() + " added to order!");
            } else {
                // Show message if flavor is out of stock
                JOptionPane.showMessageDialog(this, 
                    flavor.getFlavorName() + " is out of stock.");
            }
        });

        // Add components to the card
        card.add(nameLabel, BorderLayout.NORTH);
        card.add(imageLabel, BorderLayout.CENTER);
        card.add(addButton, BorderLayout.SOUTH);

        return card;
    }
}
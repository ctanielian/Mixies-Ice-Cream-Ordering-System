import javax.swing.*;
import java.awt.*;


public class IceCreamFlavorPanel extends JPanel{
    public final MixiesService service;
    public final OrderItem CustomOrderItem;
    
    // Constructor to initialize the panel with the service and order item
    public IceCreamFlavorPanel(MixiesService service, OrderItem OrderItem) {
        this.service = service;
        this.CustomOrderItem = OrderItem;
        
        setLayout(new BorderLayout());

        JPanel flavorGrid = new JPanel(new GridLayout(0, 3, 10, 10));

        // Fetch flavors from the service and create cards
        for (IceCreamFlavor flavor : service.getAllFlavors()) {
            if (!flavor.getAvailabilityStatus().equalsIgnoreCase("Unavailable")) {
                flavorGrid.add(createFlavorCard(flavor));
            }
        }


        add(new JScrollPane(flavorGrid), BorderLayout.CENTER);
    }

    // Method for creating a card for each flavor
    private JPanel createFlavorCard(IceCreamFlavor flavor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel(flavor.getFlavorName(), SwingConstants.CENTER);
        card.add(nameLabel, BorderLayout.NORTH);

        JTextArea detailsArea = new JTextArea(
                "Seasonality: " + flavor.getSeasonality() + "\n" +
                "Stock Level: " + flavor.getStockLevel() + "\n" +
                "Remake Threshold: " + flavor.getRemakeThreshold() + "\n" +
                "Allergens: " + flavor.getAllergens() + "\n" +
                "Availability: " + flavor.getAvailabilityStatus()
        );
        detailsArea.setEditable(false);
        detailsArea.setBackground(card.getBackground());
        card.add(detailsArea, BorderLayout.CENTER);

        JButton addButton = new JButton("Add to Order");
        addButton.addActionListener(e -> {
            if (flavor.getStockLevel() > 0) {
                CustomOrderItem.addItem(flavor);
                JOptionPane.showMessageDialog(this, flavor.getFlavorName() + " added to order!");
            } else {
                JOptionPane.showMessageDialog(this, flavor.getFlavorName() + " is out of stock.");
            }
        });
        card.add(addButton, BorderLayout.SOUTH);

        return card;
    }

}

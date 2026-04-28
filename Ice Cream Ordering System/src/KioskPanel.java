import javax.swing.*;
import java.awt.*;

/**
 * KioskPanel represents the customer-facing kiosk UI.
 * 
 * It is responsible for:
 * - Holding all kiosk screens using CardLayout
 * - Initializing shared objects (session + navigator)
 * - Controlling the starting point (welcome screen)
 */
public class KioskPanel extends JPanel {

    private final MixiesService service;
    private final Employee employee;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final KioskSession session;
    private final KioskNavigator navigator;

    public KioskPanel(MixiesService service, Employee employee, OrdersPanel ordersPanel) {
        this.service = service;
        this.employee = employee;

        setLayout(new BorderLayout());

        session = new KioskSession();
        navigator = new KioskNavigator(cardLayout, cardPanel);

        cardPanel.add(buildWelcomeScreen(), "welcome");
        cardPanel.add(new IceCreamMenuPanel(service, session, navigator), "menu");
        cardPanel.add(new IceCreamCustomizationPanel(service, session, navigator), "customize");
        cardPanel.add(new CartPanel(service, session, navigator), "cart");
        cardPanel.add(new CheckoutPanel(service, session, navigator, ordersPanel), "checkout");

        add(cardPanel, BorderLayout.CENTER);

        navigator.showWelcome();
    }

    private JPanel buildWelcomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Welcome to Mixies", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));

        JButton startButton = new JButton("Start Order");
        startButton.setPreferredSize(new Dimension(180, 60));

        startButton.addActionListener(e -> {
            if (session.getCurrentOrderID() == -1) {
                int orderID = service.createOrder(employee, 0.0, 0.0);

                if (orderID == -1) {
                    JOptionPane.showMessageDialog(this, "Could not start order.");
                    return;
                }

                session.setCurrentOrderID(orderID);
            }

            navigator.showMenu();
        });

        JPanel center = new JPanel();
        center.add(startButton);

        panel.add(title, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        return panel;
    }
}

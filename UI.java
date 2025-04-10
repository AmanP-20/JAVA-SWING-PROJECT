import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI extends JFrame {
    private final Backend backend = new Backend();

    public UI() {
        setSize(1600, 900);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel background = new JLabel(new ImageIcon("bgr.png"));
        background.setLayout(new BorderLayout());
        add(background, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel pcControllerPanel = new JPanel(new GridLayout(2, 1));
        pcControllerPanel.setOpaque(false);
        pcControllerPanel.setPreferredSize(new Dimension(500, 100));

        JLabel pcLabel = new JLabel("PC", SwingConstants.CENTER);
        JLabel controllerLabel = new JLabel("CONTROLLER", SwingConstants.CENTER);
        pcLabel.setForeground(Color.WHITE);
        controllerLabel.setForeground(Color.WHITE);
        pcLabel.setFont(new Font("Orbitron", Font.BOLD, 60));
        controllerLabel.setFont(new Font("Bebas Neue", Font.BOLD, 60));

        pcControllerPanel.add(pcLabel);
        pcControllerPanel.add(controllerLabel);

        JPanel leftContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftContainer.setOpaque(false);
        leftContainer.setBorder(BorderFactory.createEmptyBorder(250, 250, 0, 0));
        leftContainer.add(pcControllerPanel);

        ImageIcon originalIcon = new ImageIcon("bg.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));

        JPanel rightContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightContainer.setOpaque(false);
        rightContainer.setBorder(BorderFactory.createEmptyBorder(80, 0, 0, 270));
        rightContainer.add(imageLabel);

        topPanel.add(leftContainer, BorderLayout.WEST);
        topPanel.add(rightContainer, BorderLayout.EAST);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        centerPanel.setOpaque(false);

        centerPanel.add(createDevicePanel("Camera", "camera"));
        centerPanel.add(createDevicePanel("Keyboard", "keyboard"));
        centerPanel.add(createDevicePanel("Mouse", "mouse"));

        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.add(centerPanel, BorderLayout.CENTER);
        centerContainer.setBorder(BorderFactory.createEmptyBorder(0, 200, 200, 200));

        background.add(topPanel, BorderLayout.NORTH);
        background.add(centerContainer, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel createDevicePanel(String deviceName, String id) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        ImageIcon icon = new ImageIcon(deviceName.toLowerCase() + ".jpg");
        Image scaled = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaled));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        bottomPanel.setOpaque(false);

        JLabel label = new JLabel(deviceName);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 20));

        ToggleSwitch toggle = new ToggleSwitch(id);
        bottomPanel.add(label);
        bottomPanel.add(toggle);

        panel.add(imageLabel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    class ToggleSwitch extends JComponent implements KeyListener, FocusListener {
        private boolean on = true;
        private int circleX = 27;
        private final int WIDTH = 50, HEIGHT = 25;
        private boolean hasFocus = false;
        private final String deviceId;

        public ToggleSwitch(String deviceId) {
            this.deviceId = deviceId;
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFocusable(true);

            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    requestFocusInWindow();
                    toggle();
                }
            });

            addFocusListener(this);
            addKeyListener(this);
        }

        private void toggle() {
            on = !on;
            if (on) {
                backend.enableDevice(deviceId);
            } else {
                backend.disableDevice(deviceId);
            }
            animateToggle();
        }

        private void animateToggle() {
            Timer timer = new Timer(5, null);
            timer.addActionListener(e -> {
                if (on && circleX < WIDTH - HEIGHT + 2) circleX++;
                else if (!on && circleX > 2) circleX--;
                else ((Timer) e.getSource()).stop();
                repaint();
            });
            timer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(on ? Color.GREEN : Color.GRAY);
            g2.fillRoundRect(0, 0, WIDTH, HEIGHT, HEIGHT, HEIGHT);
            g2.setColor(Color.WHITE);
            g2.fillOval(circleX, 2, HEIGHT - 4, HEIGHT - 4);
            if (hasFocus) {
                g2.setColor(Color.YELLOW);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, WIDTH - 1, HEIGHT - 1, HEIGHT, HEIGHT);
            }
            g2.dispose();
        }

        @Override
        public void keyTyped(KeyEvent e) {}
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) toggle();
        }
        @Override
        public void keyReleased(KeyEvent e) {}
        @Override
        public void focusGained(FocusEvent e) {
            hasFocus = true;
            repaint();
        }
        @Override
        public void focusLost(FocusEvent e) {
            hasFocus = false;
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UI::new);
    }
}

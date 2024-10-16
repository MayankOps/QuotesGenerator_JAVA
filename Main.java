import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Quote class to represent a quote and its author
class Quote {
    String text;
    String author;

    Quote(String text, String author) {
        this.text = text;
        this.author = author;
    }

    @Override
    public String toString() {
        return "\"" + text + "\" - " + author;
    }
}

// QuoteGenerator class to load quotes from a text file
class QuoteGenerator {
    private List<Quote> quotes;
    private Random random;

    QuoteGenerator(String filename) {
        quotes = new ArrayList<>();
        random = new Random();
        loadQuotesFromFile(filename);
    }

    private void loadQuotesFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int lastDashIndex = line.lastIndexOf('-');
                if (lastDashIndex != -1) {
                    String text = line.substring(0, lastDashIndex).trim();
                    String author = line.substring(lastDashIndex + 1).trim();
                    quotes.add(new Quote(text, author));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading quotes from file: " + e.getMessage());
        }
    }

    public Quote getRandomQuote() {
        int index = random.nextInt(quotes.size());
        return quotes.get(index);
    }
}

// Main class for GUI
public class Main {
    private static QuoteGenerator quoteGenerator;
    private static JTextArea quoteArea;
    private static JButton generateButton;
    private static JFrame frame;
    private static JTextField usernameField;
    private static JPasswordField passwordField;

    public static void main(String[] args) {
        // Initialize the QuoteGenerator with the quotes file
        quoteGenerator = new QuoteGenerator("YOUR_PATH/quotes.txt");

        // Create the login frame
        createLoginFrame();
    }

    // Create the login frame
    private static void createLoginFrame() {
        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 240, 240)); // Light background

        // Create a panel for the login form
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(240, 240, 240));

        // Username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Login button
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setBackground(new Color(255, 69, 0)); // Red-Orange color
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hardcoded username and password
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Validate credentials
                if (username.equals("user") && password.equals("password")) {
                    frame.dispose(); // Close the login frame
                    createQuoteFrame(); // Open the quote generator frame
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add components to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(loginButton, gbc);

        // Add panel to the frame
        frame.add(panel, BorderLayout.CENTER);

        // Set frame visibility
        frame.setVisible(true);
    }

    // Create the quote generator frame
    private static void createQuoteFrame() {
        JFrame quoteFrame = new JFrame("Random Quote Generator");
        quoteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        quoteFrame.setSize(400, 300);
        quoteFrame.setLayout(new BorderLayout());

        // Create the quote area
        quoteArea = new JTextArea();
        quoteArea.setFont(new Font("Arial", Font.BOLD, 16));
        quoteArea.setBackground(Color.WHITE);
        quoteArea.setForeground(Color.RED);
        quoteArea.setEditable(false);
        quoteArea.setLineWrap(true);
        quoteArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(quoteArea);

        // Create the generate button
        generateButton = new JButton("Get Random Quote");
        generateButton.setFont(new Font("Arial", Font.BOLD, 14));
        generateButton.setBackground(Color.RED);
        generateButton.setForeground(Color.WHITE);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Quote randomQuote = quoteGenerator.getRandomQuote();
                quoteArea.setText(randomQuote.toString());
            }
        });

        // Create the exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 14));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(quoteFrame, "Do you really want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    // Display a thank you message in a separate dialog
                    JOptionPane.showMessageDialog(quoteFrame, "Thank You for Using Our Quotes Generator!", "Goodbye", JOptionPane.PLAIN_MESSAGE);
                    System.exit(0); // Exit the application
                }
            }
        });

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(generateButton);
        buttonPanel.add(exitButton);

        // Add components to the quote frame
        quoteFrame.add(scrollPane, BorderLayout.CENTER);
        quoteFrame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the quote frame visibility
        quoteFrame.setVisible(true);
    }
}

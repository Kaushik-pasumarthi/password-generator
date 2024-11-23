import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordGeneratorGUI extends JFrame {
    private PasswordGenerator passwordGenerator;

    public PasswordGeneratorGUI() {
        // Set up the JFrame
        super("Password Generator");
        setSize(540, 800); // Adjusted height to fit all components
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize Password Generator
        passwordGenerator = new PasswordGenerator();

        // Render GUI components
        addGuiComponents();
    }

    private void addGuiComponents() {
        // Use a panel with a scroll pane to handle all components
        JPanel panel = new JPanel(null); // Use null layout for custom positioning
        panel.setPreferredSize(new Dimension(540, 800)); // Match increased height

        JScrollPane scrollPane = new JScrollPane(panel);
        setContentPane(scrollPane);

        // Title Label
        JLabel titleLabel = new JLabel("Password Generator");
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 32));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(0, 10, 540, 39);
        panel.add(titleLabel);

        // Password Output Area
        JTextArea passwordOutput = new JTextArea();
        passwordOutput.setEditable(false);
        passwordOutput.setFont(new Font("Dialog", Font.BOLD, 32));
        JScrollPane passwordOutputPane = new JScrollPane(passwordOutput);
        passwordOutputPane.setBounds(25, 97, 479, 70);
        passwordOutputPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(passwordOutputPane);

        // Password Length Label
        JLabel passwordLengthLabel = new JLabel("Password Length: ");
        passwordLengthLabel.setFont(new Font("Dialog", Font.PLAIN, 32));
        passwordLengthLabel.setBounds(25, 215, 272, 39);
        panel.add(passwordLengthLabel);

        // Password Length Input
        JTextArea passwordLengthInputArea = new JTextArea();
        passwordLengthInputArea.setFont(new Font("Dialog", Font.PLAIN, 32));
        passwordLengthInputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        passwordLengthInputArea.setBounds(310, 215, 192, 39);
        panel.add(passwordLengthInputArea);

        // Toggle Buttons
        JToggleButton uppercaseToggle = new JToggleButton("Uppercase");
        uppercaseToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        uppercaseToggle.setBounds(25, 302, 225, 56);
        panel.add(uppercaseToggle);

        JToggleButton lowercaseToggle = new JToggleButton("Lowercase");
        lowercaseToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        lowercaseToggle.setBounds(282, 302, 225, 56);
        panel.add(lowercaseToggle);

        JToggleButton numbersToggle = new JToggleButton("Numbers");
        numbersToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        numbersToggle.setBounds(25, 373, 225, 56);
        panel.add(numbersToggle);

        JToggleButton symbolsToggle = new JToggleButton("Symbols");
        symbolsToggle.setFont(new Font("Dialog", Font.PLAIN, 26));
        symbolsToggle.setBounds(282, 373, 225, 56);
        panel.add(symbolsToggle);

        // Generate Button
        JButton generateButton = new JButton("Generate");
        generateButton.setFont(new Font("Dialog", Font.PLAIN, 32));
        generateButton.setBounds(155, 477, 222, 41);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (passwordLengthInputArea.getText().length() <= 0) return;
                boolean anyToggleSelected = lowercaseToggle.isSelected() ||
                        uppercaseToggle.isSelected() ||
                        numbersToggle.isSelected() ||
                        symbolsToggle.isSelected();

                int passwordLength = Integer.parseInt(passwordLengthInputArea.getText());
                if (anyToggleSelected) {
                    String generatedPassword = passwordGenerator.generatePassword(passwordLength,
                            uppercaseToggle.isSelected(),
                            lowercaseToggle.isSelected(),
                            numbersToggle.isSelected(),
                            symbolsToggle.isSelected());
                    passwordOutput.setText(generatedPassword);
                }
            }
        });
        panel.add(generateButton);

        // Password Strength Components
        JLabel strengthInputLabel = new JLabel("Check Password Strength:");
        strengthInputLabel.setFont(new Font("Dialog", Font.PLAIN, 32));
        strengthInputLabel.setBounds(25, 540, 500, 39);
        panel.add(strengthInputLabel);

        JTextArea strengthInputArea = new JTextArea();
        strengthInputArea.setFont(new Font("Dialog", Font.PLAIN, 26));
        strengthInputArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        strengthInputArea.setBounds(25, 590, 479, 39);
        panel.add(strengthInputArea);

        JButton checkStrengthButton = new JButton("Check Strength");
        checkStrengthButton.setFont(new Font("Dialog", Font.PLAIN, 32));
        checkStrengthButton.setBounds(155, 650, 222, 41);
        panel.add(checkStrengthButton);

        JLabel strengthResultLabel = new JLabel();
        strengthResultLabel.setFont(new Font("Dialog", Font.BOLD, 28));
        strengthResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        strengthResultLabel.setBounds(25, 710, 479, 50);
        panel.add(strengthResultLabel);

        // Strength Check Action Listener
        checkStrengthButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userPassword = strengthInputArea.getText();
                if (userPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter a password to check its strength.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int strengthScore = calculatePasswordStrength(userPassword);
                String strengthMessage = generateStrengthMessage(strengthScore);
                strengthResultLabel.setText("Strength: " + strengthScore + "/10 (" + strengthMessage + ")");
            }
        });
    }

    // Calculate Password Strength
    private int calculatePasswordStrength(String password) {
        int strengthScore = 0;

        // Length Criteria
        if (password.length() >= 8) strengthScore += 2; // Strong if 8+ characters
        if (password.length() >= 12) strengthScore += 2; // Extra points for 12+ characters

        // Character Variety
        if (password.matches(".*[A-Z].*")) strengthScore += 2; // Uppercase letters
        if (password.matches(".*[a-z].*")) strengthScore += 2; // Lowercase letters
        if (password.matches(".*[0-9].*")) strengthScore += 1; // Numbers
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) strengthScore += 1; // Special symbols

        // Deduct points for repetitive characters
        if (password.matches(".*(.)\\1{2,}.*")) strengthScore -= 1; // 3+ consecutive identical characters

        // Ensure score is within 0-10 range
        return Math.max(0, Math.min(strengthScore, 10));
    }

    // Generate a Descriptive Strength Message
    private String generateStrengthMessage(int score) {
        if (score <= 3) return "Weak";
        if (score <= 6) return "Moderate";
        if (score <= 8) return "Strong";
        return "Very Strong";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PasswordGeneratorGUI().setVisible(true);
        });
    }
}

// Mock Password Generator (Replace with your actual implementation)
class PasswordGenerator {
    private final String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";
    private final String numbers = "0123456789";
    private final String symbols = "!@#$%^&*()-_=+[]{}|;:',.<>?/";

    public String generatePassword(int length, boolean useUppercase, boolean useLowercase, boolean useNumbers, boolean useSymbols) {
        StringBuilder characterPool = new StringBuilder();

        // Add character sets based on user preferences
        if (useUppercase) characterPool.append(uppercaseLetters);
        if (useLowercase) characterPool.append(lowercaseLetters);
        if (useNumbers) characterPool.append(numbers);
        if (useSymbols) characterPool.append(symbols);

        // Validate that at least one character set is selected
        if (characterPool.length() == 0) {
            return "Select at least one option!";
        }

        // Generate the password
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characterPool.length());
            password.append(characterPool.charAt(randomIndex));
        }

        return password.toString();
    }
}


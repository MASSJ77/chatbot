import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.StandardOpenOption;


public class AdvancedChatbot {

    private enum ConversationState { GREETING, QUESTION, ANSWERING, FAREWELL }
    private enum Personality { FRIENDLY, FORMAL, HUMOROUS }
    private MusicPlayer musicPlayer = new MusicPlayer(); 
    private MusicPlayerGUI musicPlayerGUI;

    private ConversationState conversationState = ConversationState.GREETING;
    private Personality personality = Personality.FRIENDLY;
    private Map<String, String> rules = new HashMap<>();
    private Map<String, String> userProfile = new HashMap<>();
    private List<String> recentQuestions = new ArrayList<>();
    private List<String> smallTalkResponses = new ArrayList<>();
    private List<String> recentConversations = new ArrayList<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private Map<String, String> faqMap = new HashMap<>(); 
    public AdvancedChatbot() {
        initializeRules();
        loadUserProfile();
        loadConversationHistory();
        initializeSmallTalkResponses();
        loadFAQ("faqDatabase.txt"); // Load FAQs during initialization
        musicPlayerGUI = new MusicPlayerGUI(musicPlayer);
    }
    
    private void initializeRules() {
        rules.put(".*\\bhello\\b.*", "Hello! How can I assist you today?");
        rules.put(".*\\bhow are you\\b.*", "I'm here to help! What can I do for you?");
        rules.put(".*\\bname\\b.*", "I'm your AI assistant, ready to answer your questions.");
        rules.put(".*\\bweather\\b.*", "Please enter your location to get the weather updates!");
        rules.put(".*\\bbye\\b.*", "Goodbye! It was nice chatting with you.");
    }
     
      private void loadFAQ(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("::", 2);
                if (parts.length == 2) {
                    faqMap.put(parts[0].trim(), parts[1].trim());
                }
            }
            System.out.println("FAQ database loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading FAQ file: " + e.getMessage());
        }
    }

    private String getFAQResponse(String query) {
        String bestMatch = null;
        double bestScore = 0.0;
        for (String question : faqMap.keySet()) {
            double score = Similarity(query, question);
            if (score > bestScore) {
                bestScore = score;
                bestMatch = question;
            }
        }
        return (bestMatch != null) ? faqMap.get(bestMatch) : null;
    }

   private double Similarity(String query, String question) {
        Map<String, Integer> queryWordFreq = getWordFrequency(query);
        Map<String, Integer> questionWordFreq = getWordFrequency(question);

        Set<String> uniqueWords = new HashSet<>();
        uniqueWords.addAll(queryWordFreq.keySet());
        uniqueWords.addAll(questionWordFreq.keySet());

        double dotProduct = 0.0;
        double queryMagnitude = 0.0;
        double questionMagnitude = 0.0;

        for (String word : uniqueWords) {
            int queryFreq = queryWordFreq.getOrDefault(word, 0);
            int questionFreq = questionWordFreq.getOrDefault(word, 0);

            dotProduct += queryFreq * questionFreq;
            queryMagnitude += queryFreq * queryFreq;
            questionMagnitude += questionFreq * questionFreq;
        }

        if (queryMagnitude == 0.0 || questionMagnitude == 0.0) {
            return 0.0;
        }

        return dotProduct / (Math.sqrt(queryMagnitude) * Math.sqrt(questionMagnitude)); // Cosine similarity
    }

      private Map<String, Integer> getWordFrequency(String text) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        String[] words = text.split("\\s+");
        for (String word : words) {
            word = word.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }
        return wordFrequency;
    }
    private void loadUserProfile() {
        Path profilePath = Paths.get("userProfile.txt");
        if (!Files.exists(profilePath)) {
            System.out.println("User profile file not found. Creating a new profile.");
            try {
                Files.createFile(profilePath);
            } catch (IOException e) {
                logError("Could not create user profile file: " + e.getMessage());
                return;
            }
        }
        try {
            List<String> lines = Files.readAllLines(profilePath);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    userProfile.put(parts[0].trim(), parts[1].trim());
                }
            }
            System.out.println("User profile loaded successfully.");
        } catch (IOException e) {
            logError("Error loading user profile: " + e.getMessage());
        }
    }

    public void saveUserProfile() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("userProfile.txt"))) {
            for (Map.Entry<String, String> entry : userProfile.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
            System.out.println("User profile saved successfully.");
        } catch (IOException e) {
            logError("Error saving user profile: " + e.getMessage());
        }
    }
 
    private void loadConversationHistory() {
        Path historyPath = Paths.get("conversationHistory.txt");
        if (Files.exists(historyPath)) {
            try {
                recentConversations = Files.readAllLines(historyPath);
                System.out.println("Conversation history loaded.");
            } catch (IOException e) {
                logError("Error loading conversation history: " + e.getMessage());
            }
        }
    }

    public void saveConversationHistory() {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("conversationHistory.txt"))) {
            for (String conv : recentConversations) {
                writer.write(conv);
                writer.newLine();
            }
            System.out.println("Conversation history saved.");
        } catch (IOException e) {
            logError("Error saving conversation history: " + e.getMessage());
        }
    }

    private void logError(String message) {
        System.err.println("Error: " + message);
    }

public String handleMusicCommand(String userInput) {
   
    return "Here You Go.";
}



    private String analyzeSentiment(String input) {
        if (input.matches(".*(sad|unhappy|angry|frustrated|disappointed).*")) {
            return "I'm sorry to hear that. I'm here if you need to talk.";
        } else if (input.matches(".*(happy|great|excited|joyful).*")) {
            return "That's wonderful to hear!";
        }
        return "";
    }

    private String getCurrentTimeInIST() {
        ZonedDateTime currentISTTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return currentISTTime.format(formatter);
    }

    private String getWeather(String location) {
        return "The current weather in " + location + " is sunny and 25C."; // Placeholder weather response
    }
    
     private String getDynamicGreeting() {
        int hour = ZonedDateTime.now(ZoneId.of("Asia/Kolkata")).getHour();
        if (hour >= 5 && hour < 12) {
            return "Good Morning! How can I assist you today?";
        } else if (hour >= 12 && hour < 17) {
            return "Good Afternoon! How can I assist you today?";
        } else {
            return "Good Evening! How can I assist you today?";
        }
    }

   
   public void collectFeedback(String response, String feedback) {
    try (BufferedWriter writer = Files.newBufferedWriter(
            Paths.get("feedback.txt"),
            StandardOpenOption.CREATE, 
            StandardOpenOption.APPEND)) {
        writer.write("Response: " + response + "\nFeedback: " + feedback + "\n\n");
        System.out.println("Feedback recorded successfully.");
    } catch (IOException e) {
        logError("Error saving feedback: " + e.getMessage());
    }
}
    
    public String summarizeSession() {
        StringBuilder summary = new StringBuilder("Session Summary:\n");
        summary.append("Total exchanges: ").append(recentConversations.size()).append("\n");
        summary.append("Topics discussed: ").append(getDistinctTopics()).append("\n");
        summary.append("Thank you for chatting with me!");
        return summary.toString();
    }
 public void endSession() {
        System.out.println(summarizeSession());
        System.out.println(getTotalWordCount());
    }

    
    private Set<String> getDistinctTopics() {
        Set<String> topics = new HashSet<>();
        for (String convo : recentConversations) {
            if (convo.startsWith("User:")) {
                String[] words = convo.replace("User:", "").toLowerCase().split("\\W+");
                topics.addAll(Arrays.asList(words));
            }
        }
        return topics;
    }

    
    public void addSmallTalkResponse(String response) {
        smallTalkResponses.add(response);
        System.out.println("New small talk response added: " + response);
    }

    
    public int getTotalWordCount() {
        int wordCount = 0;
        for (String convo : recentConversations) {
            wordCount += convo.split("\\s+").length;
        }
        return wordCount;
    }

public String evaluateMathExpression(String input) {
        input = input.replaceAll(" ", ""); // Remove any whitespace
        Stack<Double> values = new Stack<>();
        Stack<Character> ops = new Stack<>();

        try {
            for (int i = 0; i < input.length(); i++) {
                char ch = input.charAt(i);

                // If current character is a digit or a decimal point, parse the full number
                if (Character.isDigit(ch) || ch == '.') {
                    StringBuilder sbuf = new StringBuilder();
                    while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                        sbuf.append(input.charAt(i++));
                    }
                    values.push(Double.parseDouble(sbuf.toString()));
                    i--; // Move back as the loop will increment it
                }
                // Handle negative numbers
                else if (ch == '-' && (i == 0 || input.charAt(i - 1) == '(')) {
                    StringBuilder sbuf = new StringBuilder("-");
                    i++;
                    while (i < input.length() && (Character.isDigit(input.charAt(i)) || input.charAt(i) == '.')) {
                        sbuf.append(input.charAt(i++));
                    }
                    values.push(Double.parseDouble(sbuf.toString()));
                    i--;
                }
                // If character is '(', push it to 'ops'
                else if (ch == '(') {
                    ops.push(ch);
                }
                // If character is ')', solve the expression within parentheses
                else if (ch == ')') {
                    while (!ops.isEmpty() && ops.peek() != '(') {
                        if (values.size() < 2) return "Error: Invalid expression.";
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    if (ops.isEmpty() || ops.pop() != '(') return "Error: Mismatched parentheses.";
                }
                // If character is an operator, process based on precedence
                else if (isOperator(ch)) {
                    while (!ops.isEmpty() && hasPrecedence(ch, ops.peek())) {
                        if (values.size() < 2) return "Error: Invalid expression.";
                        values.push(applyOp(ops.pop(), values.pop(), values.pop()));
                    }
                    ops.push(ch);
                }
                // If character is unexpected, return an error
                else {
                    return "Error: Invalid character in expression.";
                }
            }

            // Complete any remaining operations in the stacks
            while (!ops.isEmpty()) {
                if (values.size() < 2) return "Error: Invalid expression.";
                values.push(applyOp(ops.pop(), values.pop(), values.pop()));
            }

            // Final check: there should only be one result in the values stack
            return values.size() == 1 ? "The result is: " + values.pop() : "Error: Invalid expression.";

        } catch (Exception e) {
            return "Error: Invalid expression.";
        }
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        return (op1 != '*' && op1 != '/' && op1 != '^') || (op2 != '+' && op2 != '-');
    }

    private double applyOp(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': 
                if (b == 0) throw new ArithmeticException("Division by zero.");
                return a / b;
            case '^': return Math.pow(a, b);
            default: throw new IllegalArgumentException("Unsupported operator: " + op);
        }
    }

    private boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^';
    }
    
    private void initializeSmallTalkResponses() {
        smallTalkResponses.add("I'm here to help!");
        smallTalkResponses.add("How can I assist you?");
        smallTalkResponses.add("What would you like to know?");
        smallTalkResponses.add("Feel free to ask me anything.");
        smallTalkResponses.add("I'm listening!");
    }

public String getResponse(String userInput) {
    recentConversations.add("User: " + userInput);
    String normalizedInput = userInput.toLowerCase().trim();
    // Default response if nothing matches
    String sentimentResponse = analyzeSentiment(normalizedInput);
    
      // Check FAQ database
        String faqResponse = getFAQResponse(normalizedInput);
            
    if (!sentimentResponse.isEmpty()) {
        return sentimentResponse;
    }
    if (normalizedInput.contains("generate code") || normalizedInput.contains("code for")) {
            AdvancedCodeGenerator codeGenerator = new AdvancedCodeGenerator();
            return codeGenerator.generateCode(userInput);
        }
     if (userInput.toLowerCase().startsWith("music")) {
          SwingUtilities.invokeLater(() -> musicPlayerGUI.showMusicPlayer());
        return handleMusicCommand(userInput);
    }
    if (normalizedInput.matches(".*\\b(hello|hi|hey)\\b.*")) {
            conversationState = ConversationState.QUESTION;
            return "Hello! How can I assist you today?";
        }
    if (conversationState == ConversationState.GREETING) {
            conversationState = ConversationState.QUESTION;
            return getDynamicGreeting();
    }
       if (normalizedInput.contains("summarize")) {
        String summary = summarizeSession();
        recentConversations.add("Chatbot: " + summary);
        return summary;
    }
    // Handling different types of user queries in the QUESTION state
      if (conversationState == ConversationState.QUESTION) {
        if (normalizedInput.matches(".*\\b(current time|clock)\\b.*")) {
            return "The current time in India is " + getCurrentTimeInIST();
        } else if (normalizedInput.matches(".*\\b(weather|forecast|temperature)\\b.*")) {
            return "Please provide your location for weather updates.";
        } else if (Pattern.matches(".*\\b\\d+([\\+\\-\\*\\/\\(\\)]*\\d+)*\\b.*", normalizedInput)) {
            return evaluateMathExpression(normalizedInput);
        } else if (normalizedInput.matches(".*\\bbye\\b.*")) {
            conversationState = ConversationState.FAREWELL;
            return "Goodbye! It was nice chatting with you.";
        } else  if (faqResponse != null) {
            return faqResponse;
        }
    
        else {
            // Use predefined rules for small talk or other questions
             for (Map.Entry<String, String> entry : rules.entrySet()) {
        if (Pattern.matches(entry.getKey(), normalizedInput)) {
            String response = entry.getValue();
            recentConversations.add("Chatbot: " + response);
            saveConversationHistory(); // Save history after every interaction
            return response;
        }
    }

    String fallbackResponse = "Sorry i can't help you with that\n" ;
    recentConversations.add("Chatbot: " + fallbackResponse);
    saveConversationHistory(); // Save history after every interaction
    return fallbackResponse;
}}
    
    if (conversationState == ConversationState.FAREWELL) {
        saveConversationHistory();
        endSession();
        return "Goodbye! I hope to talk with you again soon.";
    }
  
    return smallTalkResponses.get((int) (Math.random() * smallTalkResponses.size()));
}
    
public static void main(String[] args) {
    JFrame frame = new JFrame("AdvancedChatbot");
    AdvancedChatbot chatbot = new AdvancedChatbot();
    
      String disclaimer = "Disclaimer: ChatBot can make mistakes.\n" +
                        "It is currently under development stage.\n" +
                        "Special Thanks To ChatGPT.";
    JOptionPane.showMessageDialog(frame, disclaimer, "Disclaimer", JOptionPane.WARNING_MESSAGE);

    
     Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        chatbot.saveConversationHistory();
        System.out.println("Conversation history saved on exit.");
    }));

    // Frame setup for a modern, fluid style
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(700, 800);
    frame.setLayout(new BorderLayout());
    frame.getContentPane().setBackground(new Color(245, 250, 255));

    // Top panel for personality selection
    JPanel personalityPanel = new JPanel();
    personalityPanel.setBackground(new Color(220, 230, 240));
    personalityPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

    JLabel personalityLabel = new JLabel("Choose Personality:");
    personalityLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

    JComboBox<String> personalitySelector = new JComboBox<>(new String[]{"FRIENDLY","FORMAL", "HUMOROUS"});
    personalitySelector.setFont(new Font("SansSerif", Font.PLAIN, 14));
    personalitySelector.setBackground(new Color(255, 255, 255));
    

    personalityPanel.add(personalityLabel);
    personalityPanel.add(personalitySelector);
    frame.add(personalityPanel, BorderLayout.NORTH);

    // Chat area with smooth scrolling
    JPanel chatArea = new JPanel();
    chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
    chatArea.setBackground(new Color(245, 250, 255));
    chatArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JScrollPane scrollPane = new JScrollPane(chatArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.getVerticalScrollBar().setUnitIncrement(8);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 230, 240), 2));
    frame.add(scrollPane, BorderLayout.CENTER);

    // Input panel with modern styling
    JPanel inputPanel = new JPanel(new BorderLayout());
    JTextField inputField = new JTextField();
    inputField.setFont(new Font("SansSerif", Font.PLAIN, 16));
    inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JButton sendButton = new JButton("Send");
    sendButton.setBackground(new Color(30, 215, 96));
    sendButton.setForeground(Color.WHITE);
    sendButton.setFont(new Font("SansSerif", Font.BOLD, 14));
    sendButton.setFocusPainted(false);
    sendButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

    inputPanel.add(inputField, BorderLayout.CENTER);
    inputPanel.add(sendButton, BorderLayout.EAST);
    inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    frame.add(inputPanel, BorderLayout.SOUTH);
    
     personalityPanel.setLayout(new BorderLayout());

      // Feedback button setup
JButton feedbackButton = new JButton("Feedback");
feedbackButton.setFont(new Font("SansSerif", Font.BOLD, 14));
feedbackButton.setBackground(new Color(30, 215, 96)); // Initial orange color
feedbackButton.setForeground(Color.WHITE);
feedbackButton.setFocusPainted(false);
feedbackButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
feedbackButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

// Make button corners rounded
feedbackButton.setContentAreaFilled(false);
feedbackButton.setOpaque(true);

// Add hover effect for smooth transition
feedbackButton.addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mouseEntered(java.awt.event.MouseEvent evt) {
        feedbackButton.setBackground(new Color(25, 190, 85)); // Darker orange on hover
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent evt) {
        feedbackButton.setBackground(new Color(30, 215, 96)); // Original color
    }
});

// Add action listener to collect feedback
feedbackButton.addActionListener(e -> {
    String feedback = JOptionPane.showInputDialog(frame, 
                                                  "Please provide your feedback:", 
                                                  "Feedback", JOptionPane.PLAIN_MESSAGE);
    if (feedback != null && !feedback.trim().isEmpty()) {
        chatbot.collectFeedback("General Feedback", feedback);
        JOptionPane.showMessageDialog(frame, 
                                      "Thank you for your feedback!", 
                                      "Feedback", 
                                      JOptionPane.INFORMATION_MESSAGE);
    }
});
    personalityPanel.add(feedbackButton, BorderLayout.WEST);
    // Add personality label and selector to the center of the panel
JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
centerPanel.setBackground(personalityPanel.getBackground());
centerPanel.add(personalityLabel);
centerPanel.add(personalitySelector);

// Add the center panel to the personalityPanel
personalityPanel.add(centerPanel, BorderLayout.CENTER);
    // Action listener for sending messages
    ActionListener sendAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userInput = inputField.getText().trim();
            if (userInput.isEmpty()) return;

            // Add user message bubble with water drop effect
            addMessageBubble(chatArea, "You: " + userInput, new Color(30, 144, 255), Color.WHITE);

            // Get chatbot response based on selected personality
            String response = chatbot.getResponse(userInput);
            addMessageBubble(chatArea, "Chatbot: " + response, new Color(173, 216, 230), Color.BLACK);

            inputField.setText("");
            chatArea.revalidate();
            chatArea.repaint();

            // Smooth scroll to bottom
            SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
        }
    };

    inputField.addActionListener(sendAction);
    sendButton.addActionListener(sendAction);

    frame.setVisible(true);
}

// Helper method to add message bubble with water drop animation
private static void addMessageBubble(JPanel chatArea, String message, Color background, Color textColor) {
    JPanel bubblePanel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        }
    };
    bubblePanel.setBackground(new Color(0, 0, 0, 0));
    bubblePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    bubblePanel.setLayout(new BorderLayout());

    JLabel messageLabel = new JLabel("<html><body style='width: 250px;'>" + message + "</body></html>");
    messageLabel.setForeground(textColor);
    messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
    bubblePanel.add(messageLabel, BorderLayout.CENTER);

    bubblePanel.setMaximumSize(new Dimension(500, bubblePanel.getPreferredSize().height));

    JPanel messageContainer = new JPanel();
    messageContainer.setLayout(new BoxLayout(messageContainer, BoxLayout.Y_AXIS));
    messageContainer.setOpaque(false);
    messageContainer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    messageContainer.add(bubblePanel);

    chatArea.add(messageContainer);

    // Water drop effect using timer-based animation
    javax.swing.Timer timer = new javax.swing.Timer(15, null);
    timer.addActionListener(new ActionListener() {
        private float dropHeight = -40; // Initial height above view
        private boolean bounceUp = false;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!bounceUp) {
                dropHeight += 4;
                if (dropHeight >= 0) {
                    dropHeight = 0;
                    bounceUp = true;
                }
            } else {
                dropHeight -= 2;
                if (dropHeight <= -5) {
                    bounceUp = false;
                    timer.stop();
                }
            }

            bubblePanel.setLocation(bubblePanel.getX(), (int) dropHeight);
            bubblePanel.repaint();
        }
    });
    timer.start();
}}

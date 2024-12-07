import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.Scanner;

public class EnhancedUltimateTestBot extends AdvancedChatbot {

    private static final String LOG_FILE = "ultimate_test_results_with_ratings.log";
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static double totalRating = 0.0;

    public static void main(String[] args) {
        AdvancedChatbot chatbot = new AdvancedChatbot();
        System.out.println("=== Welcome to the Enhanced Ultimate Test Bot with Ratings ===");
        System.out.println("1. Interactive Mode");
        System.out.println("2. Batch Mode (Run Predefined Tests)");
        System.out.println("Enter your choice (1 or 2): ");

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine().trim();

        try (BufferedWriter logWriter = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            logWriter.write("==== New Test Session ====\n");

            if ("1".equals(choice)) {
                runInteractiveMode(chatbot, scanner, logWriter);
            } else if ("2".equals(choice)) {
                runBatchMode(chatbot, logWriter);
            } else {
                System.out.println("Invalid choice. Exiting.");
            }

            logWriter.write("Session Summary:\n");
            logWriter.write("Total Tests: " + totalTests + "\n");
            logWriter.write("Passed Tests: " + passedTests + "\n");
            logWriter.write("Success Rate: " + ((double) passedTests / totalTests) * 100 + "%\n");
            logWriter.write("Average Rating: " + (totalRating / totalTests) + "/5.0\n");

            if ((totalRating / totalTests) < 3.0) {
                logWriter.write("Suggestions: Improve response accuracy, handle edge cases better, and optimize response times.\n");
            } else {
                logWriter.write("Overall Performance: Satisfactory, but there is room for improvement.\n");
            }
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        } finally {
            scanner.close();
        }

        System.out.println("Test session complete. Results logged to: " + LOG_FILE);
    }

    private static void runInteractiveMode(AdvancedChatbot chatbot, Scanner scanner, BufferedWriter logWriter) throws IOException {
        System.out.println("Interactive mode started. Type 'bye' to exit.");
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine();

            if ("bye".equalsIgnoreCase(userInput.trim())) {
                System.out.println("Exiting interactive mode.");
                break;
            }

            Instant start = Instant.now();
            String response = chatbot.getResponse(userInput);
            Instant end = Instant.now();

            long responseTime = Duration.between(start, end).toMillis();
            int rating = rateResponse(userInput, response, responseTime);
            totalRating += rating;

            System.out.println("Chatbot: " + response + " (Response Time: " + responseTime + " ms, Rating: " + rating + "/5)");

            logTestResult(logWriter, userInput, response, responseTime, rating, rating >= 3 ? "PASS" : "FAIL");
        }
    }

    private static void runBatchMode(AdvancedChatbot chatbot, BufferedWriter logWriter) throws IOException {
        System.out.println("Batch mode started. Running predefined tests...");

        String[][] testCases = {
                {"hello", "Basic greeting"},
                {"What time is it?", "Current time retrieval"},
                {"2+2*5", "Valid math expression"},
                {"2++2", "Invalid math expression"},
                {"Tell me the weather in New York", "Weather query"},
                {"What's the meaning of life?", "Unsupported query"},
                {"I feel happy today!", "Sentiment analysis (positive)"},
                {"I am so sad and disappointed.", "Sentiment analysis (negative)"},
                {"Generate code for a calculator", "FAQ/code generation query"},
                {"This is a very long input that is meant to test how the chatbot handles extremely verbose and nonsensical input without breaking or crashing in the process.", "Stress test with long input"}
        };

        for (String[] testCase : testCases) {
            executeTest(chatbot, testCase[0], testCase[1], logWriter);
        }

        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            String randomInput = generateRandomInput(random);
            executeTest(chatbot, randomInput, "Random edge case test", logWriter);
        }

        System.out.println("Batch mode complete. Check log file for results.");
    }

    private static void executeTest(AdvancedChatbot chatbot, String input, String description, BufferedWriter logWriter) throws IOException {
        totalTests++;
        System.out.println("Test " + totalTests + ": " + description);
        System.out.println("Input: " + input);

        Instant start = Instant.now();
        String response = chatbot.getResponse(input);
        Instant end = Instant.now();

        long responseTime = Duration.between(start, end).toMillis();
        int rating = rateResponse(input, response, responseTime);
        totalRating += rating;

        System.out.println("Chatbot: " + response + " (Response Time: " + responseTime + " ms, Rating: " + rating + "/5)");
        logTestResult(logWriter, input, response, responseTime, rating, rating >= 3 ? "PASS" : "FAIL");
    }

    private static void logTestResult(BufferedWriter logWriter, String input, String response, long responseTime, int rating, String result) throws IOException {
        logWriter.write("Test Case:\n");
        logWriter.write("Input: " + input + "\n");
        logWriter.write("Response: " + response + "\n");
        logWriter.write("Response Time: " + responseTime + " ms\n");
        logWriter.write("Rating: " + rating + "/5\n");
        logWriter.write("Result: " + result + "\n\n");

        if ("FAIL".equals(result)) {
            logWriter.write("Reason for Failure: Response was inaccurate, irrelevant, or too slow.\n");
        }
    }

    private static int rateResponse(String input, String response, long responseTime) {
        int rating = 5;

        // Penalize irrelevant or fallback responses
        if (response.contains("I don't know") || response.contains("Error")) {
            rating -= 2;
        }

        // Penalize slow responses
        if (responseTime > 1000) { // More than 1 second
            rating -= 1;
        }

        // Penalize lack of relevance
        if (!isRelevant(input, response)) {
            rating -= 1;
        }

        return Math.max(rating, 1); // Ensure the rating is at least 1
    }

    private static boolean isRelevant(String input, String response) {
        input = input.toLowerCase();
        response = response.toLowerCase();

        // Check for keywords in the response
        if (input.contains("time") && response.contains("time")) return true;
        if (input.contains("weather") && response.contains("weather")) return true;
        if (input.matches(".*\\d+.*") && response.matches(".*\\d+.*")) return true;
        return !response.contains("I don't know") && !response.contains("Error");
    }

    private static String generateRandomInput(Random random) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
        int length = random.nextInt(250) + 50;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }

        return sb.toString();
    }
}

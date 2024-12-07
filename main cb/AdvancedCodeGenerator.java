import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AdvancedCodeGenerator extends AdvancedChatbot {

    // Main method to generate code based on input patterns
    public String generateCode(String input) {
        String response;

        // Match basic loops
        if (Pattern.compile("\\b(loop|iterate)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getLoopExample(input);
        }
        // Match conditions
        else if (Pattern.compile("\\b(if statement|condition)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getIfStatementExample();
        }
        // Match sorting algorithms
        else if (Pattern.compile("\\bsort\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getSortingAlgorithmExample(input);
        }
        // Match array processing examples
        else if (Pattern.compile("\\b(array|list)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getArrayProcessingExample(input);
        }
        // Match database connection setup
        else if (Pattern.compile("\\bdatabase\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getDatabaseConnectionExample();
        }
        // Match advanced features like recursion, threading, etc.
        else if (Pattern.compile("\\b(recursion|factorial|fibonacci)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getRecursionExample(input);
        }
        else if (Pattern.compile("\\b(threading|multithreading)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getThreadingExample(input);
        }
        else if (Pattern.compile("\\b(reflection)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getReflectionExample(input);
        }
        else if (Pattern.compile("\\b(annotation)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getAnnotationExample(input);
        }
        else if (Pattern.compile("\\b(graph|dijkstra)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getGraphExample(input);
        }
        else if (Pattern.compile("\\b(memory|gc)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getMemoryManagementExample(input);
        }
        else if (Pattern.compile("\\b(knapsack|algorithm)\\b", Pattern.CASE_INSENSITIVE).matcher(input).find()) {
            response = getAdvancedAlgorithmExample(input);
        }
        // Provide feedback if no patterns match
        else {
            response = "I'm not sure how to generate code for that request. Could you be more specific?";
        }

        return response;
    }

    // Example method to provide a dynamic loop template
    private String getLoopExample(String input) {
        if (input.toLowerCase().contains("while")) {
            return "Here's a while loop in Java:\n\n" +
                    "int i = 0;\nwhile (i < 10) {\n" +
                    "    System.out.println(i);\n" +
                    "    i++;\n" +
                    "}\n";
        } else {
            return "Here's a basic for loop in Java:\n\n" +
                    "for (int i = 0; i < 10; i++) {\n" +
                    "    System.out.println(i);\n" +
                    "}\n";
        }
    }

    // Example method to provide a conditional statement template
    private String getIfStatementExample() {
        return "Here’s an if-else statement in Java:\n\n" +
                "if (condition) {\n" +
                "    // code if condition is true\n" +
                "} else {\n" +
                "    // code if condition is false\n" +
                "}\n";
    }

    // Example method for various sorting algorithms
    private String getSortingAlgorithmExample(String input) {
        if (input.toLowerCase().contains("bubble")) {
            return "Here's a Bubble Sort algorithm in Java:\n\n" +
                    "for (int i = 0; i < arr.length - 1; i++) {\n" +
                    "    for (int j = 0; j < arr.length - i - 1; j++) {\n" +
                    "        if (arr[j] > arr[j + 1]) {\n" +
                    "            int temp = arr[j];\n" +
                    "            arr[j] = arr[j + 1];\n" +
                    "            arr[j + 1] = temp;\n" +
                    "        }\n" +
                    "    }\n" +
                    "}\n";
        } else {
            return "Here's a Selection Sort algorithm in Java:\n\n" +
                    "for (int i = 0; i < arr.length - 1; i++) {\n" +
                    "    int minIdx = i;\n" +
                    "    for (int j = i + 1; j < arr.length; j++) {\n" +
                    "        if (arr[j] < arr[minIdx]) {\n" +
                    "            minIdx = j;\n" +
                    "        }\n" +
                    "    }\n" +
                    "    int temp = arr[minIdx];\n" +
                    "    arr[minIdx] = arr[i];\n" +
                    "    arr[i] = temp;\n" +
                    "}\n";
        }
    }

    // Example method for array processing
    private String getArrayProcessingExample(String input) {
        if (input.toLowerCase().contains("reverse")) {
            return "Here's how to reverse an array in Java:\n\n" +
                    "for (int i = 0; i < arr.length / 2; i++) {\n" +
                    "    int temp = arr[i];\n" +
                    "    arr[i] = arr[arr.length - 1 - i];\n" +
                    "    arr[arr.length - 1 - i] = temp;\n" +
                    "}\n";
        } else {
            return "Here's how to find the maximum element in an array in Java:\n\n" +
                    "int max = arr[0];\nfor (int i = 1; i < arr.length; i++) {\n" +
                    "    if (arr[i] > max) {\n" +
                    "        max = arr[i];\n" +
                    "    }\n" +
                    "}\n";
        }
    }

    // Example method for a database connection
    private String getDatabaseConnectionExample() {
        return "Here’s how to set up a basic MySQL database connection in Java:\n\n" +
                "import java.sql.Connection;\n" +
                "import java.sql.DriverManager;\n" +
                "import java.sql.SQLException;\n\n" +
                "public class DatabaseConnection {\n" +
                "    public static Connection connect() throws SQLException {\n" +
                "        String url = \"jdbc:mysql://localhost:3306/dbname\";\n" +
                "        String user = \"username\";\n" +
                "        String password = \"password\";\n" +
                "        return DriverManager.getConnection(url, user, password);\n" +
                 "    }\n" +
                "}\n";
    }

    // Advanced Recursion Example (Factorial, Fibonacci)
    private String getRecursionExample(String input) {
        return "Here’s an example of calculating factorial recursively in Java:\n\n" +
               "public class Factorial {\n" +
               "    public static int factorial(int n) {\n" +
               "        if (n == 0) return 1;\n" +
               "        return n * factorial(n - 1);\n" +
               "    }\n" +
               "}\n";
    }

      // Custom Annotation Example
    private String getAnnotationExample(String input) {
        return "Here’s an example of a custom annotation in Java:\n\n" +
               "import java.lang.annotation.*;\n\n" +
               "@Retention(RetentionPolicy.RUNTIME)\n" +
               "@Target(ElementType.METHOD)\n" +
               "public @interface MyAnnotation {\n" +
               "    String value() default \"Hello\";\n" +
               "}\n\n" +
               "public class MyClass {\n" +
               "    @MyAnnotation(value = \"Custom Annotation\")\n" +
               "    public void myMethod() {\n" +
               "        System.out.println(\"Method with annotation\");\n" +
               "    }\n" +
               "}\n";
    }

    // Threading Example
    private String getThreadingExample(String input) {
        return "Here’s an example of a thread in Java:\n\n" +
               "class MyThread extends Thread {\n" +
               "    public void run() {\n" +
               "        System.out.println(\"Thread is running\");\n" +
               "    }\n" +
               "}\n" +
               "public static void main(String[] args) {\n" +
               "    MyThread t = new MyThread();\n" +
               "    t.start();\n" +
               "}\n";
    }

    // Reflection Example
    private String getReflectionExample(String input) {
        return "Here’s an example of reflection in Java:\n\n" +
               "import java.lang.reflect.Method;\n\n" +
               "public class ReflectionExample {\n" +
               "    public void hello() {\n" +
               "        System.out.println(\"Hello, Reflection!\");\n" +
               "    }\n\n" +
               "    public static void main(String[] args) throws Exception {\n" +
               "        ReflectionExample obj = new ReflectionExample();\n" +
               "        Method method = obj.getClass().getMethod(\"hello\");\n" +
               "        method.invoke(obj);\n" +
               "    }\n" +
               "}\n";
    }

    // Example for Graph Algorithms
    private String getGraphExample(String input) {
        return "Here’s an example of Dijkstra’s Algorithm in Java:\n\n" +
               "import java.util.*;\n\n" +
               "public class Dijkstra {\n" +
               "    public static void dijkstra(int[][] graph, int start) {\n" +
               "        int V = graph.length;\n" +
               "        int[] dist = new int[V];\n" +
               "        Arrays.fill(dist, Integer.MAX_VALUE);\n" +
               "        dist[start] = 0;\n" +
               "        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));\n" +
               "        pq.offer(new int[]{start, 0});\n" +
               "        while (!pq.isEmpty()) {\n" +
               "            int[] u = pq.poll();\n" +
               "            for (int v = 0; v < V; v++) {\n" +
               "                if (graph[u[0]][v] != 0 && dist[u[0]] + graph[u[0]][v] < dist[v]) {\n" +
               "                    dist[v] = dist[u[0]] + graph[u[0]][v];\n" +
               "                    pq.offer(new int[]{v, dist[v]});\n" +
               "                }\n" +
               "            }\n" +
               "        }\n" +
               "        System.out.println(Arrays.toString(dist));\n" +
               "    }\n" +
               "}\n";
    }

    // Memory management and Garbage Collection Example
    private String getMemoryManagementExample(String input) {
        return "Here's a basic memory management example:\n\n" +
               "public class MemoryManagement {\n" +
               "    public static void main(String[] args) {\n" +
               "        MemoryManagement obj = new MemoryManagement();\n" +
               "        obj = null; // object is dereferenced, eligible for GC\n" +
               "        System.gc(); // request garbage collection\n" +
               "    }\n" +
               "}\n";
    }

    // Advanced Algorithm Example (Knapsack)
    private String getAdvancedAlgorithmExample(String input) {
        return "Here’s an example of the 0/1 Knapsack Problem in Java:\n\n" +
               "public class Knapsack {\n" +
               "    public static int knapSack(int W, int wt[], int val[], int n) {\n" +
               "        int[][] dp = new int[n + 1][W + 1];\n" +
               "        for (int i = 0; i <= n; i++) {\n" +
               "            for (int w = 0; w <= W; w++) {\n" +
               "                if (i == 0 || w == 0)\n" +
               "                    dp[i][w] = 0;\n" +
               "                else if (wt[i - 1] <= w)\n" +
               "                    dp[i][w] = Math.max(val[i - 1] + dp[i - 1][w - wt[i - 1]], dp[i - 1][w]);\n" +
               "                else\n" +
               "                    dp[i][w] = dp[i - 1][w];\n" +
               "            }\n" +
               "        }\n" +
               "        return dp[n][W];\n" +
               "    }\n" +
               "}\n";
    }
}

   
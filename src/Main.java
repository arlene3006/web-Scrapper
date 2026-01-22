import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Welcome to Simple Web Scraper");

        // Step 1: Enter URL
        System.out.print("Enter website URL: ");
        String url = sc.nextLine();

        // URL validation
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            System.out.println("Invalid URL. Please include http:// or https://");
            sc.close();
            return;
        }

        // Step 2: Menu for scraping choice
        System.out.println("\nSelect data to extract:");
        System.out.println("1. Headlines");
        System.out.println("2. Links");
        System.out.print("Enter your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        if (choice != 1 && choice != 2) {
            System.out.println("Invalid choice. Program terminated.");
            sc.close();
            return;
        }

        // Step 3: Ask for file format
        System.out.println("\nChoose file format to save data:");
        System.out.println("1. TXT");
        System.out.println("2. CSV");
        System.out.print("Enter your choice: ");
        int fileChoice = sc.nextInt();
        sc.nextLine(); // consume newline

        if (fileChoice != 1 && fileChoice != 2) {
            System.out.println("Invalid file choice. Program terminated.");
            sc.close();
            return;
        }

        // Step 4: Connect to website using Jsoup
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println("\nConnected successfully!");

            Elements elements;      // To hold headlines or links
            String filename;        // File name for saving

            if (choice == 1) {
                elements = doc.select("h1, h2, h3");
                filename = (fileChoice == 1) ? "headlines.txt" : "headlines.csv";
            } else {
                elements = doc.select("a[href]");
                filename = (fileChoice == 1) ? "links.txt" : "links.csv";
            }

            if (elements.isEmpty()) {
                System.out.println("No data found on this page.");
            } else {
                // Display in console
                System.out.println("\nData found:");
                int count = 1;
                for (Element el : elements) {
                    if (choice == 1) {
                        System.out.println(count + ". " + el.text());
                    } else {
                        System.out.println(count + ". " + el.attr("href"));
                    }
                    count++;
                }

                // Save to file
                try (FileWriter writer = new FileWriter(filename)) {
                    for (Element el : elements) {
                        if (choice == 1) {
                            if (fileChoice == 1) {
                                writer.write(el.text() + "\n");         // TXT
                            } else {
                                writer.write("\"" + el.text() + "\"\n"); // CSV
                            }
                        } else {
                            if (fileChoice == 1) {
                                writer.write(el.attr("href") + "\n");
                            } else {
                                writer.write("\"" + el.attr("href") + "\"\n");
                            }
                        }
                    }
                    System.out.println("\nData saved successfully as " + filename);
                } catch (IOException ioe) {
                    System.out.println("Error writing to file: " + ioe.getMessage());
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to connect to website.");
        }

        sc.close();
    }
}

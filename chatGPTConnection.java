import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.*;
import java.io.*;

public class chatGPTConnection {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        System.out.println("Enter prompt or EXIT to quit");
        String x = input.nextLine();
        while (!x.equals("EXIT")) {
            System.out.printf(fixIt(chatGPT(x)) + "%n");
            x = input.nextLine();
        }

    }

    public static String chatGPT(String message) {

        String url = "https://api.openai.com/v1/chat/completions";
        String apiKey = "sk-proj-pEhIlFfqSDLrb98bihiHT3BlbkFJ2okwWbubt16bmogE4Ojn";
        String model = "gpt-3.5-turbo";

        try {
            URL obj = URI.create(url).toURL();
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer " + apiKey);
            con.setRequestProperty("Content-Type", "application/json");

            String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message
                    + "\"}]}";
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(body);
            writer.flush();
            writer.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return extractContentFromResponse(response.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String extractContentFromResponse(String response) {
        int startMarker = response.indexOf("content") + 11; // Marker for where the content starts.
        int endMarker = response.indexOf("\"", startMarker); // Marker for where the content ends.
        return response.substring(startMarker, endMarker); // Returns the substring containing only the response.
    }

    public static String fixIt(String input) {
        String patternToReplace = "\\\\n";
        // Define the replacement string
        String replacement = "%n";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(patternToReplace);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(input);

        // Use the replaceAll() method to perform the replacement
        String result = matcher.replaceAll(replacement);

        return result;

    }

}
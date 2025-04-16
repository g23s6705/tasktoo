//about to write my java code in here
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class XMLToJsonConverterWithValidation {
    public static void main(String[] args) {
        try {
            File inputFile = new File("data.xml"); // Replace "data.xml" with your actual file name
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            List<String> availableFields = getAvailableFields(doc); // Get available fields from the XML

            System.out.println("Available fields: " + availableFields);
            System.out.print("Enter the fields to display (comma-separated): ");
            String selectedFieldsInput = reader.readLine();
            List<String> selectedFields = Arrays.asList(selectedFieldsInput.split(",\\s*"));

            // Validate user input
            for (String field : selectedFields) {
                if (!availableFields.contains(field.trim())) {
                    System.out.println("Warning: Field '" + field.trim() + "' not found in the XML.");
                }
            }

            NodeList recordList = doc.getElementsByTagName("record");

            for (int i = 0; i < recordList.getLength(); i++) {
                Element record = (Element) recordList.item(i);
                JSONObject jsonRecord = new JSONObject();
                NodeList childNodes = record.getChildNodes();

                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element element = (Element) childNodes.item(j);
                        if (selectedFields.contains(element.getTagName())) {
                            jsonRecord.put(element.getTagName(), element.getTextContent());
                        }
                    }
                }
                System.out.println(jsonRecord.toString(2));
            }
            reader.close();

        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred during XML processing: " + e.getMessage());
        }
    }

    // Helper function to get all available field names from the first record
    private static List<String> getAvailableFields(Document doc) {
        List<String> fields = new java.util.ArrayList<>();
        NodeList recordList = doc.getElementsByTagName("record");
        if (recordList.getLength() > 0) {
            Element firstRecord = (Element) recordList.item(0);
            NodeList childNodes = firstRecord.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                    fields.add(((Element) childNodes.item(i)).getTagName());
                }
            }
        }
        return fields;
    }
}
//about to write my java code in here
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.json.JSONObject;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class XMLToJsonConverterSAXCombined {

    public static void main(String[] args) {
        try {
            File inputFile = new File("data.xml"); // Replace "data.xml" with your actual file name

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the fields to display (comma-separated): ");
            String selectedFieldsInput = reader.readLine();
            List<String> selectedFields = Arrays.asList(selectedFieldsInput.split(",\\s*"));
            reader.close(); // Close the reader after getting input

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            UserSelectedFieldsHandler handler = new UserSelectedFieldsHandler(selectedFields);
            saxParser.parse(inputFile, handler);

        } catch (IOException e) {
            System.err.println("Error reading input or file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An error occurred during SAX parsing: " + e.getMessage());
        }
    }
}

class UserSelectedFieldsHandler extends DefaultHandler {
    private List<String> selectedFields;
    private String currentElement;
    private JSONObject currentRecord;
    private StringBuilder currentValue;
    private boolean isFirstRecord = true;

    public UserSelectedFieldsHandler(List<String> selectedFields) {
        this.selectedFields = selectedFields;
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("["); // Start of the JSON array
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;
        if (qName.equals("record")) {
            currentRecord = new JSONObject();
            if (!isFirstRecord) {
                System.out.println(",");
            }
            isFirstRecord = false;
        } else if (selectedFields.contains(qName)) {
            currentValue = new StringBuilder();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentValue != null) {
            currentValue.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentRecord != null && selectedFields.contains(qName) && currentValue != null) {
            currentRecord.put(qName, currentValue.toString().trim());
            currentValue = null; // Reset for the next element
        }

        if (qName.equals("record") && currentRecord != null) {
            System.out.print(currentRecord.toString(2));
            currentRecord = null; // Reset for the next record
        }
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("]"); // End of the JSON array
    }
}
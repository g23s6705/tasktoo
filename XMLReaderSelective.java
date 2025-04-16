//about to write my java code in here
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

public class XMLReaderSelective {
    public static void main(String[] args) {
        try {
            File inputFile = new File("data.xml"); // Replace "data.xml" with your actual file name
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter the fields to display (comma-separated): ");
            String selectedFieldsInput = reader.readLine();
            List<String> selectedFields = Arrays.asList(selectedFieldsInput.split(",\\s*"));

            NodeList recordList = doc.getElementsByTagName("record");

            for (int i = 0; i < recordList.getLength(); i++) {
                Element record = (Element) recordList.item(i);
                NodeList childNodes = record.getChildNodes();

                System.out.println("--- Record " + (i + 1) + " ---");
                for (int j = 0; j < childNodes.getLength(); j++) {
                    if (childNodes.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element element = (Element) childNodes.item(j);
                        if (selectedFields.contains(element.getTagName())) {
                            System.out.println(element.getTagName() + ": " + element.getTextContent());
                        }
                    }
                }
                System.out.println();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        System.out.println(json);
        try {
            writeString(json, "employees.json");
            System.out.println("JSON записан в файл employees.json");
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
        // Задача 2: XML - JSON парсер

        List<Employee> list2 = parseXML("data.xml");
    }

    public static List parseCSV(String[] columnMapping, String nameFile) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(nameFile);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        CSVReader csvReader = new CSVReader(fileReader);
        com.opencsv.bean.ColumnPositionMappingStrategy<Employee> strategyResult = new ColumnPositionMappingStrategy<>();
        strategyResult.setType(Employee.class);
        strategyResult.setColumnMapping(columnMapping);
        CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader).withMappingStrategy(strategyResult).build();
        List<Employee> list = csvToBean.parse();
        return list;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    public static void writeString(String json, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(json);
        }
    }
    public static List<Employee> parseXML(String filePath) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(filePath));
        Node root = document.getDocumentElement();
        NodeList list = root.getChildNodes();
        for(int i = 0;i<list.getLength();i++){

        }
    }
}

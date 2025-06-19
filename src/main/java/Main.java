import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import io.basc.framework.env.Sys;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        // Задача 1: CSV - JSON парсер
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

        // Нужно из XML файла сделать парсер в JSON

        System.out.println("\r\nЗадача № 2");
        List<Employee> listXML = parseXML("data.xml");
        String result = listToJson(listXML);
        writeString(result, "data2.json");
    }

    public static List<Employee> parseXML(String nameXML) throws IOException, ParserConfigurationException, SAXException {
        /*
           При реализации метода parseXML() вам необходимо получить экземпляр класса Document с использованием DocumentBuilderFactory и DocumentBuilder через метод parse().
           Далее получите из объекта Document корневой узел Node с помощью метода getDocumentElement().
           Из корневого узла извлеките список узлов NodeList с помощью метода getChildNodes(). Пройдитесь по списку узлов и получите из каждого из них Element. У элементов получите значения, с помощью которых создайте экземпляр класса Employee. Так как элементов может быть несколько, организуйте всю работу в цикле. Метод parseXML() должен возвращать список сотрудников.
           С помощью ранее написанного метода listToJson() преобразуйте список в JSON и запишите его в файл c помощью метода writeString().
         */
        List<Employee> employees = new ArrayList<>();
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document dopc = builder.parse(new File("data.xml"));
        Node root = dopc.getDocumentElement();
        System.out.println("Получаю корневой узел: " + root.getNodeName());
        NodeList nodeList = root.getChildNodes(); // Получаю список узлов
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element employee = (Element) node;
                //============================
                System.out.println("id - " + employee.getElementsByTagName("id").item(0).getTextContent());
                System.out.println("firstName - " + employee.getElementsByTagName("firstName").item(0).getTextContent());
                System.out.println("lastName - " + employee.getElementsByTagName("lastName").item(0).getTextContent());
                System.out.println("country - " + employee.getElementsByTagName("country").item(0).getTextContent());
                System.out.println("age - " + employee.getElementsByTagName("age").item(0).getTextContent());
                Long id = Long.parseLong(employee.getElementsByTagName("id").item(0).getTextContent());
                String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                String country = employee.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());
                Employee employee1 = new Employee(id, firstName, lastName, country, age);
                employees.add(employee1);
            }
        }
        return employees;
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
}

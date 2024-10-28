package app.classes;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SimpleCSVReader {
    private String filePath;
    private String[][] data;

    public SimpleCSVReader(String filePath) {
        this.filePath = filePath;
        this.data = readCSV();
    }

    private String[][] readCSV() {
        ArrayList<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assume que a vírgula é o delimitador
                rows.add(values); // Adiciona a linha à lista
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Converte a lista de linhas em uma matriz
        return rows.toArray(new String[0][0]);
    }

    public String[][] getColumns() {
        return data;
    }

}

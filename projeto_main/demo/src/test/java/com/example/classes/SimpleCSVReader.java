package com.example.classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimpleCSVReader {
    private String filePath;

    public SimpleCSVReader(String filePath) {
        this.filePath = filePath;
    }

    public List<String[]> readCSV() {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Pode ser ajustado para o delimitador que você precisa
                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Tratar a exceção
        }

        return data;
    }
}

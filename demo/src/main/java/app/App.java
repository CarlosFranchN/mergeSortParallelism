package app;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.swing.JFrame;

import app.classes.Graph2;
import app.classes.MergeSort;
import app.classes.SimpleCSVReader;

public class App 
{
    
     @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        // HashMap<String, long[]> times = new HashMap<String, long[]>();
        
        // int[] arr = {38, 27, 43, 3, 9, 82, 10}; // Array de exemplo
        int[] arr = gerarArrayAleatorio(1000);
        MergeSort array = new MergeSort(arr, 0, arr.length-1, 2);
        // MergeSortParalelo_test newArray = new MergeSortParalelo_test(arr, 0, arr.length-1);
        // System.out.println(array.array);
        
        
        System.out.print("Array Original: ");
        // printarArray(array.array);
        // printarArray(newArray.array);

        System.out.print("Array Ordenado Serial: ");
        array.serial();
        // printarArray(array.array_ord_serial);
        System.out.println("Tempo Serial: " + array.time_serial);

        System.out.print("Array Ordenado Paralelo: ");
        array.paralelo();
        // printarArray(array.array_ord_paralelo);
        System.out.println("Tempo Paralelo: " + array.time_paralelo);

        // System.out.println("Tempo dos teste serial");
        // printarArray(array.gerarTesteSerial(5));

        // System.out.println("Tempo dos teste paralelo");
        // printarArray(array.gerarTesteParalelo(5));
        // newArray.executarMergeSortParalelo(2);
        // printarArray(newArray.array_ord);
        // System.out.println();
        array.gerandoTeste();
        // array.printMapTimes2();
        array.printarFinalMap();
        // gerarCsv(array);
        // System.out.println(newArray.getMap(2,5));
        // printarArray(newArray.getMap(2,5).get("MergeSortParalelo"));

        // gerarCsv(array);
        // for (long teste : newArray.gerarTestes(2,5)) {
        //     System.out.print(teste + " ");
        // // }
        // for (long time : newArray.getMap(2,5).get("MergeSortParalelo")) {
        //     System.out.println(time + " ");
        // }
        // newArray.gerarCsv();
        // // System.out.println(newArray.getMap(2, 5));
        // new BarGraph();
        gerarGrafico(array);


  
    }
        public static int[] gerarArrayAleatorio(int N) {
        Random random = new Random();
        int[] array = new int[N];

        for (int i = 0; i < N; i++) {
            array[i] = random.nextInt(100);  // Gera números aleatórios de 0 a 99
        }

        return array;
    }

        static void printarArray(int[] array){
            for (int valor: array){
                System.out.print(valor + " ");
            }
            System.out.println();
        }

        static String gerarCsv(MergeSort array) {
            
        TreeMap<Integer, Long> map = array.getFinalMap();
        String path = "C:\\Users\\Admin\\Desktop\\Pastas\\Java\\javaMeOdeia\\myMergeSort_paralelo\\projeto_main\\demo\\src\\main\\java\\app\\csv/merge.csv";
        FileWriter writer = null;
        try {
            // Verifica se o arquivo já existe e modifica o nome para evitar sobrescrita
            File file = new File(path);
            String originalPath = path;
            int counter = 1;
    
            // Se o arquivo existir, adiciona um sufixo para criar um novo arquivo
            while (file.exists()) {
                String newPath = originalPath.replace(".csv", "_" + counter + ".csv");
                file = new File(newPath);
                path = newPath;  // Atualiza o path para o novo nome
                counter++;
            }
    
            writer = new FileWriter(path);  // Cria o arquivo novo
            writer.append("Amostra,0,2,4,5,10,100,1000\n");

    
            writer.append(String.valueOf(array.array.length) + ",");
            for (Entry<Integer, Long> entrada : map.entrySet()) {
                writer.append(String.valueOf(entrada.getValue())).append(",");
            }
            writer.append("\n");
    
            System.out.println("Arquivo CSV gerado com sucesso! Caminho: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return path;
    }
        
        static void gerarGrafico(MergeSort arr){
int[] dimensions = {1200, 750};
int[] adj = {50, 50, 150};
TreeMap<Integer, Long> map = arr.getFinalMap();
String path = gerarCsv(arr);

SimpleCSVReader file = new SimpleCSVReader(path);
String[][] dados = file.getColumns();
// for (String[] x : dados) {
//     for (String i : x) {
//         System.out.println(i);
//     }
// }

// Pulando o cabeçalho
List<Integer> times = new ArrayList<>();
// []int times = new int[dados.length];
List<Integer> keys = new ArrayList<>();

try {
    // Inicializando mergeSort com dados do CSV
    for (int i = 1; i <= 7; i++) {  // Usando dados.size() em vez de map.size()
        // System.out.println(dados[1][i]);
        // Normalizando e adicionando os tempos
        times.add(Integer.parseInt(dados[1][i]) / 1000);
        // microssegundo
        keys.add(Integer.parseInt(dados[0][i]));
    }
    // for (Integer valor : times) {
    //     System.out.println("Carlos" + valor);
        
    // }
    // Coletando as chaves do TreeMap
    keys.addAll(map.keySet());

    // Convertendo para arrays
    int[] chaves = keys.stream().mapToInt(Integer::intValue).toArray();
    int[] tempos = times.stream().mapToInt(Integer::intValue).toArray();
    String[] StringChaves = keys.stream().map(String::valueOf).toArray(String[]::new);
    String[] StringTempos = times.stream().map(String::valueOf).toArray(String[]::new);

    // Preparando os valores para o gráfico
    int[][] xyValues = { tempos,chaves };
    String[] xlabels = StringTempos;
    String[] ylabels = StringChaves;
    System.out.println(tempos.length);

    // Criando o gráfico
    Graph2 graph = new Graph2(dimensions, adj, xlabels, ylabels, xyValues);
    JFrame frame = new JFrame("Merge Sort Tempos");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(dimensions[0], dimensions[1]);
    frame.add(graph);
    frame.setVisible(true);
} catch (NumberFormatException e) {
    System.err.println("Erro ao converter os dados do CSV: " + e.getMessage());
} catch (Exception e) {
    e.printStackTrace();
}


    }

}

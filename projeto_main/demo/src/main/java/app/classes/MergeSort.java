package app.classes;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.RecursiveAction;

public class MergeSort extends RecursiveAction {
    public int[] array;
    public int[] array_ord_paralelo;
    public int[] array_ord_serial;
    public int left;
    public int right;
    public long time_paralelo;
    public long time_serial;
    public int nThreads;
    private static HashMap<String, int[]> mapTimes = new HashMap<>();

    public static HashMap<String, int[]> getMapTimes() {
        return mapTimes;
    }

    public MergeSort(int[] array, int left, int right, int nThreads){
        this.array = array;
        this.array_ord_paralelo = new int[array.length];
        this.array_ord_serial = new int[array.length];
        this.left = left;
        this.right = right;
        this.nThreads = nThreads;
        this.time_paralelo = 0;
        this.time_serial = 0;
    }

    @Override
    protected void compute() {
        
    }
    private int[] merge(int[] array, int left, int mid, int right,String tipo  ) {
        
        int[] temp = new int[array.length];  // Array auxiliar
        int i = left;
        int j = mid + 1;
        int k = left;

        // Mescla os dois subarrays de forma ordenada
        while (i <= mid && j <= right) {
            if (array[i] <= array[j]) {
                temp[k++] = array[i++];
            } else {
                temp[k++] = array[j++];
            }
        }

        // Copia os elementos restantes do subarray da esquerda
        while (i <= mid) {
            temp[k++] = array[i++];
        }

        // Copia os elementos restantes do subarray da direita
        while (j <= right) {
            temp[k++] = array[j++];
        }

        // Copia o array temporário de volta ao array original
        for (i = left; i <= right; i++) {
            array[i] = temp[i];
        }
        if (tipo == "serial") {
            this.array_ord_serial = temp;
        }else if(tipo == "paralelo"){
            this.array_ord_paralelo = temp;

        }
        return temp;
    }
    public void serial(){
        long inicio = System.nanoTime();
    
        // Chama o método de MergeSort serial recursivo
        mergeSortSerial(this.array, this.left, this.right);
    
        long fim = System.nanoTime();
        this.time_serial = fim - inicio;
    }

    private void mergeSortSerial(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
    
            // Chamadas recursivas para ordenar as duas metades
            mergeSortSerial(array, left, mid);     // Ordena a metade esquerda
            mergeSortSerial(array, mid + 1, right); // Ordena a metade direita
    
            // Mescla as duas metades ordenadas
            this.merge(array, left, mid, right, "serial");
        }
    }
    public void paralelo(){
        long inicio = System.nanoTime();
    
        // Chama o método de MergeSort serial recursivo
        mergeSortParalelo(this.array, this.left, this.right);
    
        long fim = System.nanoTime();
        this.time_paralelo = fim - inicio;
    }

    private void mergeSortParalelo(int[] array, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            
            // Cria as duas tarefas paralelas para dividir o array
            MergeSort leftTask = new MergeSort(array, left, mid,nThreads);
            MergeSort rightTask = new MergeSort(array, mid + 1, right,nThreads);

            invokeAll(leftTask, rightTask);  // Executa ambas as tarefas em paralelo

            // Mescla as duas metades
            this.merge(array, left, mid, right, "paralelo");

            
        }
}

    public int[] gerarTesteSerial(int repeticoes){
        int[] resultados = new int[repeticoes];
        for( int i=0; i<repeticoes;i++){
            MergeSort task = new MergeSort(this.array, this.left,this.right-1,this.nThreads);
            task.serial();
            resultados[i] = (int) task.time_serial;
        }
        mapTimes.put("MergeSortSerial", resultados);
        return resultados;
    }
    public int[] gerarTesteParalelo(int repeticoes){
        int[] resultados = new int[repeticoes];
        for( int i=0; i<repeticoes;i++){
            MergeSort task = new MergeSort(this.array, this.left,this.right-1,this.nThreads);
            task.paralelo();
            resultados[i] = (int) task.time_paralelo;
        }
        mapTimes.put("MergeSortParalelo", resultados);
        return resultados;
    }

    public void gerarCsv() {
        String path = "projeto_test/demo/csv/mergeSort.csv";
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
            writer.append("Algoritmo,Tempos\n");
    
            for (Entry<String, int[]> entrada : mapTimes.entrySet()) {
                writer.append(entrada.getKey()).append(",");
    
                int[] valores = entrada.getValue();
                for (int i = 0; i < valores.length; i++) {
                    writer.append(String.valueOf(valores[i]));
                    if (i < valores.length - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }
    
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
    }
    

}

package app.classes;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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
    private static HashMap<String, Long> mapTimes = new HashMap<>();
    private static HashMap<Integer, Long> mapTimes2 = new HashMap<>();
    private static TreeMap<Integer, Long> finalMap = new TreeMap<>();

    public HashMap<String, Long> getMapTimes() {
        return mapTimes;
    }
    public HashMap<Integer, Long> getMapTimes2() {
        return mapTimes2;
    }

    public  TreeMap<Integer, Long> getFinalMap() {
        return finalMap;
    }
    public void printMapTimes() {
        System.out.println("Tempos de execução armazenados no mapTimes:");
        for (Map.Entry<String, Long> entry : mapTimes.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue() + " ns");
        }
    }

    public void printMapTimes2() {
        System.out.println("Tempos de execução armazenados no mapTimes:");
        for (Map.Entry<Integer, Long> entry : mapTimes2.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue() + " ns");
        }
    }
    public void printarFinalMap(){
        TreeMap<Integer, Long> Map = new TreeMap<>(mapTimes2);
        for (Map.Entry<Integer, Long> entry : Map.entrySet()) {
            finalMap.put(entry.getKey(), entry.getValue());
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
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

    public void gerarTesteSerial(){

        MergeSort task = new MergeSort(this.array, this.left,this.right-1,0);
        task.serial();

        mapTimes.put("MergeSortSerial", task.time_serial);
        mapTimes2.put(0, task.time_serial);

    }
    public void gerarTesteParalelo(){
        // int[] resultados = new int[repeticoes];
        int[] numThreads = {2,4,5,10,100,1000,10000};
        // for( int i=0; i<repeticoes;i++){
        //     MergeSort task = new MergeSort(this.array, this.left,this.right-1,this.nThreads);
        //     task.paralelo();
        //     resultados[i] = (int) task.time_paralelo;
        // }
        for (int t : numThreads) {
            MergeSort task = new MergeSort(this.array, this.left,this.right-1,t);
            task.paralelo();
            String key = String.format("MergeSortParalelo%d", t);
            mapTimes.put(key, task.time_paralelo);
            mapTimes2.put(t,task.time_paralelo);
        }

    }
    public void gerandoTeste(){
        try {
            gerarTesteSerial();
            gerarTesteParalelo();
            System.out.println("Testes gerados!");
            
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

}

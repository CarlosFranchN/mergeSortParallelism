package com.example;


import java.util.Random;

import com.example.classes.BarGraph;
import com.example.classes.MergeSort_test;


public class AppTest {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        // HashMap<String, long[]> times = new HashMap<String, long[]>();
        
        // int[] arr = {38, 27, 43, 3, 9, 82, 10}; // Array de exemplo
        int[] arr = gerarArrayAleatorio(10);
        MergeSort_test array = new MergeSort_test(arr, 0, arr.length-1, 2);
        // MergeSortParalelo_test newArray = new MergeSortParalelo_test(arr, 0, arr.length-1);
        // System.out.println(array.array);
        
        
        System.out.print("Array Original: ");
        printarArray(array.array);
        // printarArray(newArray.array);

        System.out.print("Array Ordenado Serial: ");
        array.serial();
        printarArray(array.array_ord_serial);
        System.out.println("Tempo Serial: " + array.time_serial);

        System.out.print("Array Ordenado Paralelo: ");
        array.paralelo();
        printarArray(array.array_ord_paralelo);
        System.out.println("Tempo Paralelo: " + array.time_paralelo);

        System.out.println("Tempo dos teste serial");
        printarArray(array.gerarTesteSerial(5));

        System.out.println("Tempo dos teste paralelo");
        printarArray(array.gerarTesteParalelo(5));
        // newArray.executarMergeSortParalelo(2);
        // printarArray(newArray.array_ord);
        // System.out.println();
        
        // System.out.println(newArray.getMap(2,5));
        // printarArray(newArray.getMap(2,5).get("MergeSortParalelo"));
        System.out.println("Testes: ");
        array.gerarCsv();
        // for (long teste : newArray.gerarTestes(2,5)) {
        //     System.out.print(teste + " ");
        // // }
        // for (long time : newArray.getMap(2,5).get("MergeSortParalelo")) {
        //     System.out.println(time + " ");
        // }
        // newArray.gerarCsv();
        // // System.out.println(newArray.getMap(2, 5));
        new BarGraph();

  
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
        

}

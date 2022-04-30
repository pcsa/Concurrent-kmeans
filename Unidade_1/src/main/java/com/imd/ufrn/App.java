package com.imd.ufrn;

import java.io.IOException;

import com.imd.ufrn.model.KMeans;
import com.imd.ufrn.utils.CsvReader;

public class App {
    public static void main( String[] args ) throws InterruptedException {
        final String FILE_PATH_NAME = "test.csv";
        try {
            CsvReader csvReader = new CsvReader();

            long start = System.currentTimeMillis();   

            KMeans kmeans = new KMeans(3, csvReader.read(FILE_PATH_NAME));

            long elapsedTime = System.currentTimeMillis() - start;

            System.out.println("\nLeitura de arquivo completa. "+elapsedTime/1000F+"s\n");
            
            start = System.currentTimeMillis();    
            
            kmeans.runSerial();

            elapsedTime = System.currentTimeMillis() - start;
            System.out.println("\nFinalizado em serial. "+elapsedTime/1000F+"s\n");

            Thread.sleep(5000);

            start = System.currentTimeMillis();
            
            kmeans.runParallel(Runtime.getRuntime().availableProcessors());
            
            elapsedTime = System.currentTimeMillis() - start;
            System.out.println("\nFinalizado em paralelo. "+elapsedTime/1000F+"s\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

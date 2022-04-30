package com.imd.ufrn;

import java.io.IOException;
import java.util.List;

import com.imd.ufrn.model.IrisSample;
import com.imd.ufrn.model.KMeans;
import com.imd.ufrn.model.logic.ConcurrentKMeans;
import com.imd.ufrn.model.logic.SerialKMeans;
import com.imd.ufrn.utils.CsvReader;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main( String[] args ) throws InterruptedException {
        final String FILE_PATH_NAME = "test.csv";
        try {
            CsvReader csvReader = new CsvReader();
            KMeans kmeans;
            int runs = 0;

            long start = System.currentTimeMillis();   

            List<IrisSample> dataset = csvReader.read(FILE_PATH_NAME);
            kmeans = new SerialKMeans(3, dataset);

            long elapsedTime = System.currentTimeMillis() - start;
            
            log.info("Leitura de arquivo completa. "+elapsedTime/1000F+"s\n");
            
            start = System.currentTimeMillis();    
            
            runs = kmeans.fit();
            kmeans.printCentroids();

            elapsedTime = System.currentTimeMillis() - start;
            log.info("Finalizado em serial. "+elapsedTime/1000F+"s | "+runs+" runs\n");
            log.info("\n"+kmeans.printCentroids());

            kmeans = new ConcurrentKMeans(3, dataset);

            start = System.currentTimeMillis();
            
            runs = kmeans.fit();
            
            elapsedTime = System.currentTimeMillis() - start;
            log.info("Finalizado em paralelo. "+elapsedTime/1000F+"s | "+runs+" runs\n");
            log.info("\n"+kmeans.printCentroids());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

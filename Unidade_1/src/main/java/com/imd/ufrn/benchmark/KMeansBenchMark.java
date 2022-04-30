package com.imd.ufrn.benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.imd.ufrn.model.IrisSample;
import com.imd.ufrn.model.KMeans;
import com.imd.ufrn.utils.CsvReader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;

public class KMeansBenchMark {
    private static final String FILE_PATH_NAME = "test.csv";
    private static final List<IrisSample> testData = new ArrayList<>();
    private static final CsvReader csvReader = new CsvReader();
    
    @Setup
	public static final void setup() {
		try {
            testData.addAll(csvReader.read(FILE_PATH_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Benchmark
	public void serialKMeans() {
        KMeans kmeans = new KMeans(3, testData);
		kmeans.runSerial();
	}

	@Benchmark
	public void parallelKMeans() {
		KMeans kmeans = new KMeans(3, testData);
		kmeans.runParallel(Runtime.getRuntime().availableProcessors());
	}

}

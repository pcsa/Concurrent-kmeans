package com.imd.ufrn.benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.imd.ufrn.model.IrisSample;
import com.imd.ufrn.model.logic.ConcurrentKMeans;
import com.imd.ufrn.model.logic.SerialKMeans;
import com.imd.ufrn.utils.CsvReader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
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
        SerialKMeans kmeans = new SerialKMeans(3, testData);
		kmeans.fit();
	}

	@Benchmark
	public void parallelKMeans() {
		ConcurrentKMeans kmeans = new ConcurrentKMeans(3, testData);
		kmeans.fit();
	}

}

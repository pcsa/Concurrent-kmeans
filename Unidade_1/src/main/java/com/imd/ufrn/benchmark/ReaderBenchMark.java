package com.imd.ufrn.benchmark;

import java.io.IOException;

import com.imd.ufrn.utils.CsvReader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ReaderBenchMark {

    private static final String FILE_PATH_NAME = "test.csv";
    private static final CsvReader csvReader = new CsvReader();

	@Benchmark
	public void serialReader() {
        try {
            csvReader.read(FILE_PATH_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@Benchmark
	public void parallelReader() {
		try {
            csvReader.parallelRead(Runtime.getRuntime().availableProcessors(), FILE_PATH_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
    
}

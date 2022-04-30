package com.imd.ufrn;

import com.imd.ufrn.benchmark.KMeansBenchMark;
import com.imd.ufrn.benchmark.ReaderBenchMark;

import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchMarkRunner {

    public static void main(String[] args) throws Exception {
		Options opt = new OptionsBuilder()
				.include(KMeansBenchMark.class.getSimpleName())
                .include(ReaderBenchMark.class.getSimpleName())
				.warmupIterations(5)
				.shouldDoGC(true)
				.measurementIterations(5).forks(1)
				.addProfiler(GCProfiler.class)
				.addProfiler(StackProfiler.class)
				.jvmArgs("-server", "-Xms2048m","-Xmx8g") //Não funciona ?
                .build();
		try {
			new Runner(opt).run();
		} catch (RunnerException e) {
			e.printStackTrace();
		}
	}
}

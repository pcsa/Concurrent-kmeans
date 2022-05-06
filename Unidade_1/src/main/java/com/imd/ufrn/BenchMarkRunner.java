package com.imd.ufrn;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.imd.ufrn.benchmark.KMeansBenchMark;
import com.imd.ufrn.benchmark.ReaderBenchMark;

import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.profile.StackProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.ChainedOptionsBuilder;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchMarkRunner {

    public static void main(String[] args) {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		final String runBatch = sdf.format(new Timestamp(System.currentTimeMillis()));
		final String fileType = ".log.md";
		final String defaultReportName = "report_";
		final String alternativeGC = "ZGC";

		String reportName = defaultReportName+runBatch+fileType;
		String reportNameZGC = defaultReportName+alternativeGC+"_"+runBatch+fileType;

		ChainedOptionsBuilder opt = new OptionsBuilder()
				.include(KMeansBenchMark.class.getSimpleName())
				.warmupIterations(5)
				.shouldDoGC(true)
				.measurementIterations(5).forks(1)
				.addProfiler(GCProfiler.class)
				.addProfiler(StackProfiler.class)
				.output(reportName);
		try {
			new Runner(opt.build()).run();
			new Runner(opt
				.jvmArgsAppend("-XX:+UnlockExperimentalVMOptions","-XX:+Use"+alternativeGC)
				.output(reportNameZGC).build()).run();
		} catch (RunnerException e) {
			e.printStackTrace();
		}

		final String defaultFileReportName = "fileReadingReport"; 

		String fileReaderReportName = defaultFileReportName+runBatch+fileType;
		String fileReaderReportNameZGC = defaultFileReportName+alternativeGC+"_"+runBatch+fileType;
		ChainedOptionsBuilder opt2 = new OptionsBuilder()
				.include(ReaderBenchMark.class.getSimpleName())
				.warmupIterations(5)
				.shouldDoGC(true)
				.measurementIterations(5).forks(1)
				.addProfiler(GCProfiler.class)
				.addProfiler(StackProfiler.class)
				.jvmArgs("-Xms2048m","-Xmx8g")
				.output(fileReaderReportName);
		try {
			new Runner(opt2.build()).run();
			new Runner(opt2
					.jvmArgsAppend("-XX:+UnlockExperimentalVMOptions","-XX:+Use"+alternativeGC)
					.output(fileReaderReportNameZGC).build()).run();
		} catch (RunnerException e) {
			e.printStackTrace();
		}
	}
}

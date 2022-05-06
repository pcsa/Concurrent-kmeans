package com.imd.ufrn.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.imd.ufrn.model.IrisSample;


public class CsvReader {

	public List<IrisSample> read(String path) throws IOException {
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
			return bufferedReader.lines().map(sample -> new IrisSample(sample.split(","))).collect(Collectors.toList());
		}
	}

	public List<IrisSample> parallelRead(int nThreads, String path) throws IOException {
		List<IrisSample> data = new ArrayList<>();
		String dataString = "";
		
		try (final FileChannel channel = new FileInputStream(path).getChannel()) {
			MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			dataString = StandardCharsets.UTF_8.decode(buffer).toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ThreadFileRead[] threads = new ThreadFileRead[nThreads];
		int lineSize = dataString.indexOf("\n")+1;
		int nLines = (int) dataString.lines().count();
		int chunks = (nLines/nThreads)*lineSize;
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new ThreadFileRead(dataString.subSequence(i*chunks, (i+1)*chunks).toString());
			threads[i].start();
		}

		for (ThreadFileRead threadFileRead : threads) {
			try {
				threadFileRead.join();
				data.addAll(threadFileRead.getData());
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		return data;
	}

	private class ThreadFileRead extends Thread {
		private List<IrisSample> data;
		private String dataString;

		public ThreadFileRead(String dataString) {
			this.data = new ArrayList<>();
			this.dataString = dataString; 
		}

		public List<IrisSample> getData(){
			return this.data;
		}

		@Override
		public void run() {
			this.data = dataString.lines().map(sample -> new IrisSample(sample.split(","))).collect(Collectors.toList());
		}
	}

}
package com.imd.ufrn.model.logic;

import java.util.ArrayList;
import java.util.List;

import com.imd.ufrn.model.IrisSample;
import com.imd.ufrn.model.KMeans;

public class ConcurrentKMeans extends KMeans{

    public ConcurrentKMeans(int k, List<IrisSample> dataset) {
        super(k, dataset);
    }

    public int fit() {
        return runParallel(Runtime.getRuntime().availableProcessors());
    }

    public int runParallel(int nThreads) {
        KMeansThread[] threads = new KMeansThread[nThreads];
        int threadLoad = this.dataset.size()/nThreads;
        int runs = 0;
        boolean firstCentroid = true;

        for(int i = 0; i < nThreads; i++){
            int rangeMin = i*threadLoad;
            int rangeMax = (i+1)*threadLoad;
		    threads[i] = new KMeansThread(rangeMin, rangeMax);
		    threads[i].start();
		}

        for (KMeansThread thread : threads) {
            try {
                thread.join();
                firstCentroid = setConcurrentCentroid(thread.getCentroid(),firstCentroid);
                synchronized(this){
                    runs += thread.getCount();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
        return runs;
    }

    public synchronized boolean setConcurrentCentroid(List<IrisSample> threadCentroid, boolean first){
        if(first) {
            this.centroids = threadCentroid;
        }
        else{
            for (int i = 0; i < this.k; i++) {
                double[] mc = this.centroids.get(i).getVetorialForm();
                double[] tc = threadCentroid.get(i).getVetorialForm();
                for (int dim = 0; dim < mc.length; dim++) {
                    mc[dim]=(mc[dim]+tc[dim])/2;
                }
                this.centroids.set(i, new IrisSample(mc));
            }
        }
        return false;
    }

    private class KMeansThread extends Thread {

        private int rangeMin;
        private int rangeMax;
        private int runs;
        private List<IrisSample> centroid;

        public KMeansThread(int rangeMin, int rangeMax) {
            this.rangeMin = rangeMin;
            this.rangeMax = rangeMax;
        }

        public int getCount(){
            return this.runs;
        }

        public List<IrisSample> getCentroid() {
            return centroid;
        }

        @Override
		public void run() {
			SerialKMeans kMeans = new SerialKMeans(k, new ArrayList<>(dataset.subList(rangeMin, rangeMax)));
            this.runs = kMeans.fit();
            this.centroid = kMeans.getCentroids();
		}
	}
}

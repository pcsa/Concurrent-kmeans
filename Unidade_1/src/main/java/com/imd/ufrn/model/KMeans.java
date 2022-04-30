package com.imd.ufrn.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KMeans {
    private int k;
    private int dimensions;
    private List<IrisSample> dataset;
    private List<IrisSample> centroids;
    
    public KMeans(int k, List<IrisSample> dataset) {
        this.k = k;
        this.dataset = dataset;
        this.dimensions = dataset.get(0).getVetorialForm().length;
    }

    public List<IrisSample> getCentroids() {
        return this.centroids;
    }

    private double getDistance(double[] v1, double[] v2) {
		double sum = 0.0;
		for(int i=0; i < v1.length-1; i++) {
			sum += Math.pow(v1[i] - v2[i], 2);
		}
		return Math.sqrt(sum);
	}

    private int kMeansCalculation(){
        int runs = 0;
        boolean convergence = false;
        initCentroids();

        while(!convergence){
            
            double[][] clusterSums = new double[this.k][this.dimensions];
            int[] clusterSize = new int[k];

            for (int i=0; i < dataset.size(); i++) {
                int closestCluster = -1;
                double min = Double.MAX_VALUE;
                for (int kc = 0; kc < this.k; kc++) {
                    double distance = getDistance(dataset.get(i).getVetorialForm(), this.centroids.get(kc).getVetorialForm());
                    if(min > distance){
                        min = distance;
                        closestCluster = kc;
                    }
                }

                for(int d=0; d<dimensions; d++){
                    clusterSums[closestCluster][d]+=dataset.get(i).getVetorialForm()[d];
                }
                clusterSize[closestCluster]++;
            }

            List<IrisSample> prevCentroids = new ArrayList<>();
            prevCentroids.addAll(this.centroids);
            updateCentroids(clusterSums, clusterSize);
            convergence = checkConvergence(prevCentroids);
            ++runs;
        }
        return runs;
    }

    public void runSerial(){
        kMeansCalculation();
    }

    public void runParallel(int nThreads) {
        KMeansThread[] threads = new KMeansThread[nThreads];
        int threadLoad = this.dataset.size()/nThreads;
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
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
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

    public String printCentroids(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("0.00");

        for (int c=0; c < centroids.size(); c++) {
            double[] tmp = centroids.get(c).getVetorialForm();
            sb.append("c"+c+" - ");
            for (int i = 0; i < tmp.length; i++) {
                sb.append(df.format(tmp[i])+" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private boolean checkConvergence(List<IrisSample> prev) {
        for (int kc = 0; kc < this.k; kc++) {
            double[] newCentroid = centroids.get(kc).getVetorialForm();
            double[] prevCentroid = prev.get(kc).getVetorialForm();
            for (int i = 0; i < this.dimensions; i++) {
                if (Math.abs(newCentroid[i] - prevCentroid[i]) > 0.1) {
                    return false;
                } 
            }
        }
        return true;
    }

    private void initCentroids() {
        centroids = new ArrayList<>();
        for (int i = 0; i < this.k; i++) {
            centroids.add(dataset.get(i));
        }
    }

    private void updateCentroids(double[][] clusterSums, int[] clusterSize){
        for (int kc = 0; kc < this.k; kc++) {
            for (int d = 0; d < this.dimensions; d++) {
                if(clusterSize[kc] == 0) clusterSums[kc][d] = 0;
                else clusterSums[kc][d]/=clusterSize[kc];
            }
            this.centroids.set(kc, new IrisSample(clusterSums[kc]));
        }
    }
    
    private class KMeansThread extends Thread {

        private int rangeMin;
        private int rangeMax;
        private List<IrisSample> centroid;

        public KMeansThread(int rangeMin, int rangeMax) {
            this.rangeMin = rangeMin;
            this.rangeMax = rangeMax;
        }

        public List<IrisSample> getCentroid() {
            return centroid;
        }

        @Override
		public void run() {
			KMeans kMeans = new KMeans(k, new ArrayList<>(dataset.subList(rangeMin, rangeMax)));
            kMeans.kMeansCalculation();
            this.centroid = kMeans.getCentroids();
		}
	}

}

package com.imd.ufrn.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class KMeans {
    protected int k;
    protected int dimensions;
    protected List<IrisSample> dataset;
    protected List<IrisSample> centroids;

    protected KMeans(int k, List<IrisSample> dataset) {
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

    public abstract int fit();

    protected int kMeansCalculation(){
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
}

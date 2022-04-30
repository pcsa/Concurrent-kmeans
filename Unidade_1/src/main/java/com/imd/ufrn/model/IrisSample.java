package com.imd.ufrn.model;

public class IrisSample {
    private double sepalLength;
    private double sepalWidth;
    private double petalLength;
    private double petalWidth;

    public IrisSample(double[] sample){
        this.sepalLength = sample[0];
        this.sepalWidth = sample[1];
        this.petalLength = sample[2];
        this.petalWidth = sample[3];
    }

    public IrisSample(String[] sample) {
        this.sepalLength = Double.parseDouble(sample[0]);
        this.sepalWidth = Double.parseDouble(sample[1]);
        this.petalLength = Double.parseDouble(sample[2]);
        this.petalWidth = Double.parseDouble(sample[3]);
    }

    public double[] getVetorialForm(){
        double[] tmp = {this.sepalLength, this.sepalWidth, this.petalLength, this.petalWidth};
        return tmp;
    }
}

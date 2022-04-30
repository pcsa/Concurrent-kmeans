package com.imd.ufrn.model;

import java.util.Objects;

public class IrisSample {
    private double sepalLength;
    private double sepalWidth;
    private double petalLength;
    private double petalWidth;

    public IrisSample() {
    }

    public IrisSample(double sepalLength, double sepalWidth, double petalLength, double petalWidth) {
        this.sepalLength = sepalLength;
        this.sepalWidth = sepalWidth;
        this.petalLength = petalLength;
        this.petalWidth = petalWidth;
    }

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

    public double getSepalLength() {
        return this.sepalLength;
    }

    public void setSepalLength(double sepalLength) {
        this.sepalLength = sepalLength;
    }

    public double getSepalWidth() {
        return this.sepalWidth;
    }

    public void setSepalWidth(double sepalWidth) {
        this.sepalWidth = sepalWidth;
    }

    public double getPetalLength() {
        return this.petalLength;
    }

    public void setPetalLength(double petalLength) {
        this.petalLength = petalLength;
    }

    public double getPetalWidth() {
        return this.petalWidth;
    }

    public void setPetalWidth(double petalWidth) {
        this.petalWidth = petalWidth;
    }

    public double[] getVetorialForm(){
        double[] tmp = {this.sepalLength, this.sepalWidth, this.petalLength, this.petalWidth};
        return tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof IrisSample)) {
            return false;
        }
        IrisSample irisSample = (IrisSample) o;
        return sepalLength == irisSample.sepalLength && sepalWidth == irisSample.sepalWidth && petalLength == irisSample.petalLength && petalWidth == irisSample.petalWidth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sepalLength, sepalWidth, petalLength, petalWidth);
    }

}

package com.imd.ufrn.model.logic;

import java.util.List;

import com.imd.ufrn.model.IrisSample;
import com.imd.ufrn.model.KMeans;

public class SerialKMeans extends KMeans{
    
    public SerialKMeans(int k, List<IrisSample> dataset) {
        super(k, dataset);
    }

    public int fit(){
        return this.kMeansCalculation();
    }
}

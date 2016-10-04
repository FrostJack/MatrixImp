package edu.spbu;

/**
 * Created by Evgueni on 27.09.2016.
 */

import java.io.*;
import java.util.*;

public class DMatrix extends Matrix {
    double[][] data;

    public DMatrix(String filename) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(filename)));
            ArrayList<String> ArList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                ArList.add(line);
            }
            this.n = ArList.size();
            //String[] Array = ArList.toArray(new String[n]);
            this.m = ArList.get(0).split("\\s+").length;
            double[][] data = new double[n][m];
            for (int i = 0; i < n; i++) {
                String[] StringSplit = ArList.get(i).split("\\s+");
                for (int j = 0; j < m; j++)
                    data[i][j] = Double.parseDouble(StringSplit[j]);
            }
            this.data = data;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DMatrix (double[][] data) {
        n = data.length;
        m = data[0].length;
        this.data = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                this.data[i][j] = data[i][j];
    }

    public DMatrix (int n, int m) {
        this.n = n;
        this.m = m;
        this.data = new double[n][m];
    }

    public Matrix mul (Matrix B){
        if (B instanceof DMatrix) return (this.mul((DMatrix) B));
        else return (this.mul((SMatrix) B));
    }

    public DMatrix mul(DMatrix B) {
        DMatrix A = this;
        if (A.m != B.n) throw new RuntimeException("Illegal matrix dimensions.");
        DMatrix C = new DMatrix(A.n, B.m);
        for (int i = 0; i < C.n; i++)
            for (int j = 0; j < C.m; j++)
                for (int k = 0; k < A.m; k++) {
                    C.data[i][j] += (A.data[i][k]) * (B.data[k][j]);
                }
        return C;
    }

    public SMatrix mul (SMatrix B){
        DMatrix A = this;
        if (A.m != B.n) throw new RuntimeException("Illegal matrix dimensions.");
        SMatrix C = new SMatrix(A.n, B.m);
        for (Map.Entry<Coordinate, Double> e : B.data.entrySet())
            for (int i = 1; i < B.n; i++) {
                Coordinate k = new Coordinate(i, e.getKey().m);
                C.data.put(k, C.data.getOrDefault(k, 0.0) + e.getValue()*(A.data[i][e.getKey().n]));
            }
        return C;
    }

    public String toString (){
        return(Arrays.deepToString(this.data));
    }

    public double getelement (int i, int j){
        return this.data[i][j];
    }
}

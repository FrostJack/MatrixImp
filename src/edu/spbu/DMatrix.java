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
        if (B instanceof DMatrix)
            try {
            return(this.mul((DMatrix) B));
            } catch (InterruptedException e){
                e.printStackTrace();
                return null;
            }
        else return (this.mul((SMatrix) B));
    }

    public DMatrix mul(DMatrix B) throws InterruptedException {
        DMatrix A = this;
        if (A.m != B.n) throw new RuntimeException("Illegal matrix dimensions.");
        DMatrix D = B.transpon();
        DMatrix C = new DMatrix(A.n, D.n);
        final int temp = Runtime.getRuntime().availableProcessors();
        Thread thread[] = new Thread[temp];
        mulStream s = new mulStream(A, D, C);
        for (int i = 0; i < temp; i++){
            thread[i] = new Thread(s);
            thread[i].start();
        }

        for (int i = 0; i < temp; i++)
            thread[i].join();

        return C;
    }

    private class mulStream implements Runnable {
        int t = 0;
        DMatrix A;
        DMatrix D;
        DMatrix C;

        private mulStream (DMatrix A, DMatrix D, DMatrix C){
            this.A = A;
            this.D = D;
            this.C = C;
        }

        public void run(){
            for (int i = nextRow(); i < A.n; i = nextRow())
                for (int j = 0; j < D.n; j++)
                    for (int k = 0; k < A.m; k++)
                        C.data[i][j] += A.data[i][k] * D.data[j][k];
        }

        private int nextRow() {
            synchronized (this){
                return t++;
            }
        }

    }
    public SMatrix mul (SMatrix B){
        DMatrix A = this;
        if (A.m != B.n) throw new RuntimeException("Illegal matrix dimensions.");
        SMatrix C = new SMatrix(A.n, B.m);
        for (Map.Entry<Coordinate, Double> e : B.data.entrySet())
            for (int i = 0; i < A.n; i++) {
                Coordinate k = new Coordinate(i + 1, e.getKey().m);
                C.data.put(k, C.data.getOrDefault(k, 0.0) + e.getValue()*(A.data[i][e.getKey().n - 1]));
            }
        return C;
    }

    public String toString (){
        return(Arrays.deepToString(this.data));
    }

    public double getelement (int i, int j){
        return this.data[i-1][j-1];
    }

    public DMatrix transpon () {
        DMatrix A = this;
        DMatrix B = new DMatrix (this.m, this.n);
        for (int i = 0; i < A.n; i++)
            for (int j = 0; j < A.m; j++) {
            B.data[j][i] = A.data[i][j];
            }
        return B;
    }
}

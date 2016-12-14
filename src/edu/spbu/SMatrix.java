package edu.spbu;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Evgueni on 27.09.2016.
 */
public class SMatrix extends Matrix{
    Map<Coordinate, Double> data;

    public SMatrix(int n, int m) {
        this.n = n;
        this.m = m;
        this.data = new Hashtable<Coordinate, Double>();
    }

    public SMatrix(String filename) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(filename)));
            ArrayList<String> ArList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                ArList.add(line);
            }
            this.n = ArList.size();
            this.m = ArList.get(0).split("\\s+").length;
            double c;
            this.data = new Hashtable<Coordinate, Double>();
            for (int i = 0; i < n; i++) {
                String[] StringSplit = ArList.get(i).split("\\s+");
                for (int j = 0; j < m; j++) {
                    c = Double.parseDouble(StringSplit[j]);
                    if (c != 0) {
                        Coordinate a = new Coordinate(i + 1 , j + 1);
                        this.data.put(a, c);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Matrix mul (Matrix B){
        if (B instanceof SMatrix)
            try {
                return(this.mul((SMatrix) B));
            } catch (InterruptedException e){
                e.printStackTrace();
                return null;
            }
        else return (this.mul((DMatrix) B));
}

    public SMatrix mul (SMatrix B) throws InterruptedException {
        SMatrix A = this;
        if (A.m != B.n) throw new RuntimeException("Illegal matrix dimensions.");
        SMatrix C = new SMatrix (A.n, B.m);
        final int temp = Runtime.getRuntime().availableProcessors();
        Thread thread[] = new Thread[temp];
        mulStream s = new mulStream(A, B, C);

        for (int i = 0; i < temp; i++){
            thread[i] = new Thread(s);
            thread[i].start();
        }

        for (int i = 0; i < temp; i++)
            thread[i].join();

        return C;
    }

    private class mulStream implements Runnable {
        Iterator<Map.Entry<Coordinate, Double>> j;
        SMatrix A;
        SMatrix B;
        SMatrix C;

        private mulStream (SMatrix A, SMatrix B, SMatrix C){
            this.j = A.data.entrySet().iterator();
            this.A = A;
            this.B = B;
            this.C = C;
        }

        public void run() {
            Map.Entry<Coordinate, Double> e = nextMapElement();
            while (!(e == null)) {
                for (Map.Entry<Coordinate, Double> e2 : B.data.entrySet())
                    if (e.getKey().m == e2.getKey().n) {
                        Coordinate k3 = new Coordinate(e.getKey().n, e2.getKey().m);
                        C.data.put(k3, C.data.getOrDefault(k3, 0.0) + e.getValue() * e2.getValue());
                    }
                e = nextMapElement();
            }
        }


        private Map.Entry<Coordinate, Double> nextMapElement() {
            synchronized (this) {
                if (j.hasNext())
                    return j.next();
                else return null;
            }
        }
    }

    public SMatrix mul (DMatrix B){
        SMatrix A = this;
        if (A.m != B.n) throw new RuntimeException("Illegal matrix dimensions.");
        SMatrix C = new SMatrix (A.n, B.m);
        for (Map.Entry<Coordinate, Double> e : A.data.entrySet())
            for (int j = 0; j < B.m; j++) {
                Coordinate k = new Coordinate(e.getKey().n, j + 1);
                C.data.put(k, C.data.getOrDefault(k, 0.0) + e.getValue()*(B.data[e.getKey().m - 1][j]));
            }
        return C;
    }


    public String toString (){
        Coordinate q = new Coordinate(1 , 1);
        String test = new String();
        if (this.data.containsKey(q)) test += (this.data.get(q) + " ");
        else test += "0 ";
        while ((q.n != this.n) | (q.m != this.m)){
            if (q.m == this.m) {
                q.m = 1;
                q.n++;
                test += "\n";
            }
            else q.m++;
            if (this.data.containsKey(q)) test += (this.data.get(q) + " ");
            else test += "0 ";
        }
        return test;
    }

    public double getelement( int i, int j) {
        Coordinate q = new Coordinate(i, j);
        int a = 0;
        if (this.data.containsKey(q)) return (this.data.get(q));
        else return(a);
    }
}

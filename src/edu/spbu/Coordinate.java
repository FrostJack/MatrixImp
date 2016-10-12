package edu.spbu;

/**
 * Created by Evgueni on 28.09.2016.
 */
public class Coordinate {

    public int n;
    public int m;

    public Coordinate(int n, int m) {
        this.n = n;
        this.m = m;
    }

   /* public int compareTo (Coordinate key){
        int comparison = 0;
        comparison = Integer.compare(n, key.n);
        if (comparison != 0) return(comparison);
        comparison = Integer.compare(m, key.m);
        return(comparison);
    } */
    public int hashCode() {
        return (this.n + this.m);
    }

    public boolean equals (Object a){
        if (a instanceof Coordinate){
            return (this.n == ((Coordinate) a).n) & (this.m == ((Coordinate) a).m);
        }
        else return false;
    }
}


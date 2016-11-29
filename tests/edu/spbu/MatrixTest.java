package edu.spbu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by st049857 on 29.09.2016.
 */


public class MatrixTest {
    @Test
    public void testDDMul() {
        DMatrix d1 = new DMatrix("dmatrix1.txt");
        DMatrix d2 = new DMatrix("dmatrix2.txt");
        DMatrix d3 = new DMatrix("goldenddmatrix.txt");
        try {
            assertTrue(d1.mul(d2).equals(d3));
        } catch (InterruptedException e0) {
            e0.printStackTrace();
        }
    }

    @Test
    public void testSSMul() {
        SMatrix s1 = new SMatrix("dmatrix1.txt");
        SMatrix s2 = new SMatrix("dmatrix2.txt");
        SMatrix s3 = new SMatrix("goldenddmatrix.txt");
        assertTrue(s1.mul(s2).equals(s3));
    }

    @Test
    public void testSDMul() {
        SMatrix s1 = new SMatrix("dmatrix1.txt");
        DMatrix d1 = new DMatrix("dmatrix2.txt");
        SMatrix s2 = new SMatrix("goldenddmatrix.txt");
        assertTrue(s1.mul(d1).equals(s2));
    }

    @Test
    public void testDSMul() {
        DMatrix d1 = new DMatrix("dmatrix1.txt");
        SMatrix s1 = new SMatrix("dmatrix2.txt");
        SMatrix s3 = new SMatrix("goldenddmatrix.txt");
        assertTrue(d1.mul(s1).equals(s3));
    }

}

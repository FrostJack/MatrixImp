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
        assertTrue(d1.mul(d2).equals(d3));

    }

}

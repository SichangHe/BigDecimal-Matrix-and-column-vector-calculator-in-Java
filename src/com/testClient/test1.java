package com.testClient;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class test1 {
    public static void main(String[] args){
        MathContext mc = new MathContext(34, RoundingMode.HALF_DOWN);

        BigDecimal a = new BigDecimal(".66");
        BigDecimal b = new BigDecimal(".56");
        BigDecimal c = b.divide(a, mc);
        System.out.println(c);
        System.out.println(c.multiply(a));
        System.out.println(c.multiply(a).setScale(33, RoundingMode.HALF_UP));

        double a1 = .66, b1 = .56, c1 = b1 / a1;
        System.out.println(c1);
        System.out.println(c1 * a1);
    }
}

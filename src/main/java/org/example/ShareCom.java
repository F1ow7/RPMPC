package org.example;

import java.math.BigInteger;

public class ShareCom {

    public static BigInteger add(BigInteger a,BigInteger b) {
        BigInteger result = a.add(b);
        return result;
    }

    public static BigInteger[] mul1(BigInteger x,BigInteger y,BigInteger a,BigInteger b){
        BigInteger d = x.subtract(a);
        BigInteger e = y.subtract(b);
        BigInteger[] result ={d,e};
        return result;
    }

    public static BigInteger mul2(BigInteger a,BigInteger b,BigInteger c,BigInteger d,BigInteger e){
        BigInteger temp1 = e.multiply(a);
        BigInteger temp2 = d.multiply(b);
        BigInteger temp3 = d.multiply(e);
        BigInteger result = c.add(temp1).add(temp2).add(temp3);
        return result;
    }

    public static BigInteger mul3(BigInteger a,BigInteger b,BigInteger c,BigInteger d,BigInteger e){
        BigInteger temp1 = e.multiply(a);
        BigInteger temp2 = d.multiply(b);
        BigInteger result = c.add(temp1).add(temp2);
        return result;
    }


}

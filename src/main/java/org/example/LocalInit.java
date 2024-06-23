package org.example;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Objects;

public class LocalInit {
    public static BigInteger p;
    public static BigInteger q;
    public static BigInteger g;
    public static BigInteger h;

    public static BigInteger s;
    public static int n;
    public static int m;
    public static int t;
    public static int threshold;

    public static void init() {
        n = 30;    //todo: 节点总数

        m=30;
        t = 3;    //todo: 参与计算的节点数量
        threshold = 2; //todo: shamir秘密共享阈值
        SecureRandom random = new SecureRandom();

        while (true) {
            q = BigInteger.probablePrime(256, random);
            p = q.multiply(BigInteger.valueOf(2)).add(BigInteger.valueOf(1));
            if (p.isProbablePrime(1)) {
                break;
            }
        }

        while(true){
            BigInteger t = new BigInteger(256, random);
            while(t.compareTo(p.subtract(BigInteger.valueOf(2))) == 1 || t.compareTo(BigInteger.valueOf(2)) == -1){
                t = new BigInteger(256, random);
            }
            if (!Objects.equals(t.modPow(BigInteger.valueOf(2), p), BigInteger.ONE) && !Objects.equals(t.modPow(q, p), BigInteger.ONE)){
                g=t;
                break;
            }
        }

        s = new BigInteger(256, random);
        while(s.compareTo(p.subtract(BigInteger.valueOf(2))) == 1 || s.compareTo(BigInteger.valueOf(2)) == -1){
            s = new BigInteger(256, random);
        }
        h = g.modPow(s, p);



    }
}

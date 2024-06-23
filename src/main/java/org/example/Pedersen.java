package org.example;

import java.math.BigInteger;
import java.util.Random;

public class Pedersen {

    /**
     * Pedersen承诺
     *
     * @param m
     *            需要承诺的值
     * @param r
     *            盲化因子
     * @return 承诺值
     *
     */
    public static BigInteger commit(BigInteger m, BigInteger r) {

        BigInteger c = LocalInit.g.modPow(m, LocalInit.p).multiply(LocalInit.h.modPow(r, LocalInit.p)).mod(LocalInit.p);
        return c;
    }

    /**
     * Pedersen承诺打开
     *
     * @param m
     *            明文值
     * @param c
     *            承诺值
     * @param r
     *            盲化因子
     */
    public static boolean reveal(BigInteger m, BigInteger c, BigInteger r) {
        if (c.equals(LocalInit.g.modPow(m, LocalInit.p).multiply(LocalInit.h.modPow(r, LocalInit.p)).mod(LocalInit.p))) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        LocalInit.init();
        Random random = new Random();
        BigInteger m = new BigInteger(250,random);
        BigInteger rtemp = new BigInteger(128, random);
        long stime1 = System.nanoTime();
        for (int t=1;t<1000000;t++){
            BigInteger comm = commit(m,rtemp);
        }
        long etime1 = System.nanoTime();
        System.out.println("承诺生成时间"+(etime1-stime1)/1000000+"纳秒");
    }


}


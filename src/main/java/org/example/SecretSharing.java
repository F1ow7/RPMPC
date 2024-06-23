package org.example;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;


public class SecretSharing {

    public BigInteger[] commitments = new BigInteger[LocalInit.t];

    public BigInteger[][] share(BigInteger secret, BigInteger r,int n, int t ) {

        BigInteger[] coefficients = new BigInteger[t];
        BigInteger[] coefficients2 = new BigInteger[t];
        coefficients[0] = secret;
        coefficients2[0] = r;
        commitments[0] = Pedersen.commit(secret,r);

//        coefficients[1] = BigInteger.valueOf(2);
//        coefficients[2] = BigInteger.valueOf(7);
//        coefficients2[1] = BigInteger.valueOf(3);
//        coefficients2[2] = BigInteger.valueOf(5);

        for (int i = 1; i < t; i++) {
            coefficients[i] = generateRandomBigInteger();
            coefficients2[i] = generateRandomBigInteger();
            commitments[i] = Pedersen.commit(coefficients[i],coefficients2[i]);
        }

        BigInteger[][] userShares = new BigInteger[n][2];
        //进行秘密分享
        for (int i = 0; i < n; i++) {
            userShares[i][0] = computeShare(coefficients, (i + 1));
            userShares[i][1] = computeShare(coefficients2, (i + 1));

        }
        return userShares;
    }

//    public void init(int t) {
//        commitments = new BigInteger[t];
//    }


    public BigInteger computeShare(BigInteger[] coefficients, int userIndex) {
        BigInteger index = new BigInteger(String.valueOf(userIndex));
        int len = coefficients.length;
        BigInteger temp = BigInteger.ONE;
        BigInteger result = BigInteger.ZERO;
        for (int i = 0; i < len; i++) {
            BigInteger cur = coefficients[i].multiply(temp);
            temp = temp.multiply(index);
            result = result.add(cur);
        }
        return result;
    }


    public BigInteger generateRandomBigInteger() {
        Random random = new Random();
        BigInteger result;
        do {
            result = new BigInteger(LocalInit.p.bitLength(), random);
        } while ((result.compareTo(LocalInit.p) >= 0) && (result.compareTo(BigInteger.ZERO) != 0));
        return result;
    }


    public BigInteger reconstruction(BigInteger[][] shares, int t) throws Exception {
        int n = shares.length;
        if (t > n) {
            throw new Exception("当前收集的秘密份额不足以恢复秘密");
        }
        //验证份额有效性
        Boolean bool = verifySecretShares(shares,t);
        BigInteger result = new BigInteger("0");
        for (int i = 0; i < t; i++) {
            result = result.add(interpolation(shares, i + 1, t));
        }

        return result.mod(LocalInit.p);
    }


    public BigInteger fastreconstruction(BigInteger[]shares, int t) throws Exception {
        int n = shares.length;
        if (t > n) {
            throw new Exception("当前收集的秘密份额不足以恢复秘密");
        }
//        //验证份额有效性
//        Boolean bool = verifySecretShares(shares,t);
        BigInteger result = new BigInteger("0");
        for (int i = 0; i < t; i++) {
            result = result.add(fastinterpolation(shares, i + 1, t));
        }

        return result.mod(LocalInit.p);
    }

    public Boolean verifySecretShares(BigInteger[][]shares, int t){
        int count = 0;
        //验证份额有效性
        for (int i = 0; i < t; i++){
            BigInteger ctemp = Pedersen.commit(shares[i][0],shares[i][1]);
            BigInteger verifyctemp = BigInteger.ONE;
            for (int j=0;j<t;j++){
                BigInteger ctemp2 = commitments[j].modPow(BigInteger.valueOf((int) Math.pow(i+1,j)),LocalInit.p);
                verifyctemp = verifyctemp.multiply(ctemp2).mod(LocalInit.p);
            }
            System.out.println(ctemp);
            System.out.println(verifyctemp);
            System.out.println(ctemp.equals(verifyctemp));

            if (ctemp.equals(verifyctemp)){
//                System.out.println("第"+ (i + 1) +"个份额验证有效");
                count += 1;
//                System.out.println(count);
            }
        }
        if (count==t){
            return true;
        }
        return false;
    }

    public Boolean fastverifySecretShares(BigInteger[][]shares, int t, int i){
        //验证份额有效性
        BigInteger ctemp = Pedersen.commit(shares[i][0],shares[i][1]);
        BigInteger verifyctemp = BigInteger.ONE;
        for (int j=0;j<t;j++){
            BigInteger ctemp2 = commitments[j].modPow(BigInteger.valueOf((int) Math.pow(i+1,j)),LocalInit.p);
            verifyctemp = verifyctemp.multiply(ctemp2).mod(LocalInit.p);
        }
//        System.out.println(ctemp);
//        System.out.println(verifyctemp);
//        System.out.println(ctemp.equals(verifyctemp));

        if (ctemp.equals(verifyctemp)){
//                System.out.println("第"+ (i + 1) +"个份额验证有效");
            return true;
//                System.out.println(count);
        }
        return false;
    }

    /**
     * 求第i号用户(xK = i + 1)的了拉格朗日插值
     *
     * @param values
     * @param t
     * @return
     */
    public static BigInteger interpolation(BigInteger[][] values, int xK, int t) {
        BigInteger result;
        //常量0，计算f(0)
        BigInteger zero = BigInteger.ZERO;
        BigInteger x_k = new BigInteger(String.valueOf(xK));
        //拉格朗日多项式
        BigInteger up = BigInteger.ONE;
        BigInteger down = BigInteger.ONE;
        //i代表第i个用户的份额
        for (int i = 0; i < t; i++) {
            BigInteger x_i = new BigInteger(String.valueOf((i + 1)));
            if (x_i.equals(x_k)) {
                continue;
            }
            up = up.multiply(zero.subtract(x_i));
            down = down.multiply(x_k.subtract(x_i));
        }
        result = up.multiply(down.modInverse(LocalInit.p));
        result = result.multiply(values[xK - 1][0]);
        return result;
    }


    public BigInteger fastinterpolation(BigInteger[] values, int xK, int t) {
        BigInteger result;
        //常量0，计算f(0)
        BigInteger zero = BigInteger.ZERO;
        BigInteger x_k = new BigInteger(String.valueOf(xK));
        //拉格朗日多项式
        BigInteger up = BigInteger.ONE;
        BigInteger down = BigInteger.ONE;
        //i代表第i个用户的份额
        for (int i = 0; i < t; i++) {
            BigInteger x_i = new BigInteger(String.valueOf((i + 1)));
            if (x_i.equals(x_k)) {
                continue;
            }
            up = up.multiply(zero.subtract(x_i));
            down = down.multiply(x_k.subtract(x_i));
        }
        result = up.multiply(down.modInverse(LocalInit.p));
        result = result.multiply(values[xK - 1]);
        return result;
    }


    public static void main(String[] args) throws Exception {
        int times = 100000;
        LocalInit.init();
        System.out.println("测试开始.....");
        SecretSharing secretsharing = new SecretSharing();
        BigInteger secret = secretsharing.generateRandomBigInteger();
        BigInteger r = secretsharing.generateRandomBigInteger();
        int m = 5;
        int t = 3;
        BigInteger[][] shares = secretsharing.share(secret,r, m, t);
        long stime1 = System.nanoTime();
        for (int i = 0;i < times;i++){
            //生成小于p的随机秘密
//            BigInteger secret = secretsharing.generateRandomBigInteger();
//            BigInteger r = secretsharing.generateRandomBigInteger();
//            BigInteger secret = new BigInteger("21312312414412");
//            BigInteger r = BigInteger.valueOf(10);
//            int m = 5;
//            int t = 3;
            //阈值必须小于总用户数
//            while (t > m){
//                t = (int) (Math.random() * 50 ) + 1;
//            }
//            System.out.println("p为"+LocalInit.p);
//            System.out.println("n为"+m);
//            System.out.println("t为"+t);
//            System.out.println("秘密为"+secret);
//            System.out.println("r为"+r);
//            BigInteger[][] shares = secretsharing.share(secret,r, m, t);
//            for (int j = 0; j < m; j++) {
//                System.out.println(shares[j][0]);
//            }
//            for (int j = 0; j < m; j++) {
//                System.out.println(shares[j][1]);
//            }
            BigInteger reconstruction = secretsharing.reconstruction(shares, t);
//            if (reconstruction.compareTo(secret) != 0){
//                System.out.println("秘密值恢复错误");
//            }
        }
        long etime1 = System.nanoTime();
        System.out.println("秘密重构时间"+(etime1-stime1)/times+"纳秒");
        System.out.println("测试结束.....");
    }
}



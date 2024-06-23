package org.example.verify;

import org.example.LocalInit;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LiftedElgamal {

    public static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥

    /**
     * 随机生成密钥对
     */
    public static void genKeyPair() {
        BigInteger sk = LocalInit.s;  //s
        BigInteger pk = LocalInit.h;
        String pkStr = String.valueOf(pk);
        String skStr = String.valueOf(sk);
        keyMap.put(0,pkStr);  //0表示公钥
        keyMap.put(1,skStr);  //1表示私钥
    }

    /**
     * Lifted ElGamal加密
     *
     * @param str
     *            加密的字符串
     * @param publicKey
     *            公钥
     * @param r
     *            随机数
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static Map<Integer, String> encrypt( String str, String publicKey,BigInteger r ) throws Exception{
        Map<Integer, String> CipherMap = new HashMap<Integer, String>();
        BigInteger pk = new BigInteger(publicKey);
        BigInteger m = new BigInteger(str);

        BigInteger c0 = LocalInit.g.modPow(r,LocalInit.p);   //g^r
        BigInteger c1 = LocalInit.g.modPow(m,LocalInit.p).multiply(pk.modPow(r,LocalInit.p)).mod(LocalInit.p);  //g^m*h^r
        String c0Str = String.valueOf(c0);
        String c1Str = String.valueOf(c1);
        CipherMap.put(0,c0Str);
        CipherMap.put(1,c1Str);
        return CipherMap;
    }

    /**
     * Lifted ElGamal解密
     *
     * @param c0str
     *            密文0
     * @param c1str
     *            密文1
     * @param privateKey
     *            私钥
     * @return 明文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static BigInteger decrypt( String c0str,String c1str, String privateKey ) throws Exception{
        BigInteger result =BigInteger.ZERO;
        BigInteger range = new BigInteger("4294967296");
        BigInteger c0 =new BigInteger(c0str);   //g^r
        BigInteger c1 =new BigInteger(c1str);   //g^m*h^r
        BigInteger sk =new BigInteger(privateKey);  //s

        BigInteger temp1 = c0.modPow(sk,LocalInit.p);  //g^rs
        BigInteger temp2 = temp1.modInverse(LocalInit.p);   //(g^rs)^-1
        BigInteger temp3 = c1.multiply(temp2).mod(LocalInit.p);   //g^m

        for (BigInteger i =BigInteger.ZERO;i.compareTo(range)<=0;i = i.add(BigInteger.ONE)){
            if (LocalInit.g.modPow(i, LocalInit.p).equals(temp3)){
                result=i;
                break;
            }
        }
        return result;

    }

    public static void main(String[] args) throws Exception {
        LocalInit.init();
        LiftedElgamal.genKeyPair();
        Random random = new Random();
        BigInteger rtemp = new BigInteger(128, random);
        Map<Integer, String> CipherMap = encrypt("44444", keyMap.get(0), rtemp);
        long stime1 = System.nanoTime();
        for (int t=1;t<1000;t++){
            BigInteger jiemi = decrypt(CipherMap.get(0), CipherMap.get(1),keyMap.get(1) );
        }
        long etime1 = System.nanoTime();
        System.out.println("Lifted ElGamal解密时间"+(etime1-stime1)/1000+"纳秒");
    }


}

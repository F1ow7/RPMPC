package org.example.verify;

import org.example.LocalInit;
import org.example.select.RsaVRF;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultVerify {

    public static List<String> resultEncrypt(BigInteger result, String pk,BigInteger r) throws Exception {
        SecureRandom random = new SecureRandom();
        String binaryStr = result.toString(2);
        String bi2Str = String.format("%0256d",new BigInteger(binaryStr));  
        List<String> splitList = new ArrayList<>();
        int i = bi2Str.length()-16;
        while (true){
            splitList.add(bi2Str.substring(i, i + 16));
            if(i-16<=0){
                splitList.add(bi2Str.substring(0, i));
                break;
            }
            else {
                i=i-16;
            }
        }
        List<BigInteger> splitDataList = new ArrayList<>();
        for (String splitStr : splitList) {
            splitDataList.add(new BigInteger(splitStr, 2));
        }
        List<BigInteger> rList = new ArrayList<>();
        BigInteger rtemp;
        while (true){
            do {
                rtemp = new BigInteger(128, random);  //todo: 128
            } while ((rtemp.compareTo(LocalInit.p.subtract(BigInteger.ONE)) >= 0) && (rtemp.compareTo(BigInteger.ZERO) != 0));
            rList.add(rtemp);
            if (rList.size() >= splitDataList.size()-1){
                break;
            }
        }
        BigInteger kk = new BigInteger("2");
        BigInteger rsum = new BigInteger("0");
        for (int k = 0; k < rList.size(); k++){
            BigInteger temp = rList.get(k);
            rsum=rsum.add(temp.multiply(kk.pow(16*(k+1))));

        }
        rsum=rsum.mod(LocalInit.p.subtract(BigInteger.ONE));
        BigInteger re = r.subtract(rsum).mod(LocalInit.p.subtract(BigInteger.ONE));
        rList.add(0,re);

        List<String> encResult = new ArrayList<>();
        for (int j=0;j<splitDataList.size();j++){
            Map<Integer, String> CipherMap = LiftedElgamal.encrypt(splitDataList.get(j).toString(), pk,rList.get(j));
            encResult.add(CipherMap.get(0));
            encResult.add(CipherMap.get(1));
            List<String> temp = cipherIntegrityCom(CipherMap.get(0),CipherMap.get(1),pk,splitDataList.get(j).toString(),rList.get(j).toString());
            encResult.add(temp.get(0));
            encResult.add(temp.get(1));
            encResult.add(temp.get(2));
            encResult.add(temp.get(3));
        }
        return encResult;
    }



    public static BigInteger resultDecrypt(List<String> cipherlist,String sk) throws Exception {

        List<BigInteger> resultList = new ArrayList<>();

        for (int i=0 ;i< cipherlist.size();i=i+6){
            resultList.add(LiftedElgamal.decrypt(cipherlist.get(i),cipherlist.get(i+1),sk));
        }

        BigInteger kk = new BigInteger("2");
        BigInteger zsum = new BigInteger("0");
        for (int k = 0; k < resultList.size(); k++){
            BigInteger temp = resultList.get(k);
            zsum=zsum.add(temp.multiply(kk.pow(16*k)));
        }
        return zsum;
    }


    public static List<String> cipherIntegrityCom(String c0,String c1,String publickey,String v,String r) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        BigInteger a;
        do {
            a = new BigInteger(128, random);
        } while ((a.compareTo(LocalInit.p.subtract(BigInteger.ONE)) >= 0) && (a.compareTo(BigInteger.ZERO) != 0));
        BigInteger b;
        do {
            b = new BigInteger(128, random);
        } while ((b.compareTo(LocalInit.p.subtract(BigInteger.ONE)) >= 0) && (b.compareTo(BigInteger.ZERO) != 0));

        BigInteger pk = new BigInteger(publickey);
        BigInteger a0 = LocalInit.g.modPow(b,LocalInit.p);
        BigInteger a1 = LocalInit.g.modPow(a,LocalInit.p).multiply(pk.modPow(b,LocalInit.p)).mod(LocalInit.p);
        String cstr = RsaVRF.SHA256(c0+c1+String.valueOf(a0)+String.valueOf(a1)+String.valueOf(LocalInit.g)+String.valueOf(LocalInit.h));
        BigInteger c = new BigInteger(cstr,16);
        BigInteger d = a.add(c.multiply(new BigInteger(v)));
        BigInteger f = b.add(c.multiply(new BigInteger(r)));
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(a0));
        result.add(String.valueOf(a1));
        result.add(String.valueOf(d));
        result.add(String.valueOf(f));
        return result;
    }


}

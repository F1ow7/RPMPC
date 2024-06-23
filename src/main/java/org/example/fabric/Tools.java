package org.example.fabric;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.StringUtils;
import org.example.*;
import org.hyperledger.fabric.client.*;

import java.util.List;
import java.util.Map;

public class Tools {

    static final Gateway gateway;

    static final Network network;

    static final Contract contract;

    static {
        try {
            gateway = FabricGateway.gateway();
            System.out.println("获取gateway");
            network = gateway.getNetwork("mychannel");
//            network = FabricGateway.network(gateway);
            System.out.println("获取channel");
            contract = network.getContract("blockmpc");
//            contract = FabricGateway.getContract(network);
            System.out.println("获取contract");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> signNode(String id,String pk) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();
//        Contract contract = getContract();

        contract.newProposal("signComNode")
                .addArguments(id,id,pk)
                .build()
                .endorse()
                .submitAsync();

        result.put("status", "ok");
        return result;
    }


    public static Map<String, Object> queryNode(String key) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();

        byte[] bytes = contract.evaluateTransaction("queryComNode", key);
        result.put("payload", StringUtils.newStringUtf8(bytes));
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> queryData(String key) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();

        byte[] bytes = contract.evaluateTransaction("queryDataSlice", key);
        result.put("payload", StringUtils.newStringUtf8(bytes));
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> querySele(String key) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();

        byte[] bytes = contract.evaluateTransaction("querySelectData", key);
        result.put("payload", StringUtils.newStringUtf8(bytes));
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> submitPara(Parameters parameters) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();
        String paras = JSON.toJSONString(parameters);
        byte[] bytes = contract.submitTransaction("submitParam", parameters.getId(),paras);
        result.put("payload", StringUtils.newStringUtf8(bytes));
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> submitBeav(BeaverData beaverData) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();
        String paras = JSON.toJSONString(beaverData);

        contract.newProposal("submitBeav")
                .addArguments(beaverData.getId(),paras)
                .build()
                .endorse()
                .submitAsync();
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> submitComm(String key,String dcomm, String ecomm) throws  Exception {
        Map<String, Object> result = Maps.newConcurrentMap();

        contract.newProposal("submitComm")
                .addArguments(key,dcomm,ecomm)
                .build()
                .endorse()
                .submitAsync();
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> submitTemp(TempData tempData) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();
        String paras = JSON.toJSONString(tempData);

        contract.newProposal("submitTemp")
                .addArguments(tempData.getId(),paras)
                .build()
                .endorse()
                .submitAsync();
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> submitData(DataSlice dataSlice) throws  Exception {

        Map<String, Object> result = Maps.newConcurrentMap();
        String paras = JSON.toJSONString(dataSlice);

        contract.newProposal("submitDataSlice")
                .addArguments(dataSlice.getDataId(),paras)
                .build()
                .endorse()
                .submitAsync();

        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> sortVerify(String key,String value,String proof,String j) throws Exception {

        Map<String, Object> result = Maps.newConcurrentMap();

        contract.newProposal("VRFSortitionVerify")
                .addArguments(key,value,proof,j)
                .build()
                .endorse()
                .submitAsync();

        result.put("status","ok");
        return result;
    }

    public static Map<String, Object> updateSelectNode() throws Exception {

        Map<String, Object> result = Maps.newConcurrentMap();
        byte[] bytes = contract.submitTransaction("updateSelectNode");
        result.put("payload", StringUtils.newStringUtf8(bytes));
        result.put("status", "ok");
        return result;
    }

    public static Map<String, Object> resultVerify(String key, String sliceidstr, List<String> cipherlist,
                                                   String pk,String aorm) throws Exception {

        Map<String, Object> result = Maps.newConcurrentMap();
        String jsoncipher = JSON.toJSONString(cipherlist);

        contract.newProposal("resultVerify")
                .addArguments(key,sliceidstr,jsoncipher,pk,aorm)
                .build()
                .endorse()
                .submitAsync();

//        byte[] bytes = contract.submitTransaction("resultVerify",key,sliceidstr,jsoncipher,pk,aorm);
//        result.put("payload", StringUtils.newStringUtf8(bytes));

        result.put("status", "ok");
        return result;
    }



}

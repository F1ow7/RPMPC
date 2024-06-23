package org.example.fabric;

import org.example.LocalInit;
import org.example.select.RsaVRF;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FabricInit {


    public static List<Map<Integer, String>> nodeKeyMapList = new ArrayList<>();


    public static void init() throws Exception {

        Tools.contract.newProposal("initLedger")
                .build()
                .endorse()
                .submitAsync();
        Thread.sleep(2000);

        for (int i = 0; i < LocalInit.n; i++) {
            nodeKeyMapList.add(RsaVRF.genKeyPair());
            String pk = nodeKeyMapList.get(i).get(0);
            Map<String, Object> result = Tools.signNode("node-"+(i+1),pk);

        }
        Thread.sleep(2000);

    }
}

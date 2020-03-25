package io.appform.foxtrot.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.appform.foxtrot.client.cluster.FoxtrotNodeDiscoveryCluster;
import io.appform.foxtrot.client.cluster.FoxtrotClusterMember;
import io.appform.foxtrot.client.selectors.MemberSelector;
import io.appform.foxtrot.client.senders.HttpAsyncEventSender;
import io.appform.foxtrot.client.serialization.JacksonJsonSerializationHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * Created by santanu.s on 06/01/16.
 */
public class ManualRealAppTesting {

    //The following test is only for manual usage to test with local
    //docker based setup. As such it's marked as ignore. This will fail if run through CI.

    @Test
    @Ignore
    public void testWithRealEndPoint() throws Exception {
        FoxtrotClientConfig clientConfig = new FoxtrotClientConfig();
        clientConfig.setHost("192.168.99.100"); //CHANGE TO YOU LOCAL IP ON LINUX
        clientConfig.setPort(17000);
        clientConfig.setTable("testapp");
        FoxtrotNodeDiscoveryCluster foxtrotCluster = new FoxtrotNodeDiscoveryCluster(clientConfig, new MemberSelector() {
            @Override
            public FoxtrotClusterMember selectMember(List<FoxtrotClusterMember> members) {
                return new FoxtrotClusterMember("192.168.99.100", 17000);  //CHANGE TO YOU LOCAL IP ON LINUX
            }
        });
        HttpAsyncEventSender eventSender = new HttpAsyncEventSender(clientConfig, foxtrotCluster, JacksonJsonSerializationHandler.INSTANCE);

        FoxtrotClient client = new FoxtrotClient(foxtrotCluster, eventSender);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        for(int i = 0; i <100; i++) {
            try {
                client.send(
                        new Document(
                                UUID.randomUUID().toString(),
                                System.currentTimeMillis(),
                                new ObjectNode(nodeFactory)
                                        .put("testField", "Santanu Sinha")
                        )
                );
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

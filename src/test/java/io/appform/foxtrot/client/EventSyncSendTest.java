package io.appform.foxtrot.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.appform.foxtrot.client.cluster.FoxtrotClusterMember;
import io.appform.foxtrot.client.cluster.FoxtrotNodeDiscoveryCluster;
import io.appform.foxtrot.client.cluster.IFoxtrotCluster;
import io.appform.foxtrot.client.selectors.MemberSelector;
import io.appform.foxtrot.client.senders.HttpSyncEventSender;
import io.appform.foxtrot.client.serialization.JacksonJsonSerializationHandler;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static io.appform.foxtrot.client.selectors.EndpointType.DISCOVERY;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class EventSyncSendTest extends BaseTest {

    private TestHostPort testHostPort = new TestHostPort("localhost", 8888);

    @Test
    public void testSyncSend() throws Exception {
        FoxtrotClientConfig clientConfig = new FoxtrotClientConfig();
        clientConfig.setHost(testHostPort.getHostName());
        clientConfig.setPort(testHostPort.getPort());
        clientConfig.setTable("test");
        clientConfig.setEndpointType(DISCOVERY);
        IFoxtrotCluster foxtrotCluster = new FoxtrotNodeDiscoveryCluster(clientConfig, new MemberSelector() {
            @Override
            public FoxtrotClusterMember selectMember(List<FoxtrotClusterMember> members) {
                return new FoxtrotClusterMember(testHostPort.getHostName(), testHostPort.getPort());
            }
        });
        HttpSyncEventSender eventSender = new HttpSyncEventSender(clientConfig, foxtrotCluster, JacksonJsonSerializationHandler.INSTANCE);

        FoxtrotClient client = new FoxtrotClient(foxtrotCluster, eventSender);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        for (int i = 0; i < 200; i++) {
            try {
                client.send(
                        new Document(
                                UUID.randomUUID().toString(),
                                System.currentTimeMillis(),
                                new ObjectNode(nodeFactory)
                                        .put("testField", "Santanu Sinha")
                        )
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        verify(200, postRequestedFor(urlEqualTo("/foxtrot/v1/document/test/bulk")));
        client.close();
    }

}

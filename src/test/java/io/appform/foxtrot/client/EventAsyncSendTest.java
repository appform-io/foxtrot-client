
package io.appform.foxtrot.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.appform.foxtrot.client.serialization.JacksonJsonSerializationHandler;
import org.junit.Test;

import java.util.UUID;

import static io.appform.foxtrot.client.selectors.EndpointType.SIMPLE;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class EventAsyncSendTest extends BaseTest {

    private TestHostPort testHostPort = new TestHostPort("localhost", 8888);

    @Test
    public void testAsyncSend() throws Exception {

        FoxtrotClientConfig clientConfig = new FoxtrotClientConfig();
        clientConfig.setHost(testHostPort.getHostName());
        clientConfig.setPort(testHostPort.getPort());
        clientConfig.setEndpointType(SIMPLE);
        clientConfig.setTable("test");

        FoxtrotClient client = new FoxtrotClient(clientConfig, JacksonJsonSerializationHandler.INSTANCE);
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
        Thread.sleep(10000);
        verify(200, postRequestedFor(urlEqualTo("/foxtrot/v1/document/test/bulk")));
        client.close();
    }
}

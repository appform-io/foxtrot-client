package io.appform.foxtrot.client;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.appform.foxtrot.client.serialization.JacksonJsonSerializationHandler;
import io.appform.foxtrot.client.selectors.EndpointType;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class EventQueuedSendTest extends BaseTest {

    private TestHostPort testHostPort = new TestHostPort("localhost", 8888);

    @Test
    public void testQueuedSend() throws Exception {
        Path tmpPath = Files.createTempDirectory("tmp");
        tmpPath.toFile().deleteOnExit();
        final String path = tmpPath.toString();


        FoxtrotClientConfig clientConfig = new FoxtrotClientConfig();
        clientConfig.setHost(testHostPort.getHostName());
        clientConfig.setPort(testHostPort.getPort());
        clientConfig.setEndpointType(EndpointType.SIMPLE);
        clientConfig.setTable("test");
        clientConfig.setQueuePath(path);
        clientConfig.setBatchSize(50);

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

    @Test
    public void testQueuedSendBulk() throws Exception {
        Path tmpPath = Files.createTempDirectory("tmp");
        tmpPath.toFile().deleteOnExit();
        final String path = tmpPath.toString();


        FoxtrotClientConfig clientConfig = new FoxtrotClientConfig();
        clientConfig.setHost(testHostPort.getHostName());
        clientConfig.setPort(testHostPort.getPort());
        clientConfig.setEndpointType(EndpointType.SIMPLE);
        clientConfig.setTable("test");
        clientConfig.setQueuePath(path);
        clientConfig.setBatchSize(50);

        FoxtrotClient client = new FoxtrotClient(clientConfig, JacksonJsonSerializationHandler.INSTANCE);
        JsonNodeFactory nodeFactory = new JsonNodeFactory(false);
        List<Document> documents = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            documents.add(new Document(UUID.randomUUID().toString(),
                            System.currentTimeMillis(),
                            new ObjectNode(nodeFactory).put("testField", "Rishabh Goyal")
                    )
            );
        }
        try {
            client.send(documents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(10000);
        verify(1, postRequestedFor(urlEqualTo("/foxtrot/v1/document/test/bulk")));
        client.close();
    }
}

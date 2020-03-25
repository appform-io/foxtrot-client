
package io.appform.foxtrot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.appform.foxtrot.client.cluster.FoxtrotNodeDiscoveryCluster;
import io.appform.foxtrot.client.cluster.FoxtrotClusterMember;
import io.appform.foxtrot.client.cluster.FoxtrotClusterStatus;
import io.appform.foxtrot.client.selectors.RandomSelector;
import io.appform.foxtrot.client.selectors.RoundRobinSelector;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestSelectorNoMember {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8888);

    @Before
    public void setup() throws Exception {
        stubFor(get(urlEqualTo("/foxtrot/v1/cluster/members"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(mapper.writeValueAsBytes(new FoxtrotClusterStatus(
                                Lists.newArrayList(new FoxtrotClusterMember("host1", 18000), new FoxtrotClusterMember("host2", 18000)))))
                        .withHeader("Content-Type", "application/json")));
    }


     @Test
    public void testMemberGet() throws Exception {
        FoxtrotClientConfig clientConfig = new FoxtrotClientConfig();
        clientConfig.setHost("localhost");
        clientConfig.setPort(8888);
        FoxtrotNodeDiscoveryCluster foxtrotCluster = new FoxtrotNodeDiscoveryCluster(clientConfig, new RandomSelector());
        Assert.assertNotNull(foxtrotCluster.member());
    }

    @Test
    public void testMemberGetRR() throws Exception {
        FoxtrotClientConfig clientConfig = new FoxtrotClientConfig();
        clientConfig.setHost("localhost");
        clientConfig.setPort(8888);
        FoxtrotNodeDiscoveryCluster foxtrotCluster = new FoxtrotNodeDiscoveryCluster(clientConfig, new RoundRobinSelector());
        Assert.assertNotNull(foxtrotCluster.member());
    }

}

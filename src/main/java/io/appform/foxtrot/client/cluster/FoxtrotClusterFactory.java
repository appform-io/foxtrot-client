package io.appform.foxtrot.client.cluster;

import io.appform.foxtrot.client.FoxtrotClientConfig;
import io.appform.foxtrot.client.selectors.EndpointType;
import io.appform.foxtrot.client.selectors.RandomSelector;

public class FoxtrotClusterFactory {

    private final FoxtrotClientConfig clientConfig;

    public IFoxtrotCluster getCluster(EndpointType endpointType) throws Exception {
        switch (endpointType) {
            case SIMPLE:
                return new FoxtrotSimpleCluster(clientConfig);
            case DISCOVERY:
                return new FoxtrotNodeDiscoveryCluster(clientConfig, new RandomSelector());
            default:
                throw new UnsupportedOperationException("Endpoint type: " + endpointType + " not supported");
        }
    }


    public FoxtrotClusterFactory(final FoxtrotClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }
}

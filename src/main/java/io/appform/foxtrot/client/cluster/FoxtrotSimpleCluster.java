package io.appform.foxtrot.client.cluster;

import io.appform.foxtrot.client.FoxtrotClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FoxtrotSimpleCluster implements IFoxtrotCluster {

    private static final Logger logger = LoggerFactory.getLogger(FoxtrotSimpleCluster.class.getSimpleName());

    private final FoxtrotClusterMember clusterMember;

    public FoxtrotSimpleCluster(final FoxtrotClientConfig config) {
        this.clusterMember = new FoxtrotClusterMember(config.getHost(), config.getPort());
    }

    public FoxtrotClusterMember member() {
        return clusterMember;
    }

    public void stop() {
        // Do nothing
    }
}

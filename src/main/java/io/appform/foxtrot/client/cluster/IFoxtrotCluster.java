package io.appform.foxtrot.client.cluster;

public interface IFoxtrotCluster {

    FoxtrotClusterMember member();

    void stop();

}

package io.appform.foxtrot.client;

import io.appform.foxtrot.client.selectors.EndpointType;

/**
 * Configuration for the foxtrot client.
 */
public class FoxtrotClientConfig {
    /**
     * The foxtrot table to connect to.
     */
    private String table;

    /**
     * The foxtrot host or load balancer from which cluster member information will be polled.
     */
    private String host;

    /**
     * The port on the host or load balancer from which cluster member information will be polled. (Default: 80)
     */
    private int port = 80;

    /**
     * Endpoint type :
     * SIMPLE : for load balancer endpoint
     * DISCOVERY: for discovering cluster members
     */
    private EndpointType endpointType = EndpointType.SIMPLE;

    /**
     * Maximum number of connections to establish for metadata polling. (Default: 10)
     */
    private int maxConnections = 10;

    /**
     * Cluster metadata polling interval in seconds. (Default: 1 sec)
     */
    private int refreshIntervalSecs = 1;

    /**
     * Type of client which will init. Can be any of {@link ClientType}
     * Default value is {@link ClientType#sync}.
     * WARN: The Async client suffers from a memory leak, do not use it for now. {@see https://issues.apache.org/jira/browse/HTTPASYNC-94}
     */
    private ClientType clientType = ClientType.sync;

    /**
     * Used if clientType is {@link ClientType#queued}
     * or {@link ClientType#queued}
     * Temporary file system path where events will be saved before ingestion.
     */
    private String queuePath;

    /**
     * Connection keepalive time
     */
    private long keepAliveTimeMillis = 30000;

    /**
     * Used if clientType is {@link ClientType#queued}
     * or {@link ClientType#queued}
     * Number of messages to push per batch.
     * (Default: 200)
     */
    private int batchSize = 200;

    private String authToken;

    public FoxtrotClientConfig() {
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getRefreshIntervalSecs() {
        return refreshIntervalSecs;
    }

    public void setRefreshIntervalSecs(int refreshIntervalSecs) {
        this.refreshIntervalSecs = refreshIntervalSecs;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public String getQueuePath() {
        return queuePath;
    }

    public void setQueuePath(String queuePath) {
        this.queuePath = queuePath;
    }

    public long getKeepAliveTimeMillis() {
        return keepAliveTimeMillis;
    }

    public void setKeepAliveTimeMillis(long keepAliveTimeMillis) {
        this.keepAliveTimeMillis = keepAliveTimeMillis;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package io.appform.foxtrot.client;

public class TestHostPort {
    private String hostName;
    private int port;

    public TestHostPort(final String hostName, final int port) {
        this.hostName = hostName;
        this.port = port;
    }

    public TestHostPort() {
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

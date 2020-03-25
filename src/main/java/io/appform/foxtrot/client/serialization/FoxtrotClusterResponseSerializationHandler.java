package io.appform.foxtrot.client.serialization;

import io.appform.foxtrot.client.cluster.FoxtrotClusterStatus;

public interface FoxtrotClusterResponseSerializationHandler {
    FoxtrotClusterStatus deserialize(byte[] data) throws DeserializationException;
}

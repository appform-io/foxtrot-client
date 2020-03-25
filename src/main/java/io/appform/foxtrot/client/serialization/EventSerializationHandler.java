package io.appform.foxtrot.client.serialization;

import io.appform.foxtrot.client.Document;

import java.util.List;

public interface EventSerializationHandler {
    byte[] serialize(Document document) throws SerializationException;

    byte[] serialize(List<Document> documents) throws SerializationException;

    Document deserialize(byte[] data) throws DeserializationException;
}

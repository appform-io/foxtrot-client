package io.appform.foxtrot.client.senders;

import com.google.common.net.HttpHeaders;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.auth.BasicAuthRequestInterceptor;
import io.appform.foxtrot.client.Document;
import io.appform.foxtrot.client.EventSender;
import io.appform.foxtrot.client.FoxtrotClientConfig;
import io.appform.foxtrot.client.cluster.FoxtrotClusterMember;
import io.appform.foxtrot.client.cluster.IFoxtrotCluster;
import io.appform.foxtrot.client.selectors.FoxtrotTarget;
import io.appform.foxtrot.client.serialization.EventSerializationHandler;
import io.appform.foxtrot.client.serialization.SerializationException;
import com.google.common.base.Preconditions;
import com.squareup.okhttp.ConnectionPool;
import feign.Feign;
import feign.FeignException;
import feign.Response;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class HttpSyncEventSender extends EventSender {
    private static final Logger logger = LoggerFactory.getLogger(HttpSyncEventSender.class.getSimpleName());

    private final String table;
    private final IFoxtrotCluster client;
    private FoxtrotHttpClient httpClient;

    private final static Slf4jLogger slf4jLogger = new Slf4jLogger();


    public HttpSyncEventSender(final FoxtrotClientConfig config, IFoxtrotCluster client, EventSerializationHandler serializationHandler) {
        super(serializationHandler);
        this.table = config.getTable();
        this.client = client;
        com.squareup.okhttp.OkHttpClient okHttpClient = new com.squareup.okhttp.OkHttpClient();
        okHttpClient.setConnectionPool(new ConnectionPool(config.getMaxConnections(), config.getKeepAliveTimeMillis()));
        this.httpClient = Feign.builder()
                .client(new OkHttpClient(okHttpClient))
                .logger(slf4jLogger)
                .requestInterceptor(getIntercepter(config))
                .logLevel(feign.Logger.Level.BASIC)
                .target(new FoxtrotTarget<>(FoxtrotHttpClient.class, "foxtrot", client));
    }

    @Override
    public void send(Document document) {
        send(table, document);
    }

    @Override
    public void send(String table, Document document) {
        send(table, Collections.singletonList(document));
    }

    @Override
    public void send(List<Document> documents) {
        send(table, documents);
    }

    @Override
    public void send(String table, List<Document> documents) {
        try {
            send(table, getSerializationHandler().serialize(documents));
        } catch (SerializationException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void close() {

    }

    public void send(final String table, byte[] payload) {
        FoxtrotClusterMember clusterMember = client.member();
        Preconditions.checkNotNull(clusterMember, "No members found in foxtrot cluster");
        try {
            Response response = httpClient.send(table, payload);
            if (is2XX(response.status())) {
                logger.info("table={} messages_sent host={} port={}", table, clusterMember.getHost(), clusterMember.getPort());
            } else if (response.status() == 400) {
                logger.error("table={} host={} port={} statusCode={}", table, clusterMember.getHost(), clusterMember.getPort(), response.status());
            } else {
                throw new RuntimeException(String.format("table=%s event_send_failed status [%d] exception_message=%s", table, response.status(), response.reason()));
            }
        } catch (FeignException e) {
            logger.error("table={} msg=event_publish_failed", new Object[]{table}, e);
        }
    }

    private boolean is2XX(int status) {
        return status / 100 == 2;
    }

    private RequestInterceptor getIntercepter(FoxtrotClientConfig foxtrotClientConfig){
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                if (StringUtils.isEmpty(foxtrotClientConfig.getAuthToken())){
                    return;
                }
                template.header(HttpHeaders.AUTHORIZATION,  String.format("Bearer %s", foxtrotClientConfig.getAuthToken()));
            }
        };
    }
}

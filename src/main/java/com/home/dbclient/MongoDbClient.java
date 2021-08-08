package com.home.dbclient;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;
import lombok.experimental.UtilityClass;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

@UtilityClass
public class MongoDbClient {

    private static final String DEFAULT_CONNECTION_SETTINGS = "mongodb://localhost:27017";

    ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
            .minSize(2)
            .maxSize(10)
            .maxConnectionIdleTime(10, MINUTES)
            .maxConnectionLifeTime(60, MINUTES)
            .build();

    SocketSettings socketSettings = SocketSettings.builder()
            .connectTimeout(10, SECONDS)
            .readTimeout(10, SECONDS)
            .build();

    private static MongoClientSettings initClientSettings(final String connectionSettings) {
        if (null == connectionSettings || connectionSettings.isEmpty()) {
            return initClientSettings(DEFAULT_CONNECTION_SETTINGS);
        }
        return MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionSettings))
                .applyToConnectionPoolSettings(builder -> builder.applySettings(connectionPoolSettings))
                .applyToSocketSettings(builder -> builder.applySettings(socketSettings))
                .build();
    }

    public static MongoClient getClient(final String connectionSettings) {
        return MongoClients.create(initClientSettings(connectionSettings));
    }
}

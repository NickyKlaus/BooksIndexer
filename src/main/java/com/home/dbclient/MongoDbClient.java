package com.home.dbclient;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.connection.SocketSettings;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class MongoDbClient {
    private static final String DEFAULT_CONNECTION_SETTINGS = "mongodb://localhost:27017";
    private static final ConnectionPoolSettings connectionPoolSettings = ConnectionPoolSettings.builder()
            .minSize(2)
            .maxSize(10)
            .maxConnectionIdleTime(10, MINUTES)
            .maxConnectionLifeTime(30, MINUTES)
            .build();
    private static final SocketSettings socketSettings = SocketSettings.builder()
            .connectTimeout(10, SECONDS)
            .readTimeout(10, SECONDS)
            .build();

    private MongoDbClient() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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

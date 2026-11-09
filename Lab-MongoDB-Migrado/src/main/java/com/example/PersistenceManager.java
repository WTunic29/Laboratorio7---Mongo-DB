package com.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Administrador singleton de la conexión MongoDB. Variables opcionales:
 * MONGO_URI y MONGO_DATABASE.
 */
public final class PersistenceManager {

    private static final PersistenceManager INSTANCE = new PersistenceManager();

    private final MongoClient client;
    private final MongoDatabase database;

    private PersistenceManager() {
        String uri = System.getenv().getOrDefault("MONGO_URI", "mongodb://localhost:27017");
        String databaseName = System.getenv().getOrDefault("MONGO_DATABASE", "ArquiSW");
        this.client = MongoClients.create(uri);
        this.database = client.getDatabase(databaseName);
    }

    public static PersistenceManager getInstance() {
        return INSTANCE;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void ping() {
        database.runCommand(new Document("ping", 1));
        System.out.println("Conexión correcta con MongoDB. Base de datos: " + database.getName());
    }

    public void close() {
        client.close();
    }
}

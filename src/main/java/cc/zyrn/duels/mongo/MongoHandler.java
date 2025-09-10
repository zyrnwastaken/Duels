package cc.zyrn.duels.mongo;

import cc.zyrn.duels.Duels;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class MongoHandler {

    private final Duels duels;

    private MongoDatabase database;
    private MongoClient client;
    private MongoCollection<Document> profiles, arenas, kits;

    public MongoHandler(Duels duels) {
        this.duels = duels;

        this.init();
    }

    public void init() {
        final FileConfiguration config = duels.getConfig();

        if (config.getBoolean("mongo.uri-mode")) {
            this.client = MongoClients.create(config.getString("mongo.uri.connection-string"));
            this.database = client.getDatabase(config.getString("mongo.uri.database"));

            this.loadCollections();
            return;
        }

        boolean auth = config.getBoolean("mongo.normal.auth.enabled");
        String host = config.getString("mongo.normal.host");
        int port = config.getInt("mongo.normal.port");

        String uri = "mongodb://" + host + ":" + port;

        if (auth) {
            String username = config.getString("mongo.normal.auth.username");
            String password = config.getString("mongo.normal.auth.password");
            uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        }

        this.client = MongoClients.create(uri);
        this.database = client.getDatabase(config.getString("mongo.uri.database"));

        this.loadCollections();
    }

    public void loadCollections() {
        profiles = this.database.getCollection("profiles");
        arenas = this.database.getCollection("arenas");
        kits = this.database.getCollection("kits");
    }

}
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Producto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.bson.types.ObjectId;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductoService {
    private MongoDatabase db() {
        return PersistenceManager.getInstance().getDatabase();
    }

    /** Lista los productos almacenados en la colección productos. */
    @GET
    public Response getAll() {
        List<Producto> result = new ArrayList<>();
        for (Document d : db().getCollection("productos").find()) {
            Producto p = new Producto();
            p.setId(d.getObjectId("_id").toHexString());
            p.setNombre(d.getString("nombre"));
            p.setMarca(d.getString("marca"));
            p.setFechaCompra(d.getDate("fechaCompra"));
            result.add(p);
        }
        return Response.ok(result)
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }
}

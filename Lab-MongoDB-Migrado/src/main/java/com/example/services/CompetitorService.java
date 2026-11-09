package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;
import com.example.models.Producto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.bson.types.ObjectId;

@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompetitorService {

    private MongoDatabase db() {
        return PersistenceManager.getInstance().getDatabase();
    }

    @GET
    public Response getAll() {
        
        List<Competitor> result = new ArrayList<>();
        
        for (Document d : db().getCollection("competitors").find().sort(Sorts.ascending("surname"))) {
            result.add(fromDocument(d));
        }
        
        return cors(Response.ok(result)).build();
    }

    /**
     * Crea el producto y el competidor. El producto es obligatorio.
     */
    @POST
    public Response createCompetitor(CompetitorDTO dto) {
        
        if (dto == null || dto.getProducto() == null
                || blank(dto.getProducto().getNombre())
                || blank(dto.getProducto().getMarca())
                || dto.getProducto().getFechaCompra() == null) {
            
            return cors(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiMessage("El competidor debe incluir producto.nombre, producto.marca y producto.fechaCompra.")))
                    .build();
            
        }
        
        if (blank(dto.getName()) || blank(dto.getSurname())) {
            
            return cors(Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiMessage("Los campos name y surname son obligatorios."))).build();
        
        }

        MongoCollection<Document> products = db().getCollection("productos");
        MongoCollection<Document> competitors = db().getCollection("competitors");

        Document productDoc = new Document("nombre", dto.getProducto().getNombre())
                .append("marca", dto.getProducto().getMarca())
                .append("fechaCompra", dto.getProducto().getFechaCompra());
        products.insertOne(productDoc);
        ObjectId productId = productDoc.getObjectId("_id");

        try {
            
            Document competitorDoc = new Document("productoId", productId)
                    .append("name", dto.getName())
                    .append("surname", dto.getSurname())
                    .append("age", dto.getAge())
                    .append("telephone", dto.getTelephone())
                    .append("cellphone", dto.getCellphone())
                    .append("address", dto.getAddress())
                    .append("city", dto.getCity())
                    .append("country", dto.getCountry())
                    .append("winner", false)
                    .append("createdAt", new Date())
                    .append("updatedAt", new Date());
            competitors.insertOne(competitorDoc);
            ObjectId competitorId = competitorDoc.getObjectId("_id");

            return cors(Response.status(Response.Status.CREATED)
                    .entity(new CreatedResponse(competitorId.toHexString(), productId.toHexString()))).build();
        
        } catch (RuntimeException ex) {
            
            // En MongoDB standalone no se presupone que haya transacciones multi-documento.
            products.deleteOne(Filters.eq("_id", productId));
            throw ex;
        }
        
    }

    /**
     * Devuelve solo los competidores cuyo nombre comienza por A, sin distinguir
     * mayúsculas.
     */
    @GET
    @Path("/starts-with-a")
    public Response getNamesStartingWithA() {
        
        List<String> names = new ArrayList<>();
        
        for (Document d : db().getCollection("competitors")
                .find(Filters.regex("name", "^A", "i")).sort(Sorts.ascending("name"))) {
            names.add(d.getString("name"));
        }
        
        return cors(Response.ok(names)).build();
    }

    private Competitor fromDocument(Document d) {
        
        Competitor c = new Competitor();
        
        c.setId(d.getObjectId("_id").toHexString());
        ObjectId productId = d.getObjectId("productoId");
        c.setProductoId(productId == null ? null : productId.toHexString());
        c.setName(d.getString("name"));
        c.setSurname(d.getString("surname"));
        c.setAge(d.getInteger("age", 0));
        c.setTelephone(d.getString("telephone"));
        c.setCellphone(d.getString("cellphone"));
        c.setAddress(d.getString("address"));
        c.setCity(d.getString("city"));
        c.setCountry(d.getString("country"));
        c.setWinner(d.getBoolean("winner", false));
        
        return c;
        
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private Response.ResponseBuilder cors(Response.ResponseBuilder builder) {
        
        return builder.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
                .header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    
    }

    @OPTIONS
    public Response options() {
        return cors(Response.ok()).build();
    }

    public static class ApiMessage {

        private String message;

        public ApiMessage() {
        }

        public ApiMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
        
    }

    public static class CreatedResponse {

        private String competitorId;
        private String productoId;

        public CreatedResponse() {
        }

        public CreatedResponse(String competitorId, String productoId) {
            this.competitorId = competitorId;
            this.productoId = productoId;
        }

        public String getCompetitorId() {
            return competitorId;
        }

        public String getProductoId() {
            return productoId;
        }
        
    }
    
}

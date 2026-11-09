package co.edu.unipiloto.laboratorio7mongodb.services;

import co.edu.unipiloto.laboratorio7mongodb.models.Competitor;
import co.edu.unipiloto.laboratorio7mongodb.models.CompetitorDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author poeta
 */
@Path("/competitors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("MongoPU");

    @POST
    @Path("/add")
    public Response addCompetitor(CompetitorDTO dto) {

        EntityManager em = EMF.createEntityManager();

        try {
            
            em.getTransaction().begin();

            Competitor competitor = new Competitor();

            competitor.setName(dto.getName());
            competitor.setSurname(dto.getSurname());
            competitor.setAge(dto.getAge());
            competitor.setTelephone(dto.getTelephone());
            competitor.setCellphone(dto.getCellphone());
            competitor.setAddress(dto.getAddress());
            competitor.setCity(dto.getCity());
            competitor.setCountry(dto.getCountry());
            competitor.setVehicle(dto.getVehicle());

            em.persist(competitor);
            em.getTransaction().commit();

            Map<String, String> result = new HashMap<>();
            result.put("competitor_id", competitor.getId());

            return Response.status(Response.Status.CREATED).entity(result).build();

        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al registrar el competidor").build();

        } finally {
            em.close();
        }
        
    }

}

package co.edu.unipiloto.laboratorio7mongodb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import co.edu.unipiloto.laboratorio7mongodb.models.Competitor;
import co.edu.unipiloto.laboratorio7mongodb.models.Vehicle;

public class MongoConnectionTest {

    public static void main(String[] args) {

        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            
            System.out.println("Iniciando prueba de persistencia...");

            emf = Persistence.createEntityManagerFactory("MongoPU");
            em = emf.createEntityManager();

            em.getTransaction().begin();

            Competitor competitor = new Competitor();

            competitor.setName("Carlos");
            competitor.setSurname("Prueba MongoDB");
            competitor.setAge(26);
            competitor.setTelephone("6011234567");
            competitor.setCellphone("3001234567");
            competitor.setAddress("carlos@example.com");
            competitor.setCity("Bogota");
            competitor.setCountry("Colombia");
            competitor.setWinner(false);

            Vehicle vehicle = new Vehicle();
            
            vehicle.setPlate("TRE-465");
            vehicle.setBrand("BMW");
            vehicle.setModelYear(2022);

            competitor.setVehicle(vehicle);

            em.persist(competitor);

            em.getTransaction().commit();

            System.out.println("Competidor registrado correctamente.");
            System.out.println("ID generado: " + competitor.getId());

        } catch (Exception e) {

            System.err.println("ERROR EN LA PERSISTENCIA:");

            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

        } finally {

            if (em != null && em.isOpen()) {
                em.close();
            }

            if (emf != null && emf.isOpen()) {
                emf.close();
            }
            
        }
        
    }
    
}

package com.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Inicia la API REST sobre Jetty embebido.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        
        String portValue = System.getenv().getOrDefault("PORT", "8080");
        int port = Integer.parseInt(portValue);

        // Comprueba la conexión antes de iniciar el servidor HTTP.
        PersistenceManager.getInstance().ping();

        Server server = new Server(port);
        WebAppContext web = new WebAppContext();
        web.setContextPath("/");
        web.setDescriptor("src/main/webapp/WEB-INF/web.xml");
        web.setResourceBase("src/main/webapp/");
        web.setParentLoaderPriority(true);
        server.setHandler(web);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            PersistenceManager.getInstance().close();
        }));

        server.start();
        System.out.println("API disponible en http://localhost:" + port);
        server.join();
    
    }
    
}

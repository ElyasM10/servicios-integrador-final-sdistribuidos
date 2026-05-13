package org.distribuidos.libros;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.distribuidos.libros.modelo.Libro;
import org.distribuidos.libros.repository.LibroRepository;
import org.jboss.logging.Logger;

@Startup
@ApplicationScoped
public class DataLoader {

    @Inject
    LibroRepository repo;

    @Inject
    Logger auditor;

    void onStop(@Observes ShutdownEvent ev) {
        auditor.info("Servicio libro se esta deteniendo...");
    }

    void onStart(@Observes StartupEvent ev) {

        auditor.info(
                "\n================================\n" +
                        "       SERVICIO LIBROS\n" +
                        "       INICIADO OK \n" +
                        "================================\n"
        );


        // Zafón (autorId = 1)
        cargarLibro("La sombra del viento", 1L);
        cargarLibro("El juego del ángel", 1L);

        // Allende (autorId = 2)
        cargarLibro("La casa de los espíritus", 2L);
        cargarLibro("Eva Luna", 2L);
    }

    private void cargarLibro(String titulo, Long autorId) {

        auditor.debug("Intentando cargar libro: " + titulo);

        //  VALIDAR SI YA EXISTE
        boolean existe = repo.findAll().stream()
                .anyMatch(l ->
                        l.getTitulo().equalsIgnoreCase(titulo)
                                && l.getAutorId().equals(autorId)
                );

        if (existe) {
            auditor.debug("Libro ya existe, no se carga: " + titulo);
            return;
        }

        Libro l = new Libro();
        l.setTitulo(titulo);
        l.setAutorId(autorId);

        repo.guardarLibro(l);

        auditor.debug("Libro cargado correctamente: " + titulo);
    }

}
package org.distribuidos.editorial;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.distribuidos.editorial.modelo.Editorial;
import org.distribuidos.editorial.repository.EditorialRepository;
import org.jboss.logging.Logger;

@Startup
@ApplicationScoped
public class DataLoader {

    @Inject
    EditorialRepository repo;

    @Inject
    Logger auditor;

    void onStop(@Observes ShutdownEvent ev) {
        auditor.info("Servicio editoriales se esta deteniendo...");
    }

    void onStart(@Observes StartupEvent ev) {

        auditor.info(
                "\n================================\n" +
                        "       SERVICIO EDITORIALES\n" +
                        "       INICIADO OK \n" +
                        "================================\n"
        );

        cargarEditorial("Grupo Planeta", "España"); // id = 1
    }

    private void cargarEditorial(String nombre, String pais) {

        auditor.debug("Intentando cargar editorial: " + nombre);

        boolean existe = repo.findAll().stream()
                .anyMatch(e ->
                        e.getNombre().equalsIgnoreCase(nombre)
                                && e.getPais().equalsIgnoreCase(pais)
                );

        if (existe) {
            auditor.debug("Editorial ya existe, no se carga: " + nombre);
            return;
        }

        Editorial e = new Editorial();
        e.setNombre(nombre);
        e.setPais(pais);

        repo.guardarEditorial(e);

        auditor.debug("Editorial cargada correctamente: " + nombre);
    }

}
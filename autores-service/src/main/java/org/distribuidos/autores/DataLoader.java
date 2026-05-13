package org.distribuidos.autores;

import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.distribuidos.autores.modelo.Autor;
import org.distribuidos.autores.repository.AutorRepository;
import org.jboss.logging.Logger;

@Startup
@ApplicationScoped
public class DataLoader {

    @Inject
    AutorRepository repo;

    @Inject
    Logger auditor;


    void onStart(@Observes StartupEvent ev) {

        auditor.info(
                "\n================================\n" +
                        "       SERVICIO AUTORES\n" +
                        "       INICIADO OK \n" +
                        "================================\n"
        );

        cargarAutor("Carlos Ruiz Zafón", 1L);
        cargarAutor("Isabel Allende", 1L);
    }
/*
    private void cargarAutor(String nombre, Long editorialId) {
        auditor.debug("Cargando autores: " +nombre);
        Autor a = new Autor();
        a.setNombre(nombre);
        a.setEditorialId(editorialId);
        repo.guardar(a);
    }
*/
    private void cargarAutor(String nombre, Long editorialId) {

        auditor.debug("Intentando cargar autor: " + nombre);

        if (repo.existeAutor(nombre, editorialId)) {
            auditor.debug("Autor ya existe, no se carga: " + nombre);
            return;
        }

        Autor a = new Autor();
        a.setNombre(nombre);
        a.setEditorialId(editorialId);

        repo.guardarAutor(a);

        auditor.debug("Autor cargado correctamente: " + nombre);
    }

}
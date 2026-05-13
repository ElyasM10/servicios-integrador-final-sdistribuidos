package org.distribuidos.editorial.recurso;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.distribuidos.editorial.modelo.Editorial;
import org.distribuidos.editorial.repository.EditorialRepository;
import org.distribuidos.editorial.clients.AutorClient;
import org.distribuidos.editorial.clients.LibroClient;
import org.distribuidos.editorial.transferible.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Path("/editorial")
@Tag(name = "Editorial")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecursoEditorial {

    @Inject
    Logger auditor;

    @Inject
    EditorialRepository repo;

    @Inject
    @RestClient
    AutorClient autorClient;

    @Inject
    @RestClient
    LibroClient libroClient;

    @POST
    @Operation(summary = "Permite crear una editorial")
    public Response crear(Editorial e){

        auditor.debug("Crear editorial: " + e);

        if (e.getNombre() == null || e.getNombre().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El nombre es obligatorio")
                    .build();
        }

        if (e.getPais() == null || e.getPais().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El pais es obligatorio")
                    .build();
        }

        Editorial editorialGuardada = repo.guardarEditorial(e);

        auditor.debug("Editorial creada correctamente: " + editorialGuardada);

        return Response.status(Response.Status.CREATED)
                .entity(Map.of(
                        "mensaje", "Editorial creada con exito",
                        "editorial", editorialGuardada
                ))
                .build();
    }


    @GET
    @Path("/{id}")
    @Operation( summary = "Retorna una editorial específica por su ID")
    public Editorial obtenerPorId(@PathParam("id") Long id){
        auditor.debug("Obtener por id: "+id);
        return repo.findById(id);
    }

    @GET
    @Path("/")
    @Operation( summary = "Retorna todas las editoriales disponibles")
    public List<Editorial> obtenerTodos(){
        auditor.debug("Obtener todos");
        return repo.findAll();
    }

    @GET
    @Path("/{id}/dashboard")
    @Operation(summary = "Retorna los datos completos de la editorial, sus autores y los libros de cada autor.")
    public TransferibleDashBoard dashboard(@PathParam("id") Long id){

        auditor.debug("Dashboard para editorial ID: " + id);

        Editorial editorial = repo.findById(id);
        auditor.debug("Editorial: " + editorial);

        List<TransferibleAutor> autores = autorClient.obtenerPorEditorial(id);
        auditor.debug("Cantidad de autores: " + (autores != null ? autores.size() : 0));

        List<TransferibleLibro> libros = new ArrayList<>();

        if (autores != null) {
            for (TransferibleAutor autor : autores) {

                auditor.debug(" Buscando libros para autor ID: " + autor.getId());

                List<TransferibleLibro> librosAutor = libroClient.obtenerPorAutor(autor.getId());

                auditor.debug(" Libros encontrados: " + (librosAutor != null ? librosAutor.size() : 0));

                if (librosAutor != null) {
                    libros.addAll(librosAutor);
                }
            }
        }

        auditor.debug("Total libros acumulados: " + libros.size());

        Map<Long, List<TransferibleLibro>> librosPorAutor = libros.stream()
                .collect(Collectors.groupingBy(l -> l.getAutor().getId()));

        auditor.debug("Autores con libros: " + librosPorAutor.keySet());

        List<TransferibleAutorDashboard> autoresDTO = (autores != null ? autores : List.<TransferibleAutor>of())
                .stream()
                .map(a -> {

                    auditor.debug("Armando DTO autor ID: " + a.getId());

                    TransferibleAutorDashboard dto = new TransferibleAutorDashboard();
                    dto.setId(a.getId());
                    dto.setNombre(a.getNombre());

                    List<TransferibleLibroSimple> librosDTO = librosPorAutor
                            .getOrDefault(a.getId(), List.of())
                            .stream()
                            .map(l -> {
                                TransferibleLibroSimple ls = new TransferibleLibroSimple();
                                ls.setId(l.getId());
                                ls.setTitulo(l.getTitulo());
                                return ls;
                            })
                            .toList();

                    auditor.debug("Libros en DTO: " + librosDTO.size());

                    dto.setLibros(librosDTO);

                    return dto;
                })
                .toList();

        TransferibleDashBoard response = new TransferibleDashBoard();
        response.setEditorial(editorial);
        response.setAutores(autoresDTO);

        auditor.debug("Dashboard armado correctamente para editorial ID: " + id);

        return response;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation( summary = "Permite saber que sistema es el que responde")
    @Path("/whoami")
    public String whoAmI() {
        String serverName = System.getenv().getOrDefault("SERVER_NAME", "unknown");
        auditor.debug("Quien soy?: "+ serverName);
        return serverName + "\n";
    }

}

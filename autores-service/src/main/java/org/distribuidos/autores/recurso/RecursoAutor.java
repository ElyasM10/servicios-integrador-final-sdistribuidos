package org.distribuidos.autores.recurso;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.distribuidos.autores.clients.EditorialClient;
import org.distribuidos.autores.modelo.Autor;
import org.distribuidos.autores.repository.AutorRepository;
import org.distribuidos.autores.transferible.TransferibleAutor;
import org.distribuidos.autores.transferible.TransferibleAutorPost;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/autor")
@Tag(name = "Autor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecursoAutor {

    @Inject
    Logger auditor;

    @Inject
    AutorRepository repo;

    @Inject
    @RestClient
    EditorialClient editorialClient;

    public TransferibleAutor aTransferible(Autor autor) {
        auditor.debug("aTransferible()");
        TransferibleAutor transferible = new TransferibleAutor();
        transferible.setId(autor.getId());
        transferible.setNombre(autor.getNombre());

        // llamada al otro microservicio
        transferible.setEditorial(editorialClient.obtenerPorId(autor.getEditorialId()));

        return transferible;
    }

    @POST
    @Operation(summary = "Permite crear un autor.")
    @Path("/")
    public Response crear(TransferibleAutorPost transferibleAutorPost){

        auditor.debug("Crear autor: " + transferibleAutorPost);

        if (transferibleAutorPost.getNombre() == null || transferibleAutorPost.getNombre().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El nombre es obligatorio")
                    .build();
        }

        if (transferibleAutorPost.getEditorialId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El editorialId es obligatorio")
                    .build();
        }

        Autor autor = new Autor();
        autor.setNombre(transferibleAutorPost.getNombre());
        autor.setEditorialId(transferibleAutorPost.getEditorialId());

        Autor autorGuardado = repo.guardarAutor(autor);

     //   TransferibleAutor resultado = aTransferible(autorGuardado);

        auditor.debug("Retornando autor creado: " + autorGuardado);

        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Autor creado con exito");
        response.put("autor", autorGuardado);

        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation( summary = "Retorna un autor en particular mediante su ID.")
    public TransferibleAutor obtenerPorId(@PathParam("id") Long id){
        auditor.debug("Obtener autor por id: "+id);
        Autor autor = repo.findById(id);
        TransferibleAutor resultadoAutor = aTransferible(autor);
       return resultadoAutor;
    }

    @GET
    @Path("/")
    @Operation( summary = "Retorna todos los autores disponibles.")
    public List<Autor> obtenerTodos(){
        auditor.debug("Obtener todos");
        return repo.findAll();
    }

    @GET
    @Path("/editorial")
    @Operation(summary = "Retorna todos los autores que pertenecen a una editorial específica.")
    public List<TransferibleAutor> obtenerPorEditorial(@QueryParam("editorial") Long editorialId){
        auditor.debug("Obtener por editorial: "+editorialId);
        List<Autor> autores = repo.findByEditorial(editorialId);
     //   return autores;
        return autores.stream()
                .map(this::aTransferible)
                .toList();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation( summary = "Permite saber que sistema es el que responde.")
    @Path("/whoami")
    public String whoAmI() {
        String serverName = System.getenv().getOrDefault("SERVER_NAME", "unknown");
        auditor.debug("Quien soy?: "+ serverName);
        return serverName + "\n";
    }


}

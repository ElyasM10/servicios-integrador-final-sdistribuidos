package org.distribuidos.libros.recurso;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.distribuidos.libros.clients.AutorClient;
import org.distribuidos.libros.modelo.Libro;
import org.distribuidos.libros.repository.LibroRepository;
import org.distribuidos.libros.transferible.TransferibleLibro;
import org.distribuidos.libros.transferible.TransferibleLibroPost;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import java.util.Arrays;
import java.util.List;

@Path("/libros")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Libros")
public class RecursoLibro {

    @Inject
    LibroRepository repo;

    @Inject
    Logger auditor;

     @Inject
     @RestClient
     AutorClient autorClient;

    public TransferibleLibro aTransferible(Libro libro) {
        TransferibleLibro transferible = new TransferibleLibro();

        transferible.setId(libro.getId());
        transferible.setTitulo(libro.getTitulo());

        // llamada al microservicio de Autor
        transferible.setAutor(
                autorClient.obtenerPorId(libro.getAutorId())
        );

        return transferible;
    }

    @GET
    @Path("/{id}")
    @Operation( summary = "Permite obtener un libro en particular mediante su ID.")
    public TransferibleLibro obtenerPorId(@PathParam("id") Long id){
        auditor.debug("Obtener libro por id: " + id);

        Libro libro = repo.findById(id);

        TransferibleLibro resultadoLibro = aTransferible(libro);
        return resultadoLibro;
    }


    @GET
    @Path("/por-autor")
    @Operation(summary = "Retorna todos los libros que pertenecen a una autor específico.")
    public List<TransferibleLibro> obtenerPorAutor(@QueryParam("autor") Long autorId){
        auditor.debug("Obtener libro por autor: " + autorId);

        List<Libro> libros = repo.findByAutor(autorId);

       //   return libros;
        return libros.stream()
                .map(this::aTransferible)
                .toList();
    }

    @GET
    @Path("/")
    @Operation( summary = "Permite obtener todos los libros.")
    public List<Libro> obtenerTodos(){
        auditor.debug("Obtener todos");
        return repo.findAll();
    }

    @POST
    @Operation(summary = "Permite crear un libro.")
    public Response crear(TransferibleLibroPost transferibleLibroPost){

        auditor.debug("Crear libro: " + transferibleLibroPost);


        if (transferibleLibroPost.getTitulo() == null || transferibleLibroPost.getTitulo().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El titulo es obligatorio")
                    .build();
        }

        if (transferibleLibroPost.getAutorId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El autorId es obligatorio")
                    .build();
        }

        Libro libro = new Libro();
        libro.setTitulo(transferibleLibroPost.getTitulo());
        libro.setAutorId(transferibleLibroPost.getAutorId());

        auditor.debug("Probando guardar libro json");
        Libro resultado = repo.guardarLibro(libro);
        auditor.debug("Exito al guardar: " + resultado);
        return Response.status(Response.Status.CREATED).entity(resultado).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Operation( summary = "Permite saber que sistema es el que esta respondiendo.")
    @Path("/whoami")
    public String whoAmI() {
        String serverName = System.getenv().getOrDefault("SERVER_NAME", "unknown");
        auditor.debug("Quien soy?: "+ serverName);
        return serverName + "\n";
    }

}

package org.distribuidos.libros.clients;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.distribuidos.libros.transferible.TransferibleAutor;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "autor-api")
@Path("/autor")
public interface AutorClient {

    @GET
    @Path("/{id}")
    TransferibleAutor obtenerPorId(@PathParam("id") Long id);
}
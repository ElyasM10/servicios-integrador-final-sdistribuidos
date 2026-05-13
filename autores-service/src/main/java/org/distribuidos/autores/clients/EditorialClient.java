package org.distribuidos.autores.clients;

import org.distribuidos.autores.modelo.Editorial;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "editorial-api")
@Path("/editorial")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface EditorialClient {

    @GET
    @Path("/{id}")
    Editorial obtenerPorId(@PathParam("id") Long id);
}
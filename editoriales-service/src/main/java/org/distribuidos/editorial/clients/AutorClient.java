package org.distribuidos.editorial.clients;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.distribuidos.editorial.transferible.TransferibleAutor;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "autores-api")
@Path("/autor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AutorClient {

    @GET
    @Path("/editorial")
    List<TransferibleAutor> obtenerPorEditorial(@QueryParam("editorial") Long id);
}
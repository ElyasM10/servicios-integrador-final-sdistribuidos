package org.distribuidos.editorial.clients;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.distribuidos.editorial.transferible.TransferibleLibro;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@RegisterRestClient(configKey = "libros-api")
@Path("/libros")
@Produces(MediaType.APPLICATION_JSON)
public interface LibroClient {

    @GET
    @Path("/por-autor")
    List<TransferibleLibro>  obtenerPorAutor(@QueryParam("autor") Long id);

    @GET
    @Path("/por-autores")
    List<TransferibleLibro> obtenerPorAutores(@QueryParam("autores") String autores);
}
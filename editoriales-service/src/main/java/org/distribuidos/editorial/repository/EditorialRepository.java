package org.distribuidos.editorial.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.distribuidos.editorial.modelo.Editorial;
import org.distribuidos.editorial.transferible.TransferibleEditorial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class EditorialRepository {

    private Map<Long, Editorial> data = new HashMap<>();
    private AtomicLong seq = new AtomicLong();

    public Editorial guardar(TransferibleEditorial dto) {

        Editorial e = new Editorial();
        e.setId(seq.incrementAndGet());
        e.setNombre(dto.getNombre());
        e.setPais(dto.getPais());

        data.put(e.getId(), e);

        return e;
    }
/*
    public Editorial findById(Long id) {
        return data.get(id);
    }

    public List<Editorial> findAll(){
        return new ArrayList<>(data.values());
    }
*/
    private static final Logger auditor = LoggerFactory.getLogger(EditorialRepository.class);

    private static final String RUTA = "/var/servicios/json/editoriales.json";

    // OBTENER TODOS
    public List<Editorial> findAll() {
        auditor.debug("Obteniendo todas las editoriales");
        return leerArchivo();
    }

    // OBTENER POR ID
    public Editorial findById(Long id) {
        auditor.debug("Buscando editorial por ID: " + id);

        return leerArchivo().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    //GUARDAR
    public Editorial guardarEditorial(Editorial editorial) {

        auditor.debug("Iniciando guardado de editorial");

        List<Editorial> editoriales = leerArchivo();

        editorial.setId(generarId(editoriales));

        editoriales.add(editorial);

        escribirArchivo(editoriales);

        auditor.debug("Editorial guardada con ID: " + editorial.getId());

        return editorial;
    }

    // LEER
    private List<Editorial> leerArchivo() {
        try {
            File file = new File(RUTA);

            if (!file.exists()) {
                auditor.debug("Archivo editoriales.json no existe");
                return new ArrayList<>();
            }

            ObjectMapper mapper = new ObjectMapper();

            return new ArrayList<>(
                    Arrays.asList(mapper.readValue(file, Editorial[].class))
            );

        } catch (Exception e) {
            auditor.error("Error leyendo editoriales.json", e);
            return new ArrayList<>();
        }
    }

    //ESCRIBIR
    private void escribirArchivo(List<Editorial> editoriales) {
        try {
            File file = new File(RUTA);

            ObjectMapper mapper = new ObjectMapper();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, editoriales);

            auditor.debug("Archivo editoriales.json actualizado");

        } catch (Exception e) {
            auditor.error("Error escribiendo editoriales.json", e);
        }
    }

    // GENERAR ID
    private Long generarId(List<Editorial> editoriales) {
        return editoriales.stream()
                .map(Editorial::getId)
                .filter(Objects::nonNull)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

}
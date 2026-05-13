package org.distribuidos.autores.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.distribuidos.autores.modelo.Autor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class AutorRepository {

    private Map<Long, Autor> data = new HashMap<>();
    private AtomicLong seq = new AtomicLong();

    public Autor guardar(Autor autor){
        autor.setId(seq.incrementAndGet());
        data.put(autor.getId(), autor);
        return autor;
    }
/*
    public Autor findById(Long id){
        return data.get(id);
    }

    public List<Autor> findAll(){
        return new ArrayList<>(data.values());
    }

    public List<Autor> findByEditorial(Long editorialId){
        return data.values()
                .stream()
                .filter(a -> a.getEditorialId().equals(editorialId))
                .toList();
    }

 */
private static final Logger auditor = LoggerFactory.getLogger(AutorRepository.class);

    private static final String RUTA = "/var/servicios/json/autores.json";

    // OBTENER TODOS
    public List<Autor> findAll() {
        auditor.debug("Obteniendo todos los autores");
        return leerArchivo();
    }

    // OBTENER POR ID
    public Autor findById(Long id) {
        auditor.debug("Buscando autor por ID: " + id);

        return leerArchivo().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    //  OBTENER POR EDITORIAL
    public List<Autor> findByEditorial(Long editorialId) {
        auditor.debug("Buscando autores por editorialId: " + editorialId);

        return leerArchivo().stream()
                .filter(a -> a.getEditorialId().equals(editorialId))
                .toList();
    }

    // GUARDAR
    public Autor guardarAutor(Autor autor) {

        auditor.debug("Iniciando guardado de autor");

        List<Autor> autores = leerArchivo();

        autor.setId(generarId(autores));

        autores.add(autor);

        escribirArchivo(autores);

        auditor.debug("Autor guardado con ID: " + autor.getId());

        return autor;
    }

    //  LEER
    private List<Autor> leerArchivo() {
        try {
            File file = new File(RUTA);

            if (!file.exists()) {
                auditor.debug("Archivo autores.json no existe");
                return new ArrayList<>();
            }

            ObjectMapper mapper = new ObjectMapper();

            return new ArrayList<>(
                    Arrays.asList(mapper.readValue(file, Autor[].class))
            );

        } catch (Exception e) {
            auditor.error("Error leyendo autores.json", e);
            return new ArrayList<>();
        }
    }

    //  ESCRIBIR
    private void escribirArchivo(List<Autor> autores) {
        try {
            File file = new File(RUTA);

            ObjectMapper mapper = new ObjectMapper();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, autores);

            auditor.debug("Archivo autores.json actualizado");

        } catch (Exception e) {
            auditor.error("Error escribiendo autores.json", e);
        }
    }

    // GENERAR ID
    private Long generarId(List<Autor> autores) {
        return autores.stream()
                .map(Autor::getId)
                .filter(Objects::nonNull)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }

    public boolean existeAutor(String nombre, Long editorialId) {

        return leerArchivo().stream()
                .anyMatch(a ->
                        a.getNombre().equalsIgnoreCase(nombre)
                                && a.getEditorialId().equals(editorialId)
                );
    }


}
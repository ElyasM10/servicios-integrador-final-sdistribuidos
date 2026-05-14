package org.distribuidos.libros.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.distribuidos.libros.modelo.Libro;
import java.io.File;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LibroRepository {

   private static final Logger auditor = LoggerFactory.getLogger(LibroRepository.class);

    private static final String RUTA = "/var/servicios/json/libros.json";
 //   private static final String RUTA = "servicios/json/libros.json"; //para probar localmente y no en la maquina virtual
    public List<Libro> findAll() {

        auditor.debug("Obteniendo todos los libros");

        return leerArchivo();
    }

    //  OBTENER POR ID
    public Libro findById(Long id) {

        auditor.debug("Buscando libro por ID: " + id);

        return leerArchivo().stream()
                .filter(l -> l.getId().equals(id))
                .findFirst()
                .orElse(null);
    }


    // OBTENER POR AUTOR
    public List<Libro> findByAutor(Long autorId) {

        auditor.debug("Buscando libros por autorId: " + autorId);

        return leerArchivo().stream()
                .filter(l -> l.getAutorId().equals(autorId))
                .toList();
    }

    private void escribirArchivo(List<Libro> libros) {

        try {

            auditor.debug("Iniciando escritura de archivo libros.json");

            File file = new File(RUTA);

            /*
            // CREAR DIRECTORIOS SI NO EXISTEN
            File directorio = file.getParentFile();

            if (!directorio.exists()) {

                auditor.debug("Creando directorios: " + directorio.getAbsolutePath());

                directorio.mkdirs();
            }
*/
            auditor.debug("Ruta del archivo: " + file.getAbsolutePath());

            ObjectMapper mapper = new ObjectMapper();

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(file, libros);

            auditor.debug("Archivo guardado correctamente");

        } catch (Exception e) {
            auditor.error("Error escribiendo archivo libros.json", e);
            e.printStackTrace();
        }
    }

    private List<Libro> leerArchivo() {

        try {
            File file = new File(RUTA);

            if (!file.exists()) {
                auditor.debug("Archivo no existe, retornando lista vacía");
                return new ArrayList<>();
            }

            ObjectMapper mapper = new ObjectMapper();

            return new ArrayList<>(
                    Arrays.asList(mapper.readValue(file, Libro[].class))
            );

        } catch (Exception e) {
            auditor.error("Error leyendo archivo JSON", e);
            return new ArrayList<>();
        }
    }

    private Long generarId(List<Libro> libros) {

        auditor.debug("Generando nuevo ID para libro");

        Long nuevoId = libros.stream()
                .map(Libro::getId)
                .filter(Objects::nonNull)
                .max(Long::compareTo)
                .orElse(0L) + 1;

        auditor.debug("Nuevo ID generado: " + nuevoId);

        return nuevoId;
    }

    public Libro guardarLibro(Libro libro){

        auditor.debug("Iniciando guardado de libro");

        List<Libro> libros = leerArchivo();

        auditor.debug("Cantidad de libros existentes: " + libros.size());

        libro.setId(generarId(libros));

        auditor.debug("Asignado ID al libro: " + libro.getId());

        libros.add(libro);

        auditor.debug("Libro agregado a la lista. Nueva cantidad: " + libros.size());

        escribirArchivo(libros);

        auditor.debug("Proceso de guardado finalizado correctamente");

        return libro;
    }

}
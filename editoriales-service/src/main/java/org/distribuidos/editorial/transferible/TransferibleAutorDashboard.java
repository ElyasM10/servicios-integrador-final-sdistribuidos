package org.distribuidos.editorial.transferible;

import java.util.List;

public class TransferibleAutorDashboard {
    private Long id;
    private String nombre;
    private List<TransferibleLibroSimple> libros;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<TransferibleLibroSimple> getLibros() {
        return libros;
    }

    public void setLibros(List<TransferibleLibroSimple> libros) {
        this.libros = libros;
    }
}

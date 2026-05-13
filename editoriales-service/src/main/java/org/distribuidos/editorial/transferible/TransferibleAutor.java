package org.distribuidos.editorial.transferible;

import org.distribuidos.editorial.modelo.Editorial;

public class TransferibleAutor {

    private Long id;
    private String nombre;
    private Editorial editorial;

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

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

}

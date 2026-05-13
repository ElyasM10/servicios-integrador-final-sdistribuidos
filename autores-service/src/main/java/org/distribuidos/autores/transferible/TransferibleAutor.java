package org.distribuidos.autores.transferible;

import org.distribuidos.autores.modelo.Editorial;

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

    @Override
    public String toString() {
        return "TransferibleAutor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", editorial=" + editorial +
                '}';
    }
}
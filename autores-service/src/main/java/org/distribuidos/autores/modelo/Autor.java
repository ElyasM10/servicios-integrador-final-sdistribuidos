package org.distribuidos.autores.modelo;

public class Autor {

    private Long id;
    private String nombre;
    private Long editorialId;

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

    public Long getEditorialId() {
        return editorialId;
    }

    public void setEditorialId(Long editorialId) {
        this.editorialId = editorialId;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", editorialId=" + editorialId +
                '}';
    }
}
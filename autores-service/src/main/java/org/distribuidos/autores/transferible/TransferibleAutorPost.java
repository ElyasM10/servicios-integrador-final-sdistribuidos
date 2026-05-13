package org.distribuidos.autores.transferible;

public class TransferibleAutorPost {

    private  String nombre;

    private Long editorialId;

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
        return "TransferibleAutorPost{" +
                "nombre='" + nombre + '\'' +
                ", editorialId=" + editorialId +
                '}';
    }
}

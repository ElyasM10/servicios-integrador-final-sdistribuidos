package org.distribuidos.libros.transferible;


public class TransferibleLibroPost {

    private String titulo;
    private Long autorId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getAutorId() {
        return autorId;
    }

    public void setAutorId(Long autorId) {
        this.autorId = autorId;
    }

    @Override
    public String toString() {
        return "TransferibleLibro{" +
                "titulo='" + titulo + '\'' +
                ", autorId=" + autorId +
                '}';
    }
}
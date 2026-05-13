package org.distribuidos.libros.transferible;

public class TransferibleLibro {

    private Long id;
    private String titulo;
    private TransferibleAutor autor;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransferibleAutor getAutor() {
        return autor;
    }

    public void setAutor(TransferibleAutor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "TransferibleLibro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor=" + autor +
                '}';
    }
}
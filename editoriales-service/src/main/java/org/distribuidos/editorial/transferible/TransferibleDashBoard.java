package org.distribuidos.editorial.transferible;

import org.distribuidos.editorial.modelo.Editorial;

import java.util.List;

public class TransferibleDashBoard {

    private Editorial editorial;
    private List<TransferibleAutorDashboard> autores;


    public List<TransferibleAutorDashboard> getAutores() {
        return autores;
    }

    public void setAutores(List<TransferibleAutorDashboard> autores) {
        this.autores = autores;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }


}

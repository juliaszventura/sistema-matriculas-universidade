
package br.com.sistemamatriculas.model;

import java.time.LocalDate;

public class PeriodoMatricula {

    private LocalDate dataInicio;
    private LocalDate dataFim;

    public PeriodoMatricula(
        LocalDate dataInicio,
        LocalDate dataFim
    ) {
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public boolean estaAberto() {
        // TODO: implementar na próxima sprint
        return false;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }
}
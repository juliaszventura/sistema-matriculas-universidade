package br.com.sistemamatriculas.model;

public class SemestreLetivo {
    private int ano;
    private int periodo;
    private PeriodoMatricula periodoMatricula;

    public SemestreLetivo(int ano, int periodo, PeriodoMatricula periodoMatricula) {
        this.ano = ano;
        this.periodo = periodo;
        this.periodoMatricula = periodoMatricula;
    }

    public String descricao() {
        // TODO: implementar na próxima sprint
        return null;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public PeriodoMatricula getPeriodoMatricula() {
        return periodoMatricula;
    }

    public void setPeriodoMatricula(PeriodoMatricula periodoMatricula) {
        this.periodoMatricula = periodoMatricula;
    }

}
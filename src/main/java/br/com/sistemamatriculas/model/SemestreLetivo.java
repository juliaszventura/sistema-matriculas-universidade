package br.com.sistemamatriculas.model;

import java.util.ArrayList;
import java.util.List;

public class SemestreLetivo {

    private int ano;
    private int periodo;
    private PeriodoMatricula periodoMatricula;
    private List<OfertaDisciplina> ofertas = new ArrayList<>();

    public SemestreLetivo(int ano, int periodo, PeriodoMatricula periodoMatricula) {
        this.ano = ano;
        this.periodo = periodo;
        this.periodoMatricula = periodoMatricula;
        this.ofertas = new ArrayList<>();
    }

    public String descricao() {
        return ano + "/" + periodo;
    }

    public void adicionarOferta(OfertaDisciplina oferta) {
        if (oferta == null) {
            throw new IllegalArgumentException(
                    "A oferta não pode ser nula."
            );
        }

        if (!ofertas.contains(oferta)) {
            ofertas.add(oferta);
        }
    }

    public void removerOferta(OfertaDisciplina oferta) {
        ofertas.remove(oferta);
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

    public void setPeriodoMatricula(
            PeriodoMatricula periodoMatricula
    ) {
        this.periodoMatricula = periodoMatricula;
    }

    public List<OfertaDisciplina> getOfertas() {
        return ofertas;
    }

    public void setOfertas(List<OfertaDisciplina> ofertas) {
        if (ofertas == null) {
            this.ofertas = new ArrayList<>();
        } else {
            this.ofertas = ofertas;
        }
    }

    @Override
    public String toString() {
        return "SemestreLetivo [ano=" + ano
                + ", periodo=" + periodo
                + ", periodoMatricula=" + periodoMatricula
                + "]";
    }
}

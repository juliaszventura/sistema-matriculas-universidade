package br.com.sistemamatriculas.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurriculoSemestral {

    private Long id;
    private Date dataGeracao;
    private List<OfertaDisciplina> ofertas = new ArrayList<>();

    public CurriculoSemestral(Long id, Date dataGeracao) {
        this.id = id;
        this.dataGeracao = dataGeracao;
    }

    public void adicionarOferta(OfertaDisciplina ofertaDisciplina) {
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException("A oferta não pode ser nula.");
        }
        if (!ofertas.contains(ofertaDisciplina)) {
            ofertas.add(ofertaDisciplina);
        }
    }

    public void removerOferta(OfertaDisciplina ofertaDisciplina) {
        if (ofertaDisciplina == null) {
            throw new IllegalArgumentException(
                    "A oferta não pode ser nula."
            );
        }

        ofertas.remove(ofertaDisciplina);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(Date dataGeracao) {
        this.dataGeracao = dataGeracao;
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
}
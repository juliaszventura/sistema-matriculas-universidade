package br.com.sistemamatriculas.model;

import java.time.LocalDate;

import br.com.sistemamatriculas.enums.TipoOpcao;

public class ItemMatricula {

    private Long id;
    private TipoOpcao tipo;
    private LocalDate dataInclusao;

    private OfertaDisciplina ofertaDisciplina;

    public ItemMatricula(
        Long id,
        TipoOpcao tipo,
        LocalDate dataInclusao,
        OfertaDisciplina ofertaDisciplina
    ) {
        this.id = id;
        this.tipo = tipo;
        this.dataInclusao = dataInclusao;
        this.ofertaDisciplina = ofertaDisciplina;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoOpcao getTipo() {
        return tipo;
    }

    public void setTipo(TipoOpcao tipo) {
        this.tipo = tipo;
    }

    public LocalDate getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(LocalDate dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public void setOfertaDisciplina(OfertaDisciplina ofertaDisciplina) {
        this.ofertaDisciplina = ofertaDisciplina;
    }
}
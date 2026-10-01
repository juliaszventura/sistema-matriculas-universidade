package br.com.sistemamatriculas.model;

import java.time.LocalDate;

import br.com.sistemamatriculas.enums.TipoOpcao;

public class ItemMatricula {

    private static long ultimoId = 0;

    public static synchronized Long proximoId() {
        return ++ultimoId;
    }

    private static synchronized void registrarId(Long id) {

        if (id != null && id > ultimoId) {
            ultimoId = id;
        }
    }

    private Long id;
    private TipoOpcao tipo;
    private LocalDate dataInclusao;
    private MatriculaSemestral matriculaSemestral;
    private OfertaDisciplina ofertaDisciplina;

    public ItemMatricula(
            Long id,
            TipoOpcao tipo,
            LocalDate dataInclusao,
            MatriculaSemestral matriculaSemestral,
            OfertaDisciplina ofertaDisciplina
    ) {
        this.id = id;
        this.tipo = tipo;
        this.dataInclusao = dataInclusao;
        this.matriculaSemestral = matriculaSemestral;
        this.ofertaDisciplina = ofertaDisciplina;

        registrarId(id);
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

    public MatriculaSemestral getMatriculaSemestral() {
        return matriculaSemestral;
    }

    public void setMatriculaSemestral(
            MatriculaSemestral matriculaSemestral
    ) {
        this.matriculaSemestral = matriculaSemestral;
    }

    public OfertaDisciplina getOfertaDisciplina() {
        return ofertaDisciplina;
    }

    public void setOfertaDisciplina(
            OfertaDisciplina ofertaDisciplina
    ) {
        this.ofertaDisciplina = ofertaDisciplina;
    }

    @Override
    public String toString() {
        return "ItemMatricula [id=" + id
                + ", tipo=" + tipo
                + ", dataInclusao=" + dataInclusao
                + ", ofertaDisciplina=" + ofertaDisciplina
                + "]";
    }
}
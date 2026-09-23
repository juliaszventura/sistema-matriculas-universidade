package br.com.sistemamatriculas.model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import br.com.sistemamatriculas.enums.TipoOpcao;

public class MatriculaSemestral {
    private Long id;
    private Date dataMatricula;
    private SemestreLetivo semestreLetivo;
    private List<ItemMatricula> itens = new ArrayList<>();
    private SistemaCobranca sistemaCobranca;
    
    public MatriculaSemestral(Long id, Date dataMatricula) {
        this.id = id;
        this.dataMatricula = dataMatricula;
    }

    public void adicionarItem(OfertaDisciplina oferta, TipoOpcao tipo) {
        // TODO: implementar
    }

    public void cancelarItem(ItemMatricula item) {
        // TODO: implementar
    }

    public void notificarCobranca() {
        // TODO: implementar
    }

    public Long getId() {
        return id;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public SemestreLetivo getSemestreLetivo() {
        return semestreLetivo;
    }

    public List<ItemMatricula> getItens() {
        return itens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public void setSemestreLetivo(SemestreLetivo semestreLetivo) {
        this.semestreLetivo = semestreLetivo;
    }

    public void setItens(List<ItemMatricula> itens) {
        this.itens = itens;
    }

    public void setSistemaCobranca(SistemaCobranca sistemaCobranca) {
        this.sistemaCobranca = sistemaCobranca;
    }

}

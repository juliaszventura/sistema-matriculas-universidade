package br.com.sistemamatriculas.model;

import br.com.sistemamatriculas.enums.StatusOfertaDisciplina;

public class OfertaDisciplina {
    private Long id;
    private Disciplina disciplina;
    private int vagasMaximas = 60;
    private int minimoAlunos = 3;
    private StatusOfertaDisciplina status;

    public OfertaDisciplina(Long id, Disciplina disciplina) {
        this.id = id;
        this.disciplina = disciplina;
        this.status = StatusOfertaDisciplina.PREVISTA;
    }

    public boolean verificarDisponibilidadeVagas() {
        // TODO: implementar

        return true;
    }

    public int totalInscritos() {
        // TODO: implementar

        return 1;
    }

    public void encerrarInscricoes() {
        // TODO: implementar
    }

    public void avaliarAtivacao() {
        // TODO: implementar 
    }

    public Long getId() {
        return id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public StatusOfertaDisciplina getStatus() {
        return status;
    }

    public void setStatus(StatusOfertaDisciplina status) {
        this.status = status;
    }
}

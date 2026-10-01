package br.com.sistemamatriculas.model;

import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.enums.StatusOfertaDisciplina;

public class OfertaDisciplina {

    private Long id;
    private Disciplina disciplina;
    private Professor professor;
    private SemestreLetivo semestreLetivo;

    private int vagasMaximas = 60;
    private int minimoAlunos = 3;

    private StatusOfertaDisciplina status;

    private List<ItemMatricula> itensMatricula;

    public OfertaDisciplina(
            Long id,
            Disciplina disciplina,
            Professor professor,
            SemestreLetivo semestreLetivo) {
        this.id = id;
        this.disciplina = disciplina;
        this.professor = professor;
        this.semestreLetivo = semestreLetivo;

        this.status = StatusOfertaDisciplina.PREVISTA;
        this.itensMatricula = new ArrayList<>();
    }

    public boolean verificarDisponibilidadeVagas() {
        return totalInscritos() < vagasMaximas
                && status != StatusOfertaDisciplina.CANCELADA
                && status != StatusOfertaDisciplina.ENCERRADA;
    }

    public List<Aluno> getAlunosMatriculados() {

        List<Aluno> alunos = new ArrayList<>();

        for (ItemMatricula item : itensMatricula) {

            if (item.getMatriculaSemestral() == null) {
                continue;
            }

            Aluno aluno = item
                    .getMatriculaSemestral()
                    .getAluno();

            if (aluno != null && !alunos.contains(aluno)) {
                alunos.add(aluno);
            }
        }

        return alunos;
    }

    public int totalInscritos() {
        return itensMatricula.size();
    }

    public void adicionarItemMatricula(ItemMatricula item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "O item da matrícula não pode ser nulo.");
        }

        if (!verificarDisponibilidadeVagas()) {
            throw new IllegalStateException(
                    "Não existem vagas disponíveis para esta oferta.");
        }

        if (!itensMatricula.contains(item)) {
            itensMatricula.add(item);
        }

        if (totalInscritos() >= vagasMaximas) {
            encerrarInscricoes();
        }
    }

    public void removerItemMatricula(ItemMatricula item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "O item da matrícula não pode ser nulo.");
        }

        itensMatricula.remove(item);

        // Se a oferta estava encerrada por lotacao, a vaga liberada reabre
        // as inscricoes.
        if (status == StatusOfertaDisciplina.ENCERRADA
                && totalInscritos() < vagasMaximas) {

            status = StatusOfertaDisciplina.PREVISTA;
        }
    }

    public void encerrarInscricoes() {
        status = StatusOfertaDisciplina.ENCERRADA;
    }

    public void avaliarAtivacao() {

        if (totalInscritos() >= minimoAlunos) {
            status = StatusOfertaDisciplina.ATIVA;
        } else {
            status = StatusOfertaDisciplina.CANCELADA;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public SemestreLetivo getSemestreLetivo() {
        return semestreLetivo;
    }

    public void setSemestreLetivo(
            SemestreLetivo semestreLetivo) {
        this.semestreLetivo = semestreLetivo;
    }

    public int getVagasMaximas() {
        return vagasMaximas;
    }

    public void setVagasMaximas(int vagasMaximas) {
        this.vagasMaximas = vagasMaximas;
    }

    public int getMinimoAlunos() {
        return minimoAlunos;
    }

    public void setMinimoAlunos(int minimoAlunos) {
        this.minimoAlunos = minimoAlunos;
    }

    public StatusOfertaDisciplina getStatus() {
        return status;
    }

    public void setStatus(StatusOfertaDisciplina status) {
        this.status = status;
    }

    public List<ItemMatricula> getItensMatricula() {
        return itensMatricula;
    }

    public void setItensMatricula(
            List<ItemMatricula> itensMatricula) {
        if (itensMatricula == null) {
            this.itensMatricula = new ArrayList<>();
        } else {
            this.itensMatricula = itensMatricula;
        }
    }

    @Override
    public String toString() {
        return "OfertaDisciplina [id=" + id
                + ", disciplina=" + disciplina
                + ", professor=" + professor
                + ", semestreLetivo=" + semestreLetivo
                + ", vagasMaximas=" + vagasMaximas
                + ", minimoAlunos=" + minimoAlunos
                + ", totalInscritos=" + totalInscritos()
                + ", status=" + status
                + "]";
    }
}
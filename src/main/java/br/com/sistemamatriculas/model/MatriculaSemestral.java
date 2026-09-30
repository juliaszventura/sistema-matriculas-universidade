package br.com.sistemamatriculas.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.enums.TipoOpcao;

public class MatriculaSemestral {

    private Long id;
    private LocalDate dataMatricula;
    private Aluno aluno;
    private SemestreLetivo semestreLetivo;
    private List<ItemMatricula> itens;

    public MatriculaSemestral(
            Long id,
            LocalDate dataMatricula,
            SemestreLetivo semestreLetivo) {
        this.id = id;
        this.dataMatricula = dataMatricula;
        this.semestreLetivo = semestreLetivo;
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(
            OfertaDisciplina oferta,
            TipoOpcao tipo) {
        if (oferta == null) {
            throw new IllegalArgumentException(
                    "A oferta da disciplina não pode ser nula.");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo da matrícula não pode ser nulo.");
        }

        if (semestreLetivo == null) {
            throw new IllegalStateException(
                    "A matrícula deve estar associada a um semestre letivo.");
        }

        if (semestreLetivo.getPeriodoMatricula() == null) {
            throw new IllegalStateException(
                    "O semestre não possui período de matrícula.");
        }

        if (!semestreLetivo.getPeriodoMatricula().estaAberto()) {
            throw new IllegalStateException(
                    "O período de matrícula está fechado.");
        }

        if (!oferta.verificarDisponibilidadeVagas()) {
            throw new IllegalStateException(
                    "Não existem vagas disponíveis para essa disciplina.");
        }

        long quantidadeObrigatorias = itens.stream()
                .filter(item -> item.getTipo() == TipoOpcao.OBRIGATORIA)
                .count();

        long quantidadeOptativas = itens.stream()
                .filter(item -> item.getTipo() == TipoOpcao.OPTATIVA)
                .count();

        if (tipo == TipoOpcao.OBRIGATORIA
                && quantidadeObrigatorias >= 4) {

            throw new IllegalStateException(
                    "O aluno já possui 4 disciplinas obrigatórias.");
        }

        if (tipo == TipoOpcao.OPTATIVA
                && quantidadeOptativas >= 2) {

            throw new IllegalStateException(
                    "O aluno já possui 2 disciplinas optativas.");
        }

        boolean jaMatriculado = itens.stream()
                .anyMatch(item -> item.getOfertaDisciplina() == oferta);

        if (jaMatriculado) {
            throw new IllegalStateException(
                    "O aluno já está matriculado nessa disciplina.");
        }

        Long itemId = (long) (itens.size() + 1);

        ItemMatricula novoItem = new ItemMatricula(
                itemId,
                tipo,
                LocalDate.now(),
                this,
                oferta);

        itens.add(novoItem);

        oferta.adicionarItemMatricula(novoItem);

        notificarCobranca();
    }

    public void cancelarItem(ItemMatricula item) {

        if (item == null) {
            throw new IllegalArgumentException(
                    "O item da matrícula não pode ser nulo.");
        }

        if (semestreLetivo == null
                || semestreLetivo.getPeriodoMatricula() == null) {

            throw new IllegalStateException(
                    "Não existe período de matrícula configurado.");
        }

        if (!semestreLetivo.getPeriodoMatricula().estaAberto()) {
            throw new IllegalStateException(
                    "Não é possível cancelar fora do período de matrícula.");
        }

        if (!itens.contains(item)) {
            throw new IllegalArgumentException(
                    "Este item não pertence a esta matrícula.");
        }

        itens.remove(item);
        item.getOfertaDisciplina().removerItemMatricula(item);
    }

    public void notificarCobranca() {
        SistemaCobranca sistemaCobranca = new SistemaCobranca();

        if (sistemaCobranca != null) {
            sistemaCobranca.receberNotificacao(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public SemestreLetivo getSemestreLetivo() {
        return semestreLetivo;
    }

    public void setSemestreLetivo(
            SemestreLetivo semestreLetivo) {
        this.semestreLetivo = semestreLetivo;
    }

    public List<ItemMatricula> getItens() {
        return itens;
    }

    public void setItens(List<ItemMatricula> itens) {

        if (itens == null) {
            this.itens = new ArrayList<>();
        } else {
            this.itens = itens;
        }
    }

    @Override
    public String toString() {
        return "MatriculaSemestral [id=" + id
                + ", aluno=" + (aluno != null ? aluno.getNome() : null)
                + ", semestreLetivo=" + semestreLetivo
                + ", itens=" + itens.size()
                + "]";
    }
}
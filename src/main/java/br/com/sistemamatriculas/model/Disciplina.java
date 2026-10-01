package br.com.sistemamatriculas.model;

import br.com.sistemamatriculas.enums.TipoOpcao;

public class Disciplina {

    private int codigo;
    private String nome;
    private int creditos;
    private int cargaHoraria;
    private Curso curso;

    private TipoOpcao tipo;

    public Disciplina(
            int codigo,
            String nome,
            int creditos,
            int cargaHoraria,
            Curso curso,
            TipoOpcao tipo
    ) {
        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo da disciplina (obrigatoria ou optativa) e obrigatorio.");
        }

        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
        this.cargaHoraria = cargaHoraria;
        this.tipo = tipo;
        this.curso = null;

        setCurso(curso);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public TipoOpcao getTipo() {
        return tipo;
    }

    public void setTipo(TipoOpcao tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "O tipo da disciplina nao pode ser nulo.");
        }

        this.tipo = tipo;
    }

    public boolean isObrigatoria() {
        return tipo == TipoOpcao.OBRIGATORIA;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {

        Curso anterior = this.curso;

        if (anterior == curso) {
            return;
        }

        this.curso = curso;

        if (anterior != null) {
            anterior.removerDisciplina(this);
        }

        if (curso != null
                && !curso.getDisciplinas().contains(this)) {

            curso.adicionarDisciplina(this);
        }
    }

    @Override
    public String toString() {
        return "Disciplina [codigo=" + codigo
                + ", nome=" + nome
                + ", creditos=" + creditos
                + ", cargaHoraria=" + cargaHoraria
                + ", tipo=" + tipo
                + ", curso=" + (curso != null ? curso.getNome() : "sem curso")
                + "]";
    }
}
package br.com.sistemamatriculas.model;

public class Disciplina {

    private String codigo;
    private String nome;
    private int creditos;
    private int cargaHoraria;

    public Disciplina(
        String codigo,
        String nome,
        int creditos,
        int cargaHoraria
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
        this.cargaHoraria = cargaHoraria;
    }

    public boolean podeAbrirTurma() {
        // TODO: implementar
        return false;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
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
}
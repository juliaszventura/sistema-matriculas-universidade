package br.com.sistemamatriculas.model;

import java.util.List;

public class Professor extends Usuario {
    private String registro;

    public Professor(Long id, String nome, String login, String senha, String registro) {
        super(id, nome, login, senha);
        this.registro = registro;
    }

    public List<Aluno> consultarAlunosMatriculados(OfertaDisciplina oferta) {
        // TODO: implementar

        return null; 
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }

    
}

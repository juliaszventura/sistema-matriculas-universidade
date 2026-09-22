package br.com.sistemamatriculas.model;

import java.util.List;

public class Aluno extends Usuario {
    private String matricula;
    private List<MatriculaSemestral> matriculas;

    public Aluno(Long id, String nome, String login, String senha, String matricula) {
        super(id, nome, login, senha);
        this.matricula = matricula;
    }

    public List<OfertaDisciplina> consultarDisciplinasDisponiveis() {
        // TODO: implementar

        return null;
    }

    public void realizarMatricula(MatriculaSemestral matricula) {
        // TODO: implementar
    }

    public void cancelarMatricula(ItemMatricula item) {
        // TODO: implementar
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public List<MatriculaSemestral> getMatriculas() {
        return matriculas;
    }

    public void setMatriculas(List<MatriculaSemestral> matriculas) {
        this.matriculas = matriculas;
    }

    
}

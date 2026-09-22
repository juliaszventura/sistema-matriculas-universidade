package br.com.sistemamatriculas.model;

public class Secretaria extends Usuario {
    private String setor;

    public Secretaria(Long id, String nome, String login, String senha, String setor) {
        super(id, nome, login, senha);
        this.setor = setor;
    }

    public CurriculoSemestral gerarCurriculo(SemestreLetivo semestre) {
        // TODO: implementar  

        return null;
    }

    public void gerenciarCursos() {
        // TODO: implementar
    }

    public void gerenciarDisciplinas() {
        // TODO: implementar
    }

    public void gerenciarProfessores() {
        // TODO: implementar
    }

    public void gerenciarAlunos() {
        // TODO: implementar
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }
    
}

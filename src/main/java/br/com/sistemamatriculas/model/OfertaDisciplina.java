package br.com.sistemamatriculas.model;

import br.com.sistemamatriculas.enums.StatusOfertaDisciplina;

public class OfertaDisciplina {
    private Long id;
    private int vagasMaximas = 60;
    private int minimoAlunos = 3;
    private StatusOfertaDisciplina status;

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
}

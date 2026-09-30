package br.com.sistemamatriculas.model;

public class SistemaCobranca {

    public void receberNotificacao(MatriculaSemestral matricula) {

        if (matricula == null) {
            throw new IllegalArgumentException(
                    "A matrícula não pode ser nula."
            );
        }

        System.out.println(
                "Sistema de cobrança notificado sobre a matrícula "
                        + matricula.getId() + "."
        );
    }
}
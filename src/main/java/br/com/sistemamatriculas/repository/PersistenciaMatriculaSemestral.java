package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.model.Aluno;
import br.com.sistemamatriculas.model.MatriculaSemestral;
import br.com.sistemamatriculas.model.SemestreLetivo;
import br.com.sistemamatriculas.model.Usuario;

public class PersistenciaMatriculaSemestral {

    private static final Path ARQUIVO =
            Path.of("dados", "matricula.txt");

    public static List<MatriculaSemestral> carregar(
            List<Usuario> usuarios,
            List<SemestreLetivo> semestres) {

        List<MatriculaSemestral> matriculas =
                new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return matriculas;
        }

        try {

            List<String> linhas =
                    Files.readAllLines(ARQUIVO);

            for (String linha : linhas) {

                if (linha.isBlank()) {
                    continue;
                }

                String[] dados =
                        linha.split(";", -1);

                Long id =
                        Long.parseLong(dados[0]);

                LocalDate data =
                        LocalDate.parse(dados[1]);

                Long alunoId =
                        Long.parseLong(dados[2]);

                int ano =
                        Integer.parseInt(dados[3]);

                int periodo =
                        Integer.parseInt(dados[4]);

                Aluno aluno = null;
                SemestreLetivo semestre = null;

                for (Usuario usuario : usuarios) {

                    if (usuario instanceof Aluno a
                            && a.getId()
                            .equals(alunoId)) {

                        aluno = a;
                        break;
                    }
                }

                for (SemestreLetivo s : semestres) {

                    if (s.getAno() == ano
                            && s.getPeriodo()
                            == periodo) {

                        semestre = s;
                        break;
                    }
                }

                if (aluno == null
                        || semestre == null) {

                    throw new IllegalStateException(
                            "Aluno ou semestre da matrícula não encontrado."
                    );
                }

                MatriculaSemestral matricula =
                        new MatriculaSemestral(
                                id,
                                data,
                                semestre
                        );

                aluno.realizarMatricula(
                        matricula
                );

                matriculas.add(matricula);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar matrículas.",
                    e
            );
        }

        return matriculas;
    }

    public static void salvar(
            List<MatriculaSemestral> matriculas) {

        List<String> linhas =
                new ArrayList<>();

        for (MatriculaSemestral matricula
                : matriculas) {

            String linha =
                    matricula.getId() + ";"
                    + matricula.getDataMatricula() + ";"
                    + matricula.getAluno()
                            .getId() + ";"
                    + matricula.getSemestreLetivo()
                            .getAno() + ";"
                    + matricula.getSemestreLetivo()
                            .getPeriodo();

            linhas.add(linha);
        }

        try {

            Files.createDirectories(
                    ARQUIVO.getParent()
            );

            Files.write(
                    ARQUIVO,
                    linhas
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao salvar matrículas.",
                    e
            );
        }
    }
}
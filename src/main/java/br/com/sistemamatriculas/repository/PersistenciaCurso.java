package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.model.Curso;
import br.com.sistemamatriculas.model.Disciplina;

public class PersistenciaCurso {

    private static final Path ARQUIVO =
            Path.of("dados", "curso.txt");

    public static List<Curso> carregar(
            List<Disciplina> disciplinas) {

        List<Curso> cursos =
                new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return cursos;
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

                Curso curso = new Curso(
                        Integer.parseInt(dados[0]),
                        dados[1],
                        Integer.parseInt(dados[2])
                );

                if (dados.length > 3
                        && !dados[3].isBlank()) {

                    String[] codigos =
                            dados[3].split(",");

                    for (String codigo : codigos) {

                        int codigoDisciplina =
                                Integer.parseInt(codigo);

                        for (Disciplina disciplina
                                : disciplinas) {

                            if (disciplina.getCodigo()
                                    == codigoDisciplina) {

                                curso.adicionarDisciplina(
                                        disciplina
                                );

                                break;
                            }
                        }
                    }
                }

                cursos.add(curso);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar cursos.",
                    e
            );
        }

        return cursos;
    }

    public static void salvar(
            List<Curso> cursos) {

        List<String> linhas =
                new ArrayList<>();

        for (Curso curso : cursos) {

            List<String> codigos =
                    new ArrayList<>();

            for (Disciplina disciplina
                    : curso.getDisciplinas()) {

                codigos.add(
                        String.valueOf(
                                disciplina.getCodigo()
                        )
                );
            }

            String linha =
                    curso.getCodigo() + ";"
                    + curso.getNome() + ";"
                    + curso.getNumeroCreditos() + ";"
                    + String.join(",", codigos);

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
                    "Erro ao salvar cursos.",
                    e
            );
        }
    }
}
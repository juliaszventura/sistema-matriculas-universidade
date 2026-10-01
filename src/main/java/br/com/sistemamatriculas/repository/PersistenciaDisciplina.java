package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.enums.TipoOpcao;
import br.com.sistemamatriculas.model.Curso;
import br.com.sistemamatriculas.model.Disciplina;

public class PersistenciaDisciplina {

    private static final Path ARQUIVO =
            Path.of("dados", "disciplina.txt");

    public static List<Disciplina> carregar(List<Curso> cursos) {

        List<Disciplina> disciplinas =
                new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return disciplinas;
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

                Curso curso = null;

                if (dados.length > 4
                        && !dados[4].isBlank()) {

                    int codigoCurso =
                            Integer.parseInt(dados[4]);

                    for (Curso c : cursos) {

                        if (c.getCodigo() == codigoCurso) {
                            curso = c;
                            break;
                        }
                    }

                    if (curso == null) {

                        throw new IllegalStateException(
                                "Curso "
                                        + codigoCurso
                                        + " da disciplina "
                                        + dados[0]
                                        + " nao encontrado."
                        );
                    }
                }

                TipoOpcao tipo = TipoOpcao.OBRIGATORIA;

                if (dados.length > 5
                        && !dados[5].isBlank()) {

                    tipo = TipoOpcao.valueOf(dados[5]);
                }

                Disciplina disciplina =
                        new Disciplina(
                                Integer.parseInt(dados[0]),
                                dados[1],
                                Integer.parseInt(dados[2]),
                                Integer.parseInt(dados[3]),
                                curso,
                                tipo
                        );

                disciplinas.add(disciplina);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar disciplinas.",
                    e
            );
        }

        return disciplinas;
    }

    public static void salvar(
            List<Disciplina> disciplinas) {

        List<String> linhas =
                new ArrayList<>();

        for (Disciplina disciplina : disciplinas) {

            String codigoCurso =
                    disciplina.getCurso() != null
                            ? String.valueOf(
                            disciplina.getCurso().getCodigo())
                            : "";

            String linha =
                    disciplina.getCodigo() + ";"
                            + disciplina.getNome() + ";"
                            + disciplina.getCreditos() + ";"
                            + disciplina.getCargaHoraria() + ";"
                            + codigoCurso + ";"
                            + disciplina.getTipo();

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
                    "Erro ao salvar disciplinas.",
                    e
            );
        }
    }
}
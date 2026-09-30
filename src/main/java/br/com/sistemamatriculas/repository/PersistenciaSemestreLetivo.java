package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.model.PeriodoMatricula;
import br.com.sistemamatriculas.model.SemestreLetivo;

public class PersistenciaSemestreLetivo {

    private static final Path ARQUIVO =
            Path.of("dados", "semestre.txt");

    public static List<SemestreLetivo> carregar() {

        List<SemestreLetivo> semestres =
                new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return semestres;
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

                PeriodoMatricula periodo =
                        new PeriodoMatricula(
                                LocalDate.parse(dados[2]),
                                LocalDate.parse(dados[3])
                        );

                SemestreLetivo semestre =
                        new SemestreLetivo(
                                Integer.parseInt(dados[0]),
                                Integer.parseInt(dados[1]),
                                periodo
                        );

                semestres.add(semestre);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar semestres.",
                    e
            );
        }

        return semestres;
    }

    public static void salvar(
            List<SemestreLetivo> semestres) {

        List<String> linhas =
                new ArrayList<>();

        for (SemestreLetivo semestre : semestres) {

            String linha =
                    semestre.getAno() + ";"
                    + semestre.getPeriodo() + ";"
                    + semestre
                            .getPeriodoMatricula()
                            .getDataInicio()
                    + ";"
                    + semestre
                            .getPeriodoMatricula()
                            .getDataFim();

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
                    "Erro ao salvar semestres.",
                    e
            );
        }
    }
}
package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.enums.TipoOpcao;
import br.com.sistemamatriculas.model.ItemMatricula;
import br.com.sistemamatriculas.model.MatriculaSemestral;
import br.com.sistemamatriculas.model.OfertaDisciplina;

public class PersistenciaItemMatricula {

    private static final Path ARQUIVO =
            Path.of("dados", "item-matricula.txt");

    public static void carregar(
            List<MatriculaSemestral> matriculas,
            List<OfertaDisciplina> ofertas) {

        if (!Files.exists(ARQUIVO)) {
            return;
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

                TipoOpcao tipo =
                        TipoOpcao.valueOf(
                                dados[1]
                        );

                LocalDate data =
                        LocalDate.parse(
                                dados[2]
                        );

                Long matriculaId =
                        Long.parseLong(dados[3]);

                Long ofertaId =
                        Long.parseLong(dados[4]);

                MatriculaSemestral matricula =
                        null;

                OfertaDisciplina oferta =
                        null;

                for (MatriculaSemestral m
                        : matriculas) {

                    if (m.getId()
                            .equals(matriculaId)) {

                        matricula = m;
                        break;
                    }
                }

                for (OfertaDisciplina o
                        : ofertas) {

                    if (o.getId()
                            .equals(ofertaId)) {

                        oferta = o;
                        break;
                    }
                }

                if (matricula == null
                        || oferta == null) {

                    throw new IllegalStateException(
                            "Matrícula ou oferta do item não encontrada."
                    );
                }

                ItemMatricula item =
                        new ItemMatricula(
                                id,
                                tipo,
                                data,
                                matricula,
                                oferta
                        );

                if (!matricula
                        .getItens()
                        .contains(item)) {

                    matricula
                            .getItens()
                            .add(item);
                }

                if (!oferta
                        .getItensMatricula()
                        .contains(item)) {

                    oferta
                            .getItensMatricula()
                            .add(item);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar itens de matrícula.",
                    e
            );
        }
    }

    public static void salvar(
            List<MatriculaSemestral> matriculas) {

        List<String> linhas =
                new ArrayList<>();

        for (MatriculaSemestral matricula
                : matriculas) {

            for (ItemMatricula item
                    : matricula.getItens()) {

                String linha =
                        item.getId() + ";"
                        + item.getTipo() + ";"
                        + item.getDataInclusao() + ";"
                        + matricula.getId() + ";"
                        + item
                                .getOfertaDisciplina()
                                .getId();

                linhas.add(linha);
            }
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
                    "Erro ao salvar itens de matrícula.",
                    e
            );
        }
    }
}
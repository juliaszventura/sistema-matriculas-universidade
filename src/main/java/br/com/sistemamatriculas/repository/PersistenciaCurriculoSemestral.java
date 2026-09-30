package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sistemamatriculas.model.CurriculoSemestral;
import br.com.sistemamatriculas.model.OfertaDisciplina;

public class PersistenciaCurriculoSemestral {

    private static final Path ARQUIVO =
            Path.of("dados", "curriculo.txt");

    public static List<CurriculoSemestral> carregar(
            List<OfertaDisciplina> ofertas) {

        List<CurriculoSemestral> curriculos =
                new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return curriculos;
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

                Date data =
                        new Date(
                                Long.parseLong(dados[1])
                        );

                CurriculoSemestral curriculo =
                        new CurriculoSemestral(
                                id,
                                data
                        );

                if (dados.length > 2
                        && !dados[2].isBlank()) {

                    String[] ids =
                            dados[2].split(",");

                    for (String idOferta : ids) {

                        Long ofertaId =
                                Long.parseLong(
                                        idOferta
                                );

                        for (OfertaDisciplina oferta
                                : ofertas) {

                            if (oferta.getId()
                                    .equals(ofertaId)) {

                                curriculo
                                        .adicionarOferta(
                                                oferta
                                        );

                                break;
                            }
                        }
                    }
                }

                curriculos.add(curriculo);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar currículos.",
                    e
            );
        }

        return curriculos;
    }

    public static void salvar(
            List<CurriculoSemestral> curriculos) {

        List<String> linhas =
                new ArrayList<>();

        for (CurriculoSemestral curriculo
                : curriculos) {

            List<String> idsOfertas =
                    new ArrayList<>();

            for (OfertaDisciplina oferta
                    : curriculo.getOfertas()) {

                idsOfertas.add(
                        String.valueOf(
                                oferta.getId()
                        )
                );
            }

            String linha =
                    curriculo.getId() + ";"
                    + curriculo
                            .getDataGeracao()
                            .getTime()
                    + ";"
                    + String.join(
                            ",",
                            idsOfertas
                    );

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
                    "Erro ao salvar currículos.",
                    e
            );
        }
    }
}
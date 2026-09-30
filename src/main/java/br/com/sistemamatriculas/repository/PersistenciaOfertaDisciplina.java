package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.enums.StatusOfertaDisciplina;
import br.com.sistemamatriculas.model.Disciplina;
import br.com.sistemamatriculas.model.OfertaDisciplina;
import br.com.sistemamatriculas.model.Professor;
import br.com.sistemamatriculas.model.SemestreLetivo;
import br.com.sistemamatriculas.model.Usuario;

public class PersistenciaOfertaDisciplina {

    private static final Path ARQUIVO =
            Path.of("dados", "oferta.txt");

    public static List<OfertaDisciplina> carregar(
            List<Disciplina> disciplinas,
            List<Usuario> usuarios,
            List<SemestreLetivo> semestres) {

        List<OfertaDisciplina> ofertas =
                new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return ofertas;
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

                int codigoDisciplina =
                        Integer.parseInt(dados[1]);

                Long professorId =
                        Long.parseLong(dados[2]);

                int ano =
                        Integer.parseInt(dados[3]);

                int periodo =
                        Integer.parseInt(dados[4]);

                Disciplina disciplina = null;
                Professor professor = null;
                SemestreLetivo semestre = null;

                for (Disciplina d : disciplinas) {
                    if (d.getCodigo()
                            == codigoDisciplina) {
                        disciplina = d;
                        break;
                    }
                }

                for (Usuario usuario : usuarios) {

                    if (usuario instanceof Professor p
                            && p.getId()
                            .equals(professorId)) {

                        professor = p;
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

                if (disciplina == null
                        || professor == null
                        || semestre == null) {

                    throw new IllegalStateException(
                            "Relacionamento da oferta não encontrado."
                    );
                }

                OfertaDisciplina oferta =
                        new OfertaDisciplina(
                                id,
                                disciplina,
                                professor,
                                semestre
                        );

                oferta.setVagasMaximas(
                        Integer.parseInt(dados[5])
                );

                oferta.setMinimoAlunos(
                        Integer.parseInt(dados[6])
                );

                oferta.setStatus(
                        StatusOfertaDisciplina.valueOf(
                                dados[7]
                        )
                );

                semestre.adicionarOferta(oferta);

                ofertas.add(oferta);
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar ofertas.",
                    e
            );
        }

        return ofertas;
    }

    public static void salvar(
            List<OfertaDisciplina> ofertas) {

        List<String> linhas =
                new ArrayList<>();

        for (OfertaDisciplina oferta : ofertas) {

            String linha =
                    oferta.getId() + ";"
                    + oferta.getDisciplina()
                            .getCodigo() + ";"
                    + oferta.getProfessor()
                            .getId() + ";"
                    + oferta.getSemestreLetivo()
                            .getAno() + ";"
                    + oferta.getSemestreLetivo()
                            .getPeriodo() + ";"
                    + oferta.getVagasMaximas() + ";"
                    + oferta.getMinimoAlunos() + ";"
                    + oferta.getStatus();

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
                    "Erro ao salvar ofertas.",
                    e
            );
        }
    }
}
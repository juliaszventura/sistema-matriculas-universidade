package br.com.sistemamatriculas.repository;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import br.com.sistemamatriculas.model.Aluno;
import br.com.sistemamatriculas.model.Professor;
import br.com.sistemamatriculas.model.Secretaria;
import br.com.sistemamatriculas.model.Usuario;

public class PersistenciaUsuario {
    private static final Path ARQUIVO = Path.of("dados", "usuario.txt");

    public static List<Usuario> carregar() {
        List<Usuario> usuarios = new ArrayList<>();

        if (!Files.exists(ARQUIVO)) {
            return usuarios;
        }

        try {

            List<String> linhas = Files.readAllLines(ARQUIVO);

            for (String linha : linhas) {

                if (linha.isBlank()) {
                    continue;
                }

                String[] dados = linha.split(";");

                String tipo = dados[0];

                switch (tipo) {
                    case "ALUNO":
                        Aluno aluno = new Aluno(
                                Long.parseLong(dados[1]),
                                dados[2],
                                dados[3],
                                dados[4],
                                dados[5]);

                        usuarios.add(aluno);
                        break;

                    case "PROFESSOR":
                        Professor professor = new Professor(
                                Long.parseLong(dados[1]),
                                dados[2],
                                dados[3],
                                dados[4],
                                dados[5]);

                        usuarios.add(professor);
                        break;

                    case "SECRETARIA":
                        Secretaria secretaria = new Secretaria(
                                Long.parseLong(dados[1]),
                                dados[2],
                                dados[3],
                                dados[4],
                                dados[5]);

                        usuarios.add(secretaria);
                        break;

                    default:
                        throw new IllegalArgumentException(
                                "Tipo de usuário inválido: " + tipo);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao carregar usuários.", e);

        }

        return usuarios;
    }

    public static void salvar(List<Usuario> usuarios) {

        List<String> linhas = new ArrayList<>();

        for (Usuario usuario : usuarios) {

            String linha;

            if (usuario instanceof Aluno aluno) {

                linha = "ALUNO;"
                        + aluno.getId() + ";"
                        + aluno.getNome() + ";"
                        + aluno.getLogin() + ";"
                        + aluno.getSenha() + ";"
                        + aluno.getMatricula();

            } else if (usuario instanceof Professor professor) {

                linha = "PROFESSOR;"
                        + professor.getId() + ";"
                        + professor.getNome() + ";"
                        + professor.getLogin() + ";"
                        + professor.getSenha() + ";"
                        + professor.getRegistro();

            } else if (usuario instanceof Secretaria secretaria) {

                linha = "SECRETARIA;"
                        + secretaria.getId() + ";"
                        + secretaria.getNome() + ";"
                        + secretaria.getLogin() + ";"
                        + secretaria.getSenha() + ";"
                        + secretaria.getSetor();

            } else {
                continue;
            }

            linhas.add(linha);
        }

        try {

            Files.createDirectories(
                    ARQUIVO.getParent());

            Files.write(
                    ARQUIVO,
                    linhas);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Erro ao salvar usuários.",
                    e);
        }
    }
}

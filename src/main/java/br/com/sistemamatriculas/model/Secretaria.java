package br.com.sistemamatriculas.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.sistemamatriculas.repository.PersistenciaUsuario;

public class Secretaria extends Usuario {

    private String setor;

    private List<Curso> cursos;
    private List<Disciplina> disciplinas;
    private List<Professor> professores;
    private List<Aluno> alunos;

    public Secretaria(
            Long id,
            String nome,
            String login,
            String senha,
            String setor) {

        super(id, nome, login, senha);

        this.setor = setor;

        this.cursos = new ArrayList<>();
        this.disciplinas = new ArrayList<>();
        this.professores = new ArrayList<>();
        this.alunos = new ArrayList<>();
    }

    public CurriculoSemestral gerarCurriculo(SemestreLetivo semestre) {

        if (semestre == null) {
            throw new IllegalArgumentException(
                    "O semestre letivo não pode ser nulo.");
        }

        CurriculoSemestral curriculo = new CurriculoSemestral(
                System.currentTimeMillis(),
                new Date());

        for (OfertaDisciplina oferta : semestre.getOfertas()) {
            curriculo.adicionarOferta(oferta);
        }

        return curriculo;
    }

    public void gerenciarCursos(Curso curso) {

        if (curso == null) {
            throw new IllegalArgumentException(
                    "O curso não pode ser nulo.");
        }

        Path arquivo = Path.of("dados", "cursos.txt");

        try {

            Files.createDirectories(arquivo.getParent());

            List<String> linhas = new ArrayList<>();

            if (Files.exists(arquivo)) {
                linhas.addAll(Files.readAllLines(arquivo));
            }

            String inicioLinha = curso.getCodigo() + ";";

            boolean existe = false;

            for (String linha : linhas) {

                if (linha.startsWith(inicioLinha)) {
                    existe = true;
                    break;
                }
            }

            if (existe) {

                linhas.removeIf(
                        linha -> linha.startsWith(inicioLinha));

                cursos.removeIf(
                        c -> c.getCodigo() == curso.getCodigo());

                System.out.println(
                        "Curso removido com sucesso.");

            } else {

                String linha = curso.getCodigo() + ";"
                        + curso.getNome() + ";"
                        + curso.getNumeroCreditos();

                linhas.add(linha);

                cursos.add(curso);

                System.out.println(
                        "Curso adicionado com sucesso.");
            }

            Files.write(arquivo, linhas);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerenciar cursos.",
                    e);
        }
    }

    public void gerenciarDisciplinas(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina não pode ser nula.");
        }

        Path arquivo = Path.of(
                "dados",
                "disciplinas.txt");

        try {

            Files.createDirectories(
                    arquivo.getParent());

            List<String> linhas = new ArrayList<>();

            if (Files.exists(arquivo)) {
                linhas.addAll(
                        Files.readAllLines(arquivo));
            }

            String inicioLinha = disciplina.getCodigo() + ";";

            boolean existe = false;

            for (String linha : linhas) {

                if (linha.startsWith(inicioLinha)) {
                    existe = true;
                    break;
                }
            }

            if (existe) {

                linhas.removeIf(
                        linha -> linha.startsWith(inicioLinha));

                disciplinas.removeIf(
                        d -> d.getCodigo() == disciplina.getCodigo());

                System.out.println(
                        "Disciplina removida com sucesso.");

            } else {

                String linha = disciplina.getCodigo() + ";"
                        + disciplina.getNome() + ";"
                        + disciplina.getCreditos() + ";"
                        + disciplina.getCargaHoraria();

                linhas.add(linha);

                disciplinas.add(disciplina);

                System.out.println(
                        "Disciplina adicionada com sucesso.");
            }

            Files.write(arquivo, linhas);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Erro ao gerenciar disciplinas.",
                    e);
        }
    }

    public void gerenciarProfessores(
            Professor professor) {

        if (professor == null) {
            throw new IllegalArgumentException(
                    "O professor não pode ser nulo.");
        }

        List<Usuario> usuarios = PersistenciaUsuario.carregar();

        Professor encontrado = null;

        for (Usuario usuario : usuarios) {

            if (usuario instanceof Professor p
                    && p.getRegistro().equals(
                            professor.getRegistro())) {

                encontrado = p;
                break;
            }
        }

        if (encontrado != null) {

            Professor professorRemover = encontrado;

            usuarios.removeIf(
                    usuario -> usuario instanceof Professor p
                            && p.getRegistro().equals(
                                    professorRemover.getRegistro()));

            professores.removeIf(
                    p -> p.getRegistro().equals(
                            professor.getRegistro()));

            System.out.println(
                    "Professor removido com sucesso.");

        } else {

            usuarios.add(professor);

            professores.add(professor);

            System.out.println(
                    "Professor adicionado com sucesso.");
        }

        PersistenciaUsuario.salvar(usuarios);
    }

    public void gerenciarAlunos(Aluno aluno) {

        if (aluno == null) {
            throw new IllegalArgumentException(
                    "O aluno não pode ser nulo.");
        }

        List<Usuario> usuarios = PersistenciaUsuario.carregar();

        boolean encontrado = false;

        for (Usuario usuario : usuarios) {

            if (usuario instanceof Aluno a
                    && a.getMatricula().equals(
                            aluno.getMatricula())) {

                encontrado = true;
                break;
            }
        }

        if (encontrado) {

            usuarios.removeIf(
                    usuario -> usuario instanceof Aluno a
                            && a.getMatricula().equals(
                                    aluno.getMatricula()));

            alunos.removeIf(
                    a -> a.getMatricula().equals(
                            aluno.getMatricula()));

            System.out.println(
                    "Aluno removido com sucesso.");

        } else {

            usuarios.add(aluno);

            if (!alunos.contains(aluno)) {
                alunos.add(aluno);
            }

            System.out.println(
                    "Aluno adicionado com sucesso.");
        }

        PersistenciaUsuario.salvar(usuarios);
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public List<Curso> getCursos() {
        return cursos;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public List<Professor> getProfessores() {
        return professores;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }
}
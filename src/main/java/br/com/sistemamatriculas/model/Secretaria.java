package br.com.sistemamatriculas.model;

import java.time.LocalDate;
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
    private List<OfertaDisciplina> ofertas;

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
        this.ofertas = new ArrayList<>();
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

    public void gerenciarOfertas(OfertaDisciplina oferta) {

        if (oferta == null) {
            throw new IllegalArgumentException(
                    "A oferta nao pode ser nula.");
        }

        boolean existe = false;

        for (OfertaDisciplina o : ofertas) {

            if (o.getId().equals(oferta.getId())) {
                existe = true;
                break;
            }
        }

        if (existe) {

            ofertas.removeIf(
                    o -> o.getId().equals(oferta.getId()));

        } else {

            ofertas.add(oferta);
        }
    }

    public void encerrarPeriodoMatriculas(SemestreLetivo semestre) {

        if (semestre == null) {
            throw new IllegalArgumentException(
                    "O semestre letivo nao pode ser nulo.");
        }

        for (OfertaDisciplina oferta : semestre.getOfertas()) {
            oferta.avaliarAtivacao();
        }

        if (semestre.getPeriodoMatricula() != null) {

            semestre.getPeriodoMatricula()
                    .setDataFim(LocalDate.now().minusDays(1));
        }
    }

    public void gerenciarCursos(Curso curso) {

        if (curso == null) {
            throw new IllegalArgumentException(
                    "O curso nao pode ser nulo.");
        }

        boolean existe = false;

        for (Curso c : cursos) {

            if (c.getCodigo() == curso.getCodigo()) {
                existe = true;
                break;
            }
        }

        if (existe) {

            cursos.removeIf(
                    c -> c.getCodigo() == curso.getCodigo());

        } else {

            cursos.add(curso);
        }
    }

    public void gerenciarDisciplinas(Disciplina disciplina) {

        if (disciplina == null) {
            throw new IllegalArgumentException(
                    "A disciplina nao pode ser nula.");
        }

        boolean existe = false;

        for (Disciplina d : disciplinas) {

            if (d.getCodigo() == disciplina.getCodigo()) {
                existe = true;
                break;
            }
        }

        if (existe) {

            disciplinas.removeIf(
                    d -> d.getCodigo() == disciplina.getCodigo());

            disciplina.setCurso(null);

        } else {

            disciplinas.add(disciplina);
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

    public List<OfertaDisciplina> getOfertas() {
        return ofertas;
    }
}
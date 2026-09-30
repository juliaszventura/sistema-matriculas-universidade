package br.com.sistemamatriculas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.sistemamatriculas.enums.TipoOpcao;
import br.com.sistemamatriculas.model.Aluno;
import br.com.sistemamatriculas.model.CurriculoSemestral;
import br.com.sistemamatriculas.model.Curso;
import br.com.sistemamatriculas.model.Disciplina;
import br.com.sistemamatriculas.model.ItemMatricula;
import br.com.sistemamatriculas.model.MatriculaSemestral;
import br.com.sistemamatriculas.model.OfertaDisciplina;
import br.com.sistemamatriculas.model.PeriodoMatricula;
import br.com.sistemamatriculas.model.Professor;
import br.com.sistemamatriculas.model.Secretaria;
import br.com.sistemamatriculas.model.SemestreLetivo;
import br.com.sistemamatriculas.model.Usuario;
import br.com.sistemamatriculas.repository.PersistenciaCurriculoSemestral;
import br.com.sistemamatriculas.repository.PersistenciaCurso;
import br.com.sistemamatriculas.repository.PersistenciaDisciplina;
import br.com.sistemamatriculas.repository.PersistenciaItemMatricula;
import br.com.sistemamatriculas.repository.PersistenciaMatriculaSemestral;
import br.com.sistemamatriculas.repository.PersistenciaOfertaDisciplina;
import br.com.sistemamatriculas.repository.PersistenciaSemestreLetivo;
import br.com.sistemamatriculas.repository.PersistenciaUsuario;
import br.com.sistemamatriculas.util.ConsoleUI;

public class Main {

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {

        List<Usuario> usuarios = PersistenciaUsuario.carregar();

        List<Disciplina> disciplinas = PersistenciaDisciplina.carregar();

        List<Curso> cursos = PersistenciaCurso.carregar(
                disciplinas);

        List<SemestreLetivo> semestres = PersistenciaSemestreLetivo.carregar();

        List<OfertaDisciplina> ofertas = PersistenciaOfertaDisciplina.carregar(
                disciplinas,
                usuarios,
                semestres);

        List<MatriculaSemestral> matriculas = PersistenciaMatriculaSemestral.carregar(
                usuarios,
                semestres);

        PersistenciaItemMatricula.carregar(
                matriculas,
                ofertas);

        List<CurriculoSemestral> curriculos = PersistenciaCurriculoSemestral.carregar(
                ofertas);

        ConsoleUI.limparTela();
        ConsoleUI.cabecalho("SISTEMA DE MATRÍCULAS — 2026/2");

        Usuario usuarioLogado = null;

        while (usuarioLogado == null) {

            ConsoleUI.menu("Acesso ao Sistema", List.of(
                    "Criar conta",
                    "Fazer login",
                    "Sair"));

            int opcao = lerOpcao();

            switch (opcao) {

                case 1 -> {

                    ConsoleUI.secao("Criar Conta");

                    System.out.println("1 - Aluno");
                    System.out.println("2 - Professor");
                    System.out.println("3 - Secretaria");

                    System.out.print("Tipo de usuário: ");
                    int tipo = lerOpcao();

                    if (tipo < 1 || tipo > 3) {
                        ConsoleUI.erro("Tipo de usuário inválido.");
                        continue;
                    }

                    System.out.print("Nome: ");
                    String nome = SC.nextLine().trim();

                    System.out.print("Login: ");
                    String login = SC.nextLine().trim();

                    boolean loginExistente = false;

                    for (Usuario usuario : usuarios) {
                        if (usuario.getLogin().equals(login)) {
                            loginExistente = true;
                            break;
                        }
                    }

                    if (loginExistente) {
                        ConsoleUI.erro("Esse login já está cadastrado.");
                        continue;
                    }

                    System.out.print("Senha: ");
                    String senha = SC.nextLine().trim();

                    long maiorId = 0;

                    for (Usuario usuario : usuarios) {
                        if (usuario.getId() != null
                                && usuario.getId() > maiorId) {

                            maiorId = usuario.getId();
                        }
                    }

                    Long id = maiorId + 1;

                    Usuario novoUsuario = null;

                    switch (tipo) {

                        case 1 -> {
                            System.out.print("Matrícula: ");
                            String matricula = SC.nextLine().trim();

                            novoUsuario = new Aluno(
                                    id,
                                    nome,
                                    login,
                                    senha,
                                    matricula);
                        }

                        case 2 -> {
                            System.out.print("Registro: ");
                            String registro = SC.nextLine().trim();

                            novoUsuario = new Professor(
                                    id,
                                    nome,
                                    login,
                                    senha,
                                    registro);
                        }

                        case 3 -> {
                            System.out.print("Setor: ");
                            String setor = SC.nextLine().trim();

                            novoUsuario = new Secretaria(
                                    id,
                                    nome,
                                    login,
                                    senha,
                                    setor);
                        }
                    }

                    usuarios.add(novoUsuario);

                    PersistenciaUsuario.salvar(usuarios);

                    ConsoleUI.sucesso("Conta criada com sucesso!");
                }

                case 2 -> {
                    usuarioLogado = autenticar(usuarios);
                }

                case 3 -> {
                    ConsoleUI.info("Sistema encerrado.");
                    return;
                }

                default -> ConsoleUI.erro("Opção inválida.");
            }
        }

        SemestreLetivo semestre;

        if (semestres.isEmpty()) {

            PeriodoMatricula periodo = new PeriodoMatricula(
                    LocalDate.now().minusDays(5),
                    LocalDate.now().plusDays(30));

            semestre = new SemestreLetivo(
                    2026,
                    2,
                    periodo);

            semestres.add(semestre);

            PersistenciaSemestreLetivo.salvar(
                    semestres);

        } else {

            semestre = semestres.get(0);
        }

        ConsoleUI.sucesso(
                "Bem-vindo(a), " + usuarioLogado.getNome() + "!");

        if (usuarioLogado instanceof Aluno) {

            menuAluno((Aluno) usuarioLogado);

        } else if (usuarioLogado instanceof Professor) {

            menuProfessor(
                    (Professor) usuarioLogado,
                    ofertas);

        } else if (usuarioLogado instanceof Secretaria) {

            menuSecretaria(
                    (Secretaria) usuarioLogado,
                    cursos,
                    disciplinas,
                    usuarios,
                    semestre,
                    curriculos);
        }

        ConsoleUI.info("Sistema encerrado. Até logo!");
    }

    private static Usuario autenticar(List<Usuario> usuarios) {
        ConsoleUI.secao("Login");
        System.out.print("Login: ");
        String login = SC.nextLine().trim();
        System.out.print("Senha: ");
        String senha = SC.nextLine().trim();

        for (Usuario usuario : usuarios) {
            if (usuario.autenticar(login, senha)) {
                return usuario;
            }
        }

        ConsoleUI.erro("Login ou senha inválidos. Tente novamente.");
        return null;
    }

    private static void menuAluno(Aluno aluno) {
        boolean continuar = true;

        while (continuar) {
            ConsoleUI.menu("Menu do Aluno", List.of(
                    "Ver disciplinas disponíveis",
                    "Matricular em disciplina",
                    "Cancelar matrícula",
                    "Sair"));

            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> consultarDisciplinas(aluno);
                case 2 -> realizarMatricula(aluno);
                case 3 -> cancelarMatricula(aluno);
                case 4 -> continuar = false;
                default -> ConsoleUI.erro("Opção inválida.");
            }
        }
    }

    private static void consultarDisciplinas(Aluno aluno) {
        List<OfertaDisciplina> disponiveis = aluno.consultarDisciplinasDisponiveis();

        if (disponiveis.isEmpty()) {
            ConsoleUI.aviso("Nenhuma disciplina disponível no momento.");
            return;
        }

        List<List<String>> linhas = new ArrayList<>();
        for (int i = 0; i < disponiveis.size(); i++) {
            OfertaDisciplina o = disponiveis.get(i);
            linhas.add(List.of(
                    String.valueOf(i + 1),
                    o.getDisciplina().getNome(),
                    o.getProfessor().getNome(),
                    o.totalInscritos() + "/" + o.getVagasMaximas(),
                    o.getStatus().toString()));
        }

        ConsoleUI.secao("Disciplinas Disponíveis");
        ConsoleUI.tabela(List.of("#", "Disciplina", "Professor", "Vagas", "Status"), linhas);
    }

    private static void realizarMatricula(Aluno aluno) {
        List<OfertaDisciplina> disponiveis = aluno.consultarDisciplinasDisponiveis();

        if (disponiveis.isEmpty()) {
            ConsoleUI.aviso("Nenhuma disciplina disponível para matrícula.");
            return;
        }

        consultarDisciplinas(aluno);
        System.out.print("Número da disciplina para matricular (0 para cancelar): ");
        int escolha = lerOpcao();

        if (escolha == 0) {
            return;
        }

        if (escolha < 1 || escolha > disponiveis.size()) {
            ConsoleUI.erro("Opção inválida.");
            return;
        }

        OfertaDisciplina oferta = disponiveis.get(escolha - 1);

        System.out.print("Tipo [1] Obrigatória  [2] Optativa: ");
        int tipoEscolhido = lerOpcao();
        TipoOpcao tipo = (tipoEscolhido == 2) ? TipoOpcao.OPTATIVA : TipoOpcao.OBRIGATORIA;

        if (aluno.getMatriculas().isEmpty()) {
            ConsoleUI.erro("Aluno não possui matrícula semestral ativa.");
            return;
        }

        MatriculaSemestral matriculaAtual = aluno.getMatriculas().get(0);

        try {
            matriculaAtual.adicionarItem(oferta, tipo);
            matriculaAtual.notificarCobranca();
            ConsoleUI.sucesso("Matrícula em \"" + oferta.getDisciplina().getNome() + "\" realizada com sucesso!");
        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsoleUI.erro(e.getMessage());
        }
    }

    private static void cancelarMatricula(Aluno aluno) {
        if (aluno.getMatriculas().isEmpty() || aluno.getMatriculas().get(0).getItens().isEmpty()) {
            ConsoleUI.aviso("Você não possui disciplinas matriculadas para cancelar.");
            return;
        }

        List<ItemMatricula> itens = aluno.getMatriculas().get(0).getItens();

        System.out.print("Número da disciplina para cancelar (0 para voltar): ");
        int escolha = lerOpcao();

        if (escolha == 0) {
            return;
        }

        if (escolha < 1 || escolha > itens.size()) {
            ConsoleUI.erro("Opção inválida.");
            return;
        }

        ItemMatricula item = itens.get(escolha - 1);

        try {
            aluno.cancelarMatricula(item);
            ConsoleUI.sucesso("Matrícula cancelada com sucesso!");
        } catch (IllegalArgumentException e) {
            ConsoleUI.erro(e.getMessage());
        }
    }

    private static void menuProfessor(Professor professor, List<OfertaDisciplina> todasOfertas) {
        List<OfertaDisciplina> minhasOfertas = new ArrayList<>();
        for (OfertaDisciplina o : todasOfertas) {
            if (o.getProfessor() == professor) {
                minhasOfertas.add(o);
            }
        }

        boolean continuar = true;
        while (continuar) {
            ConsoleUI.menu("Menu do Professor", List.of(
                    "Ver alunos matriculados em uma disciplina",
                    "Sair"));

            int opcao = lerOpcao();

            if (opcao == 1) {
                if (minhasOfertas.isEmpty()) {
                    ConsoleUI.aviso("Você não leciona nenhuma disciplina cadastrada.");
                    continue;
                }

                List<List<String>> linhas = new ArrayList<>();
                for (int i = 0; i < minhasOfertas.size(); i++) {
                    OfertaDisciplina o = minhasOfertas.get(i);
                    linhas.add(List.of(String.valueOf(i + 1), o.getDisciplina().getNome(), o.getStatus().toString()));
                }
                ConsoleUI.secao("Minhas Disciplinas");
                ConsoleUI.tabela(List.of("#", "Disciplina", "Status"), linhas);

                System.out.print("Número da disciplina: ");
                int escolha = lerOpcao();

                if (escolha < 1 || escolha > minhasOfertas.size()) {
                    ConsoleUI.erro("Opção inválida.");
                    continue;
                }

                List<Aluno> alunos = professor.consultarAlunosMatriculados(minhasOfertas.get(escolha - 1));

                if (alunos.isEmpty()) {
                    ConsoleUI.aviso("Nenhum aluno matriculado nessa disciplina ainda.");
                    continue;
                }

                List<List<String>> linhasAlunos = new ArrayList<>();
                for (Aluno a : alunos) {
                    linhasAlunos.add(List.of(a.getNome(), a.getMatricula()));
                }
                ConsoleUI.secao("Alunos Matriculados");
                ConsoleUI.tabela(List.of("Nome", "Matrícula"), linhasAlunos);

            } else if (opcao == 2) {
                continuar = false;
            } else {
                ConsoleUI.erro("Opção inválida.");
            }
        }
    }

    private static void menuSecretaria(
            Secretaria secretaria,
            List<Curso> cursos,
            List<Disciplina> disciplinas,
            List<Usuario> usuarios,
            SemestreLetivo semestre,
            List<CurriculoSemestral> curriculos) {

        boolean continuar = true;

        while (continuar) {

            ConsoleUI.menu(
                    "Menu da Secretaria",
                    List.of(
                            "Gerar currículo semestral",
                            "Gerenciar cursos",
                            "Gerenciar disciplinas",
                            "Gerenciar professores",
                            "Gerenciar alunos",
                            "Visualizar cadastros",
                            "Sair"));

            int opcao = lerOpcao();

            switch (opcao) {

                case 1 -> {

                    CurriculoSemestral curriculo = secretaria.gerarCurriculo(
                            semestre);

                    ConsoleUI.sucesso(
                            "Currículo semestral gerado com sucesso.");
                }

                case 2 -> {

                    ConsoleUI.menu(
                            "Gerenciar Cursos",
                            List.of(
                                    "Adicionar curso",
                                    "Remover curso",
                                    "Voltar"));

                    int escolha = lerOpcao();

                    if (escolha == 1) {

                        System.out.print("Código: ");
                        int codigo = lerOpcao();

                        System.out.print("Nome: ");
                        String nome = SC.nextLine().trim();

                        System.out.print(
                                "Número de créditos: ");

                        int creditos = lerOpcao();

                        Curso novoCurso = new Curso(
                                codigo,
                                nome,
                                creditos);

                        secretaria.gerenciarCursos(novoCurso);

                        cursos.add(novoCurso);

                        PersistenciaCurso.salvar(cursos);

                    } else if (escolha == 2) {

                        System.out.print(
                                "Código do curso: ");

                        int codigo = lerOpcao();

                        Curso encontrado = null;

                        for (Curso c : cursos) {

                            if (c.getCodigo() == codigo) {
                                encontrado = c;
                                break;
                            }
                        }

                        if (encontrado == null) {

                            ConsoleUI.erro(
                                    "Curso não encontrado.");

                        } else {

                            secretaria.gerenciarCursos(encontrado);

                            cursos.remove(encontrado);

                            PersistenciaCurso.salvar(cursos);
                        }
                    }
                }

                case 3 -> {

                    ConsoleUI.menu(
                            "Gerenciar Disciplinas",
                            List.of(
                                    "Adicionar disciplina",
                                    "Remover disciplina",
                                    "Voltar"));

                    int escolha = lerOpcao();

                    if (escolha == 1) {

                        System.out.print("Código: ");
                        int codigo = lerOpcao();

                        System.out.print("Nome: ");
                        String nome = SC.nextLine().trim();

                        System.out.print("Créditos: ");
                        int creditos = lerOpcao();

                        System.out.print(
                                "Carga horária: ");

                        int cargaHoraria = lerOpcao();

                        Disciplina novaDisciplina = new Disciplina(
                                codigo,
                                nome,
                                creditos,
                                cargaHoraria);

                        secretaria
                                .gerenciarDisciplinas(
                                        novaDisciplina);

                        disciplinas.add(
                                novaDisciplina);

                    } else if (escolha == 2) {

                        System.out.print(
                                "Código da disciplina: ");

                        int codigo = lerOpcao();

                        Disciplina encontrada = null;

                        for (Disciplina d : disciplinas) {

                            if (d.getCodigo() == codigo) {

                                encontrada = d;
                                break;
                            }
                        }

                        if (encontrada == null) {

                            ConsoleUI.erro(
                                    "Disciplina não encontrada.");

                        } else {

                            secretaria
                                    .gerenciarDisciplinas(
                                            encontrada);

                            disciplinas.remove(
                                    encontrada);
                        }
                    }
                }

                case 4 -> {

                    ConsoleUI.menu(
                            "Gerenciar Professores",
                            List.of(
                                    "Adicionar professor",
                                    "Remover professor",
                                    "Voltar"));

                    int escolha = lerOpcao();

                    if (escolha == 1) {

                        System.out.print("Nome: ");
                        String nome = SC.nextLine().trim();

                        System.out.print("Login: ");
                        String login = SC.nextLine().trim();

                        System.out.print("Senha: ");
                        String senha = SC.nextLine().trim();

                        System.out.print("Registro: ");
                        String registro = SC.nextLine().trim();

                        long maiorId = 0;

                        for (Usuario usuario : usuarios) {

                            if (usuario.getId() > maiorId) {

                                maiorId = usuario.getId();
                            }
                        }

                        Professor professor = new Professor(
                                maiorId + 1,
                                nome,
                                login,
                                senha,
                                registro);

                        secretaria
                                .gerenciarProfessores(
                                        professor);

                        usuarios.clear();

                        usuarios.addAll(
                                PersistenciaUsuario
                                        .carregar());

                    } else if (escolha == 2) {

                        System.out.print(
                                "Registro do professor: ");

                        String registro = SC.nextLine().trim();

                        Professor encontrado = null;

                        for (Usuario usuario : usuarios) {

                            if (usuario instanceof Professor p
                                    && p.getRegistro()
                                            .equals(registro)) {

                                encontrado = p;
                                break;
                            }
                        }

                        if (encontrado == null) {

                            ConsoleUI.erro(
                                    "Professor não encontrado.");

                        } else {

                            secretaria
                                    .gerenciarProfessores(
                                            encontrado);

                            usuarios.clear();

                            usuarios.addAll(
                                    PersistenciaUsuario
                                            .carregar());
                        }
                    }
                }

                case 5 -> {

                    ConsoleUI.menu(
                            "Gerenciar Alunos",
                            List.of(
                                    "Adicionar aluno",
                                    "Remover aluno",
                                    "Voltar"));

                    int escolha = lerOpcao();

                    if (escolha == 1) {

                        System.out.print("Nome: ");
                        String nome = SC.nextLine().trim();

                        System.out.print("Login: ");
                        String login = SC.nextLine().trim();

                        System.out.print("Senha: ");
                        String senha = SC.nextLine().trim();

                        System.out.print("Matrícula: ");
                        String matricula = SC.nextLine().trim();

                        long maiorId = 0;

                        for (Usuario usuario : usuarios) {

                            if (usuario.getId() > maiorId) {

                                maiorId = usuario.getId();
                            }
                        }

                        Aluno aluno = new Aluno(
                                maiorId + 1,
                                nome,
                                login,
                                senha,
                                matricula);

                        secretaria
                                .gerenciarAlunos(
                                        aluno);

                        usuarios.clear();

                        usuarios.addAll(
                                PersistenciaUsuario
                                        .carregar());

                    } else if (escolha == 2) {

                        System.out.print(
                                "Matrícula do aluno: ");

                        String matricula = SC.nextLine().trim();

                        Aluno encontrado = null;

                        for (Usuario usuario : usuarios) {

                            if (usuario instanceof Aluno a
                                    && a.getMatricula()
                                            .equals(matricula)) {

                                encontrado = a;
                                break;
                            }
                        }

                        if (encontrado == null) {

                            ConsoleUI.erro(
                                    "Aluno não encontrado.");

                        } else {

                            secretaria
                                    .gerenciarAlunos(
                                            encontrado);

                            usuarios.clear();

                            usuarios.addAll(
                                    PersistenciaUsuario
                                            .carregar());
                        }
                    }
                }

                case 6 -> {

                    ConsoleUI.menu(
                            "Visualizar Cadastros",
                            List.of(
                                    "Professores",
                                    "Alunos",
                                    "Cursos",
                                    "Disciplinas",
                                    "Voltar"));

                    int escolha = lerOpcao();

                    switch (escolha) {

                        case 1 -> visualizarProfessores(usuarios);

                        case 2 -> visualizarAlunos(usuarios);

                        case 3 -> visualizarCursos(cursos);

                        case 4 -> visualizarDisciplinas(disciplinas);

                        case 5 -> {
                            // apenas volta para o menu anterior
                        }

                        default -> ConsoleUI.erro("Opção inválida.");
                    }
                }

                case 7 -> continuar = false;

                default ->
                    ConsoleUI.erro(
                            "Opção inválida.");
            }
        }
    }

    private static int lerOpcao() {
        try {
            return Integer.parseInt(SC.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void visualizarProfessores(List<Usuario> usuarios) {

        List<List<String>> linhas = new ArrayList<>();

        for (Usuario usuario : usuarios) {

            if (usuario instanceof Professor professor) {

                linhas.add(List.of(
                        String.valueOf(professor.getId()),
                        professor.getNome(),
                        professor.getLogin(),
                        professor.getRegistro()));
            }
        }

        if (linhas.isEmpty()) {
            ConsoleUI.aviso("Nenhum professor cadastrado.");
            return;
        }

        ConsoleUI.secao("Professores Cadastrados");

        ConsoleUI.tabela(
                List.of(
                        "ID",
                        "Nome",
                        "Login",
                        "Registro"),
                linhas);
    }

    private static void visualizarAlunos(List<Usuario> usuarios) {

        List<List<String>> linhas = new ArrayList<>();

        for (Usuario usuario : usuarios) {

            if (usuario instanceof Aluno aluno) {

                linhas.add(List.of(
                        String.valueOf(aluno.getId()),
                        aluno.getNome(),
                        aluno.getLogin(),
                        aluno.getMatricula()));
            }
        }

        if (linhas.isEmpty()) {
            ConsoleUI.aviso("Nenhum aluno cadastrado.");
            return;
        }

        ConsoleUI.secao("Alunos Cadastrados");

        ConsoleUI.tabela(
                List.of(
                        "ID",
                        "Nome",
                        "Login",
                        "Matrícula"),
                linhas);
    }

    private static void visualizarCursos(
            List<Curso> cursos) {

        if (cursos.isEmpty()) {
            ConsoleUI.aviso(
                    "Nenhum curso cadastrado.");
            return;
        }

        List<List<String>> linhas = new ArrayList<>();

        for (Curso curso : cursos) {

            linhas.add(List.of(
                    String.valueOf(curso.getCodigo()),
                    curso.getNome(),
                    String.valueOf(
                            curso.getNumeroCreditos())));
        }

        ConsoleUI.secao(
                "Cursos Cadastrados");

        ConsoleUI.tabela(
                List.of(
                        "Código",
                        "Nome",
                        "Créditos"),
                linhas);
    }

    private static void visualizarDisciplinas(List<Disciplina> disciplinas) {

        if (disciplinas.isEmpty()) {
            ConsoleUI.aviso("Nenhuma disciplina cadastrada.");
            return;
        }

        List<List<String>> linhas = new ArrayList<>();

        for (Disciplina disciplina : disciplinas) {

            linhas.add(List.of(
                    String.valueOf(disciplina.getCodigo()),
                    disciplina.getNome(),
                    String.valueOf(disciplina.getCreditos()),
                    String.valueOf(disciplina.getCargaHoraria())));
        }

        ConsoleUI.secao("Disciplinas Cadastradas");

        ConsoleUI.tabela(
                List.of(
                        "Código",
                        "Nome",
                        "Créditos",
                        "Carga Horária"),
                linhas);
    }
}
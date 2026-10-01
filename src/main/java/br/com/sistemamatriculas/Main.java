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

        List<Curso> cursos = PersistenciaCurso.carregar();

        List<Disciplina> disciplinas = PersistenciaDisciplina.carregar(
                cursos);

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

        if (usuarioLogado instanceof Aluno aluno) {

            MatriculaSemestral matriculaDoSemestre = obterOuCriarMatricula(
                    aluno,
                    semestre,
                    matriculas);

            menuAluno(
                    aluno,
                    matriculaDoSemestre,
                    matriculas);

        } else if (usuarioLogado instanceof Professor professor) {

            menuProfessor(
                    professor,
                    ofertas);

        } else if (usuarioLogado instanceof Secretaria secretariaLogada) {

            menuSecretaria(
                    secretariaLogada,
                    cursos,
                    disciplinas,
                    usuarios,
                    semestre,
                    semestres,
                    ofertas,
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

    private static MatriculaSemestral obterOuCriarMatricula(
            Aluno aluno,
            SemestreLetivo semestre,
            List<MatriculaSemestral> matriculas) {

        for (MatriculaSemestral matricula : aluno.getMatriculas()) {

            SemestreLetivo semestreDaMatricula =
                    matricula.getSemestreLetivo();

            if (semestreDaMatricula != null
                    && semestreDaMatricula.getAno() == semestre.getAno()
                    && semestreDaMatricula.getPeriodo() == semestre.getPeriodo()) {

                return matricula;
            }
        }

        long maiorId = 0;

        for (MatriculaSemestral matricula : matriculas) {

            if (matricula.getId() != null
                    && matricula.getId() > maiorId) {

                maiorId = matricula.getId();
            }
        }

        MatriculaSemestral nova = new MatriculaSemestral(
                maiorId + 1,
                LocalDate.now(),
                semestre);

        aluno.realizarMatricula(nova);

        matriculas.add(nova);

        PersistenciaMatriculaSemestral.salvar(matriculas);

        return nova;
    }

    private static void menuAluno(
            Aluno aluno,
            MatriculaSemestral matricula,
            List<MatriculaSemestral> matriculas) {

        boolean continuar = true;

        while (continuar) {

            ConsoleUI.menu("Menu do Aluno", List.of(
                    "Ver disciplinas disponíveis",
                    "Matricular em disciplina",
                    "Cancelar matrícula",
                    "Ver minhas disciplinas",
                    "Sair"));

            int opcao = lerOpcao();

            switch (opcao) {
                case 1 -> consultarDisciplinas(aluno);
                case 2 -> realizarMatricula(aluno, matricula, matriculas);
                case 3 -> cancelarMatricula(matricula, matriculas);
                case 4 -> visualizarMinhasDisciplinas(matricula);
                case 5 -> continuar = false;
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
                    o.getDisciplina().getTipo().toString(),
                    o.getProfessor().getNome(),
                    o.totalInscritos() + "/" + o.getVagasMaximas(),
                    o.getStatus().toString()));
        }

        ConsoleUI.secao("Disciplinas Disponíveis");
        ConsoleUI.tabela(
                List.of("#", "Disciplina", "Tipo", "Professor", "Vagas", "Status"),
                linhas);
    }

    private static void visualizarMinhasDisciplinas(
            MatriculaSemestral matricula) {

        List<ItemMatricula> itens = matricula.getItens();

        if (itens.isEmpty()) {
            ConsoleUI.aviso(
                    "Você ainda não está matriculado em nenhuma disciplina.");
            return;
        }

        List<List<String>> linhas = new ArrayList<>();

        for (int i = 0; i < itens.size(); i++) {

            ItemMatricula item = itens.get(i);

            linhas.add(List.of(
                    String.valueOf(i + 1),
                    item.getOfertaDisciplina().getDisciplina().getNome(),
                    item.getTipo().toString(),
                    item.getOfertaDisciplina().getProfessor().getNome(),
                    item.getDataInclusao().toString()));
        }

        ConsoleUI.secao(
                "Minhas Disciplinas — "
                        + matricula.getSemestreLetivo().descricao());

        ConsoleUI.tabela(
                List.of("#", "Disciplina", "Tipo", "Professor", "Inclusão"),
                linhas);
    }

    private static void realizarMatricula(
            Aluno aluno,
            MatriculaSemestral matricula,
            List<MatriculaSemestral> matriculas) {

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

        try {
            matricula.adicionarItem(oferta);

            PersistenciaMatriculaSemestral.salvar(matriculas);
            PersistenciaItemMatricula.salvar(matriculas);

            ConsoleUI.sucesso(
                    "Matrícula em \""
                            + oferta.getDisciplina().getNome()
                            + "\" ("
                            + oferta.getDisciplina().getTipo()
                            + ") realizada com sucesso!");

        } catch (IllegalArgumentException | IllegalStateException e) {
            ConsoleUI.erro(e.getMessage());
        }
    }

    private static void cancelarMatricula(
            MatriculaSemestral matricula,
            List<MatriculaSemestral> matriculas) {

        List<ItemMatricula> itens = matricula.getItens();

        if (itens.isEmpty()) {
            ConsoleUI.aviso("Você não possui disciplinas matriculadas para cancelar.");
            return;
        }

        visualizarMinhasDisciplinas(matricula);

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
            matricula.cancelarItem(item);

            PersistenciaItemMatricula.salvar(matriculas);

            ConsoleUI.sucesso("Matrícula cancelada com sucesso!");

        } catch (IllegalArgumentException | IllegalStateException e) {
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
            List<SemestreLetivo> semestres,
            List<OfertaDisciplina> ofertas,
            List<CurriculoSemestral> curriculos) {

        boolean continuar = true;

        while (continuar) {

            ConsoleUI.menu(
                    "Menu da Secretaria",
                    List.of(
                            "Gerar currículo semestral",
                            "Gerenciar cursos",
                            "Gerenciar disciplinas",
                            "Gerenciar ofertas de disciplina",
                            "Gerenciar professores",
                            "Gerenciar alunos",
                            "Encerrar período de matrículas",
                            "Visualizar cadastros",
                            "Sair"));

            int opcao = lerOpcao();

            switch (opcao) {

                case 1 -> {

                    CurriculoSemestral curriculo = secretaria.gerarCurriculo(
                            semestre);

                    curriculos.add(curriculo);

                    PersistenciaCurriculoSemestral.salvar(
                            curriculos);

                    ConsoleUI.sucesso(
                            "Currículo de "
                                    + semestre.descricao()
                                    + " gerado com "
                                    + curriculo.getOfertas().size()
                                    + " oferta(s).");
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
                                    "Alterar tipo (obrigatória/optativa)",
                                    "Voltar"));

                    int escolha = lerOpcao();

                    if (escolha == 1) {

                        if (cursos.isEmpty()) {

                            ConsoleUI.erro(
                                    "Cadastre um curso antes de cadastrar disciplinas.");

                            continue;
                        }

                        System.out.print("Código: ");
                        int codigo = lerOpcao();

                        boolean codigoEmUso = false;

                        for (Disciplina d : disciplinas) {

                            if (d.getCodigo() == codigo) {
                                codigoEmUso = true;
                                break;
                            }
                        }

                        if (codigoEmUso) {

                            ConsoleUI.erro(
                                    "Já existe uma disciplina com esse código.");

                            continue;
                        }

                        System.out.print("Nome: ");
                        String nome = SC.nextLine().trim();

                        System.out.print("Créditos: ");
                        int creditos = lerOpcao();

                        System.out.print(
                                "Carga horária: ");

                        int cargaHoraria = lerOpcao();

                        System.out.print(
                                "Tipo [1] Obrigatória  [2] Optativa: ");

                        int tipoEscolhido = lerOpcao();

                        if (tipoEscolhido != 1 && tipoEscolhido != 2) {

                            ConsoleUI.erro(
                                    "Tipo inválido. Disciplina não cadastrada.");

                            continue;
                        }

                        TipoOpcao tipoDisciplina =
                                tipoEscolhido == 2
                                        ? TipoOpcao.OPTATIVA
                                        : TipoOpcao.OBRIGATORIA;

                        visualizarCursos(cursos);

                        System.out.print(
                                "Código do curso ao qual a disciplina pertence: ");

                        int codigoCurso = lerOpcao();

                        Curso curso = null;

                        for (Curso c : cursos) {

                            if (c.getCodigo() == codigoCurso) {
                                curso = c;
                                break;
                            }
                        }

                        if (curso == null) {

                            ConsoleUI.erro(
                                    "Curso não encontrado. Disciplina não cadastrada.");

                            continue;
                        }

                        Disciplina novaDisciplina = new Disciplina(
                                codigo,
                                nome,
                                creditos,
                                cargaHoraria,
                                curso,
                                tipoDisciplina);

                        secretaria
                                .gerenciarDisciplinas(
                                        novaDisciplina);

                        disciplinas.add(
                                novaDisciplina);

                        PersistenciaDisciplina.salvar(
                                disciplinas);

                        ConsoleUI.sucesso(
                                "Disciplina "
                                        + tipoDisciplina
                                        + " vinculada ao curso \""
                                        + curso.getNome()
                                        + "\".");

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

                            encontrada.setCurso(null);

                            disciplinas.remove(
                                    encontrada);

                            PersistenciaDisciplina.salvar(
                                    disciplinas);

                            ConsoleUI.sucesso(
                                    "Disciplina removida.");
                        }

                    } else if (escolha == 3) {

                        visualizarDisciplinas(disciplinas);

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

                            continue;
                        }

                        System.out.print(
                                "Novo tipo [1] Obrigatória  [2] Optativa: ");

                        int novoTipo = lerOpcao();

                        if (novoTipo != 1 && novoTipo != 2) {

                            ConsoleUI.erro("Tipo inválido.");
                            continue;
                        }

                        encontrada.setTipo(
                                novoTipo == 2
                                        ? TipoOpcao.OPTATIVA
                                        : TipoOpcao.OBRIGATORIA);

                        PersistenciaDisciplina.salvar(disciplinas);

                        ConsoleUI.sucesso(
                                "Disciplina \""
                                        + encontrada.getNome()
                                        + "\" agora é "
                                        + encontrada.getTipo()
                                        + ".");
                    }
                }

                case 4 -> {

                    ConsoleUI.menu(
                            "Gerenciar Ofertas de Disciplina",
                            List.of(
                                    "Adicionar oferta",
                                    "Remover oferta",
                                    "Listar ofertas",
                                    "Voltar"));

                    int escolha = lerOpcao();

                    if (escolha == 1) {

                        if (disciplinas.isEmpty()) {

                            ConsoleUI.erro(
                                    "Cadastre uma disciplina antes de criar uma oferta.");

                            continue;
                        }

                        boolean temProfessor = false;

                        for (Usuario usuario : usuarios) {

                            if (usuario instanceof Professor) {
                                temProfessor = true;
                                break;
                            }
                        }

                        if (!temProfessor) {

                            ConsoleUI.erro(
                                    "Cadastre um professor antes de criar uma oferta.");

                            continue;
                        }

                        visualizarDisciplinas(disciplinas);

                        System.out.print("Código da disciplina: ");
                        int codigoDisciplina = lerOpcao();

                        Disciplina disciplina = null;

                        for (Disciplina d : disciplinas) {

                            if (d.getCodigo() == codigoDisciplina) {
                                disciplina = d;
                                break;
                            }
                        }

                        if (disciplina == null) {

                            ConsoleUI.erro("Disciplina não encontrada.");
                            continue;
                        }

                        visualizarProfessores(usuarios);

                        System.out.print("ID do professor: ");
                        int idProfessor = lerOpcao();

                        Professor professor = null;

                        for (Usuario usuario : usuarios) {

                            if (usuario instanceof Professor p
                                    && p.getId() != null
                                    && p.getId() == idProfessor) {

                                professor = p;
                                break;
                            }
                        }

                        if (professor == null) {

                            ConsoleUI.erro("Professor não encontrado.");
                            continue;
                        }

                        long maiorIdOferta = 0;

                        for (OfertaDisciplina o : ofertas) {

                            if (o.getId() != null
                                    && o.getId() > maiorIdOferta) {

                                maiorIdOferta = o.getId();
                            }
                        }

                        OfertaDisciplina novaOferta = new OfertaDisciplina(
                                maiorIdOferta + 1,
                                disciplina,
                                professor,
                                semestre);

                        semestre.adicionarOferta(novaOferta);

                        secretaria.gerenciarOfertas(novaOferta);

                        ofertas.add(novaOferta);

                        PersistenciaOfertaDisciplina.salvar(ofertas);

                        ConsoleUI.sucesso(
                                "Oferta criada: "
                                        + disciplina.getNome()
                                        + " com "
                                        + professor.getNome()
                                        + " em "
                                        + semestre.descricao()
                                        + ".");

                    } else if (escolha == 2) {

                        listarOfertas(ofertas);

                        System.out.print("ID da oferta a remover: ");
                        int idOferta = lerOpcao();

                        OfertaDisciplina encontrada = null;

                        for (OfertaDisciplina o : ofertas) {

                            if (o.getId() != null
                                    && o.getId() == idOferta) {

                                encontrada = o;
                                break;
                            }
                        }

                        if (encontrada == null) {

                            ConsoleUI.erro("Oferta não encontrada.");

                        } else if (encontrada.totalInscritos() > 0) {

                            ConsoleUI.erro(
                                    "Não é possível remover: já existem alunos matriculados.");

                        } else {

                            encontrada.getSemestreLetivo()
                                    .removerOferta(encontrada);

                            secretaria.gerenciarOfertas(encontrada);

                            ofertas.remove(encontrada);

                            PersistenciaOfertaDisciplina.salvar(ofertas);

                            ConsoleUI.sucesso("Oferta removida.");
                        }

                    } else if (escolha == 3) {

                        listarOfertas(ofertas);
                    }
                }

                case 5 -> {

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

                case 6 -> {

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

                case 7 -> {

                    if (semestre.getOfertas().isEmpty()) {

                        ConsoleUI.aviso(
                                "Não há ofertas neste semestre para avaliar.");

                        continue;
                    }

                    secretaria.encerrarPeriodoMatriculas(semestre);

                    PersistenciaOfertaDisciplina.salvar(ofertas);
                    PersistenciaSemestreLetivo.salvar(semestres);

                    List<List<String>> linhas = new ArrayList<>();

                    for (OfertaDisciplina oferta : semestre.getOfertas()) {

                        linhas.add(List.of(
                                String.valueOf(oferta.getId()),
                                oferta.getDisciplina().getNome(),
                                oferta.totalInscritos()
                                        + "/"
                                        + oferta.getMinimoAlunos(),
                                oferta.getStatus().toString()));
                    }

                    ConsoleUI.secao(
                            "Encerramento do Período — "
                                    + semestre.descricao());

                    ConsoleUI.tabela(
                            List.of(
                                    "ID",
                                    "Disciplina",
                                    "Inscritos/Mínimo",
                                    "Status"),
                            linhas);

                    ConsoleUI.info(
                            "Ofertas com menos que o mínimo de alunos foram canceladas.");
                }

                case 8 -> {

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

                case 9 -> continuar = false;

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

    private static void listarOfertas(
            List<OfertaDisciplina> ofertas) {

        if (ofertas.isEmpty()) {
            ConsoleUI.aviso("Nenhuma oferta cadastrada.");
            return;
        }

        List<List<String>> linhas = new ArrayList<>();

        for (OfertaDisciplina oferta : ofertas) {

            linhas.add(List.of(
                    String.valueOf(oferta.getId()),
                    oferta.getDisciplina().getNome(),
                    oferta.getProfessor().getNome(),
                    oferta.getSemestreLetivo().descricao(),
                    oferta.totalInscritos() + "/" + oferta.getVagasMaximas(),
                    oferta.getStatus().toString()));
        }

        ConsoleUI.secao("Ofertas de Disciplina");

        ConsoleUI.tabela(
                List.of(
                        "ID",
                        "Disciplina",
                        "Professor",
                        "Semestre",
                        "Inscritos",
                        "Status"),
                linhas);
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
                            curso.getNumeroCreditos()),
                    String.valueOf(
                            curso.getDisciplinas().size())));
        }

        ConsoleUI.secao(
                "Cursos Cadastrados");

        ConsoleUI.tabela(
                List.of(
                        "Código",
                        "Nome",
                        "Créditos",
                        "Disciplinas"),
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
                    String.valueOf(disciplina.getCargaHoraria()),
                    disciplina.getTipo().toString(),
                    disciplina.getCurso() != null
                            ? disciplina.getCurso().getNome()
                            : "-"));
        }

        ConsoleUI.secao("Disciplinas Cadastradas");

        ConsoleUI.tabela(
                List.of(
                        "Código",
                        "Nome",
                        "Créditos",
                        "Carga Horária",
                        "Tipo",
                        "Curso"),
                linhas);
    }
}
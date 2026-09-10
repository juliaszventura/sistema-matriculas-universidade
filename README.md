# 🎓 Sistema de Matrículas Universitárias
 
Sistema para informatizar o processo de matrículas de uma universidade: gestão de currículos, disciplinas e cursos pela secretaria, matrícula e cancelamento de disciplinas pelos alunos, e consulta de turmas pelos professores.
 
> 📚 Projeto desenvolvido para a disciplina **Laboratório de Desenvolvimento de Software** — PUC Minas, 4º período/2026
> 
> 👩‍🏫 Profa. Milena Menezes Adão
 
---
 
## 📌 Sumário
 
- [📖 Sobre](#-sobre)
- [🎯 Objetivo](#-objetivo)
- [👥 Atores](#-atores)
- [📝 Histórias de Usuário](#-histórias-de-usuário)
- [⚙️ Regras de Negócio](#️-regras-de-negócio)
- [🗺️ Diagrama de Caso de Uso](#️-diagrama-de-caso-de-uso)
- [🛠️ Como executar](#️-como-executar)
- [🚀 Status do Projeto](#-status-do-projeto)
---
 
## 📖 Sobre
 
O **Sistema de Matrículas Universitárias** foi idealizado para automatizar o processo de matrícula em disciplinas de uma universidade, hoje feito manualmente pela secretaria. Ele conecta três perfis de usuário: secretaria, aluno e professor, e se integra com o sistema de cobranças para notificar matrículas realizadas em cada semestre.
 
## 🎯 Objetivo
 
Fornecer uma solução centralizada que permita:
 
- ✅ Cadastrar cursos, disciplinas e professores
- ✅ Controlar períodos de matrícula por semestre
- ✅ Permitir que alunos se matriculem e cancelem disciplinas
- ✅ Aplicar automaticamente as regras de ativação/cancelamento de turmas
- ✅ Notificar o sistema de cobranças a cada matrícula
- ✅ Permitir que professores consultem suas turmas
## 👥 Atores
 
| Ator | Descrição |
|---|---|
| 🏫 **Secretaria** | Cadastra cursos, disciplinas e professores; gera o currículo do semestre |
| 🎒 **Aluno** | Realiza login, matrícula e cancelamento de disciplinas |
| 👩‍🏫 **Professor** | Realiza login e consulta os alunos matriculados em suas disciplinas |
| 💰 **Sistema de Cobranças** *(externo)* | Recebe notificações para cobrar os alunos matriculados |
 
## 📝 Histórias de Usuário
 
### 🏫 Secretaria
 
- Como secretaria, quero cadastrar um curso com nome e número de créditos, para que ele fique disponível no sistema.
- Como secretaria, quero cadastrar disciplinas vinculadas a um curso, para compor o currículo de cada semestre.
- Como secretaria, quero cadastrar professores no sistema, para vinculá-los às disciplinas que lecionam.
- Como secretaria, quero definir o período de matrículas de cada semestre, para controlar quando alunos podem se inscrever ou cancelar disciplinas.
### 🎒 Aluno
 
- Como aluno, quero fazer login no sistema, para acessar minhas funcionalidades de matrícula.
- Como aluno, quero me matricular em até 4 disciplinas obrigatórias (1ª opção), para cumprir meu currículo do semestre.
- Como aluno, quero me matricular em até 2 disciplinas optativas (2ª opção), para complementar minha formação.
- Como aluno, quero cancelar uma matrícula feita anteriormente, dentro do período de matrículas, caso eu mude de ideia.
- Como aluno, quero visualizar as disciplinas em que estou matriculado, para acompanhar minha situação no semestre.
### 👩‍🏫 Professor
 
- Como professor, quero fazer login no sistema, para acessar minhas funcionalidades.
- Como professor, quero visualizar a lista de alunos matriculados em cada disciplina que leciono, para me preparar para o semestre.
### 💰 Sistema de Cobranças *(integração)*
 
- Como sistema de matrículas, quero notificar o sistema de cobranças sempre que um aluno se inscrever em disciplinas de um semestre, para que ele possa ser cobrado corretamente.
## ⚙️ Regras de Negócio
 
- 🔒 Uma disciplina só fica ativa no semestre seguinte se tiver **no mínimo 3 alunos** inscritos ao final do período de matrículas; caso contrário, é cancelada.
- 🚫 O número máximo de alunos por disciplina é **60**; ao atingir esse limite, as inscrições são encerradas automaticamente.
- 📩 Toda matrícula realizada dispara uma notificação ao sistema de cobranças.
- 🔑 Todos os usuários (aluno, professor, secretaria) possuem login e senha para acesso.
## 🗺️ Diagrama de Caso de Uso
 
> _(inserir o diagrama)_
 
## 🛠️ Como executar
 
> _(a preencher)_
 
## 🚀 Status do Projeto
 
- [x] Lab01S01 — Diagrama de Caso de Uso + Histórias de Usuário
- [ ] Lab01S02 — Diagrama de Classes + stub do projeto Java
- [ ] Lab01S03 — Protótipo funcional (interface + persistência)

## ✍️ Autores
 
- Júlia de Souza Ventura
- Yuri Cardoso Viana

---
 
<p align="center">Desenvolvido para a disciplina de Laboratório de Desenvolvimento de Software — PUC Minas 💜</p>
 

# Sistema de Monitoramento de Ocorrências e Bem-Estar 

## 📌 Sobre o Projeto
Sistema web desenvolvido para o mapeamento e cadastro de ocorrências relacionadas à ansiedade e bem-estar, focado no atendimento da comunidade acadêmica (servidores, alunos e terceirizados) do IFRN Campus Lajes. 

O projeto foi planejado e entregue antes do prazo estipulado, demonstrando não apenas domínio técnico, mas também comprometimento com cronogramas e resolução de problemas reais de saúde institucional.

## 🚀 Tecnologias e Ferramentas Utilizadas
* **Back-End:** Java, Spring Boot
* **Persistência de Dados:** Hibernate, JPA, MySQL
* **Front-End:** HTML, CSS (com foco em usabilidade e design responsivo)
* **Validação:** Bean Validation

## ⚙️ Funcionalidades Principais
* Cadastro seguro e estruturado de usuários (alunos, servidores e terceirizados).
* Registro e histórico de ocorrências.
* Interface amigável para facilitar o relato e acompanhamento.
* Mapeamento objeto-relacional estruturado com JPA/Hibernate.

## 🛠️ Como executar este projeto
1. Clone este repositório: `git clone github.com/victorkaell/ocorrencias`
2. Configure as credenciais do seu banco de dados MySQL no arquivo `application.properties`.
3. Execute a classe principal do Spring Boot na sua IDE (Eclipse, IntelliJ, etc) ou via Maven.
4. Acesse a aplicação no navegador via `http://localhost:8080`.

### 🔑 Acesso e Testes (Dados Pré-carregados)

Para facilitar a avaliação do sistema, o banco de dados é inicializado automaticamente com categorias e um usuário de teste. Você pode testar o fluxo de registro utilizando as seguintes credenciais:

* **Matrícula:** 1234
* **Senha:** 1234
    
> **Nota de Arquitetura:** O escopo principal deste projeto foi consolidar os fundamentos do mapeamento objeto-relacional com Hibernate e a **validação estrita de dados** no Back-End. As regras de negócio e a integridade das informações foram implementadas para garantir que o CRUD de ocorrências opere com consistência, servindo como uma base sólida para futuras implementações de camadas de segurança.

### 🚧 Roadmap e Trabalhos Futuros

O sistema atual representa o MVP (Produto Mínimo Viável) focado no registro seguro de ocorrências e controle de acesso de usuários. 

**Próximas implementações mapeadas (v2):**
* **Módulo de Relatórios:** A interface da aba "Relatórios" já está prototipada no Front-End, e o desenvolvimento do Back-End para geração de métricas e exportação de dados está mapeado para o próximo ciclo de evolução da aplicação.

### 🛠️ API REST & Documentação

Além da interface Web, o sistema dispõe de uma API RESTful para integração com outros serviços.

* **Swagger UI (Documentação Interativa):** Com a aplicação rodando, acesse `http://localhost:8080/swagger-ui/index.html` para visualizar e testar os endpoints em tempo real.
* **Postman:** Na pasta `/docs` deste repositório, você encontrará o arquivo `Ocorrencias_API_Collection.json`. Basta importá-lo no Postman para ter acesso a todos os testes de GET, POST, PUT e DELETE configurados.

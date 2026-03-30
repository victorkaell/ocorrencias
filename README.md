# 🚀 [Refatoração 2026] API RESTful - Sistema de Monitoramento de Ocorrências e Bem-Estar

## 📌 Sobre o Projeto e a Evolução Arquitetural
Este sistema nasceu em 2023 como um MVP para o mapeamento e cadastro de ocorrências relacionadas à ansiedade e bem-estar no IFRN Campus Lajes, focando no atendimento da comunidade acadêmica. O projeto original cumpriu seu papel com uma arquitetura MVC tradicional.

**A Grande Atualização (2026):** O projeto passou por um intenso processo de modernização e desacoplamento. O legado foi refatorado para uma **API RESTful moderna**, projetada com foco em **Escalabilidade, Resiliência e Proteção de Rotas**, pronta para ser consumida por aplicações Front-End ou Mobile.

## 🧠 Destaques da Arquitetura Back-End
* **Resiliência e Tratamento de Erros:** Implementação de um **Global Exception Handler** (`@RestControllerAdvice`). A API intercepta exceções internas (evitando o vazamento de stacktraces/Erro 500) e devolve respostas JSON padronizadas (Erro 400/404) com mensagens claras.
* **Segurança e Blindagem de Contrato:** Uso rigoroso de **Bean Validation** (`@Valid`, `@NotBlank`, `@NotNull`) para garantir a integridade dos dados e implementação de verificação de sessão e permissões diretamente nas rotas da API, barrando acessos não autorizados.

## 🚀 Tecnologias e Ferramentas Utilizadas
* **Back-End:** Java 17+, Spring Boot 3, Spring Web (REST)
* **Persistência de Dados:** Spring Data JPA, Hibernate, MySQL
* **Documentação de API:** Swagger / OpenAPI 3.0
* **Testes de Integração:** Postman (Collection nativa inclusa)

## 🛠️ Como executar e testar a API
1. Clone este repositório: `git clone https://github.com/victorkaell/ocorrencias.git`
2. Configure as credenciais do seu banco de dados MySQL no arquivo `application.properties`.
3. Execute a classe principal do Spring Boot na sua IDE ou via Maven.
4. A aplicação estará rodando em `http://localhost:8080`.

### 📖 Swagger UI (Documentação Viva)
A API é totalmente auto-documentada. Com o servidor rodando, acesse a interface interativa do Swagger para visualizar todos os endpoints (GET, POST, PUT, DELETE), os schemas de dados esperados e realizar testes direto pelo navegador:
👉 **Acesse:** `http://localhost:8080/swagger-ui/index.html`

### ⚡ Testes via Postman
Na pasta `/docs` deste repositório, você encontrará o arquivo `Ocorrencias_API_Collection.json`. Importe este arquivo no seu Postman para ter o ambiente de testes de requisições REST totalmente configurado.

### 🔑 Acesso de Teste (Dados Pré-carregados)
O banco de dados é inicializado automaticamente com dados de teste para facilitar a validação dos endpoints.
* **Matrícula de Teste:** 1234
* **Senha:** 1234

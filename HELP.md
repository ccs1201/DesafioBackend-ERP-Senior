# Desafio Dev Java BackEnd - Senior Sistemas

### Stacks

* PostgresSQL
* Java 17
* Maven
* [Spring Boot 2.7.6](https://spring.io/projects/spring-boot)
* [Hibernate Validator](https://hibernate.org/validator/)
* [ModelMapper 3.0.0](http://modelmapper.org/)
* [Lombok](https://projectlombok.org/)
* [SpringDoc Open API 1.6.13](https://springdoc.org/)
* [Spring Hateoas](https://spring.io/projects/spring-hateoas)
* [Flyway](https://flywaydb.org/)

### Configurações
* Servidor rodando na porta 8080
* Documentação da api (Swagger UI) em /api-doc.html
* Root Entry Point / HATEOAS (JSON contendo a lista de controladores da API) em /api/v1
* Pool de conexões configurado para maxímo= 20, idle= 2, time-out=10s
* Para fazer testes de escalabilidade altere a propriedade *spring.datasource.hikari.maximum-pool-size*
para um valor compatível com a quantido de requsições simultâneas que serão executadas, para que o pool não seja o gargalo no testes
  *     Recomendo a ferrramenta K6 para testes de escalablidade (https://k6.io/) framework javascrip simples e prático

### Como executar este Projeto

* Este é um projeto Maven, baixe o fonte no seu PC e importe o projeto via pom.xml na sua IDE ou execute via Maven.
* Tenha o Postgres com um Data Base vazio disponível para conexão.
* Alterer as configurações de conexão com o banco de dados no application.properties 
com os dados necessários para conexão com o seu banco (url, username, password)
* O Flyway está configurado para:
  *     criar/validar o schema do data base.
  *     Apagar todos os registros a cada reinicialização da aplicação e inserir uma massa de dados padrão para testes.
  *     Para desativar a inserção dos dados de teste comente com um UNDERSCORE ("_") o arquivo afterMigrate.sql 
        contido em resources/db/migration/testdata
[![Actions Status](https://github.com/danielso2007/virtualLibraryAPI/workflows/virtualLibraryAPI/badge.svg)](https://github.com/danielso2007/virtualLibraryAPI/actions)
[![codecov](https://codecov.io/gh/danielso2007/virtualLibraryAPI/branch/develop/graph/badge.svg)](https://codecov.io/gh/danielso2007/virtualLibraryAPI)
![GitHub package version](https://img.shields.io/github/package-json/v/danielso2007/virtualLibraryAPI.svg)
[![GitHub pull requests](https://img.shields.io/github/issues-pr-raw/danielso2007/virtualLibraryAPI.svg)](https://github.com/danielso2007/virtualLibraryAPI/pulls)
[![GitHub issues](https://img.shields.io/github/issues/danielso2007/virtualLibraryAPI.svg)](https://github.com/danielso2007/virtualLibraryAPI/issues?q=is%3Aopen+is%3Aissue)
![GitHub last commit](https://img.shields.io/github/last-commit/danielso2007/virtualLibraryAPI.svg)
[![GitHub issue/pull request author](https://img.shields.io/github/issues/detail/u/danielso2007/virtualLibraryAPI/1.svg)](https://github.com/danielso2007/virtualLibraryAPI/pulls)
![GitHub contributors](https://img.shields.io/github/contributors/danielso2007/virtualLibraryAPI.svg)
![GitHub top language](https://img.shields.io/github/languages/top/danielso2007/virtualLibraryAPI.svg)
[![GitHub](https://img.shields.io/github/license/danielso2007/virtualLibraryAPI.svg)](https://github.com/danielso2007/virtualLibraryAPI)
[![GitHub All Releases](https://img.shields.io/github/downloads/danielso2007/virtualLibraryAPI/total.svg)](https://github.com/danielso2007/virtualLibraryAPI/archive/master.zip)
[![Conventional Commits](https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg)](https://conventionalcommits.org)

# Virtual Library API

Virtual library API. This project is used to study REST Spring Boot projects.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

Utilizar o ambiente linux e ter o mavem e o java 11 para a compilação e execução do projeto.
Este projeto utiliza o MongoDb como base de dados, para ter um banco de dados para esse projeto, siga o projeto
[dockerMongoDB](https://github.com/danielso2007/dockerMongoDB) usando o [Docker](https://www.docker.com/).

### Installing

Recomendado instalar o [sdkman](https://sdkman.io/) que é uma ferramenta para gerenciar versões paralelas de vários kits de desenvolvimento de software na maioria dos sistemas baseados em Unix.

Com o sdkman, instale o java:
```
sdk list java
sdk install java 11.0.5.j9-adpt
```

Instalando o Maven:
```
sdk install maven
```

## Test

O projeto está configurado para não executar os testes quando construído para desenvolvimento. Para executar os testes, execute o comando abaixo:

`mvn clean test -Ptest`

Os testes são executados normalmente quando construído para **produção** e na criação da imagem **Docker**. Há no arquivo do `pom.xml` essa configuração, que pode ser modificada a qualquer momento do desenvolvimento. Inicialmente, para o desenvolvimento, o desenvolvedor pode executar seus testes ao seguir o padrão TDD pela própria IDE.

### Code test coverage

É utilizado o [EclEmma Jacoco](https://www.eclemma.org/jacoco/) para verificação de cobertura de código. Para executar a cobertura:
```
mvn clean test -Ptest jacoco:report
```
Na pasta target, é gerado um "site" mostrando toda a cobertura de código. Este projeto utiliza o _Action_ para os testes e build da aplicação. Ao final do teste, é enviado para [codecov.io](https://codecov.io) todo o relatório. Para verificar a cobertura de teste deste código, acesse [codecov.io/gh/danielso2007]([codecov.io](https://codecov.io/gh/danielso2007/virtualLibraryAPI)).

## Running with docker

Há um perfil no `pom.xml` para a criação da imagem do projeto. Ao executar `mvn clean package -Pdocker`, será realizado o teste e criado o arquivo `Dockerfile` na pasta `target`, criando a imagem `virtualLibraryAPI:<project.version>`.
Para ver a imagem criada, digite no terminal o comando `docker images`.

Para "rodar" a imagem, execute:
```
docker run -p 8080:8080 --name swapi  virtualLibraryAPI:<project.version>
```

### Documentação da API - swagger (OpenAPI 3):

A aplicação usa o swagger para a exibição da documentação da API. Para verificar, acesse os links [swagger-ui](http://localhost:8080/swagger-ui.html) e [api-docs](http://localhost:8080/api-docs). Documentação [swagger.io](https://swagger.io/docs/open-source-tools/swagger-ui/usage/configuration/) e [springdoc.org](https://springdoc.org/).

Mais exemplos: [documenting-spring-boot-rest-api-springdoc-openapi-3](https://www.dariawan.com/tutorials/spring/documenting-spring-boot-rest-api-springdoc-openapi-3/)

Ou adicione o caminho abaixo:

```
http://localhost:8080/swagger-ui.html

http://localhost:8080/api-docs
```

### Teste o endereço docker (login: admin / password: admin):

```
http://localhost:8080/api/v1/books

http://localhost:8080/api/v1/ratings
```

Inicialmente só é criada a imagem da API. Posteriormente mostrarei como executar o `docker-compose` criado no build, para a execução completa da api com o banco de dados MongoDb.

## Running with docker-compose

Ao executar o maven `mvn clean package -Pdocker`, é gerado o `Dockerfile` e também o `docker-compose.yml`. Com o docker-compose é possível inicar a aplicação já com um container docker com Mongo. Inicialmente esse banco estará vazio.

Execute esse comando dentro da pasta `target`:
```
docker-compose up -d
```

Será iniciado os containers da api e do mongo. Acessando o endereço `http://localhost:8080/api/v1/books`, será retornado uma lista vazia.

Para parar os containers, execute:
```
docker-compose stop
```

Pelo `pom.xml` é possível configurar a criação do arquivo `docker-compose`. Inicialmente a porta do container mongo está exposta, mas é só modificar o arquivo `docker-compose` e remover, pois a comunicação entre a api e o banco é via `network` interno entre os containers.

## Running (No docker)

Dentro da pasta do projeto, execute:
```
mvn clean package install
```
Após compilar o projeto, dentro da pasta `target`, inicie o projeto (x.x.x é a versão atual do projeto):
```
cd target
nohup java -jar virtuallibraryapi-x.x.x.jar & tail -f nohup.out

```

### Stopping project

Para encerrar o projeto, execute o comando abaixo:
```
ps -ef | grep virtuallibraryapi
```
O comando acima exibirá o id do processo, após ver o id do processo, execute:
```
kill -9 <id_processo>
```

### Scripts de execução e encerramento do projeto

Dentro da pasta `target` é criado os arquivos `start.sh` e `stop.sh`. Esses arquivos não são executáveis e precisam ser modificados:
```
cd target
chmod a+x start.sh stop.sh
```

Para iniciar o projeto, execute (dentro da pasta `target`):
```
./start.sh
```
Para encerrar o projeto, execute (dentro da pasta `target`):
```
./stop.sh
```

## Deployment

Nada será necessário, o projeto é executado como `Fat jar`. Mas para produção e homologação, é possível usar as imagens geradas pelo comando: `mvn clean package -Pdocker`. Muito importante para o uso do CI.


## Built With

* [Java](https://www.oracle.com/br/java/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Undertow - Servidor Web](http://undertow.io/)
* [Modelmapper - Simple, Intelligent, Object Mapping](http://modelmapper.org/)
* [Project Lombok](https://projectlombok.org/)
* [QueryDsl - type-safe SQL-like queries - fluent API](http://www.querydsl.com/)
* [Swagger](https://swagger.io/)
* [Standard-version](https://github.com/conventional-changelog/standard-version)

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

Usamos [SemVer](http://semver.org/) para versionar. Para as versões disponíveis, consulte as [tags neste repositório](https://github.com/danielso2007/virtualLibraryAPI/releases). 

## Authors

* **Daniel Oliveira** - *Initial work* - [danielso2007](https://github.com/danielso2007)

See also the list of [contributors](https://github.com/danielso2007/virtualLibraryAPI/graphs/contributors) who participated in this project.

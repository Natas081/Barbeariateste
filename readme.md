# Sistema de Gerenciamento de Barbearia

Projeto desenvolvido para a disciplina de Banco de Dados.

A aplicação foi construída em Java com interface gráfica Swing e integração com MySQL utilizando JDBC e comandos SQL explícitos.

## Funcionalidades

A aplicação permite:

- Inserção de clientes
- Alteração de clientes
- Exclusão de clientes
- Visualização de clientes

- Inserção de serviços
- Alteração de serviços
- Exclusão de serviços
- Visualização de serviços

- Execução e visualização de consultas SQL
- Visualização de dashboard com gráficos

## Consultas implementadas

O sistema possui consultas para:

- Visualizar clientes e seus agendamentos
- Visualizar a quantidade de agendamentos por barbeiro
- Visualizar o valor total de cada atendimento
- Visualizar os serviços mais realizados, preço médio praticado e faturamento total

As consultas estão disponíveis no arquivo:

`SQL/consultas.sql`

## Banco de Dados

O projeto utiliza MySQL.

O arquivo responsável pela criação das tabelas e inserção dos dados está em:

`SQL/barbearia.sql`

Antes de executar a aplicação, execute esse arquivo no MySQL.

O banco utilizado pela aplicação possui o nome:

`barbearia`

## Configuração da conexão

A conexão com o banco está configurada na classe:

`ConnectionFactory.java`

Antes de executar o projeto, configure os dados de acesso ao MySQL:

```java
private static final String URL =
        "jdbc:mysql://localhost:3306/barbearia";

private static final String USER =
        "root";

private static final String PASSWORD =
        "SUA_SENHA";
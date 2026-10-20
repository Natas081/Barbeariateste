# Nova interface da Barbearia

Interface desktop Java Swing, sem novas bibliotecas de interface.

- Paleta grafite, creme e cobre, com ilustração vetorial de poste de barbearia.
- Navegação lateral com indicação da seção ativa.
- Início com atalhos para clientes, serviços e desempenho, sem dados fictícios.
- Campos agrupados horizontalmente, tabelas com linhas alternadas e gráficos em cobre.
- Consultas, DAOs e regras de cadastro originais preservados.
- As telas de dados são criadas ao abrir a seção; a tela inicial não requer MySQL.

## Executar

Requer JDK 21, Maven e MySQL configurado conforme o readme original.

```sh
mvn compile dependency:copy-dependencies
java -cp 'target/classes:target/dependency/*' com.natan.barbeariabd.BarbeariaBdApplication
```

No Windows, troque `:` por `;` no classpath.

## Validação realizada

Código principal compilado para Java 21 com Eclipse Compiler 3.38.0.
Renderização Swing em modo headless validada para a tela inicial e gráficos
vazios, preenchidos e com valores zero. Os três atalhos da tela inicial foram
acionados e suas rotas verificadas. A tela de consultas foi instanciada.
O CRUD e as consultas reais não foram executados: exigem MySQL configurado.
O teste original usa Spring/JUnit, que não constam no pom original; ele não
foi executado. A execução em uma janela desktop não foi verificada neste ambiente.

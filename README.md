# MiniJava Core

## 1. Sobre o projeto

O MiniJava Core é um projeto acadêmico desenvolvido para a disciplina de Linguagens Formais e Autômatos, com o objetivo de implementar um analisador léxico e sintático para um subconjunto da linguagem Java.

A ferramenta utiliza ANTLR 4 para reconhecer estruturas da linguagem, identificar tokens e verificar a conformidade sintática de programas escritos em MiniJava Core.

## 2. Objetivos

* Implementar um analisador léxico.
* Definir uma gramática livre de contexto.
* Implementar um analisador sintático.
* Reconhecer declarações, expressões e estruturas de controle.
* Identificar e reportar erros sintáticos.
* Validar a gramática por meio de arquivos de teste.

## 3. Tecnologias utilizadas

* Java 23
* ANTLR 4.13.2
* Visual Studio Code

## 4. Estrutura do projeto

```text
MiniJava/
├── examples/
│   ├── teste_valido.mj
│   ├── teste_invalido.mj
│   ├── teste_variaveis.mj
│   ├── teste_expressoes.mj
│   ├── teste_logico.mj
│   ├── teste_repeticao.mj
│   ├── teste_metodos.mj
│   ├── teste_objetos.mj
│   ├── teste_erro_sintatico.mj
│   └── teste_erro_condicional.mj
├── grammar/
│   ├── MiniJavaLexer.g4
│   └── MiniJavaParser.g4
├── lib/
│   └── antlr-4.13.2-complete.jar
├── src/
│   ├── generated/
│   └── App.java
└── README.md
```

## 5. Recursos da linguagem

O subconjunto implementado contempla:

* Declaração de classes.
* Método principal `main`.
* Declaração e inicialização de variáveis.
* Tipos `int`, `boolean` e identificadores de classes.
* Operações aritméticas.
* Operadores relacionais e lógicos.
* Estruturas condicionais `if` e `else`.
* Estruturas de repetição `while` e `for`.
* Declaração de métodos, parâmetros e retorno.
* Criação de objetos e chamadas de métodos.
* Comando `System.out.println`.

## 6. Análise léxica

A análise léxica é a primeira etapa do processo de compilação. Sua função é percorrer a sequência de caracteres do código-fonte, identificar lexemas e classificá-los em tokens conforme as regras definidas para a linguagem.

No MiniJava Core, essa etapa é implementada por meio do ANTLR 4, utilizando o arquivo `MiniJavaLexer.g4`.

### 6.1 Classes léxicas e tokens

O analisador léxico reconhece diferentes categorias de elementos da linguagem, conforme apresentado na tabela:

| Classe léxica          | Exemplos de lexemas              | Tokens                                                                           |        |                    |
| ---------------------- | -------------------------------- | -------------------------------------------------------------------------------- | ------ | ------------------ |
| Palavras reservadas    | `class`, `public`, `int`, `if`   | `CLASS`, `PUBLIC`, `INT`, `IF`                                                   |        |                    |
| Identificadores        | `idade`, `contador`, `_valor`    | `IDENTIFIER`                                                                     |        |                    |
| Literais inteiros      | `10`, `250`, `0`                 | `INTEGER_LITERAL`                                                                |        |                    |
| Literais booleanos     | `true`, `false`                  | `BOOLEAN_LITERAL`                                                                |        |                    |
| Operadores aritméticos | `+`, `-`, `*`, `/`, `%`          | `PLUS`, `MINUS`, `MULTIPLY`, `DIVIDE`, `MODULO`                                  |        |                    |
| Operadores relacionais | `==`, `!=`, `<`, `>`, `<=`, `>=` | `EQUAL`, `NOT_EQUAL`, `LESS_THAN`, `GREATER_THAN`, `LESS_EQUAL`, `GREATER_EQUAL` |        |                    |
| Operadores lógicos | `&&`, `||`, `!` | `AND`, `OR`, `NOT` |                                                                                  |        |                    |
| Atribuição             | `=`                              | `ASSIGN`                                                                         |        |                    |
| Delimitadores          | `(`, `)`, `{`, `}`, `;`, `,`     | `LPAREN`, `RPAREN`, `LBRACE`, `RBRACE`, `SEMI`, `COMMA`                          |        |                    |

A tabela organiza os elementos reconhecidos pelo lexer de acordo com suas funções na linguagem. Essa classificação permite que o analisador sintático receba uma sequência estruturada de tokens, em vez de trabalhar diretamente com os caracteres do código-fonte.

### 6.2 Reconhecimento de identificadores

A regra utilizada para identificar nomes de variáveis, métodos e classes é:

```antlr
IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9]*;
```

A expressão regular estabelece que:

* O primeiro caractere deve ser uma letra maiúscula, uma letra minúscula ou o caractere `_`.
* Os caracteres seguintes podem ser letras, números ou `_`.
* O operador `*` permite zero ou mais ocorrências do conjunto de caracteres seguintes.

Exemplos de identificadores reconhecidos: `idade`, `_media`, `contador1` e `valorTotal`.

A regra não reconhece como identificadores nomes iniciados por números, como `1idade`, ou contendo caracteres não permitidos, como `valor-total`.

### 6.3 Palavras reservadas e identificação de tokens

As palavras reservadas possuem regras próprias no lexer, como `CLASS`, `INT`, `IF` e `WHILE`.

O ANTLR utiliza o critério de correspondência mais longa e, em caso de empate, a prioridade da ordem das regras. Dessa forma, a palavra `int` é reconhecida como o token `INT`, enquanto `inteiro` é reconhecida como `IDENTIFIER`.

### 6.4 Espaços em branco e comentários

O lexer utiliza comandos `skip` para ignorar espaços em branco e comentários durante a geração dos tokens.

São contemplados:

* Espaços, tabulações e quebras de linha;
* Comentários de uma linha, iniciados por `//`;
* Comentários de múltiplas linhas, delimitados por `/*` e `*/`.

Esses elementos não são encaminhados ao analisador sintático como tokens, pois não participam diretamente da estrutura sintática reconhecida.

## 7. Análise sintática

A análise sintática é a etapa responsável por verificar se a sequência de tokens produzida pelo analisador léxico está organizada de acordo com as regras gramaticais da linguagem.

No MiniJava Core, essa etapa é implementada utilizando uma Gramática Livre de Contexto (GLC), definida no arquivo `MiniJavaParser.g4`.

A gramática estabelece as produções que determinam como os tokens podem ser combinados para formar estruturas válidas, como classes, declarações de variáveis, métodos, expressões e comandos de controle.

### 7.1 Gramática Livre de Contexto

Uma Gramática Livre de Contexto é formalmente representada pela tupla:

G = (V, Σ, P, S)

Em que:

* V representa o conjunto de símbolos não terminais.
* Σ representa o conjunto de símbolos terminais.
* P representa o conjunto de produções da gramática.
* S representa o símbolo inicial.

No MiniJava Core, os símbolos terminais correspondem aos tokens definidos pelo lexer, enquanto os símbolos não terminais correspondem às regras sintáticas definidas no parser.

A regra inicial da gramática é `program`, responsável por reconhecer a estrutura geral de um programa.

### 7.2 Produções principais

A estrutura inicial do programa é definida pela produção:

```antlr
program
    : mainClass classDeclaration* EOF
    ;
```

Essa regra estabelece que um programa deve conter uma classe principal, seguida de zero ou mais declarações de classes adicionais, terminando com o fim da entrada (`EOF`).

As principais produções implementadas são:

| Produção              | Função                                              |
| --------------------- | --------------------------------------------------- |
| `mainClass`           | Define a estrutura da classe principal              |
| `classDeclaration`    | Reconhece declarações de classes                    |
| `variableDeclaration` | Reconhece declarações e inicializações de variáveis |
| `methodDeclaration`   | Define métodos, parâmetros e retorno                |
| `statement`           | Organiza os comandos reconhecidos pela linguagem    |
| `ifStatement`         | Define estruturas condicionais                      |
| `whileStatement`      | Define estruturas de repetição `while`              |
| `forStatement`        | Define estruturas de repetição `for`                |
| `expression`          | Representa expressões da linguagem                  |

### 7.3 Precedência dos operadores

A gramática organiza as expressões em diferentes níveis de precedência, evitando que todos os operadores sejam interpretados no mesmo nível.

A hierarquia implementada, da menor para a maior precedência, é:

1. Operador lógico OR (`||`);
2. Operador lógico AND (`&&`);
3. Operadores de igualdade (`==`, `!=`);
4. Operadores relacionais (`<`, `>`, `<=`, `>=`);
5. Operadores aditivos (`+`, `-`);
6. Operadores multiplicativos (`*`, `/`, `%`);
7. Operadores unários (`!`, `-`);
8. Expressões primárias, como literais, identificadores e expressões entre parênteses.

Essa organização permite que expressões como `2 + 3 * 4` sejam estruturadas respeitando a precedência da multiplicação sobre a adição.

### 7.4 Integração entre o analisador léxico e o sintático

A integração entre as duas etapas é realizada por meio dos tokens produzidos pelo `MiniJavaLexer` e consumidos pelo `MiniJavaParser`.

A opção `tokenVocab = MiniJavaLexer` estabelece que o parser utiliza o vocabulário de tokens definido pelo lexer.

O analisador sintático recebe a sequência de tokens e verifica sua conformidade com as produções da gramática.

Quando a entrada apresenta uma estrutura incompatível com as regras definidas, o ANTLR informa o erro sintático identificado.

### 7.5 Árvore sintática

Após o reconhecimento de um programa válido, o parser produz uma árvore sintática que representa a organização hierárquica das estruturas reconhecidas.

Essa representação permite observar como os tokens são agrupados em construções da linguagem, como declarações, expressões, métodos e comandos.

No MiniJava Core, a árvore sintática é apresentada no terminal por meio de uma representação compacta, facilitando a inspeção dos resultados da análise.

## 8. Interpretação dos resultados

O MiniJava Core apresenta os resultados das análises léxica e sintática no terminal, permitindo observar o reconhecimento dos tokens, a identificação de erros e a estrutura sintática do programa.

### 8.1 Resultado da análise léxica

Durante a análise léxica, o código-fonte é percorrido e dividido em unidades denominadas tokens.

Cada token representa uma categoria léxica, como palavra reservada, identificador, operador ou literal.

Por exemplo, na instrução:

```java
int idade = 20;
```

São reconhecidos os seguintes elementos:

| Lexema  | Token             |
| ------- | ----------------- |
| `int`   | `INT`             |
| `idade` | `IDENTIFIER`      |
| `=`     | `ASSIGN`          |
| `20`    | `INTEGER_LITERAL` |
| `;`     | `SEMI`            |

Essa representação permite verificar como os caracteres do código-fonte são classificados pelo analisador léxico.

### 8.2 Identificação de erros léxicos

Um erro léxico ocorre quando o analisador encontra um caractere ou sequência que não pode ser reconhecida pelas regras definidas no lexer.

Quando isso acontece, o programa informa a ocorrência do erro e interrompe o processamento antes da análise sintática.

Esse comportamento evita que uma entrada lexicalmente inválida seja encaminhada ao parser como se estivesse corretamente tokenizada.

### 8.3 Resultado da análise sintática

Após a geração dos tokens, o analisador sintático verifica se a sequência corresponde às produções definidas na gramática.

Quando a estrutura é reconhecida, o programa apresenta uma mensagem indicando o sucesso da análise sintática.

Caso a sequência não esteja de acordo com a gramática, o ANTLR informa o erro sintático, incluindo informações sobre sua localização no código-fonte.

### 8.4 Árvore sintática

A árvore sintática representa a organização hierárquica das construções reconhecidas pelo parser.

Por meio dessa representação, é possível observar como elementos individuais são agrupados em estruturas maiores, como declarações, expressões e comandos.

No MiniJava Core, a árvore é apresentada em formato textual compacto no terminal.

### 8.5 Relatório de operadores

O programa também apresenta um relatório de frequência dos operadores identificados durante a análise léxica.

Esse recurso permite observar a ocorrência dos operadores presentes no código-fonte, contribuindo para a inspeção dos resultados produzidos pelo analisador.


## 9. Como executar

### Compilar o projeto

Na raiz do projeto, execute:

```powershell
javac -cp "lib/antlr-4.13.2-complete.jar" -d bin src/generated/*.java src/App.java
```

### Executar um arquivo de teste

```powershell
java -cp "bin;lib/antlr-4.13.2-complete.jar" App examples/teste_valido.mj
```

Substitua `teste_valido.mj` pelo nome do arquivo desejado.

## 10. Validação

A validação da gramática foi realizada por meio de arquivos de teste contendo programas sintaticamente válidos e inválidos.

### 10.1 Testes de reconhecimento

Os testes positivos verificam se o analisador reconhece corretamente as construções previstas na gramática.

| Arquivo               | Recurso verificado                       | Resultado |
| --------------------- | ---------------------------------------- | --------- |
| `teste_valido.mj`     | Estrutura básica do programa             | Aprovado  |
| `teste_variaveis.mj`  | Declarações e inicialização              | Aprovado  |
| `teste_expressoes.mj` | Expressões aritméticas                   | Aprovado  |
| `teste_logico.mj`     | Operadores lógicos e relacionais         | Aprovado  |
| `teste_repeticao.mj`  | Estruturas `while` e `for`               | Aprovado  |
| `teste_metodos.mj`    | Métodos, parâmetros e retorno            | Aprovado  |
| `teste_objetos.mj`    | Criação de objetos e chamadas de métodos | Aprovado  |

### 10.2 Testes de rejeição

Os testes negativos verificam a identificação de construções que não seguem as regras sintáticas da linguagem.

| Arquivo                     | Erro verificado                     | Resultado |
| --------------------------- | ----------------------------------- | --------- |
| `teste_invalido.mj`         | Declaração sintaticamente incorreta | Rejeitado |
| `teste_erro_sintatico.mj`   | Ausência de delimitador obrigatório | Rejeitado |
| `teste_erro_condicional.mj` | Condição malformada                 | Rejeitado |

### 10.3 Resultado da validação

Os testes realizados demonstraram que o analisador reconhece os exemplos válidos e identifica erros sintáticos nos exemplos inválidos.

A validação foi realizada por meio da execução dos arquivos de teste no terminal.

## 11. Cobertura e limitações da gramática

A gramática do MiniJava Core foi desenvolvida para reconhecer um subconjunto da linguagem Java, contemplando estruturas fundamentais de programação.

### 11.1 Recursos contemplados

A implementação reconhece:

* Classes e classe principal;
* Declarações e inicializações de variáveis;
* Tipos inteiros, booleanos e identificadores de classes;
* Declarações de métodos, parâmetros e retorno;
* Expressões aritméticas, relacionais e lógicas;
* Estruturas condicionais e de repetição;
* Criação de objetos;
* Chamadas simples de métodos;
* Comando de saída `System.out.println`.

### 11.2 Limitações da implementação

O projeto concentra-se nas etapas de análise léxica e sintática. Portanto, algumas funcionalidades presentes em compiladores completos não são contempladas.

Entre as limitações estão:

* Ausência de análise semântica completa;
* Ausência de verificação de compatibilidade entre tipos;
* Ausência de verificação de declaração e escopo de variáveis;
* Ausência de validação da existência de métodos e classes referenciados;
* Ausência de geração de código executável;
* Ausência de execução dos programas reconhecidos.

Além disso, determinadas construções da linguagem Java não fazem parte da gramática implementada, pois o projeto utiliza um subconjunto específico da linguagem.

Essas limitações são compatíveis com o objetivo acadêmico de estudar a construção e a integração de analisadores léxicos e sintáticos.


## 12. Escopo

O projeto realiza análise léxica e sintática. Não contempla, nesta etapa, análise semântica completa, geração de código executável ou execução dos programas MiniJava.

## 13. Projeto acadêmico

Desenvolvido como atividade prática da disciplina de Linguagens Formais e Autômatos.

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import generated.MiniJavaLexer;
import generated.MiniJavaParser;

import java.nio.file.Path;
import java.util.*;

public class App {

    public static void main(String[] args) throws Exception {

        Path arquivo = Path.of(
                args.length > 0 ? args[0] : "examples/teste_valido.mj"
        );

        CharStream entrada = CharStreams.fromPath(arquivo);

        MiniJavaLexer lexer = new MiniJavaLexer(entrada);

// Controle de erros léxicos
boolean[] erroLexico = {false};

// Remove o tratamento padrão do ANTLR
lexer.removeErrorListeners();

// Adiciona um tratamento personalizado
lexer.addErrorListener(new BaseErrorListener() {

    @Override
    public void syntaxError(
            Recognizer<?, ?> recognizer,
            Object offendingSymbol,
            int line,
            int charPositionInLine,
            String msg,
            RecognitionException e) {

        erroLexico[0] = true;

        System.err.printf(
                "Erro léxico na linha %d, coluna %d: %s%n",
                line,
                charPositionInLine + 1,
                msg
        );
    }
});

CommonTokenStream tokens = new CommonTokenStream(lexer);
        // =====================================
        // ANÁLISE LÉXICA
        // =====================================

        System.out.println("\n=== ANÁLISE LÉXICA ===");

        tokens.fill();

        for (Token token : tokens.getTokens()) {

            if (token.getType() == Token.EOF) {
                continue;
            }

            String nomeToken = lexer.getVocabulary()
                    .getSymbolicName(token.getType());

            System.out.printf(
                    "Lexema: %-12s | Token: %s%n",
                    token.getText(),
                    nomeToken
            );
        }

        System.out.println("======================\n");

        // =====================================
        // RELATÓRIO DE OPERADORES
        // =====================================

        System.out.println("\n====================================");
        System.out.println("       RELATÓRIO DE OPERADORES");
        System.out.println("====================================");

        Map<String, Map<String, Integer>> relatorio =
                new LinkedHashMap<>();

        relatorio.put("ARITMÉTICOS", new LinkedHashMap<>());
        relatorio.put("RELACIONAIS", new LinkedHashMap<>());
        relatorio.put("LÓGICOS", new LinkedHashMap<>());
        relatorio.put("ATRIBUIÇÃO", new LinkedHashMap<>());

        for (Token token : tokens.getTokens()) {

            String lexema = token.getText();

            String tipo = lexer.getVocabulary()
                    .getSymbolicName(token.getType());

            String categoria = null;

            if (Arrays.asList(
                    "PLUS", "MINUS", "MULTIPLY",
                    "DIVIDE", "MODULO"
            ).contains(tipo)) {

                categoria = "ARITMÉTICOS";

            } else if (Arrays.asList(
                    "EQUAL", "NOT_EQUAL",
                    "LESS_EQUAL", "GREATER_EQUAL",
                    "LESS_THAN", "GREATER_THAN"
            ).contains(tipo)) {

                categoria = "RELACIONAIS";

            } else if (Arrays.asList(
                    "AND", "OR", "NOT"
            ).contains(tipo)) {

                categoria = "LÓGICOS";

            } else if ("ASSIGN".equals(tipo)) {

                categoria = "ATRIBUIÇÃO";
            }

            if (categoria != null) {

                Map<String, Integer> operadores =
                        relatorio.get(categoria);

                String identificacao = lexema + " (" + tipo + ")";

                operadores.put(
                        identificacao,
                        operadores.getOrDefault(identificacao, 0) + 1
                );
            }
        }

        // Exibe o relatório agrupado

        for (String categoria : relatorio.keySet()) {

            System.out.println("\n[" + categoria + "]");

            Map<String, Integer> operadores =
                    relatorio.get(categoria);

            if (operadores.isEmpty()) {
                System.out.println("Nenhum operador identificado.");
            }

            for (Map.Entry<String, Integer> operador
                    : operadores.entrySet()) {

                System.out.printf(
                        "%-25s : %d ocorrência(s)%n",
                        operador.getKey(),
                        operador.getValue()
                );
            }
        }

       System.out.println("\n====================================");

// Verifica se ocorreram erros léxicos
if (erroLexico[0]) {

    System.out.println("\nAnálise interrompida.");
    System.out.println("O programa contém erros léxicos.");

    return;
}

tokens.seek(0);

MiniJavaParser parser = new MiniJavaParser(tokens);
        MiniJavaParser.ProgramContext arvore = parser.program();

        if (parser.getNumberOfSyntaxErrors() == 0) {

            System.out.println("Programa reconhecido sintaticamente!");

            System.out.println("\nÁrvore sintática (Parse Tree):");

            imprimirArvoreCompacta(arvore, parser, "", true);

        } else {

            System.out.println("Foram encontrados erros sintáticos.");
        }
    }

    // =====================================
    // IMPRESSÃO DA ÁRVORE SINTÁTICA
    // =====================================

    private static void imprimirArvoreCompacta(
            ParseTree no,
            Parser parser,
            String prefixo,
            boolean ultimo) {

        ParseTree atual = no;

        StringBuilder rotulo = new StringBuilder();

        // Agrupa regras intermediárias com apenas um filho

        while (atual instanceof ParserRuleContext
                && atual.getChildCount() == 1) {

            ParserRuleContext contexto =
                    (ParserRuleContext) atual;

            String nomeRegra =
                    parser.getRuleNames()[contexto.getRuleIndex()];

            if (rotulo.length() > 0) {
                rotulo.append(" → ");
            }

            rotulo.append(nomeRegra);

            atual = atual.getChild(0);
        }

        // Identifica o último nó da cadeia

        if (atual instanceof TerminalNode) {

            String texto = atual.getText();

            if (rotulo.length() > 0) {
                rotulo.append(" → ");
            }

            rotulo.append(texto);

        } else if (rotulo.length() == 0) {

            ParserRuleContext contexto =
                    (ParserRuleContext) atual;

            rotulo.append(
                    parser.getRuleNames()[contexto.getRuleIndex()]
            );
        }

        System.out.println(
                prefixo
                + (ultimo ? "└── " : "├── ")
                + rotulo
        );

        // Continua a impressão dos filhos

        int quantidade = atual.getChildCount();

        for (int i = 0; i < quantidade; i++) {

            imprimirArvoreCompacta(
                    atual.getChild(i),
                    parser,
                    prefixo + (ultimo ? "    " : "│   "),
                    i == quantidade - 1
            );
        }
    }
}
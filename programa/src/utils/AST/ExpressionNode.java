package utils.AST;

import bin.Parser;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import utils.SymbolsTable.SymbolTable;
import utils.SymbolsTable.VariableSymbol;

public class ExpressionNode extends ASTNode{
    public String expression;//Contiene el string concatenado de las expresiones que se asigna o evalúa.
    ASTNode subExpression;//Contiene la expresión hijo que compone la actual expresión. Puede ser null en caso de ser una expresión base
    ExpressionEnum expressionType;//Determina el tipo de expresión que está evaluando como conjunto del atributo llamado "subExpression".
    ASTNode treeElementNode; //Contiene el nodo en caso que la expresión sea el resultado de un nodo del arbol AST, por ejemlo FunctionCallNode. Puede ser null en algunos casos.
    
    ASTNode leftExpression; //En caso de expresiones binarias que incluyen el siguiente formato (exp op exp), verificar según expressionType
    ASTNode operatorExpression; //En caso de expresiones binarias que incluyen el siguiente formato (exp op exp), verificar según expressionType
    ASTNode rightExpression; //En caso de expresiones binarias que incluyen el siguiente formato (exp op exp), verificar según expressionType
    
    
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    public String reducedType;
    
    public ExpressionNode(String expression){
        this.expression = expression;
    }
    
    public ExpressionNode(ExpressionEnum expressionType, ASTNode subExpression, String expression){
        this.expressionType = expressionType;
        this.subExpression = subExpression;
        this.expression = expression;
    }
    
    //Constructor opcional en caso que sea necesario asignar el treeElementNode como parte de la expresión resultante.
    public ExpressionNode(ExpressionEnum expressionType, ASTNode subExpression, String expression, ASTNode treeElementNode){
        this.expressionType = expressionType;
        this.subExpression = subExpression;
        this.expression = expression;
        this.treeElementNode = treeElementNode;
    }
    
    //Constructor opcional 
    //En caso de expresiones binarias que incluyen el siguiente formato (exp op exp), verificar según expressionType
    public ExpressionNode(ExpressionEnum expressionType, String expression,
            ASTNode leftExpression, ASTNode operatorExpression, ASTNode rightExpression){
        this.expressionType = expressionType;
        this.expression = expression;
        this.leftExpression = leftExpression;
        this.operatorExpression = operatorExpression;
        this.rightExpression = rightExpression;
    }
    
    public String getType(SymbolTable symbolTable) {
        String[] parts = expression.split(":");

        if (parts.length == 2) {
            String type = parts[0].trim();
            String value = parts[1].trim();

            if (type.equals("Id")) {
                VariableSymbol variable = symbolTable.getVariableSymbols().get(value);
                if (variable != null) {
                    return variable.getType();
                } else {
                    throw new RuntimeException("Error semántico: La variable '" + value + "' no está definida.");
                }
            }

            return type;
        }

        throw new RuntimeException("Error semántico: La expresión '" + expression + "' no tiene un formato válido.");
    }
    
    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();
        
        System.out.println("Expresion: " + expression + " declarada en " + currentHash);

        // Construir la expresión de tipos
        String typeExpression = buildTypeExpression(expression);
        System.out.println("Expresion de tipos: " + typeExpression);
        
        // Reducir la expresión de tipos
        reducedType = reduceTypeExpression(typeExpression);
        
        System.out.println("Tipo reducido: " + reducedType);
    }

    /**
     * Construye una expresión de tipos a partir de una expresión dada.
     * @param expression La expresión original.
     * @param symbolTable La tabla de símbolos para buscar tipos de identificadores y funciones.
     * @return La expresión de tipos.
     */
    private String buildTypeExpression(String expression) {
        
        // Dividir la expresión en tokens, conservando los paréntesis
        String[] tokens = expression.split("((?<=\\s)|(?=\\s))");

        StringBuilder typeExpression = new StringBuilder();

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty()) {
                continue;
            }

            // Ignorar el negativo si no es un operador de resta
            if (token.equals("-")) {
                continue;
            }            
            
            if (token.matches(".*:--|.*:\\+\\+")) {
                // identificador seguido de :-- o :++
                String id = token.split(":")[0].trim();
                VariableSymbol variable = symbolTable.getVariable(id, currentHash);
                typeExpression.append(variable.getType()).append(" ");
            }
            else if (token.startsWith("Id:")) {
                // Es un identificador, obtener su tipo
                String id = token.substring(3).trim();
                VariableSymbol variable = symbolTable.getVariable(id, currentHash);
                typeExpression.append(variable.getType()).append(" ");
            }
            else if (token.startsWith("FunctionCall:")) {
                // Es una llamada a función, obtener su tipo de retorno
                String functionCall = token.substring(13).trim();
                String functionName = functionCall.split("\\[")[0].trim();
                String returnType = symbolTable.getFunctionSymbols().get(functionName).getReturnType();
                typeExpression.append(returnType).append(" ");

                // Ignorar todo lo que esté dentro de los corchetes []
                while (i < tokens.length && !tokens[i].contains("]")) {
                    i++;
                }
            }
            else if (token.startsWith("Bool:") || token.startsWith("Integer:") || token.startsWith("Float:") || token.startsWith("String:")) {
                // Es un literal, obtener su tipo
                String type = token.split(":")[0].trim();
                typeExpression.append(type).append(" ");
            }
            else if (token.equals("(") || token.equals(")")) {
                typeExpression.append(token).append(" ");
            }
            else {
                typeExpression.append(token).append(" ");
            }
        }

        return typeExpression.toString().trim();
    }

    private String reduceTypeExpression(String typeExpression) {
        String[] tokens = typeExpression.split("\\s+");

        // Pilas para manejar la precedencia de operadores
        Deque<String> values = new ArrayDeque<>();
        Deque<String> operators = new ArrayDeque<>();

        for (String token : tokens) {
            if (token.equals("(")) {
                operators.push(token);
            } else if (token.equals(")")) {
                // Procesar todo dentro del paréntesis
                while (!operators.isEmpty() && !operators.peek().equals("(")) {
                    processOperation(values, operators.pop());
                }
                operators.pop(); 
            } else if (isOperator(token)) {
                // Procesar operadores según su precedencia
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    processOperation(values, operators.pop());
                }
                operators.push(token);
            } else {
                values.push(token);
            }
        }

        while (!operators.isEmpty()) {
            processOperation(values, operators.pop());
        }

        return values.isEmpty() ? "" : values.pop();
    }

    /**
     * Verifica si un token es un operador.
     */
    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("%") || token.equals("**")
                || token.equals("<") || token.equals(">") || token.equals("<=") || token.equals(">=") || token.equals("==") || token.equals("!=")
                || token.equals("&&") || token.equals("||");
    }

    /**
     * Devuelve la precedencia de un operador.
     */
    private int precedence(String operator) {
        switch (operator) {
            case "**":
                return 4;
            case "*":
            case "/":
            case "%":
                return 3;
            case "+":
            case "-":
                return 2;
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
            case "!=":
                return 1;
            case "&&":
            case "||":
                return 0;
            default:
                return -1;
        }
    }

    /**
     * Procesa una operación y reduce los tipos.
     */
    private void processOperation(Deque<String> values, String operator) {
        String right = values.pop();
        String left = values.isEmpty() ? "Bool" : values.pop();

        // Validar compatibilidad de tipos antes de operar
        if (!areTypesCompatible(left, right, operator)) {
            throw new RuntimeException("No se puede operar " + left + " con " + right + " usando el operador " + operator);
        }        
        
        String resultType;
        switch (operator) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
            case "**":
                resultType = (left.equals("Float") || right.equals("Float")) ? "Float" : "Integer";
                break;
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
            case "!=":
            case "&&":
            case "||":
                resultType = "Bool";
                break;
            default:
                throw new RuntimeException("Operador no válido: " + operator);
        }

        values.push(resultType);
    }

    /**
     * Verifica si los tipos son compatibles para el operador dado.
     */
    private boolean areTypesCompatible(String left, String right, String operator) {
        switch (operator) {
            case "+":
            case "-":
            case "*":
            case "/":
            case "%":
            case "**":
                // Operadores aritméticos: solo permiten Integer y Float
                return (left.equals("Integer") || left.equals("Float")) && (right.equals("Integer") || right.equals("Float"));
            case "<":
            case ">":
            case "<=":
            case ">=":
            case "==":
            case "!=":
                // Operadores relacionales: permiten Integer, Float y Bool
                return (left.equals("Integer") || left.equals("Float") || left.equals("Bool")) &&
                       (right.equals("Integer") || right.equals("Float") || right.equals("Bool"));
            case "&&":
            case "||":
                // Operadores lógicos: solo permiten Bool
                return left.equals("Bool") && right.equals("Bool");
            default:
                throw new RuntimeException("Operador no válido: " + operator);
        }
    }
    
    @Override
    void generateMIPS() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        return indent +"└── Expresión: "+ expression + "\n";
    }

    @Override
    public String toString() {
        return expression;
    }
}

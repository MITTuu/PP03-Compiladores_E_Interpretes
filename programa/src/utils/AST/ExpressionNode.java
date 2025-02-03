package utils.AST;

import bin.Parser;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import utils.MIPS.GeneracionCodigo.CodeGenerator;
import utils.SymbolsTable.SymbolTable;
import utils.SymbolsTable.VariableSymbol;

/**
 * Clase que representa un nodo de expresión en el árbol sintáctico abstracto (AST).
 * 
 * Esta clase maneja expresiones de diferentes tipos y evalúa sus tipos, operaciones y semántica.
 * Se utiliza para construir las expresiones de código y gestionar las interacciones entre los nodos
 * en una estructura que representa operaciones aritméticas, lógicas, relacionales, etc.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
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
    public String reducedType = "a";

    /**
     * Constructor para crear un nodo de expresión con solo la expresión.
     * 
     * @param expression La expresión que representa el nodo.
     */    
    public ExpressionNode(String expression){
        this.expression = expression;
    }
    
    /**
     * Constructor para crear un nodo de expresión con tipo, sub-expresión y la expresión.
     * 
     * @param expressionType El tipo de la expresión.
     * @param subExpression La sub-expresión asociada a esta expresión.
     * @param expression La expresión que representa el nodo.
     */    
    public ExpressionNode(ExpressionEnum expressionType, ASTNode subExpression, String expression){
        this.expressionType = expressionType;
        this.subExpression = subExpression;
        this.expression = expression;
        generateMIPS(ASTNode.cg);
    }
    
    /**
     * Constructor opcional para asignar un nodo de árbol como parte de la expresión resultante.
     * 
     * @param expressionType El tipo de la expresión.
     * @param subExpression La sub-expresión asociada a esta expresión.
     * @param expression La expresión que representa el nodo.
     * @param treeElementNode El nodo del árbol asociado a esta expresión.
     */     
    public ExpressionNode(ExpressionEnum expressionType, ASTNode subExpression, String expression, ASTNode treeElementNode){
        this.expressionType = expressionType;
        this.subExpression = subExpression;
        this.expression = expression;
        this.treeElementNode = treeElementNode;
    }
    
    /**
     * Constructor para expresiones binarias que incluyen izquierda, operador y derecha.
     * 
     * @param expressionType El tipo de la expresión.
     * @param expression La expresión representada por este nodo.
     * @param leftExpression La expresión de la izquierda del operador.
     * @param operatorExpression El operador utilizado.
     * @param rightExpression La expresión de la derecha del operador.
     */
    public ExpressionNode(ExpressionEnum expressionType, String expression,
            ASTNode leftExpression, ASTNode operatorExpression, ASTNode rightExpression){
        this.expressionType = expressionType;
        this.expression = expression;
        this.leftExpression = leftExpression;
        this.operatorExpression = operatorExpression;
        this.rightExpression = rightExpression;
    }

    /**
     * Obtiene el tipo de la expresión a partir de la tabla de símbolos.
     * 
     * @param symbolTable La tabla de símbolos para buscar la variable.
     * @return El tipo de la expresión.
     * @throws RuntimeException Si la variable no está definida.
     */    
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
                    throw new RuntimeException("La variable '" + value + "' no está definida.");
                }
            }

            return type;
        }

        throw new RuntimeException("La expresión '" + expression + "' no tiene un formato válido.");
    }

    /**
     * Obtiene el tipo reducido de la expresión.
     * 
     * @return El tipo reducido de la expresión.
     */    
    public String getReduceType() {
        return reducedType;
    }
    
    /**
     * Verifica la semántica de la expresión, construyendo la expresión de tipos y reduciéndola.
     */    
    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();
        
        //System.out.println("Expresion: " + expression + " declarada en " + currentHash);

        // Construir la expresión de tipos
        String typeExpression = buildTypeExpression(expression);
        //System.out.println("Expresion de tipos: " + typeExpression);
        
        // Reducir la expresión de tipos
        reducedType = reduceTypeExpression(typeExpression);
        
        //System.out.println("Tipo reducido: " + reducedType);
    }

    /**
     * Construye una expresión de tipos a partir de una expresión dada.
     * 
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
                // Identificador seguido de :-- o :++
                String id = token.split(":")[0].trim();
                VariableSymbol variable = symbolTable.getVariable(id, currentHash);
                typeExpression.append(variable.getType()).append(" ");
            } else if (token.startsWith("Id:")) {
                // Es un identificador, obtener su tipo
                String id = token.substring(3).trim();
                VariableSymbol variable = symbolTable.getVariable(id, currentHash);
                typeExpression.append(variable.getType()).append(" ");
            } else if (token.startsWith("FunctionCall:")) {
                // Es una llamada a función, obtener su tipo de retorno
                String functionCall = token.substring(13).trim();
                String functionName = functionCall.split("\\[")[0].trim();
                if (!symbolTable.getFunctionSymbols().containsKey(functionName)) {
                    throw new RuntimeException("Error semántico: la función no está definida.");
                }                
                String returnType = symbolTable.getFunctionSymbols().get(functionName).getReturnType();
                typeExpression.append(returnType).append(" ");

                // Ignorar todo lo que esté dentro de los corchetes internos [ ]
                int count = 0;
                while (i < tokens.length) {
                    if (count == 2) {
                        break;
                    }
                    
                    if (tokens[i].contains("]")) {
                        count++;
                    }
                    i++;
                }                        
            } else if (token.startsWith("ArrayUse:")) {
                // Es un uso de arreglo, obtener el tipo del arreglo
                String arrayUse = token.substring(9).trim();
                String arrayName = arrayUse.split("\\[")[0].trim();
                VariableSymbol arrayVariable = symbolTable.getVariable(arrayName, currentHash);
                String name = arrayVariable.getType().split("\\[")[0];
                typeExpression.append(name).append(" ");

                // Ignorar todo lo que esté dentro de los corchetes internos [ ]
                int count = 0;
                while (i < tokens.length) {
                    if (count == 1) {
                        break;
                    }

                    if (tokens[i].contains("]")) {
                        count++;
                    }
                    i++;
                }
            } else if (token.startsWith("String:\"")) {
                // Acumular el literal completo
                StringBuilder literal = new StringBuilder(token);
                if (!token.endsWith("\"")) {
                    // Acumular tokens hasta encontrar la comilla de cierre
                    while (++i < tokens.length) {
                        String nextToken = tokens[i];
                        literal.append(nextToken);
                        if (nextToken.contains("\"")) {
                            break;
                        }
                    }
                }
                typeExpression.append("String").append(" ");
            }
            
            else if (token.startsWith("Bool:") || token.startsWith("Integer:") || token.startsWith("Float:") || token.startsWith("Char:")) {
                // Es un literal, obtener su tipo
                String type = token.split(":")[0].trim();
                typeExpression.append(type).append(" ");
            } else if (token.equals("(") || token.equals(")")) {
                // Es un paréntesis, mantenerlo en la expresión
                typeExpression.append(token).append(" ");
            } else {
                // Es un operador u otro token, mantenerlo en la expresión
                typeExpression.append(token).append(" ");
            }
        }

        return typeExpression.toString().trim();
    }

    /**
     * Reduce la expresión de tipos a un tipo simple basado en las reglas de precedencia y operación.
     * 
     * @param typeExpression La expresión de tipos a reducir.
     * @return El tipo reducido.
     */    
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
    
    /**
     * Genera el código MIPS correspondiente a la expresión.
     * 
     * @param cg El generador de código MIPS.
     * @return El código MIPS generado.
     */    
    @Override
    String toString(String indent) {
        return indent +"└── Expresión: "+ expression + "\n";
    }

    @Override
    public String toString() {
        return expression;
    }
    
    @Override
    String generateMIPS(CodeGenerator cg) {
        switch (expressionType) {
            case LOGICAL:
                // Código para expresión lógica
                break;
            case LOGICAL_OPERATOR_CONJUNCTION:
                // Código para operador lógico de conjunción
                break;
            case LOGICAL_OPERATOR_DISJUCTION:
                // Código para operador lógico de disyunción
                break;
            case RELATIONAL_EXPRESSION:
                // Código para expresión relacional
                break;
            case RELATIONAL_OPERATOR_LESS:
            case RELATIONAL_OPERATOR_LESS_EQUAL:
            case RELATIONAL_OPERATOR_GREATER:
            case RELATIONAL_OPERATOR_GREATER_EQUAL:
            case RELATIONAL_OPERATOR_EQUAL:
            case RELATIONAL_OPERATOR_NOT_EQUAL:
                // Código para operadores relacionales
                break;
            case ARITHMETIC_EXPRESSION:
                // Código para expresión aritmética
                break;
            case BINARY_ARITHMETIC_OPERATOR_SUM:
            case BINARY_ARITHMETIC_OPERATOR_SUBTRACTION:
            case BINARY_ARITHMETIC_OPERATOR_MULTIPLICATION:
            case BINARY_ARITHMETIC_OPERATOR_DIVISION:
            case BINARY_ARITHMETIC_OPERATOR_MODULE:
            case BINARY_ARITHMETIC_OPERATOR_POWER:
                // Código para operadores aritméticos binarios
                break;
            case SIMPLE_EXPRESSION:
                // Código para expresión simple
                break;
            case IDENTIFIER:
                // Código para identificador
                break;
            case LITERALS:
                String result = literalGenerator.generateCodeLiteralLoad(expression,cg);                
                return result;
            case NEGATIVE_LITERAL:
            case UNARY_ARITHMETIC_EXPRESSION:
                // Código para literales, separar tipo con split
                break;
            case ARRAY_USE_AS_EXPRESSION:
                // Código para variable resultado de uso de arreglo
                break;
            case FUNCTION_CALL_AS_EXPRESSION:
                // Código para llamada a función
                break;
            case NEGATION_SIMPLE_EXPRESSION:
                // Código para negación de expresión simple
                break;
            case RETURN_EXPRESSION:
                // Código para expresión de retorno
                break;
            case BREAK_EXPRESSION:
                // Código para expresión de break
                break;
            case VARIABLE_DECLARATION_EXPRESSION:
                // Código para declaración de variable
                break;
            case ARRAY_USE_EXPRESSION:
                // Código para puntero en uso de arreglo
                break;
            case PRINT_EXPRESSION:
                // Código para llamada a función PRINT
                break;
            case READ_EXPRESSION:
                // Código para llamada a función READ
                break;
            case FUNCTION_CALL_PARAMETER:
                // Código para parámetros de llamada de función
                break;
            case VARIABLE_ASSIGNMENT_EXPRESSION:
                // Código para asignación a variable
                break;
            case CONTROL_STRUCTURE_CONDITION:
                // Código para condición en estructura de control
                break;
            case UPDATE_EXPRESSION:
                // Código para actualización de contador en For
                break;
            case SWITCH_EXPRESSION:
                // Código para identificador de estructura SWITCH
                break;
            case CASE_LABEL:
                // Código para etiqueta de caso en SWITCH
                break;
            default:
                throw new IllegalArgumentException("Tipo de expresión no soportado: " + expressionType);
        }

        return "";
    }
}

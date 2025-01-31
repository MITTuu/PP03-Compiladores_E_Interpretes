
package utils.AST;

public enum ExpressionEnum {
    LOGICAL("Expresión lógica"),
    LOGICAL_OPERATOR_CONJUNCTION("Operador lógico de conjunción"),
    LOGICAL_OPERATOR_DISJUCTION("Operador lógico de disjunción"),
    RELATIONAL_EXPRESSION("Expresión relacional"),
    RELATIONAL_OPERATOR_LESS("Operador relacional"),
    RELATIONAL_OPERATOR_LESS_EQUAL("Operador relacional"),
    RELATIONAL_OPERATOR_GREATER("Operador relacional"),
    RELATIONAL_OPERATOR_GREATER_EQUAL("Operador relacional"),
    RELATIONAL_OPERATOR_EQUAL("Operador relacional"),
    RELATIONAL_OPERATOR_NOT_EQUAL("Operador relacional"),
    ARITHMETIC_EXPRESSION("Expresión aritmética"),
    BINARY_ARITHMETIC_OPERATOR_SUM("Operador aritmético binario"),
    BINARY_ARITHMETIC_OPERATOR_SUBTRACTION("Operador aritmético binario"),
    BINARY_ARITHMETIC_OPERATOR_MULTIPLICATION("Operador aritmético binario"),
    BINARY_ARITHMETIC_OPERATOR_DIVISION("Operador aritmético binario"),
    BINARY_ARITHMETIC_OPERATOR_MODULE("Operador aritmético binario"),
    BINARY_ARITHMETIC_OPERATOR_POWER("Operador aritmético binario"),
    SIMPLE_EXPRESSION("Expresión simple"),
    IDENTIFIER("Identificador"),
    LITERALS("Literales"), //Es un string compuesto, mediante split sacar el tipo
    NEGATIVE_LITERAL("Literal negativo"),//Es un string compuesto, mediante split sacar el tipo
    ARRAY_USE_AS_EXPRESSION("Variable resultado de uso de arreglo"),
    FUNCTION_CALL_AS_EXPRESSION("Llamada a función"),
    UNARY_ARITHMETIC_EXPRESSION("Expresión aritmética unaria"),//Es un string compuesto, mediante split sacar el tipo
    NEGATION_SIMPLE_EXPRESSION("Negación de expresión simple"),
    RETURN_EXPRESSION ("Expresión de retorno"),
    BREAK_EXPRESSION ("Expresión de break"),
    VARIABLE_DECLARATION_EXPRESSION("Expresión de declaración de variable"),
    ARRAY_USE_EXPRESSION("Expresión de puntero en uso de arreglo"),
    PRINT_EXPRESSION("Expresión de llamada de función PRINT"),
    READ_EXPRESSION("Expresión de llamada de función READ "),
    FUNCTION_CALL_PARAMETER("Expresión de parametros de llamada de función"),
    VARIABLE_ASSIGNMENT_EXPRESSION("Expresión de asignación a variable"),
    CONTROL_STRUCTURE_CONDITION("Expresión de condición en estructura de control"),
    UPDATE_EXPRESSION("Expresión de actualización de contador en estructura For"),
    SWITCH_EXPRESSION("Expresión como identificador de estructura SWITCH"),
    CASE_LABEL("Expresión de etiqueta de caso en SWITCH")   
    ; 
    
    private String description;

    
    
    ExpressionEnum(String description){
    this.description = description;
    }
    
    public String getDescription() {
        return description;
    } 

}

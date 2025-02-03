package utils.AST;

import bin.Parser;
import utils.MIPS.GeneracionCodigo.CodeGenerator;
import utils.SymbolsTable.SymbolTable;
import utils.SymbolsTable.VariableSymbol;

/**
 * Representa una asignación de variable en el árbol sintáctico abstracto (AST).
 * Este nodo puede manejar diferentes tipos de asignaciones, incluyendo 
 * asignaciones simples, asignaciones con operadores aritméticos, 
 * asignaciones a elementos de un arreglo, y asignaciones que implican el uso de arreglos.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class VariableAssignmentNode extends ASTNode {
    
    String id;
    String unaryArithmeticOperator;
    ExpressionNode expression;
    ArrayElementsNode arrayElements;
    ArrayUseNode arrayUse;
    private int VariableAssignmentCase;//Determina el tipo de contructor que se usó.
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    public String reducedType;
    
    /**
     * Constructor para una asignación simple de una variable.
     * 
     * @param id El identificador de la variable.
     * @param expression La expresión a la que se le asigna un valor.
     */      
    public VariableAssignmentNode(String id, ExpressionNode expression){
        this.id = id;
        this.expression = expression;
        this.arrayElements = null;
        this.arrayUse = null;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 1;
    }
    
    /**
     * Constructor para una asignación que involucra un operador aritmético unitario.
     * 
     * @param unaryArithmeticExpression Una cadena que contiene el identificador y el operador.
     */    
     public VariableAssignmentNode(String unaryArithmeticExpression){
         //Recibe el id y el operador en un solo string
        String[] unaryExpression = unaryArithmeticExpression.split(":");
        this.id = unaryExpression[0];
        this.unaryArithmeticOperator= unaryExpression[1];
        this.expression = new ExpressionNode(ExpressionEnum.UNARY_ARITHMETIC_EXPRESSION,null,unaryArithmeticExpression);
        this.arrayElements = null;
        this.arrayUse = null;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 2;
    }
                
    /**
     * Constructor para una asignación a elementos de un arreglo.
     * 
     * @param id El identificador del arreglo.
     * @param arrayElements Los elementos del arreglo a los cuales se asigna un valor.
     */
    public VariableAssignmentNode(String id, ArrayElementsNode arrayElements){
        this.id = id;
        this.arrayElements = arrayElements;
        this.expression = null;
        this.arrayUse = null;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 3;
    }
    
    /**
     * Constructor para una asignación que involucra el uso de un arreglo.
     * 
     * @param arrayUse El uso del arreglo.
     * @param expression La expresión a la que se le asigna un valor.
     */    
     public VariableAssignmentNode(ArrayUseNode arrayUse, ExpressionNode expression){
        this.id = arrayUse.id;
        this.expression = expression;
        this.arrayElements = null;
        this.arrayUse = arrayUse;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 4;
    }


    /**
     * Realiza la verificación semántica de la asignación, comprobando que el tipo de la variable
     * sea compatible con el tipo de la expresión que se le está asignando.
     * 
     * @throws RuntimeException Si hay una incompatibilidad de tipos.
     */     
    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();

        VariableSymbol variable = symbolTable.getVariable(id, currentHash);
        String type = variable.getType();

        if (type.equals("Char") && reducedType.equals("Integer")) {
            return;
        }        
        
        if (!type.equals("a")) {
            if (!type.equals(reducedType)) {
                // Levantar un error si los tipos no coinciden
                throw new RuntimeException("Incompatibilidad de tipos asignacion. Se esperaba " 
                    + type + " pero se asignó " + reducedType);
            }
        }        
    }

    /**
     * Genera el código MIPS correspondiente a la asignación de variable.
     * Este método no está implementado en esta clase.
     * 
     * @param cg El generador de código MIPS.     
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        String result = ""; // Inicializar result

        // Validar y concatenar solo si no son null
        if (expression != null) {
            result = result.concat(expression.generateMIPS(cg)); // Concatenar si 'expression' no es null
        }

        if (arrayElements != null) {
            result = result.concat(arrayElements.generateMIPS(cg)); // Concatenar si 'arrayElements' no es null
        }

        if (arrayUse != null) {
            result = result.concat(arrayUse.generateMIPS(cg)); // Concatenar si 'arrayUse' no es null
        }

        return result; // Retornar el resultado concatenado, vacío si todos fueron null

    }

    /**
     * Representa el nodo de la asignación de variable en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de la asignación de variable.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("├── Id: ").append(id).append("\n");
        switch (VariableAssignmentCase) {
            case 1:
                sb.append(indent).append("└── Asignación").append("\n");
                sb.append(expression.toString(indent.replace("|", " ") + "    "));
                break;
            case 2:
                sb.append(indent).append("└── Asignación: ").append("Operador aritmético unitario postfijo => ").append(this.unaryArithmeticOperator).append("\n");
                break;
            case 3:
                sb.append(indent).append("└── Asignación ").append("\n").append(arrayElements.toString(indent+ "    "));       
                break;
            case 4:
                sb.append(indent).append("├── Uso de arreglo: ").append("\n");
                sb.append(arrayUse.toString(indent.replace("|", " ") + "│    "));
                sb.append(indent).append("└── Asignación ").append("\n");
                sb.append(expression.toString(indent.replace("|", " ") + "    "));       
                break;
            default:
                sb.append(indent).append("└── Error de datos en VariableAssignmentNode ").append("\n");
                break;
        }
        return sb.toString();
    }
    
    /**
     * Representa el nodo de asignación de variable en formato de cadena sin indentación.
     * 
     * @return La representación en formato de cadena de la asignación de variable.
     */    
    @Override
    public String toString() {
        if (expression != null) {
            return expression.toString();
        }      
        
        return null;
    }
}

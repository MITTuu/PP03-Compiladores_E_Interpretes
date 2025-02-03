package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;
import bin.Parser;
import utils.SymbolsTable.SymbolTable;

/**
 * Representa una declaración de retorno en el árbol sintáctico abstracto (AST).
 * Este nodo se utiliza para manejar el retorno de valores en las funciones dentro del programa.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class ReturnStatementNode extends ASTNode {
    
    ExpressionNode expression;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    public String reducedType;

    /**
     * Crea un nuevo nodo para una declaración de retorno.
     * 
     * @param expression La expresión que se está retornando desde la función.
     */    
    public ReturnStatementNode(ExpressionNode expression){
        this.expression = expression;
    }

    /**
     * Realiza la verificación semántica del retorno, asegurándose de que el tipo de la expresión
     * coincida con el tipo de retorno esperado para la función.     
     */    
    @Override
    public void checkSemantics() {
        
        if (reducedType == null) {
            throw new RuntimeException("Retorno sin asignación en la función '" + currentHash.split("-")[0] + "'.");
        }        
        
        symbolTable = (SymbolTable) parser.getSymbolTable();
        String currentFunction = currentHash.split("-")[0];
        String returnType = symbolTable.getFunctionSymbols().get(currentFunction).getReturnType();
        
        if (returnType.equals("Char") && reducedType.equals("Integer")) {
            return;
        }        
        
        if (!returnType.equals("a")) {
            if (!returnType.equals(reducedType)) {
                // Levantar un error si los tipos no coinciden
                throw new RuntimeException("El valor de retorno de la función '" 
                                            + currentFunction + "' se esperaba de tipo " + returnType 
                                            + ", pero se trató de devolver de tipo " + reducedType);  
 
            }
        }        
    }

    /**
     * Genera el código MIPS correspondiente a la declaración de retorno.
     *   
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        if(expression == null){
            return "";
        } 
        return expression.generateMIPS(cg);
    }

    /**
     * Representa la declaración de retorno en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de la declaración de retorno, incluyendo la expresión.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Retorna expressión").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "    "));
        
        return sb.toString();
    }    
}

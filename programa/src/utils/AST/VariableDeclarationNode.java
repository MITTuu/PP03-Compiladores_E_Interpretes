package utils.AST;

import utils.SymbolsTable.SymbolTable;
import bin.Parser;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa la declaración de una variable en el árbol sintáctico abstracto (AST).
 * Este nodo maneja tanto declaraciones de variables simples como aquellas que 
 * incluyen una asignación de valor inicial.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class VariableDeclarationNode extends ASTNode{
    String type;
    String id;
    ExpressionNode expression;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    
    /**
     * Constructor para la declaración de una variable sin asignación inicial.
     * 
     * @param type El tipo de la variable.
     * @param id El identificador de la variable.
     */    
    public VariableDeclarationNode(String type, String id){
        this.type = type;
        this.id = id;
        this.expression = null;//En caso que no haya asignación se valida como null
    }
    
    /**
     * Constructor para la declaración de una variable con asignación inicial.
     * 
     * @param type El tipo de la variable.
     * @param id El identificador de la variable.
     * @param expression La expresión que se asigna a la variable durante la declaración.
     */
    public VariableDeclarationNode(String type, String id, ExpressionNode expression){
        this.type = type;
        this.id = id;
        this.expression = expression;
    }
    
    /**
     * Realiza la verificación semántica de la declaración de la variable, comprobando que no 
     * exista una variable con el mismo identificador en el mismo contexto (función).
     * 
     * @throws RuntimeException Si la variable ya está declarada en el mismo contexto.
     */    
    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();
        String functionName = currentHash.split("-")[0];
        
        if (symbolTable.containsVariableKey(id, functionName)) {
            int line = symbolTable.getVariableLine(id, functionName);
            throw new RuntimeException("La variable '" + id + "' ya está declarada en la línea " + line);
        }
    }

    /**
     * Genera el código MIPS correspondiente a la declaración de la variable.
     * Este método no está implementado en esta clase.
     * 
     * @param cg El generador de código MIPS.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        if(expression == null)return "";
        
        return expression.generateMIPS(cg);
    }

    /**
     * Representa el nodo de declaración de la variable en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de la declaración de la variable.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("├── Tipo: ").append(type).append("\n");
        if(expression !=null){
            sb.append(indent).append("├── Id: ").append(id).append("\n");
            sb.append(indent).append("└── Asignación").append("\n");
            sb.append(expression.toString(indent.replace("|", " ") + "    "));
        }else{
            sb.append(indent).append("└── Id: ").append(id).append("\n");
        }        
        return sb.toString();
    }
    
}

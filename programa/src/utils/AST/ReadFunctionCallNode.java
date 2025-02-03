package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa una llamada a la función de lectura en el árbol sintáctico abstracto (AST).
 * Esta clase es utilizada para representar las expresiones que realizan operaciones de lectura en el programa,
 * como la entrada de datos por parte del usuario.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class ReadFunctionCallNode extends ASTNode {
    
    ExpressionNode expression;
    
    /**
     * Crea un nuevo nodo para una llamada a la función de lectura.
     * 
     * @param expression La expresión en la que se almacenará el valor leído.
     */    
    public ReadFunctionCallNode(ExpressionNode expression){
        this.expression = expression;
    }
    
    /**
     * Realiza la verificación semántica de la llamada a la función de lectura.
     *    
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código MIPS correspondiente a la llamada de lectura.
     *      
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Representa la llamada a la función de lectura en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de la llamada a la función de lectura, incluyendo la expresión.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Lee expressión").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "    "));
        
        return sb.toString();
    }    
}
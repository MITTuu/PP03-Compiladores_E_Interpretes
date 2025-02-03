package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa una llamada a la función de impresión en el árbol sintáctico abstracto (AST).
 * Este nodo se utiliza para representar una instrucción que imprime una expresión en la salida.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class PrintFunctionCallNode extends ASTNode {
    
    ExpressionNode expression;

    /**
     * Constructor para inicializar el nodo de llamada a la función de impresión con la expresión a imprimir.
     * 
     * @param expression La expresión que se va a imprimir.
     */    
    public PrintFunctionCallNode(ExpressionNode expression){
        this.expression = expression;
    }
    
    /**
     * Verifica la semántica de la llamada a la función de impresión.
     * Este método no está implementado en esta clase y lanza una excepción si es llamado.
     * 
     * @throws UnsupportedOperationException si se llama a este método en esta clase.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código MIPS para la llamada a la función de impresión.
     * Este método no está implementado en esta clase y lanza una excepción si es llamado.
     * 
     * @param cg El generador de código utilizado para generar el código MIPS.
     * @throws UnsupportedOperationException si se llama a este método en esta clase.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        if(expression != null){
            return expression.generateMIPS(cg);
        }
        return "";
    }

    /**
     * Representa la llamada a la función de impresión en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de la llamada a la función de impresión.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Imprime expressión").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "    "));
        
        return sb.toString();
    }    
}

package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa el uso de un arreglo dentro del Árbol de Sintaxis Abstracta (AST).
 * 
 * Este nodo almacena la referencia a un arreglo y una expresión que indica 
 * la posición del elemento dentro del mismo.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class ArrayUseNode extends ASTNode{
    public String id;
    public ExpressionNode expression;
    
    /**
     * Constructor que inicializa el nodo con el identificador del arreglo 
     * y la expresión que indica la posición del elemento dentro del mismo.
     * 
     * @param id Identificador del arreglo.
     * @param expression Expresión que representa la posición del elemento dentro del arreglo.
     */    
    public ArrayUseNode (String id, ExpressionNode expression){
        this.id = id;
        this.expression = expression;
    }

    /**
     * Verifica la semántica del acceso al arreglo.
     * 
     * @throws UnsupportedOperationException Método aún no implementado.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código en ensamblador MIPS para el acceso a un elemento del arreglo.
     * 
     * @param cg Generador de código utilizado para la conversión a MIPS.
     * @throws UnsupportedOperationException Método aún no implementado.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera una representación en cadena del acceso al arreglo.
     * 
     * @param indent Espaciado utilizado para la indentación visual.
     * @return Representación estructurada en texto del nodo.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Arreglo: ").append("\n");
        sb.append(indent).append("    ").append("├── Id: ").append(id).append("\n");
        sb.append(indent).append("    ").append("└── Position: ").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "        "));     
        return sb.toString();
    }
    
}

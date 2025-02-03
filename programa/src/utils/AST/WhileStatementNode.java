package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa una declaración de estructura de control de tipo "while" en el árbol sintáctico abstracto (AST).
 * Este nodo maneja una expresión condicional y un conjunto de instrucciones que se ejecutan mientras 
 * la condición sea verdadera.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class WhileStatementNode extends ASTNode{
    ExpressionNode condition;
    List<BodyNode> body;

    /**
     * Constructor para crear un nodo de declaración de un ciclo while con una condición y un cuerpo de instrucciones.
     * 
     * @param condition La condición que se evalúa en cada iteración del ciclo while.
     * @param body El bloque de instrucciones que se ejecutan mientras la condición sea verdadera.
     */    
    public WhileStatementNode(ExpressionNode condition, List<BodyNode> body){
     this.condition = condition;
     this.body = body;
    }

    /**
     * Realiza la verificación semántica de la declaración del ciclo while.
     * Este método no está implementado en esta clase.
     * 
     * @throws UnsupportedOperationException Si se invoca este método.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Genera el código MIPS correspondiente al ciclo while.
     * Este método no está implementado en esta clase.
     * 
     * @param cg El generador de código MIPS.
     */
    @Override
    String generateMIPS(CodeGenerator cg) {
        // Validar si alguno de los atributos es null o si la lista está vacía
        if (condition == null ||  body == null || body.isEmpty()) {
            return "";
        }
        String condition = this.condition.generateMIPS(cg);
        String bodyList = "";
        for(BodyNode element : this.body){
            bodyList = bodyList.concat(element.generateMIPS(cg));
        }
        return  condition +  bodyList;
    }

    /**
     * Representa el nodo de declaración del ciclo while en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena del ciclo while, incluyendo la condición y el cuerpo de instrucciones.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── While - Estructura de Control").append("\n");
        sb.append(indent).append("    ├── Condición:\n");
        sb.append(condition.toString(indent + "    │   "));
        sb.append(indent).append("    └── While - Bloque de instrucciones:\n");
        for (int i = 0; i < body.size(); i++) {
            if(i+1 == body.size()){
                sb.append(indent).append("        └── ").append(body.get(i).toString(indent+"        "));
            }else{
                sb.append(indent).append("        ├── ").append(body.get(i).toString(indent+"        │"));
            }
        }
        return sb.toString();
    }
}

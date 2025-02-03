package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa un nodo en el árbol sintáctico abstracto (AST) correspondiente a una
 * declaración de una estructura de control `for` en el lenguaje de programación.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class ForStatementNode extends ASTNode {

    ASTNode initialization;
    ExpressionNode condition;
    VariableAssignmentNode updateExpression;
    List<BodyNode> body;
    
    /**
     * Constructor que inicializa un nodo de tipo `ForStatementNode` con los componentes
     * de la estructura de control `for`.
     * 
     * @param initialization Nodo que representa la inicialización del ciclo `for`.
     * @param condition Nodo que representa la condición de continuación del ciclo `for`.
     * @param updateExpression Nodo que representa la expresión de actualización del ciclo `for`.
     * @param body Lista de nodos que representan el cuerpo del ciclo `for`.
     */    
    public ForStatementNode(ASTNode initialization, ExpressionNode condition, VariableAssignmentNode updateExpression, List<BodyNode> body) {
        this.initialization = initialization;
        this.condition = condition;
        this.updateExpression = updateExpression;
        this.body = body;
    }    
    
    /**
     * Verifica la semántica de la estructura de control `for`. Actualmente, no está implementado.
     * 
     * @throws UnsupportedOperationException Si se invoca este método, ya que no está implementado.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código MIPS correspondiente para la estructura de control `for`. Actualmente, no está implementado.
     * 
     * @param cg El generador de código MIPS.     
     * @throws UnsupportedOperationException Si se invoca este método, ya que no está implementado.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        // Validar si alguno de los atributos es null o si la lista está vacía
        if (initialization == null || condition == null || updateExpression == null || 
            body == null || body.isEmpty()) {
            return "";
        }
        String initialization = this.initialization.generateMIPS(cg);
        String condition = this.condition.generateMIPS(cg);
        String updateExpression = this.updateExpression.generateMIPS(cg);
        String bodyList = "";
        for(BodyNode element : this.body){
            bodyList = bodyList.concat(element.generateMIPS(cg));
        }
        return initialization + condition+ updateExpression + bodyList;
    }

    /**
     * Genera una representación en forma de cadena de la estructura de control `for`
     * de manera jerárquica, indicando la inicialización, condición, expresión de actualización
     * y cuerpo del ciclo.
     * 
     * @param indent La cadena de indentación que se utilizará para representar la jerarquía.
     * @return Una cadena con la representación jerárquica del ciclo `for`.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── For - Estructura de Control").append("\n");
        sb.append(indent).append("    ├── Inicialización:\n");
        sb.append(initialization.toString(indent + "    │   "));
        sb.append(indent).append("    ├── Condición:\n");
        sb.append(condition.toString(indent + "    │   "));
        sb.append(indent).append("    ├── Expresión de actualización:\n");
        sb.append(updateExpression.toString(indent + "    │   "));
        sb.append(indent).append("    └── For - Bloque de instrucciones:\n");
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

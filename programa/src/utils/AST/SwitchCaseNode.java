package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa un caso dentro de una declaración switch en el árbol sintáctico abstracto (AST).
 * Este nodo puede representar tanto una etiqueta de caso específica como un caso por defecto.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class SwitchCaseNode extends ASTNode {

    String type;//Tipo de String: Case ó Default
    ExpressionNode caseLabel;
    List<BodyNode> body;
    
    /**
     * Crea un nuevo nodo para un caso dentro de una declaración switch.
     * 
     * @param type El tipo de caso ("Case" o "Default").
     * @param caseLabel La etiqueta del caso que se evalúa.
     * @param body Las instrucciones que se ejecutan si el caso se cumple.
     */    
    public SwitchCaseNode(String type, ExpressionNode caseLabel, List<BodyNode> body) {
        this.type = type;
        this.caseLabel = caseLabel;
        this.body = body;
    }    
    
    /**
     * Realiza la verificación semántica del caso en la declaración switch.
     * Este método no está implementado en esta clase.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código MIPS correspondiente al caso de la declaración switch.
     * Este método no está implementado en esta clase.
     * 
     * @param cg El generador de código MIPS.     
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Representa el nodo del caso en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena del caso dentro de la declaración switch.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Etiqueta de caso:\n");
        sb.append(caseLabel.toString(indent + "    │   "));
        sb.append(indent).append("    └── Case - Bloque de instrucciones:\n");
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

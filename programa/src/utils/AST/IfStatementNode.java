package utils.AST;

import java.util.ArrayList;
import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa una sentencia de control "if" en el árbol sintáctico abstracto (AST).
 * Esta clase soporta varios casos de estructuras de control "if" con diferentes combinaciones
 * de cláusulas "else" y "elseif".
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class IfStatementNode extends ASTNode {
    ExpressionNode condition;
    List<BodyNode> ifBody;
    List<IfStatementNode> elseIfBranches;
    List<BodyNode> finalElseBody;
    private int ifStatementCase = 0;//Revisar producciones casos 3 y 4

    /**
     * Constructor para un solo "if" sin "else" ni "elseif".
     * 
     * @param condition La condición del "if".
     * @param ifBody El bloque de instrucciones asociado al "if".
     */
    public IfStatementNode(ExpressionNode condition, List<BodyNode> ifBody) {
        ifStatementCase =1;
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseIfBranches = new ArrayList<>();
        this.finalElseBody = null;
    }
    
    /**
     * Constructor para un "if" con un bloque "else".
     * 
     * @param ifStatementNode El "if" original.
     * @param elseBody El bloque de instrucciones del "else".
     */
    public IfStatementNode(IfStatementNode ifStatementNode, List<BodyNode> elseBody) {
        ifStatementCase = 2;
        this.condition = ifStatementNode.condition;
        this.ifBody = ifStatementNode.ifBody;
        this.finalElseBody = elseBody;
        this.elseIfBranches = new ArrayList<>();
    }
    
    /**
     * Constructor para un "if" con una cadena de "elseif" y sin "else".
     * 
     * @param ifStatementNode El "if" original.
     * @param elseIfBranches Lista de ramas "elseif".
     * @param statementCase El caso de la estructura de control (indica si hay "elseif").
     */
    public IfStatementNode(IfStatementNode ifStatementNode, List<IfStatementNode> elseIfBranches, int statementCase) {
        ifStatementCase = 3;
        this.condition = ifStatementNode.condition;
        this.ifBody = ifStatementNode.ifBody;
        this.finalElseBody = null;
        this.elseIfBranches = elseIfBranches;
    }
    
    /**
     * Constructor para un "else" sin "if" (solo el bloque "else").
     * 
     * @param elseBody El bloque de instrucciones del "else".
     */
    public IfStatementNode(List<BodyNode> elseBody) {
        ifStatementCase = 4;
        this.condition = null;
        this.ifBody = null;
        this.finalElseBody = elseBody;
        this.elseIfBranches = new ArrayList<>();
    }

    /**
     * Verifica la semántica de la sentencia "if".
     * Este método no está implementado en esta clase y lanza una excepción si es llamado.
     * 
     * @throws UnsupportedOperationException si se llama a este método en esta clase.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código MIPS para la sentencia "if".
     * Este método no está implementado en esta clase y lanza una excepción si es llamado.
     * 
     * @param cg El generador de código utilizado para generar el código MIPS.     
     * @throws UnsupportedOperationException si se llama a este método en esta clase.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Representa la sentencia "if" en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de esta sentencia "if".
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        switch (ifStatementCase) {
            case 1: // Caso para solamente un if
                sb.append(indent).append("└── If - Estructura de Control").append("\n");
                sb.append(indent).append("    ├── Condition:\n");
                sb.append(condition.toString(indent + "    │   "));
                sb.append(indent).append("    └── If - Bloque de instrucciones:\n");
                for (int i = 0; i < ifBody.size(); i++) {
                    if(i+1 == ifBody.size()){
                        sb.append(indent).append("        └── ").append(ifBody.get(i).toString(indent+"        "));
                    }else{
                        sb.append(indent).append("        ├── ").append(ifBody.get(i).toString(indent+"        │"));
                    }
                }
                break;

            case 2: // Caso para un if con su else respectivo
                sb.append(indent).append("├── If - Estructura de Control").append("\n");
                sb.append(indent).append("│   ├── Condition:\n");
                sb.append(condition.toString(indent + "│   │   "));
                sb.append(indent).append("│   └── If - Bloque de instrucciones:\n");
                for (int i = 0; i < ifBody.size(); i++) {
                    if(i+1 == ifBody.size()){
                        sb.append(indent).append("│       └── ").append(ifBody.get(i).toString(indent+"│       "));
                    }else{
                        sb.append(indent).append("│       ├── ").append(ifBody.get(i).toString(indent+"│       │"));
                    }
                }
                sb.append(indent).append("└── Else - Bloque de instrucciones:\n");
                for (int i = 0; i < finalElseBody.size(); i++) {
                    if(i+1 == finalElseBody.size()){
                        sb.append(indent).append("        └── ").append(finalElseBody.get(i).toString(indent+"        "));
                    }else{
                        sb.append(indent).append("        ├── ").append(finalElseBody.get(i).toString(indent+"        │"));
                    } 
                }
                break;

            case 3: // Caso para un if con su cadena de elseif sin un else final
                sb.append(indent).append("├── If - Estructura de Control").append("\n");
                sb.append(indent).append("│   ├── Condition:\n");
                sb.append(condition.toString(indent + "│   │   "));
                sb.append(indent).append("│   └── If - Bloque de instrucciones:\n");
                for (int i = 0; i < ifBody.size(); i++) {
                    if(i+1 == ifBody.size()){
                        sb.append(indent).append("       └── ").append(ifBody.get(i).toString(indent+"       "));
                    }else{
                        sb.append(indent).append("       ├── ").append(ifBody.get(i).toString(indent+"       │"));
                    }
                }
                sb.append(indent).append("│   └── Else If - Declaraciones:\n");
                for (IfStatementNode elseIfBranch : elseIfBranches) {
                    sb.append(elseIfBranch.toString(indent.replace("|", " ") + "│       "));
                }
                break;

            case 4: // Caso para cuando solo se incluye un else final
                sb.append(indent).append("└── Else - Bloque de instrucciones:\n");
                for (int i = 0; i < finalElseBody.size(); i++) {
                    if(i+1 == finalElseBody.size()){
                        sb.append(indent).append("        └── ").append(finalElseBody.get(i).toString(indent+"       "));
                    }else{
                        sb.append(indent).append("        ├── ").append(finalElseBody.get(i).toString(indent+"       │"));
                    }
                }
                break;

            default: // Caso de error
                sb.append(indent).append("└── Error: Caso no definido para IfStatementNode\n");
                break;
        }

        return sb.toString();
    }
}


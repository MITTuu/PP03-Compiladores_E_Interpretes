
package utils.AST;

import java.util.ArrayList;
import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;


public class IfStatementNode extends ASTNode {
    ExpressionNode condition;
    List<BodyNode> ifBody;
    List<IfStatementNode> elseIfBranches;
    List<BodyNode> finalElseBody;
    private int ifStatementCase = 0;//Revisar producciones casos 3 y 4

    //Caso para solamente un if
    public IfStatementNode(ExpressionNode condition, List<BodyNode> ifBody) {
        ifStatementCase =1;
        this.condition = condition;
        this.ifBody = ifBody;
        this.elseIfBranches = new ArrayList<>();
        this.finalElseBody = null;
    }
    
    //Caso para  un if con su else respectivo
    public IfStatementNode(IfStatementNode ifStatementNode, List<BodyNode> elseBody) {
        ifStatementCase = 2;
        this.condition = ifStatementNode.condition;
        this.ifBody = ifStatementNode.ifBody;
        this.finalElseBody = elseBody;
        this.elseIfBranches = new ArrayList<>();
    }
    
    //Caso para  un if con su cadena de elseif y opcional puede incluir un else body al final(validar eso)
    public IfStatementNode(IfStatementNode ifStatementNode, List<IfStatementNode> elseIfBranches, int statementCase) {
        ifStatementCase = 3;
        this.condition = ifStatementNode.condition;
        this.ifBody = ifStatementNode.ifBody;
        this.finalElseBody = null;
        this.elseIfBranches = elseIfBranches;
    }
    
    //Caso para  cuando solo se incluye un else final
    public IfStatementNode(List<BodyNode> elseBody) {
        ifStatementCase = 4;
        this.condition = null;
        this.ifBody = null;
        this.finalElseBody = elseBody;
        this.elseIfBranches = new ArrayList<>();
    }

    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

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


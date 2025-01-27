
package utils.AST;

import java.util.List;

public class SwitchStatementNode extends ASTNode{
    ExpressionNode controlExpression;
    List<SwitchCaseNode> caseList;

    public SwitchStatementNode(ExpressionNode controlExpression, List<SwitchCaseNode> caseList) {
        this.controlExpression = controlExpression;
        this.caseList = caseList;
    } 
    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    void generateMIPS() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Switch - Estructura de Control").append("\n");
        sb.append(indent).append("    ├── Expresión de Control:\n");
        sb.append(controlExpression.toString(indent + "    │   "));
        
        sb.append(indent).append("    └── Lista de Casos:\n");
        for (int i = 0; i < caseList.size(); i++) {
            if(i+1 == caseList.size()){
                sb.append(indent).append("        └── ").append(caseList.get(i).toString(indent+"        "));
            }else{
                sb.append(indent).append("        ├── ").append(caseList.get(i).toString(indent+"        │"));
            }
        }
        return sb.toString();
    }
    
}

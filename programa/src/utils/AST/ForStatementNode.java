package utils.AST;

import java.util.List;

public class ForStatementNode extends ASTNode {

    ASTNode initialization;
    ExpressionNode condition;
    VariableAssignmentNode updateExpression;
    List<BodyNode> body;
    
    public ForStatementNode(ASTNode initialization, ExpressionNode condition, VariableAssignmentNode updateExpression, List<BodyNode> body) {
        this.initialization = initialization;
        this.condition = condition;
        this.updateExpression = updateExpression;
        this.body = body;
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

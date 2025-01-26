
package utils.AST;

import java.util.List;

public class WhileStatementNode extends ASTNode{
    ExpressionNode condition;
    List<BodyNode> body;
    
    public WhileStatementNode(ExpressionNode condition, List<BodyNode> body){
     this.condition = condition;
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
                sb.append(indent).append("└── While - Estructura de Control").append("\n");
                sb.append(indent).append("    ├── Condition:\n");
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

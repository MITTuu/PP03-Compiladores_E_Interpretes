package utils.AST;

import java.util.List;

public class FunctionCallNode extends ASTNode{
    
    String name;
    List<ExpressionNode> parameterList;

    public FunctionCallNode(String name, List<ExpressionNode> parameterList) {
        this.name = name;
        this.parameterList = parameterList;
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
        sb.append(indent).append("├── Name: ").append(name).append("\n");        
        
        // Parametros
        sb.append(indent).append("└── Lista de parametros").append("\n");
        
        for (int i = 0; i < parameterList.size(); i++) {
            if(i+1 == parameterList.size()){
                sb.append(indent).append(parameterList.get(i).toString("    "));
            }else{
                sb.append(indent).append(parameterList.get(i).toString("    "));
            }
        }
        return sb.toString();
    }
    
}

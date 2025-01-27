
package utils.AST;

public class PrintFunctionCall extends ASTNode {
    
    ExpressionNode expression;
    
    public PrintFunctionCall(ExpressionNode expression){
        this.expression = expression;
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
        sb.append(indent).append("└── Imprime expressión").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "    "));
        
        return sb.toString();
    }    
}

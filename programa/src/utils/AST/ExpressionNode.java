package utils.AST;

public class ExpressionNode extends ASTNode{
String expression;
    public ExpressionNode(String expression){
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
        return indent +"└── Expresión: "+ expression + "\n";
    }
    
}

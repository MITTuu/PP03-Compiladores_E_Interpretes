package utils.AST;

public class ParameterNode extends ASTNode{

    String variableType;
    String identifier;
    
    public ParameterNode(String variableType, String identifier){
        this.variableType = variableType;
        this.identifier = identifier;
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
        sb.append(indent).append(variableType).append(" ").append(identifier);
        return sb.toString();
    }
    
}

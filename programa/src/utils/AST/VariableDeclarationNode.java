
package utils.AST;


public class VariableDeclarationNode extends ASTNode{
    String type;
    String id;

    public VariableDeclarationNode(String type, String id){
        this.type = type;
        this.id = id;
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
        sb.append(indent).append(type).append(" ").append(id);
        return sb.toString();
    }
    
}

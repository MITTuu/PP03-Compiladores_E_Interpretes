
package utils.AST;


public class VariableDeclarationNode extends ASTNode{
    String type;
    String id;
    ExpressionNode expression;

    public VariableDeclarationNode(String type, String id){
        this.type = type;
        this.id = id;
        this.expression = null;//En caso que no haya asignación se valida como null
    }
    
    //Incluye declaración de variable con asignación.
    public VariableDeclarationNode(String type, String id, ExpressionNode expression){
        this.type = type;
        this.id = id;
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
        sb.append(indent).append("├── Type: ").append(type).append("\n");
        if(expression !=null){
            sb.append(indent).append("├── Id: ").append(id).append("\n");
            sb.append(indent).append("└── Asignación").append("\n");
            sb.append(expression.toString(indent.replace("|", " ") + "    "));
        }else{
            sb.append(indent).append("└── Id: ").append(id).append("\n");
        }        
        return sb.toString();
    }
    
}

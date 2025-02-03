package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;


public class ArrayUseNode extends ASTNode{
    public String id;
    public ExpressionNode expression;
    
    
    public ArrayUseNode (String id, ExpressionNode expression){
        this.id = id;
        this.expression = expression;
    }

    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        if(expression == null)return "";
        return expression.generateMIPS(cg);
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Arreglo: ").append("\n");
        sb.append(indent).append("    ").append("├── Id: ").append(id).append("\n");
        sb.append(indent).append("    ").append("└── Position: ").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "        "));     
        return sb.toString();
    }
    
}

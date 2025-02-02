
package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;


public class ReturnStatementNode extends ASTNode {
    
    ExpressionNode expression;
    
    public ReturnStatementNode(ExpressionNode expression){
        this.expression = expression;
    }
    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Retorna expressión").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "    "));
        
        return sb.toString();
    }    
}

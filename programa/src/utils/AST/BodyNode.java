package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;

public class BodyNode extends ASTNode{
    
    String name;
    ASTNode bodyElement;
    
    public BodyNode (String name, ASTNode bodyElement){
        this.name = name;
        this.bodyElement = bodyElement;
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
        sb.append(name).append("\n");
        sb.append(bodyElement.toString(indent+"     "));
        return sb.toString();
    }
    
}

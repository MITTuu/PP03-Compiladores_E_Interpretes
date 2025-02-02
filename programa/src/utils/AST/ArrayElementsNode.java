package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

public class ArrayElementsNode extends ASTNode {

    List<String> arrayElements;
    
    public ArrayElementsNode(List<String> arrayElements){
        this.arrayElements = arrayElements;
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
        return indent + "└── Elementos de Array " +arrayElements.toString() +" \n";
    }
    
}

package utils.AST;

import java.util.List;

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
    void generateMIPS() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        return indent + "└── Elementos de Array " +arrayElements.toString() +" \n";
    }
    
}

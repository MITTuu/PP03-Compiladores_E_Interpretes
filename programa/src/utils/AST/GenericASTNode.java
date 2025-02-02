package utils.AST;

// Nodo para declaración de variables

import utils.MIPS.GeneracionCodigo.CodeGenerator;

public class GenericASTNode extends ASTNode {
    String name;

    public GenericASTNode(String name) {
        this.name = name;
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
        // Este nodo no tiene hijos, así que simplemente imprime su nombre
        return indent + "GenericoASTNode: " + name + "\n";
    }
}

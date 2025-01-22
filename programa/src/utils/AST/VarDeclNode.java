package utils.AST;

// Nodo para declaración de variables
class VarDeclNode extends ASTNode {
    String name;

    VarDeclNode(String name) {
        this.name = name;
    }

    @Override
    void checkSemantics() {
        // Nada que verificar en este ejemplo (solo se asegura que exista en otros contextos)
    }

    @Override
    void generateMIPS() {
        System.out.println("  # Reservar espacio para la variable " + name);
        System.out.println("  # Esto depende del modelo de memoria usado");
    }
    
    @Override
    String toString(String indent) {
        // Este nodo no tiene hijos, así que simplemente imprime su nombre
        return indent + "VarDeclNode: " + name + "\n";
    }
}

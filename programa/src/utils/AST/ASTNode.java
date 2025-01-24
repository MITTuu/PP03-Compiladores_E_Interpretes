package utils.AST;

// Nodo base para el AST
public abstract class ASTNode {   
    abstract void checkSemantics(); // Verifica tipos y errores
    abstract void generateMIPS();   // Genera el código MIPS
    abstract String toString(String indent); //Genera un string de los nodos
}
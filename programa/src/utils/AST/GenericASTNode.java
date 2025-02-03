package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa un nodo genérico en el árbol sintáctico abstracto (AST).
 * Esta clase es utilizada como una representación básica de un nodo, que puede
 * ser extendida para agregar más funcionalidad o detalles a nodos específicos.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class GenericASTNode extends ASTNode {
    String name;

    /**
     * Constructor para crear un nodo genérico con un nombre.
     * 
     * @param name El nombre del nodo.
     */    
    public GenericASTNode(String name) {
        this.name = name;
    }

    /**
     * Método para verificar la semántica del nodo.
     * Este método lanza una excepción porque no está implementado para este nodo genérico.
     * 
     * @throws UnsupportedOperationException si se llama a este método en un nodo genérico.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Genera el código MIPS correspondiente para este nodo.
     * Este método lanza una excepción porque no está implementado para este nodo genérico.
     * 
     * @param cg El generador de código utilizado para generar el código MIPS.     
     * @throws UnsupportedOperationException si se llama a este método en un nodo genérico.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Representa este nodo en formato de cadena, con una indentación específica.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de este nodo genérico.
     */
    @Override
    String toString(String indent) {
        // Este nodo no tiene hijos, así que simplemente imprime su nombre
        return indent + "GenericoASTNode: " + name + "\n";
    }
}

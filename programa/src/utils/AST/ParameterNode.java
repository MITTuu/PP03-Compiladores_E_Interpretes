package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa un parámetro en una declaración de función o método en el árbol sintáctico abstracto (AST).
 * Un parámetro consta de un tipo de variable y un identificador (nombre de la variable).
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class ParameterNode extends ASTNode{

    String variableType;
    String identifier;
    
    /**
     * Constructor para inicializar un parámetro con su tipo y nombre.
     * 
     * @param variableType El tipo de la variable (ej., "int").
     * @param identifier El nombre del parámetro (ej., "x").
     */    
    public ParameterNode(String variableType, String identifier){
        this.variableType = variableType;
        this.identifier = identifier;
    }
   
    /**
     * Verifica la semántica del parámetro.
     * Este método no está implementado en esta clase y lanza una excepción si es llamado.
     * 
     * @throws UnsupportedOperationException si se llama a este método en esta clase.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Genera el código MIPS para este parámetro.
     * Este método no está implementado en esta clase y lanza una excepción si es llamado.
     * 
     * @param cg El generador de código utilizado para generar el código MIPS.
     * @throws UnsupportedOperationException si se llama a este método en esta clase.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Representa el parámetro en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena del parámetro.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append(variableType).append(" ").append(identifier);
        return sb.toString();
    }
    
}

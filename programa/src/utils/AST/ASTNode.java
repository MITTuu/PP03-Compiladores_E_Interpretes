package utils.AST;

import java.util.ArrayList;
import java.util.List;
import utils.SymbolsTable.SymbolTable;
import utils.MIPS.GeneracionCodigo.*;

/**
 * Representa un nodo base para el Árbol de Sintaxis Abstracta (AST).
 * Proporciona la estructura y métodos fundamentales para la verificación semántica
 * y la generación de código MIPS.
 * 
 * Esta clase es abstracta y debe ser extendida por otros nodos específicos del AST.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public abstract class ASTNode {   

    public static List<String> semanticErrorListAST = new ArrayList<String>();
    public SymbolTable symbolTableAST = new SymbolTable();
    LiteralGenerator literalGenerator = new LiteralGenerator();
    static CodeGenerator cg = new CodeGenerator();
    
    /**
     * Verifica la semántica del nodo actual, incluyendo la validación de tipos 
     * y la detección de errores semánticos.
     * 
     * Este método debe ser implementado por las subclases y solo debe analizar 
     * los elementos propios del nodo.
     */
    abstract void checkSemantics();
    /**
     * Genera el código MIPS correspondiente a este nodo.
     * 
     * @param cg Instancia del generador de código.
     * @return Representación en ensamblador MIPS del nodo.
     */    
    abstract String generateMIPS(CodeGenerator cg);

    /**
     * Devuelve una representación en cadena del nodo AST, incluyendo su estructura jerárquica.
     * 
     * @param indent Espaciado utilizado para la indentación visual.
     * @return Representación en formato de texto del nodo.
     */    
    abstract String toString(String indent); 
    
    /**
     * Agrega un error semántico a la lista de errores global del AST.
     * 
     * @param error Mensaje de error a registrar.
     */
    public void logSemanticError(String error) {
        semanticErrorListAST.add(error);
    }
    
    /**
     * Obtiene la lista de errores semánticos registrados en el AST.
     * 
     * @return Lista de errores semánticos detectados.
     */    
    public List<String> getSemanticErrorList(){
        return ASTNode.semanticErrorListAST;
    }

    /**
     * Asigna una nueva tabla de símbolos a este nodo del AST.
     * 
     * @param symbolTable La nueva tabla de símbolos.
     */    
    public void setSymbolTable(SymbolTable symbolTable){
        this.symbolTableAST = symbolTable;
    }
}
package utils.AST;

// Nodo base para el AST

import java.util.ArrayList;
import java.util.List;
import utils.SymbolsTable.SymbolTable;

public abstract class ASTNode {   
    //Lista de errores semánticos 
    public static List<String> semanticErrorListAST = new ArrayList<String>();
    
    //Tabla de simbolos para almacenar los alcances, tipo y valores de las variables y funciones
    public SymbolTable symbolTableAST = new SymbolTable();
    
    /**
     * Este método verifica tipos y errores
     *Debe limitarse a analizar los propios elementos de la clase 
     */
    abstract void checkSemantics(); // Verifica tipos y errores
    abstract void generateMIPS();   // Genera el código MIPS
    abstract String toString(String indent); //Genera un string de los nodos
    
    // Agrega errores semánticos a la lista
    public void logSemanticError(String error) {
        semanticErrorListAST.add(error);
    }
    
    public List<String> getSemanticErrorList(){
        return ASTNode.semanticErrorListAST;
    }
    
    public void setSymbolTable(SymbolTable symbolTable){
        this.symbolTableAST = symbolTable;
    }
}
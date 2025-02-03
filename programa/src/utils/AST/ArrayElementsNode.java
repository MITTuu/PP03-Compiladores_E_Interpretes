package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa los elementos de un arreglo en el Árbol de Sintaxis Abstracta (AST).
 * 
 * Esta clase almacena y gestiona los valores que se utilizarán para inicializar un arreglo.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class ArrayElementsNode extends ASTNode {

    List<String> arrayElements;
    
    /**
     * Constructor que inicializa el nodo con los elementos del arreglo.
     * 
     * @param arrayElements Lista de elementos con los que se inicializa el arreglo.
     */    
    public ArrayElementsNode(List<String> arrayElements){
        this.arrayElements = arrayElements;
    }

    /**
     * Verifica la semántica de los elementos del arreglo.
     * 
     * @throws UnsupportedOperationException Método aún no implementado.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código en ensamblador MIPS para la inicialización del arreglo.
     * 
     * @param cg Generador de código utilizado para la conversión a MIPS.
     * @throws UnsupportedOperationException Método aún no implementado.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        return "# Pendiente. Generar MIPS para arreglo de de elementos String de ArrayElementsNode \n"; 
    }

    /**
     * Genera una representación en cadena de los elementos del arreglo.
     * 
     * @param indent Espaciado utilizado para la indentación visual.
     * @return Representación estructurada en texto del nodo.
     */    
    @Override
    String toString(String indent) {
        return indent + "└── Elementos de Array " +arrayElements.toString() +" \n";
    }
    
}

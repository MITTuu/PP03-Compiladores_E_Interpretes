package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa el cuerpo de una estructura dentro del Árbol de Sintaxis Abstracta (AST).
 * 
 * Este nodo almacena el nombre del bloque y un elemento del cuerpo, 
 * que puede ser otra estructura o una instrucción.
 * 
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2024
 */
public class BodyNode extends ASTNode{
    
    String name;
    ASTNode bodyElement;
    
    /**
     * Constructor que inicializa el nodo con el nombre del cuerpo y su contenido.
     * 
     * @param name Nombre del bloque o estructura.
     * @param bodyElement Nodo que representa el contenido del cuerpo.
     */    
    public BodyNode (String name, ASTNode bodyElement){
        this.name = name;
        this.bodyElement = bodyElement;
    }

    /**
     * Verifica la semántica del cuerpo de la estructura.
     * 
     * @throws UnsupportedOperationException Método aún no implementado.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código en ensamblador MIPS para la estructura del cuerpo.
     * 
     * @param cg Generador de código utilizado para la conversión a MIPS.
     * @throws UnsupportedOperationException Método aún no implementado.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        if(bodyElement == null)return "";
        return bodyElement.generateMIPS(cg);
    }

    /**
     * Genera una representación en cadena del cuerpo de la estructura.
     * 
     * @param indent Espaciado utilizado para la indentación visual.
     * @return Representación estructurada en texto del nodo.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\n");
        sb.append(bodyElement.toString(indent+"     "));
        return sb.toString();
    }
    
}

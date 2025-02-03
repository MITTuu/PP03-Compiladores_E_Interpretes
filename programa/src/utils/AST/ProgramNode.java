package utils.AST;

import java.util.ArrayList;
import java.util.List;
import utils.AST.ASTNode;
import utils.AST.FunctionNode;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa un programa completo en el árbol sintáctico abstracto (AST).
 * Este nodo sirve como el nodo raíz del AST que contiene todas las funciones del programa.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class ProgramNode extends ASTNode {
    List<FunctionNode> functions = new ArrayList<>();

    /**
     * Agrega una nueva función al programa.
     * 
     * @param fn La función que se va a agregar al programa.
     */    
    public void addFunction(FunctionNode fn) {
        functions.add(fn);
    }

    /**
     * Verifica la semántica del programa, validando las funciones dentro del programa.
     * 
     * @throws RuntimeException si alguna de las funciones tiene errores semánticos.
     */
    @Override
    public void checkSemantics() {
        for (FunctionNode fn : functions) {
            fn.checkSemantics();
        }
    }

    /**
     * Genera el código MIPS para todas las funciones dentro del programa.
     * 
     * @param cg El generador de código utilizado para generar el código MIPS.     
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        for (FunctionNode fn : functions) {
            fn.generateMIPS(cg);
        }
        
        return "";
    }
    
    /**
     * Representa el programa completo en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena del programa completo, incluyendo sus funciones.
     */    
    @Override
    public String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("Programa\n");
        for (FunctionNode fn : functions) {
            
        }
        for (int i = 0; i < functions.size(); i++) {
                    if(i+1 == functions.size()){
                        sb.append(indent).append("└── ").append(functions.get(i).toString(indent+"        "));
                    }else{
                        sb.append(indent).append("├── ").append(functions.get(i).toString(indent+"│       "));
                    } 
                }
        return sb.toString();
    }
}
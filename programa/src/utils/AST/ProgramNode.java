package utils.AST;

import java.util.ArrayList;
import java.util.List;
import utils.AST.ASTNode;
import utils.AST.FunctionNode;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

// Nodo para el programa completo
public class ProgramNode extends ASTNode {
    List<FunctionNode> functions = new ArrayList<>();

    public void addFunction(FunctionNode fn) {
        functions.add(fn);
    }

    @Override
    public void checkSemantics() {
        for (FunctionNode fn : functions) {
            fn.checkSemantics();
        }
    }

    @Override
    public String generateMIPS(CodeGenerator cg) {
        if(functions == null || functions.isEmpty()){return "";}
        String result = "";
        for (FunctionNode fn : functions) {
            result = result.concat(fn.generateMIPS(cg));
        }
        
        //Incluímos las instrucciones finales para indicar que el programa corrió con existe
        //Además incluye el codigo final para indicar que finaliza el programa.
        
         String strLabel = "finalMsg";
         String mensajeString ="\"Ejecución del programa terminada con éxito\"";
         cg.addDataSection(strLabel + ": .asciiz " + mensajeString + " # Almacena el String en memoria para el mensaje final\n");
         result = result + "\n\n    # Imprimir mensaje final\n" + "la $a0, finalMsg\n" + "li $v0, 4  # Código de servicio para imprimir string\n" + "syscall\n\n" ;
         result = result + "\n\n    # Terminar el programa\n" + "li $v0, 10" + "# Código de servicio para salir\n" + "syscall\n" ;
        
        return result;
    }
    
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
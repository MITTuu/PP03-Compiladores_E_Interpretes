package utils.AST;

import java.util.ArrayList;
import java.util.List;
import utils.AST.ASTNode;
import utils.AST.FunctionNode;

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
    void generateMIPS() {
        for (FunctionNode fn : functions) {
            fn.generateMIPS();
        }
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
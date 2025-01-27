package utils.AST;

import bin.Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.SymbolsTable.FunctionSymbol;
import utils.SymbolsTable.SymbolTable;

public class FunctionCallNode extends ASTNode{
    
    String name;
    List<ExpressionNode> parameterList;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    
    public FunctionCallNode(String name, List<ExpressionNode> parameterList) {
        this.name = name;
        this.parameterList = parameterList;
    }

    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();
        // Verificar si la función existe en la tabla de símbolos
        if (!symbolTable.containsFunctionKey(name)) {
            throw new RuntimeException("Error semántico: La función '" + name + "' no está definida.");
        }

        // Obtener la definición de la función desde la tabla de símbolos
        FunctionSymbol function = symbolTable.getFunctionSymbols().get(name);

        // Convertir los parámetros de la función en una lista de tipos
        List<String> expectedParams = new ArrayList<>(Arrays.asList(function.getParameters().split(",\\s*")));

        // Verificar la cantidad de parámetros
        if (expectedParams.size() != parameterList.size()) {
            throw new RuntimeException("La función '" + name + "' espera " + 
                                       expectedParams.size() + " parámetros, pero se pasaron " + 
                                       parameterList.size() + ".");
        }
    }

    @Override
    void generateMIPS() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("├── Name: ").append(name).append("\n");        
        
        // Parametros
        sb.append(indent).append("└── Lista de parametros").append("\n");
        
        for (int i = 0; i < parameterList.size(); i++) {
            if(i+1 == parameterList.size()){
                sb.append(indent).append(parameterList.get(i).toString("    "));
            }else{
                sb.append(indent).append(parameterList.get(i).toString("    "));
            }
        }
        return sb.toString();
    }
    
}

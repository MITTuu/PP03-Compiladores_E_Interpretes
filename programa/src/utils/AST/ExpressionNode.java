package utils.AST;

import utils.SymbolsTable.SymbolTable;
import utils.SymbolsTable.VariableSymbol;

public class ExpressionNode extends ASTNode{
    String expression;
    
    public ExpressionNode(String expression){
        this.expression = expression;
    }
    
    public String getType(SymbolTable symbolTable) {
        String[] parts = expression.split(":");

        if (parts.length == 2) {
            String type = parts[0].trim();
            String value = parts[1].trim();

            if (type.equals("Id")) {
                VariableSymbol variable = symbolTable.getVariableSymbols().get(value);
                if (variable != null) {
                    return variable.getType();
                } else {
                    throw new RuntimeException("Error semántico: La variable '" + value + "' no está definida.");
                }
            }

            return type;
        }

        throw new RuntimeException("Error semántico: La expresión '" + expression + "' no tiene un formato válido.");
    }
    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    void generateMIPS() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        return indent +"└── Expresión: "+ expression + "\n";
    }

    @Override
    public String toString() {
        return expression;
    }
}


package utils.AST;

import utils.MIPS.GeneracionCodigo.CodeGenerator;
import bin.Parser;
import utils.SymbolsTable.SymbolTable;


public class ReturnStatementNode extends ASTNode {
    
    ExpressionNode expression;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    public String reducedType;
    
    public ReturnStatementNode(ExpressionNode expression){
        this.expression = expression;
    }
    
    @Override
    public void checkSemantics() {
        
        if (reducedType == null) {
            throw new RuntimeException("Retorno sin asignación en la función '" + currentHash.split("-")[0] + "'.");
        }        
        
        symbolTable = (SymbolTable) parser.getSymbolTable();
        String currentFunction = currentHash.split("-")[0];
        String returnType = symbolTable.getFunctionSymbols().get(currentFunction).getReturnType();
        
        if (returnType.equals("Char") && reducedType.equals("Integer")) {
            return;
        }        
        
        if (!returnType.equals("a")) {
            if (!returnType.equals(reducedType)) {
                // Levantar un error si los tipos no coinciden
                throw new RuntimeException("El valor de retorno de la función '" 
                                            + currentFunction + "' se esperaba de tipo " + returnType 
                                            + ", pero la se trató de devolver de tipo " + reducedType);  
 
            }
        }        
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Retorna expressión").append("\n");
        sb.append(expression.toString(indent.replace("|", " ") + "    "));
        
        return sb.toString();
    }    
}

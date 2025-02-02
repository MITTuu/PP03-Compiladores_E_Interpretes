
package utils.AST;

import utils.SymbolsTable.SymbolTable;
import bin.Parser;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

public class VariableDeclarationNode extends ASTNode{
    String type;
    String id;
    ExpressionNode expression;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    
    public VariableDeclarationNode(String type, String id){
        this.type = type;
        this.id = id;
        this.expression = null;//En caso que no haya asignación se valida como null
    }
    
    //Incluye declaración de variable con asignación.
    public VariableDeclarationNode(String type, String id, ExpressionNode expression){
        this.type = type;
        this.id = id;
        this.expression = expression;
    }
    
    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();
        String functionName = currentHash.split("-")[0];
        
        if (symbolTable.containsVariableKey(id, functionName)) {
            int line = symbolTable.getVariableLine(id, functionName);
            throw new RuntimeException("La variable '" + id + "' ya está declarada en la línea " + line);
        }
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("├── Tipo: ").append(type).append("\n");
        if(expression !=null){
            sb.append(indent).append("├── Id: ").append(id).append("\n");
            sb.append(indent).append("└── Asignación").append("\n");
            sb.append(expression.toString(indent.replace("|", " ") + "    "));
        }else{
            sb.append(indent).append("└── Id: ").append(id).append("\n");
        }        
        return sb.toString();
    }
    
}

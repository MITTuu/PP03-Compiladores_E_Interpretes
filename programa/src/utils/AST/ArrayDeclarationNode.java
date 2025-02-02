package utils.AST;

import bin.Parser;
import utils.MIPS.GeneracionCodigo.CodeGenerator;
import utils.SymbolsTable.SymbolTable;

public class ArrayDeclarationNode extends ASTNode {
    
    String type;
    String id;
    String size;
    ArrayElementsNode arrayElements;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    
    public ArrayDeclarationNode (String type, String id){
        this.type = type;
        this.id = id;
        this.size = null;
        this.arrayElements = null;
    }
    
    public ArrayDeclarationNode (String type, String id, String size){
        this.type = type;
        this.id = id;
        
        /*El tanaño del arreglo puede venir de los diferentes tipos: IntegerLiteral ó como Id de una variable  */
        this.size = size;
        this.arrayElements = null;
    }
    
    public ArrayDeclarationNode (ArrayDeclarationNode arrayDeclNode, ArrayElementsNode arrayElements){
        this.type = arrayDeclNode.type;
        this.id = arrayDeclNode.id;
        
        /*El tanaño del arreglo puede venir de los diferentes tipos: IntegerLiteral ó como Id de una variable */
        /*Puede ser null incluso*/
        this.size = arrayDeclNode.size;
        
        this.arrayElements = arrayElements;
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
        sb.append(indent).append("├── Arreglo de tipo: ").append(type).append("\n");
        if (size ==null && arrayElements == null){
            sb.append(indent).append("└── Id: ").append(id).append("\n");
        }else if (size !=null && arrayElements == null){
            sb.append(indent).append("├── Id: ").append(id).append("\n");
            sb.append(indent).append("└── Tamaño: ").append(size).append("\n");
        }else if (size ==null && arrayElements != null){
            sb.append(indent).append("├── Id: ").append(id).append("\n");
            sb.append(indent).append("└── Inicialización de array ").append(arrayElements.toString(indent+ "    ")).append("\n");
        }else if (size !=null && arrayElements != null){
            sb.append(indent).append("├── Id: ").append(id).append("\n");
            sb.append(indent).append("├── Tamaño: ").append(size).append("\n");
            sb.append(indent).append("└── Inicialización de array \n").append(arrayElements.toString(indent+ "    "));
        }else{
            sb.append(indent).append("└── Error de datos en ArrayDeclarationNode ").append("\n");
        }       
        return sb.toString();
    }  
}

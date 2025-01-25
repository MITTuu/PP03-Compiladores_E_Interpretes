package utils.AST;

public class ArrayDeclarationNode extends ASTNode {
    
    String type;
    String id;
    String size;
    ArrayElementsNode arrayElements;
    
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
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    void generateMIPS() {
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

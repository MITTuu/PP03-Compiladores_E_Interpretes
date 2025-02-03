package utils.AST;

import bin.Parser;
import utils.MIPS.GeneracionCodigo.CodeGenerator;
import utils.SymbolsTable.SymbolTable;

/**
 * Representa la declaración de un arreglo en el Árbol de Sintaxis Abstracta (AST).
 * 
 * Esta clase permite manejar la declaración de arreglos con o sin tamaño explícito,
 * y también la inicialización con elementos específicos.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class ArrayDeclarationNode extends ASTNode {
    
    String type;
    String id;
    String size;
    ArrayElementsNode arrayElements;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    
    /**
     * Constructor para la declaración de un arreglo sin tamaño ni inicialización explícita.
     * 
     * @param type Tipo de dato del arreglo.
     * @param id Identificador del arreglo.
     */    
    public ArrayDeclarationNode (String type, String id){
        this.type = type;
        this.id = id;
        this.size = null;
        this.arrayElements = null;
    }

    /**
     * Constructor para la declaración de un arreglo con tamaño definido pero sin inicialización explícita.
     * 
     * @param type Tipo de dato del arreglo.
     * @param id Identificador del arreglo.
     * @param size Tamaño del arreglo, puede ser un literal entero o una variable.
     */    
    public ArrayDeclarationNode (String type, String id, String size){
        this.type = type;
        this.id = id;
        
        /*El tanaño del arreglo puede venir de los diferentes tipos: IntegerLiteral ó como Id de una variable  */
        this.size = size;
        this.arrayElements = null;
    }
   
    /**
     * Constructor para la declaración de un arreglo con inicialización explícita.
     * 
     * @param arrayDeclNode Nodo base con la información de tipo, id y tamaño del arreglo.
     * @param arrayElements Nodo que contiene los elementos con los que se inicializa el arreglo.
     */    
    public ArrayDeclarationNode (ArrayDeclarationNode arrayDeclNode, ArrayElementsNode arrayElements){
        this.type = arrayDeclNode.type;
        this.id = arrayDeclNode.id;
        
        /*El tanaño del arreglo puede venir de los diferentes tipos: IntegerLiteral ó como Id de una variable */
        /*Puede ser null incluso*/
        this.size = arrayDeclNode.size;
        
        this.arrayElements = arrayElements;
    }

    /**
     * Verifica la semántica de la declaración del arreglo.
     * 
     * Se asegura de que la variable no haya sido declarada previamente en el mismo ámbito.
     * 
     * @throws RuntimeException Si la variable ya está declarada, se lanza un error indicando la línea donde ocurrió.
     */    
    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();
        String functionName = currentHash.split("-")[0];
        
        if (symbolTable.containsVariableKey(id, functionName)) {
            int line = symbolTable.getVariableLine(id, functionName);
            throw new RuntimeException("La variable '" + id + "' ya está declarada en la línea " + line);
        }
    }

    /**
     * Genera el código en ensamblador MIPS para la declaración del arreglo.
     * 
     * @param cg Generador de código utilizado para la conversión a MIPS.
     * @return Representación en código MIPS del nodo.
     * @throws UnsupportedOperationException Método no implementado aún.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        if(arrayElements == null)return "";
        return this.arrayElements.generateMIPS(cg);
    }

    /**
     * Genera una representación en cadena del nodo de declaración de arreglo.
     * 
     * @param indent Espaciado utilizado para la indentación visual.
     * @return Representación estructurada en texto del nodo.
     */    
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

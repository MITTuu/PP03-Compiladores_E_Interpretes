package utils.SymbolsTable;

/**
 * Representa una variable en la tabla de símbolos.
 * Contiene información sobre el nombre, tipo, alcance, valor y línea de declaración
 * de una variable en el contexto de un programa.
 * 
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class VariableSymbol {
    private String name;
    private String type;
    private String scope; 
    private Object value; 
    private int declarationLine;
    private int memoryAddress;
   

    /**
     * Constructor que inicializa una nueva instancia de VariableSymbol.
     * 
     * @param name El nombre de la variable.
     * @param type El tipo de la variable.
     * @param scope El alcance donde la variable está declarada.
     * @param value El valor inicial de la variable.
     * @param declarationLine La línea del código fuente donde la variable fue declarada.
     */
    public VariableSymbol(String name, String type, String scope, Object value, int declarationLine) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.value = value;
        this.declarationLine = declarationLine;
    }

    /**
     * Constructor que inicializa una nueva instancia de VariableSymbol.
     * 
     * @param name El nombre de la variable.
     * @param type El tipo de la variable.
     * @param scope El alcance donde la variable está declarada.
     * @param value El valor inicial de la variable.
     * @param declarationLine La línea del código fuente donde la variable fue declarada.
     *  @param memoryAddress La dirección de memoria que fué asignada para ser utilizada por el programa ensamblador.
     */
    public VariableSymbol(String name, String type, String scope, Object value, int declarationLine, int memoryAddress) {
        this.name = name;
        this.type = type;
        this.scope = scope;
        this.value = value;
        this.declarationLine = declarationLine;
        this.memoryAddress = memoryAddress;
    }
  
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getScope() {
        return scope;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
  
    public int getDeclarationLine() {
        return declarationLine;
    }
    
     public int getMemoryAddress() {
        return memoryAddress;
    }

    public void setMemoryAddress(int memoryAddress) {
        this.memoryAddress = memoryAddress;
    }
}

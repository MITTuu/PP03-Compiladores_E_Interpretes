package utils.SymbolsTable;

/**
 * Representa una función en la tabla de símbolos.
 * Contiene información sobre el nombre, tipo de retorno y parámetros de la función.
 * 
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class FunctionSymbol {
    private String name;
    private String returnType;
    private String parameters;

    /**
     * Constructor que inicializa una nueva instancia de FunctionSymbol.
     * 
     * @param name El nombre de la función.
     * @param returnType El tipo de retorno de la función.
     * @param parameters Los parámetros de la función.
     */
    public FunctionSymbol(String name, String returnType, String parameters) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getParameters() {
        return parameters;
    }
}

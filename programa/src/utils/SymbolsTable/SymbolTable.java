package utils.SymbolsTable;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * La clase SymbolTable maneja la tabla de símbolos de un programa, almacenando funciones y variables
 * junto con su información asociada. 
 * 
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class SymbolTable {

    // Mapa para almacenar funciones
    private Map<String, FunctionSymbol> functionSymbols = new HashMap<>();
    
    // Mapa para almacenar variables
    private Map<String, VariableSymbol> variableSymbols = new HashMap<>();

    // Identificador del bloque actual
    public String blockIdentifier;
    
    public String currentFunc;
    
    // Pila para manejar bloques de control
    private Deque<String> blockStack = new ArrayDeque<>();
    
    // Contadores para identificar bloques únicos por tipo
    private Map<String, Integer> blockCounters = new HashMap<>();
    
    // Dirección inicial de almacenamiento de memoria para variables
    private int nextMemoryAddress = 1000; 
    

    /**
     * Constructor de la clase SymbolTable.
     * Inicializa la pila de bloques con el alcance global.
     */
    public SymbolTable() {
        blockStack.push("global");
        currentFunc = "UNKNOWN";
    }
    
    /**
     * Agrega una nueva función a la tabla de símbolos.
     * @param name El nombre de la función.
     * @param returnType El tipo de retorno de la función.
     * @param parameters Los parámetros de la función en formato de cadena.
     * @throws RuntimeException Si la función ya está definida en la tabla de símbolos.
     */
    public void addFunction(String name, String returnType, String parameters) {
        
        if (!currentFunc.equals(name)) {
            currentFunc = name;
            blockStack.pop();
            blockStack.push(name);
        }
        
        if (functionSymbols.containsKey(name)) {
            throw new RuntimeException("La función '" + name + "' ya está definida.");
        }

        // Convierte la lista de parámetros en un arreglo de parámetros
        String[] paramArray = parameters.replaceAll("@&@", " ").replaceAll(";", ", ").split(",\\s*");

        // Invierte el orden de los parámetros
        Collections.reverse(Arrays.asList(paramArray));

        // Vuelve a juntar los parámetros en una cadena con el orden correcto
        String functionParameters = String.join(", ", paramArray);

        // Añade la función a la lista de símbolos
        functionSymbols.put(name, new FunctionSymbol(name, returnType, functionParameters));
    }


    /**
     * Agrega una nueva variable a la tabla de símbolos.
     * @param name El nombre de la variable.
     * @param type El tipo de la variable.
     * @param scope El alcance (bloque) donde la variable es válida.
     * @param value El valor de la variable.
     * @param declarationLine La línea de declaración de la variable en el código fuente.
     * @throws RuntimeException Si la variable ya está definida en la tabla de símbolos.
     */
    
    public void addVariable(String name, String type, String scope, Object value, int declarationLine) {
        String mainScope = scope.split("-")[0];
        String uniqueKey = mainScope + "." + name;

        // Verificar si la variable ya está definida en el mismo alcance
        if (variableSymbols.containsKey(uniqueKey)) {
            throw new RuntimeException("La variable '" + name + "' ya está definida en el alcance '" + mainScope + "'.");
        }
        
        //Obtiene el siguiente identificador de memoria que usará la variable, para ser usado por el ensamblador
        int memoryAddress = getNextMemoryAddress();  
        
        // Agregar la variable con la clave única
        variableSymbols.put(uniqueKey, new VariableSymbol(name, type, scope, value, declarationLine,memoryAddress));
        
        //asigna el siguiente valor a obtener
        setNextMemoryAddress();
    }
  
    /**
     * Actualiza el valor de una variable en la tabla de símbolos.
     * @param id El nombre de la variable.
     * @param asignationScope El nombre de la función (alcance).
     * @param newValue El nuevo valor de la variable.
     * @throws RuntimeException Si la variable no existe en el alcance proporcionado.
     */
    public void updateVariableValue(String id, String asignationScope, Object newValue) {
        boolean variableFound = false;
        //System.out.println(id + " asigna en " + asignationScope);
        for (VariableSymbol symbol : variableSymbols.values()) {
            // Verificar si el nombre coincide y el alcance de asignación es válido
            if (symbol.getName().equals(id)) {
                variableFound = true;
                if (asignationScope.startsWith(symbol.getScope())) {
                    symbol.setValue(newValue);
                    return;
                }
            }
        }

        if (!variableFound) {
            throw new RuntimeException("La variable '" + id + "' no fue encontrada en la tabla de símbolos.");
        } else {
            throw new RuntimeException("La variable '" + id + "' no puede ser asignada en el alcance '" + asignationScope + "'.");
        }
    }
   
    /**
     * Inicia un nuevo bloque de control, incrementando el contador para el tipo de bloque.
     * @param functionName El nombre de la función a la que pertenece el bloque.
     * @param blockType El tipo de bloque (por ejemplo, "if", "while").
     */
    public void enterBlock(String functionName, String blockType) {
        // Obtener el bloque actual desde la pila
        String currentBlock = blockStack.peek();

        // Incrementar el contador para este tipo de bloque dentro del scope actual
        String blockKey = currentBlock + "-" + blockType;
        blockCounters.put(blockKey, blockCounters.getOrDefault(blockKey, 0) + 1);
        int blockId = blockCounters.get(blockKey);

        // Crear un identificador único para el nuevo bloque anidado
        blockIdentifier = currentBlock + "-" + blockType + "_" + blockId;
        blockStack.push(blockIdentifier);
    }
    
    /**
     * Finaliza el bloque de control actual, restaurando el bloque anterior en la pila.
     * @throws RuntimeException Si se intenta salir del bloque global.
     */
    public void exitBlock() {
        if (!blockStack.isEmpty() && !blockStack.peek().equals("global")) {
            blockStack.pop();
            blockIdentifier = blockStack.peek();  // Actualiza con el bloque actual
        } else {
            throw new RuntimeException("Error: no se puede salir del alcance global.");
        }
    }


    /**
     * Obtiene el identificador del bloque actual (el bloque en la cima de la pila).
     * @return El identificador del bloque actual.
     */
    public String getCurrentBlockIdentifier() {
        return blockIdentifier;
    }

    /**
     * Obtiene el alcance actual (el bloque en la cima de la pila).
     * @return El alcance actual.
     */
    public String getCurrentScope() {
        return blockStack.peek();
    }

    /**
     * Verifica si existe una variable con el nombre dado en la tabla de símbolos.
     * @param id
     * @param functionName
     * @return true si la variable existe, false en caso contrario.
     */
    public boolean containsVariableKey(String id, String functionName) {
        for (VariableSymbol symbol : variableSymbols.values()) {
            if (symbol.getName().equals(id) && symbol.getScope().equals(functionName)) {
                return true;
            }
        }
        return false;
    }
 
    public int getVariableLine(String id, String functionName) {
        for (VariableSymbol symbol : variableSymbols.values()) {
            if (symbol.getName().equals(id) && symbol.getScope().equals(functionName)) {
                return symbol.getDeclarationLine();
            }
        }
        return 0;
    }
    
    /**
     * Verifica si existe una función con el nombre dado en la tabla de símbolos.
     * @param name El nombre de la función.
     * @return true si la función existe, false en caso contrario.
     */    
    public boolean containsFunctionKey (String name){
     return functionSymbols.containsKey(name);
    }
    
    /**
     * Obtiene el mapa de funciones en la tabla de símbolos.
     * @return Un mapa con las funciones definidas en la tabla de símbolos.
     */
    public Map<String, FunctionSymbol> getFunctionSymbols (){
     return functionSymbols;
    }

    /**
     * Obtiene el mapa de variables en la tabla de símbolos.
     * @return Un mapa con las variables definidas en la tabla de símbolos.
     */    
    public Map<String, VariableSymbol> getVariableSymbols (){
     return variableSymbols;
    }

    /**
     * Busca y devuelve una variable en la tabla de símbolos, validando que exista en el scope proporcionado.
     * @param variableName El nombre de la variable.
     * @param scope El scope desde donde se está llamando la variable.
     * @return El objeto VariableSymbol si la variable existe y es accesible.
     * @throws RuntimeException Si la variable no existe o no es accesible desde el scope proporcionado.
     */
    public VariableSymbol getVariable(String variableName, String scope) {       
        // Extraer el nombre de la función del scope
        String functionName = scope.split("-")[0];

        // Construir la clave única de la variable
        String uniqueKey = functionName + "." + variableName;

        // Verificar si la variable existe en el scope
        if (!variableSymbols.containsKey(uniqueKey)) {
            throw new RuntimeException("La variable '" + variableName + "' no existe en el scope '" + functionName + "'.");
        }

        // Obtener la variable
        VariableSymbol variable = variableSymbols.get(uniqueKey);

        // Validar que la variable sea accesible desde el scope proporcionado
        if (!scope.startsWith(variable.getScope())) {
            throw new RuntimeException("La variable '" + variableName + "' no es accesible desde el scope '" + scope + "'.");
        }

        return variable;
    }    
    
    /**
     * Imprime todas las funciones en la tabla de símbolos con formato de tabla.
     */
    public void printFunctionSymbols() {
        System.out.println("\n" + String.format("%-20s %-20s %-20s", "Funcion", "Tipo de Retorno", "Parametros"));
        System.out.println("---------------------------------------------------------------");
        for (FunctionSymbol symbol : functionSymbols.values()) {
            System.out.println(String.format("%-20s %-20s %-20s", symbol.getName(), symbol.getReturnType(), symbol.getParameters()));
        }
    }

    /**
     * Imprime todas las variables en la tabla de símbolos con formato de tabla, agrupadas por su alcance.
     */
    public void printVariableSymbols() {
        // Agrupar las variables por scope
        Map<String, List<VariableSymbol>> groupedByScope = new HashMap<>();

        for (VariableSymbol symbol : variableSymbols.values()) {
            groupedByScope.computeIfAbsent(symbol.getScope(), k -> new ArrayList<>()).add(symbol);
        }

        // Imprimir encabezado de la tabla
        System.out.println("\n" + String.format("%-20s %-15s %-10s %-10s %-10s %-22s", "Variable", "Tipo", "Alcance", "Valor", "Linea", "Dirección de Memoria"));
        System.out.println("--------------------------------------------------------------------");

        // Imprimir variables agrupadas por scope
        for (Map.Entry<String, List<VariableSymbol>> entry : groupedByScope.entrySet()) {
            for (VariableSymbol symbol : entry.getValue()) {
                System.out.println(String.format("%-20s %-15s %-10s %-10s %-10d %-10d", 
                    symbol.getName(), 
                    symbol.getType(), 
                    symbol.getScope(), 
                    symbol.getValue() != null ? symbol.getValue().toString() : "null", 
                    symbol.getDeclarationLine(),
                    symbol.getMemoryAddress()
                ));
            }
        }
    }
    
    public int getNextMemoryAddress() {
        return nextMemoryAddress;
    }

    
    public void setNextMemoryAddress() {
        //Asigna 4 bytes me memoria por eso suma 4 para la siguiente.
        this.nextMemoryAddress = nextMemoryAddress +4;
    }
}
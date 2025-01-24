package utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    // Mapa para almacenar funciones
    private Map<String, FunctionSymbol> functionSymbols = new HashMap<>();
    
    // Mapa para almacenar variables
    private Map<String, VariableSymbol> variableSymbols = new HashMap<>();

    public String blockIdentifier;
    
    // Pila para manejar bloques de control
    private Deque<String> blockStack = new ArrayDeque<>();
    
    // Contadores para identificar bloques únicos por tipo
    private Map<String, Integer> blockCounters = new HashMap<>();

    public SymbolTable() {
        // Agregar un alcance global inicial a la pila
        blockStack.push("global");
    }
    
    // Agrega una función a la tabla de símbolos
    public void addFunction(String name, String returnType, String parameters) {
        if (functionSymbols.containsKey(name)) {
            throw new RuntimeException("La función '" + name + "' ya está definida.");
        }
        functionSymbols.put(name, new FunctionSymbol(name, returnType, parameters));
    }

    // Agrega una variable a la tabla de símbolos
    public void addVariable(String name, String type, String scope, Object value, int declarationLine) {
        if (variableSymbols.containsKey(name)) {
            throw new RuntimeException("La variable '" + name + "' ya está definida.");
        }
        variableSymbols.put(name, new VariableSymbol(name, type, scope, value, declarationLine));
    }
    
    // Maneja el inicio de un nuevo bloque de control
    public void enterBlock(String functionName, String blockType) {
        // Incrementar el contador para este tipo de bloque
        String blockKey = functionName + "_" + blockType;
        blockCounters.put(blockKey, blockCounters.getOrDefault(blockKey, 0) + 1);
        int blockId = blockCounters.get(blockKey);

        // Crear un identificador único para el bloque
        blockIdentifier = functionName + "_" + blockType + "_" + blockId;
        blockStack.push(blockIdentifier);
    }

    // Maneja el fin de un bloque de control
    public void exitBlock() {
        if (!blockStack.isEmpty() && !blockStack.peek().equals("global")) {
            blockStack.pop();
        } else {
            throw new RuntimeException("Error: no se puede salir del alcance global.");
        }
    }

    // Obtiene el alcance actual (el bloque en la cima de la pila)
    public String getCurrentBlockIdentifier() {
        return blockIdentifier;
    }

    public String getCurrentScope() {
        return blockStack.peek();
    }
    
    public boolean containsVariableKey (String name){
     return variableSymbols.containsKey(name);
    }
    
    public boolean containsFunctionKey (String name){
     return variableSymbols.containsKey(name);
    }
    
    public Map<String, FunctionSymbol> getFunctionSymbols (){
     return functionSymbols;
    }
    
    public Map<String, VariableSymbol> getVariableSymbols (){
     return variableSymbols;
    }
    
    // Imprime todas las funciones en la tabla de símbolos con formato de tabla
    public void printFunctionSymbols() {
        System.out.println("\n" + String.format("%-20s %-20s %-20s", "Funcion", "Tipo de Retorno", "Parametros"));
        System.out.println("---------------------------------------------------------------");
        for (FunctionSymbol symbol : functionSymbols.values()) {
            System.out.println(String.format("%-20s %-20s %-20s", symbol.getName(), symbol.getReturnType(), symbol.getParameters()));
        }
    }

    // Imprime todas las variables en la tabla de símbolos con formato de tabla
    public void printVariableSymbols() {
        // Agrupar las variables por scope
        Map<String, List<VariableSymbol>> groupedByScope = new HashMap<>();

        for (VariableSymbol symbol : variableSymbols.values()) {
            groupedByScope.computeIfAbsent(symbol.getScope(), k -> new ArrayList<>()).add(symbol);
        }

        // Imprimir encabezado de la tabla
        System.out.println("\n" + String.format("%-20s %-15s %-10s %-10s %-10s", "Variable", "Tipo", "Alcance", "Valor", "Linea"));
        System.out.println("--------------------------------------------------------------------");

        // Imprimir variables agrupadas por scope
        for (Map.Entry<String, List<VariableSymbol>> entry : groupedByScope.entrySet()) {
            for (VariableSymbol symbol : entry.getValue()) {
                System.out.println(String.format("%-20s %-15s %-10s %-10s %-10d", 
                    symbol.getName(), 
                    symbol.getType(), 
                    symbol.getScope(), 
                    symbol.getValue() != null ? symbol.getValue().toString() : "null", 
                    symbol.getDeclarationLine()
                ));
            }
        }
    }

    // Clase para representar la información de una variable
    public class VariableSymbol {
        private String name;
        private String type;
        private String scope; 
        private Object value; 
        private int declarationLine;

        public VariableSymbol(String name, String type, String scope, Object value, int declarationLine) {
            this.name = name;
            this.type = type;
            this.scope = scope;
            this.value = value;
            this.declarationLine = declarationLine;
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
    }

    // Clase para representar la información de una función
    public class FunctionSymbol {
        private String name;
        private String returnType;
        private String parameters;

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
}
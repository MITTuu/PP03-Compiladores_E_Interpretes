package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    // Mapa para almacenar funciones
    private Map<String, FunctionSymbol> functionSymbols = new HashMap<>();
    
    // Mapa para almacenar variables (globales y locales)
    private Map<String, VariableSymbol> variableSymbols = new HashMap<>();
    
    // Agrega una función a la tabla de símbolos
    public void addFunction(String name, String returnType, String parameters) {
        if (functionSymbols.containsKey(name)) {
            throw new RuntimeException("La función '" + name + "' ya está definida.");
        }
        functionSymbols.put(name, new FunctionSymbol(name, returnType, parameters));
    }

    // Agrega una variable a la tabla de símbolos
    public void addVariable(String name, String type, String scope) {
        if (variableSymbols.containsKey(name)) {
            throw new RuntimeException("La variable '" + name + "' ya está definida.");
        }
        variableSymbols.put(name, new VariableSymbol(name, type, scope));
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
        System.out.println("\n" + String.format("%-20s %-15s %-10s", "Variable", "Tipo", "Alcance"));
        System.out.println("---------------------------------------------------------------");

        // Imprimir variables agrupadas por scope
        for (Map.Entry<String, List<VariableSymbol>> entry : groupedByScope.entrySet()) {
            for (VariableSymbol symbol : entry.getValue()) {
                System.out.println(String.format("%-20s %-15s %-10s", symbol.getName(), symbol.getType(), symbol.getScope()));
            }
        }
    }

    
    // Clase para representar la información de una variable
    public class VariableSymbol {
        private String name;
        private String type;
        private String scope;  // global o local

        public VariableSymbol(String name, String type, String scope) {
            this.name = name;
            this.type = type;
            this.scope = scope;
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
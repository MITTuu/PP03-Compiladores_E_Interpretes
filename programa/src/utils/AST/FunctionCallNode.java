package utils.AST;

import bin.Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;
import utils.SymbolsTable.FunctionSymbol;
import utils.SymbolsTable.SymbolTable;

/**
 * Representa un nodo en el árbol sintáctico abstracto (AST) para la llamada a una función.
 * Esta clase se utiliza para verificar la semántica de la llamada a la función y generar código MIPS,
 * entre otras funcionalidades relacionadas con la ejecución y validación de una llamada a función.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class FunctionCallNode extends ASTNode{
    
    public String name;
    public List<ExpressionNode> parameterList;
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
  
    /**
     * Constructor de la clase FunctionCallNode.
     * 
     * @param name Nombre de la función a la que se realiza la llamada.
     * @param parameterList Lista de nodos de expresión que representan los parámetros.
     */    
    public FunctionCallNode(String name, List<ExpressionNode> parameterList) {
        this.name = name;
        this.parameterList = parameterList;
    }

    /**
     * Verifica la semántica de la llamada a la función, asegurándose de que:
     * - La función exista en la tabla de símbolos.
     * - Los parámetros proporcionados coincidan en tipo y cantidad con los definidos para la función.
     * 
     * @throws RuntimeException Si ocurre un error en la verificación semántica (por ejemplo, si la función no existe
     *         o los tipos de parámetros no coinciden).
     */    
    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();

        // Verificar si la función existe en la tabla de símbolos
        if (!symbolTable.containsFunctionKey(name)) {
            throw new RuntimeException("La función '" + name + "' no está definida.");
        }

        // Obtener la definición de la función desde la tabla de símbolos
        FunctionSymbol function = symbolTable.getFunctionSymbols().get(name);

        // Convertir los parámetros de la función en una lista de tipos
        List<String> expectedParams = new ArrayList<>();
        String[] paramArray = function.getParameters().split(",\\s*");

        // Procesar cada parámetro, tomando solo la primera palabra (tipo)
        for (String param : paramArray) {
            if (!param.trim().isEmpty()) {
                String[] parts = param.split("\\s+");
                expectedParams.add(parts[0]);
            }
        }

        // Verificar la cantidad de parámetros
        if (expectedParams.size() != parameterList.size()) {
            throw new RuntimeException("La función '" + name + "' espera " +
                                       expectedParams.size() + " parámetros, pero se pasaron " +
                                       parameterList.size() + ".");
        }

        // Verificar que los tipos de los parámetros sean correctos
        for (int i = 0; i < parameterList.size(); i++) {
            ExpressionNode paramNode = parameterList.get(i);
            String expectedType = expectedParams.get(i);

            // Verificar que el tipo del parámetro coincide con el tipo esperado
            String actualType = paramNode.getType(symbolTable);
            if (!expectedType.equals(actualType)) {
                throw new RuntimeException("La función " + name + " espera un tipo " + "'" + expectedType + "'" + " en la posición " + (i + 1) +
                                           ", pero se proporcionó un tipo '" + actualType + "'.");
            }
        }
    }

    /**
     * Genera el código MIPS correspondiente para esta llamada a la función.
     * 
     * @param cg Objeto CodeGenerator que se utiliza para generar el código.
     * @throws UnsupportedOperationException Si esta operación no está soportada por la clase.
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Representa esta llamada a la función en formato de cadena, con una indentación específica.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de esta llamada a la función.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("├── Nombre: ").append(name).append("\n");        
        
        // Parametros
        sb.append(indent).append("└── Lista de parametros").append("\n");
        
        for (int i = 0; i < parameterList.size(); i++) {
            if(i+1 == parameterList.size()){
                sb.append(indent).append(parameterList.get(i).toString("    "));
            }else{
                sb.append(indent).append(parameterList.get(i).toString("    "));
            }
        }
        return sb.toString();
    }

    /**
     * Representa esta llamada a la función en formato de cadena.
     * 
     * @return La representación en formato de cadena de esta llamada a la función.
     */   
    @Override
    public String toString() {
        return "FunctionCall:" + name + parameterList;
    }

}

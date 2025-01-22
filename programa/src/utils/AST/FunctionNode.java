package utils.AST;

// Nodo para una función

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionNode extends ASTNode {
    public String name;
    public List<String> parameters;
    public List<String> variables;

    public FunctionNode(String name, List<String> parameters) {
        this.name = name;
        this.parameters = parameters;
    }
    
    public FunctionNode(String name, List<String> parameters, List<String> variables) {
        this.name = name;
        this.parameters = parameters;
        this.variables = variables;
    }

    @Override
    void checkSemantics() {
        // Verificar duplicados en parámetros
        Set<String> paramSet = new HashSet<>(parameters);
        if (paramSet.size() != parameters.size()) {
            throw new RuntimeException("Parámetros duplicados en función " + name);
        }

       /*// Verificar que las variables no repitan nombres con los parámetros
        for (VarDeclNode var : variables) {
            if (parameters.contains(var.name)) {
                throw new RuntimeException("Variable " + var.name + " en conflicto con parámetro en función " + name);
            }
        }*/
    }
    
     @Override
    void generateMIPS() {
        System.out.println(name + ":");
        System.out.println("  # Guardar registros si es necesario");
       /* for (VarDeclNode var : variables) {
            var.generateMIPS();
        }*/
        System.out.println("  # Código de la función");
        System.out.println("  jr $ra  # Retorno");
    }
    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("FunctionNode: ").append(name).append("\n");

        // Parámetros
        sb.append(indent).append("    ├── Parámetros: ").append(parameters).append("\n");

        // Variables
        sb.append(indent).append("    └── Variables: [");
        for (int i = 0; i < variables.size(); i++) {
            sb.append(variables.get(i));
            if (i < variables.size() - 1) sb.append(", ");
        }
        sb.append("]\n");

        return sb.toString();
    }
}
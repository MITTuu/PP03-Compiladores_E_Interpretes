package utils.AST;

// Nodo para una función

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa un nodo en el árbol sintáctico abstracto (AST) para la declaración de una función.
 * Esta clase se utiliza para modelar funciones en el código fuente, con su nombre, tipo de retorno,
 * lista de parámetros y el cuerpo de la función.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class FunctionNode extends ASTNode {
    String name;
    String returnType;
    List<ParameterNode> parameterNodeList;
    List<BodyNode> bodyNodeList;

    /**
     * Constructor para crear un nodo de función.
     *
     * @param function Cadena de texto que representa la declaración de la función, incluyendo el tipo de retorno,
     *                el nombre y, opcionalmente, los parámetros.
     * @param bodyList Lista de nodos que representan el cuerpo de la función.
     */   
    public FunctionNode(String function, List<BodyNode> bodyList) {
        String[] functionParts = function.split(":");
        //valida si es una funcion vacía
        if(functionParts.length >= 2){
            this.returnType = functionParts[0];
            this.name = functionParts[1];
            this.bodyNodeList = bodyList;
        }else{
            this.name="ERROR";
            this.returnType ="";
            this.bodyNodeList = new ArrayList<>();
        }
        //Valida si es una función con parametros
        if(functionParts.length == 3){
            String params = functionParts[2];
            SplitParametersToNodes(params);
        }else{
            //Asigna una lista vacía de parametros si no hay paramatros en la declaración de función
            List<ParameterNode> paramNodeList = new ArrayList<ParameterNode>();
        }   
        
    }

    /**
     * Verifica la semántica de la declaración de la función, asegurándose de que no haya parámetros duplicados.
     * Si se detectan parámetros duplicados, se lanza una excepción en tiempo de ejecución.
     */    
    @Override
    void checkSemantics() {
        // Verificar duplicados en parámetros
        if(parameterNodeList!=null){
            Set<ParameterNode> paramSet = new HashSet<>(parameterNodeList);
            if (paramSet.size() != parameterNodeList.size()) {
                throw new RuntimeException("Parámetros duplicados en función " + name);
            }
        }
       /*// Verificar que las variables no repitan nombres con los parámetros
        for (VarDeclNode var : variables) {
            if (parameters.contains(var.name)) {
                throw new RuntimeException("Variable " + var.name + " en conflicto con parámetro en función " + name);
            }
        }*/
    }
   
    /**
     * Genera el código MIPS correspondiente para la declaración de la función.
     * 
     * @param cg Objeto CodeGenerator utilizado para generar el código MIPS.
     */    
     @Override
    String generateMIPS(CodeGenerator cg) {
       if(parameterNodeList == null || parameterNodeList.isEmpty()){
            return "";
        }
       if ( bodyNodeList == null || bodyNodeList.isEmpty()) {
            return "";
        }
        String bodyList = "";
        for(BodyNode element : this.bodyNodeList){
            bodyList = bodyList.concat(element.generateMIPS(cg));
        }
       
        String result = "";
        for(ParameterNode param : parameterNodeList){
            result = result.concat(param.generateMIPS(cg));
        }
       return result + bodyList;
    }
    
    /**
     * Representa esta declaración de función en formato de cadena, con una indentación específica.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de esta declaración de función.
     */    
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Función-Declaración: ").append(name).append("\n");
        
        // Parámetros
        sb.append(indent).append("├── Parámetros: [ ");
        if(parameterNodeList != null){
            for(int i=0; i < parameterNodeList.size(); i++){
                sb.append(parameterNodeList.get(i).variableType).append(" ").append(parameterNodeList.get(i).identifier);
                if(i+1 < parameterNodeList.size()){
                    sb.append(", ");
                }
            }
            sb.append(" ");
        }
        sb.append("]\n");
        
        
        // Elementos del body de la función
        sb.append(indent).append("└── Bloque de instrucciones");
        sb.append("\n");
        
        for (int i = 0; i < bodyNodeList.size(); i++) {
            if(i+1 == bodyNodeList.size()){
                sb.append(indent).append("        └── ").append(bodyNodeList.get(i).toString(indent+"        "));
            }else{
                sb.append(indent).append("        ├── ").append(bodyNodeList.get(i).toString(indent+"        │"));
            }
        }

        return sb.toString();
    }

    /**
     * Separa la cadena de parámetros y crea nodos de parámetros a partir de ella.
     * Este método asigna la lista de parámetros a la variable global {@link #parameterNodeList}.
     * 
     * @param params Cadena de texto que contiene los parámetros separados por punto y coma.
     */
    private void SplitParametersToNodes(String params) {
        List<ParameterNode> paramNodeList = new ArrayList<ParameterNode>();
        String[] parameterList = params.split(";");
        for (String param : parameterList){
            String[] paramParts = param.split("@&@");
            ParameterNode paramNode = new ParameterNode(paramParts[0],paramParts[1]);
            paramNodeList.add(paramNode);
        }
        this.parameterNodeList = paramNodeList;
    }
}
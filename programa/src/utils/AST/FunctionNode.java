package utils.AST;

// Nodo para una función

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

public class FunctionNode extends ASTNode {
    String name;
    String returnType;
    List<ParameterNode> parameterNodeList;
    List<BodyNode> bodyNodeList;

   
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
    
     @Override
    String generateMIPS(CodeGenerator cg) {
        System.out.println(name + ":");
        System.out.println("  # Guardar registros si es necesario");
       /* for (VarDeclNode var : variables) {
            var.generateMIPS();
        }*/
        System.out.println("  # Código de la función");
        System.out.println("  jr $ra  # Retorno");
        return "";
    }
    
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

    //Separa el string de parametros para obtener sus tipos y nombre en un nuevo nodo
    //Asigna la lista de parametros a la variable global
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
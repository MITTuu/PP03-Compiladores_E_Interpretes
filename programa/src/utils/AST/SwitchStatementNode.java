package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

/**
 * Representa una declaración `switch` en el árbol sintáctico abstracto (AST).
 * Este nodo contiene una expresión de control y una lista de casos a evaluar.
 * 
 * @author Joselyn Jiménez Salgado
 * @author Dylan Montiel Zúñiga
 * @version 1/23/2024
 */
public class SwitchStatementNode extends ASTNode{
    ExpressionNode controlExpression;
    List<SwitchCaseNode> caseList;

    /**
     * Crea un nuevo nodo para una declaración `switch` con una expresión de control y una lista de casos.
     * 
     * @param controlExpression La expresión de control que se evalúa en la declaración `switch`.
     * @param caseList La lista de casos que se evalúan dentro de la declaración `switch`.
     */    
    public SwitchStatementNode(ExpressionNode controlExpression, List<SwitchCaseNode> caseList) {
        this.controlExpression = controlExpression;
        this.caseList = caseList;
    } 
    
    /**
     * Realiza la verificación semántica de la declaración `switch`.
     * Este método no está implementado en esta clase.
     */    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    /**
     * Genera el código MIPS correspondiente a la declaración `switch`.
     * Este método no está implementado en esta clase.
     * 
     * @param cg El generador de código MIPS.    
     */    
    @Override
    String generateMIPS(CodeGenerator cg) {
       if(controlExpression == null || caseList == null || caseList.isEmpty()){
            return "";
        } 
        String result = controlExpression.generateMIPS(cg);
        String caseListString = "";
        for(SwitchCaseNode element : this.caseList){
            caseListString = caseListString.concat(element.generateMIPS(cg));
        }
        
        result =  result.concat(caseListString);
        
        return result; 
    }

    /**
     * Representa el nodo de la declaración `switch` en formato de cadena con indentación.
     * 
     * @param indent La cadena de texto que representa la indentación para el formato.
     * @return La representación en formato de cadena de la declaración `switch`, incluyendo la expresión de control y los casos.
     */  
    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── Switch - Estructura de Control").append("\n");
        sb.append(indent).append("    ├── Expresión de Control:\n");
        sb.append(controlExpression.toString(indent + "    │   "));
        
        sb.append(indent).append("    └── Lista de Casos:\n");
        for (int i = 0; i < caseList.size(); i++) {
            if(i+1 == caseList.size()){
                sb.append(indent).append("        └── ").append(caseList.get(i).toString(indent+"        "));
            }else{
                sb.append(indent).append("        ├── ").append(caseList.get(i).toString(indent+"        │"));
            }
        }
        return sb.toString();
    }
    
}

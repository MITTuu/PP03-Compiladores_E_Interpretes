
package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

public class WhileStatementNode extends ASTNode{
    ExpressionNode condition;
    List<BodyNode> body;
    
    public WhileStatementNode(ExpressionNode condition, List<BodyNode> body){
     this.condition = condition;
     this.body = body;
    }

    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        // Validar si alguno de los atributos es null o si la lista está vacía
        if (condition == null ||  body == null || body.isEmpty()) {
            return "";
        }
        String condition = this.condition.generateMIPS(cg);
        String bodyList = "";
        for(BodyNode element : this.body){
            bodyList = bodyList.concat(element.generateMIPS(cg));
        }
        return  condition +  bodyList;
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("└── While - Estructura de Control").append("\n");
        sb.append(indent).append("    ├── Condición:\n");
        sb.append(condition.toString(indent + "    │   "));
        sb.append(indent).append("    └── While - Bloque de instrucciones:\n");
        for (int i = 0; i < body.size(); i++) {
            if(i+1 == body.size()){
                sb.append(indent).append("        └── ").append(body.get(i).toString(indent+"        "));
            }else{
                sb.append(indent).append("        ├── ").append(body.get(i).toString(indent+"        │"));
            }
        }
        return sb.toString();
    }
}

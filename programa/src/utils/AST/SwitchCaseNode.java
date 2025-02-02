package utils.AST;

import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;

public class SwitchCaseNode extends ASTNode {

    String type;//Tipo de String: Case ó Default
    ExpressionNode caseLabel;
    List<BodyNode> body;
    
    public SwitchCaseNode(String type, ExpressionNode caseLabel, List<BodyNode> body) {
        this.type = type;
        this.caseLabel = caseLabel;
        this.body = body;
    }    
    
    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Etiqueta de caso:\n");
        sb.append(caseLabel.toString(indent + "    │   "));
        sb.append(indent).append("    └── Case - Bloque de instrucciones:\n");
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

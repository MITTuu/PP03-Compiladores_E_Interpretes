
package utils.AST;

import java.util.ArrayList;
import java.util.List;


public class IfStatementNode extends ASTNode {
    private ExpressionNode condition;
    private BodyNode thenBranch;
    private List<IfStatementNode> elseIfBranches;
    private BodyNode elseBranch;

    public IfStatementNode(ExpressionNode condition, BodyNode thenBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseIfBranches = new ArrayList<>();
        this.elseBranch = null;
    }

    // Agregar un bloque "else-if"
    public void addElseIfBranch(IfStatementNode elseIf) {
        elseIfBranches.add(elseIf);
    }

    // Agregar un bloque "else"
    public void setElseBranch(BodyNode elseBranch) {
        this.elseBranch = elseBranch;
    }

    @Override
    void checkSemantics() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    void generateMIPS() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    String toString(String indent) {
        StringBuilder builder = new StringBuilder();

        // Bloque principal "if"
        builder.append("if (").append(condition.toString()).append(") {\n");
        builder.append(thenBranch.toString()).append("}");

        // Bloques "else-if"
        for (IfStatementNode elseIf : elseIfBranches) {
            builder.append(" else if (").append(elseIf.condition.toString()).append(") {\n");
            builder.append(elseIf.thenBranch.toString()).append("}");
        }

        // Bloque "else"
        if (elseBranch != null) {
            builder.append(" else {\n");
            builder.append(elseBranch.toString()).append("}");
        }

        return builder.toString();
    }
}


package utils.AST;

//Nodo para la asignación de variables

import bin.Parser;
import java.util.List;
import utils.MIPS.GeneracionCodigo.CodeGenerator;
import utils.SymbolsTable.SymbolTable;
import utils.SymbolsTable.VariableSymbol;

public class VariableAssignmentNode extends ASTNode {
    
    String id;
    String unaryArithmeticOperator;
    ExpressionNode expression;
    ArrayElementsNode arrayElements;
    ArrayUseNode arrayUse;
    private int VariableAssignmentCase;//Determina el tipo de contructor que se usó.
    SymbolTable symbolTable;
    public Parser parser;
    public String currentHash;
    public String reducedType;
    
       //Incluye declaración de variable con asignación de expresión.
    public VariableAssignmentNode(String id, ExpressionNode expression){
        this.id = id;
        this.expression = expression;
        this.arrayElements = null;
        this.arrayUse = null;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 1;
    }
    
     public VariableAssignmentNode(String unaryArithmeticExpression){
         //Recibe el id y el operador en un solo string
        String[] unaryExpression = unaryArithmeticExpression.split(":");
        this.id = unaryExpression[0];
        this.unaryArithmeticOperator= unaryExpression[1];
        this.expression = new ExpressionNode(ExpressionEnum.UNARY_ARITHMETIC_EXPRESSION,null,unaryArithmeticExpression);
        this.arrayElements = null;
        this.arrayUse = null;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 2;
    }
                
    //Incluye declaración de variable con asignación de elementos de array.
    //Para asignación de arreglos
    public VariableAssignmentNode(String id, ArrayElementsNode arrayElements){
        this.id = id;
        this.arrayElements = arrayElements;
        this.expression = null;
        this.arrayUse = null;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 3;
    }
    
     public VariableAssignmentNode(ArrayUseNode arrayUse, ExpressionNode expression){
        this.id = arrayUse.id;
        this.expression = expression;
        this.arrayElements = null;
        this.arrayUse = arrayUse;
        
        //Determina el tipo de contructor que se usó.
        this.VariableAssignmentCase = 4;
    }

    @Override
    public void checkSemantics() {
        symbolTable = (SymbolTable) parser.getSymbolTable();

        VariableSymbol variable = symbolTable.getVariable(id, currentHash);
        String type = variable.getType();

        if (type.equals("Char") && reducedType.equals("Integer")) {
            return;
        }        
        
        if (!type.equals("a")) {
            if (!type.equals(reducedType)) {
                // Levantar un error si los tipos no coinciden
                throw new RuntimeException("Incompatibilidad de tipos asignacion. Se esperaba " 
                    + type + " pero se asignó " + reducedType);
            }
        }        
    }

    @Override
    String generateMIPS(CodeGenerator cg) {
        String result = ""; // Inicializar result

        // Validar y concatenar solo si no son null
        if (expression != null) {
            result = result.concat(expression.generateMIPS(cg)); // Concatenar si 'expression' no es null
        }

        if (arrayElements != null) {
            result = result.concat(arrayElements.generateMIPS(cg)); // Concatenar si 'arrayElements' no es null
        }

        if (arrayUse != null) {
            result = result.concat(arrayUse.generateMIPS(cg)); // Concatenar si 'arrayUse' no es null
        }

        return result; // Retornar el resultado concatenado, vacío si todos fueron null

    }

    @Override
    String toString(String indent) {
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("├── Id: ").append(id).append("\n");
        switch (VariableAssignmentCase) {
            case 1:
                sb.append(indent).append("└── Asignación").append("\n");
                sb.append(expression.toString(indent.replace("|", " ") + "    "));
                break;
            case 2:
                sb.append(indent).append("└── Asignación: ").append("Operador aritmético unitario postfijo => ").append(this.unaryArithmeticOperator).append("\n");
                break;
            case 3:
                sb.append(indent).append("└── Asignación ").append("\n").append(arrayElements.toString(indent+ "    "));       
                break;
            case 4:
                sb.append(indent).append("├── Uso de arreglo: ").append("\n");
                sb.append(arrayUse.toString(indent.replace("|", " ") + "│    "));
                sb.append(indent).append("└── Asignación ").append("\n");
                sb.append(expression.toString(indent.replace("|", " ") + "    "));       
                break;
            default:
                sb.append(indent).append("└── Error de datos en VariableAssignmentNode ").append("\n");
                break;
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        if (expression != null) {
            return expression.toString();
        }      
        
        return null;
    }
}

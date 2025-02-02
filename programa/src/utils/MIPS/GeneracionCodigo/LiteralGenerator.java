
package utils.MIPS.GeneracionCodigo;

public class LiteralGenerator {

    public LiteralGenerator() {
    }
    
    
    public String generateCodeLiteralLoad(String literal, CodeGenerator cg){
       //Primero separa el tipo, ya que viene junto en el string
       String[] splitedLiteral = literal.split(":");
       if (splitedLiteral.length < 2){
           System.err.println("Error al tratar de general el código para el literal: " + literal);
           return "";
       }
       String reg;// Registro a asignar según el caso
       String literalType = splitedLiteral[0];
       String literalValue = splitedLiteral[1];
       
       switch(literalType){
           case "Integer":
           case "Bool":
                    reg = cg.getTemporaryRegister();
                    System.out.println("li " + reg + ", " + literalValue + "  # Cargar entero/bool en " + reg);
                    return "li " + reg + ", " + literalValue + "  # Cargar entero/bool en " + reg + "\n";
           case "Char":
                     if (literalValue.length() == 3) {
                        int asciiValue = (int) literalValue.charAt(1);
                        reg = cg.getTemporaryRegister();
                        System.out.println("li " + reg + ", " + asciiValue + "  # Cargar carácter " + literalValue + " (ASCII " + asciiValue + ")");
                        return "li " + reg + ", " + asciiValue + "  # Cargar carácter " + literalValue + " (ASCII " + asciiValue + ")\n";
                    } else {
                        System.err.println("Error: Char inválido -> " + literalValue);
                        return "";
                    }
           case "Float":
                    String floatLabel = cg.generateLabel();
                    cg.addDataSection(floatLabel + ": .float " + literalValue +"# Almacena el float en memoria");
                    System.out.println(floatLabel + ": .float " + literalValue + "# Almacena el float en memoria");
                    System.out.println("lwc1 $f0, " + floatLabel + "  # Cargar float " + literalValue + " en $f0");
                    return "lwc1 $f0, " + floatLabel + "  # Cargar float " + literalValue + " en $f0\n";
           case "String":
                    String strLabel = cg.generateLabel();
                    cg.addDataSection(strLabel + ": .asciiz " + literalValue + "# Almacena el String en memoria");
                    System.out.println(strLabel + ": .asciiz " + literalValue + "# Almacena el String en memoria");
                    System.out.println("la $a0, " + strLabel + "  # Cargar dirección de la cadena " + literalValue + " en $a0");
                    return "la $a0, " + strLabel + "  # Cargar dirección de la cadena " + literalValue + " en $a0\n";
           default:
            System.err.println("Error: Tipo de literal no reconocido -> " + literalType);
            return "";
       }
    }
}

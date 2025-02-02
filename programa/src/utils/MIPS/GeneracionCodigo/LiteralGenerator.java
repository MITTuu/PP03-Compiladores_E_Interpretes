
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
       String reg;//valor a asignar el registro dependiendo del caso
       String literalType = splitedLiteral[0];
       String literalValue = splitedLiteral[1];
       
       switch(literalType){
           case "Integer":
           case "Bool":
               reg = cg.getTemporaryRegister();
               System.out.println("li " + reg + ", " + literalValue + "\n");
               return "li " + reg + ", " + literalValue + "\n";
       }
       
    //    String reg;
    /*if (valor instanceof Integer || valor instanceof Boolean || valor instanceof Character) {
        reg = cg.obtenerRegistroTemporal();
        return "li " + reg + ", " + valor + "\n";
    } else if (valor instanceof Float) {
        reg = cg.obtenerRegistroFlotanteTemporal();
        return "li.s " + reg + ", " + valor + "\n";
    } else if (valor instanceof String) {
        String etiqueta = cg.generarEtiquetaString(valor.toString());
        reg = cg.obtenerRegistroTemporal();
        return "la " + reg + ", " + etiqueta + "\n";
    } else {
        throw new IllegalArgumentException("Tipo de literal no soportado: " + valor.getClass().getSimpleName());
    }*/
    return "";
    }
}

package utils.MIPS.GeneracionCodigo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CodeGenerator {
     // Pila para llevar la cuenta de los registros temporales disponibles.
    private Stack<String> temporaryRegisters;
    // Registro donde se almacenará el resultado de la última 
    private String lastRegister;
    // Almacenará las declaraciones de .data
    private List<String> dataSection;  

    // Contador para generar etiquetas únicas (si es necesario).
    private int labelCounter;

    public CodeGenerator() {
        temporaryRegisters = new Stack<>();
        dataSection = new ArrayList<>();
        recargarPilaSoluciónTempEnDesarrollo();
        labelCounter = 0;
    }

    /**
     * Obtiene un registro temporal disponible.
     * @return El nombre del registro.
     */
    public String getTemporaryRegister() {
        if (temporaryRegisters.isEmpty()) {
            // Si se agotan, se podría implementar una estrategia para guardar/restaurar registros.
            //throw new RuntimeException("No temporary registers available");
            recargarPilaSoluciónTempEnDesarrollo();
        }
        String reg = temporaryRegisters.pop();
        lastRegister = reg;
        return reg;
    }
    
    private void recargarPilaSoluciónTempEnDesarrollo(){
        temporaryRegisters.push("$t9");
        temporaryRegisters.push("$t8");
        temporaryRegisters.push("$t7");
        temporaryRegisters.push("$t6");
        temporaryRegisters.push("$t5");
        temporaryRegisters.push("$t4");
        temporaryRegisters.push("$t3");
        temporaryRegisters.push("$t2");
        temporaryRegisters.push("$t1");
        temporaryRegisters.push("$t0");
    }

    /**
     * Devuelve el registro utilizado en la última operación.
     * @return El nombre del registro.
     */
    public String getLastRegister() {
        return lastRegister;
    }

    /**
     * Permite establecer el registro que contiene el resultado de una operación.
     * @param reg El nombre del registro.
     */
    public void setLastRegister(String reg) {
        lastRegister = reg;
    }

    /**
     * Libera un registro temporal (por ejemplo, después de usarlo).
     * @param reg El nombre del registro a liberar.
     */
    public void releaseTemporaryRegister(String reg) {
        temporaryRegisters.push(reg);
    }

    /**
     * Genera una etiqueta única para saltos o condiciones.
     * @return Una etiqueta única.
     */
    public String generateLabel() {
        return "L" + (labelCounter++);
    }
    
    // Agrega una línea a la sección .data
    public void addDataSection(String data) {
        dataSection.add(data);
    }

    // Devuelve todo el contenido de la sección .data
    public String getDataSection() {
        return String.join("\n", dataSection);
    }
}

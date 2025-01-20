package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Clase principal para generar archivos .java usando las librerías JFlex y Java CUP.
 * La clase ejecuta el proceso de generación de los archivos necesarios para la 
 * compilación del analizador léxico y sintáctico, y mueve los archivos generados
 * a la carpeta de destino correspondiente.
 * 
 * @author dylan
 */
public class Main {
    /**
     * Método principal que se encarga de generar los archivos .java usando las herramientas 
     * JFlex y Java CUP. Además, mueve los archivos generados a la carpeta de destino.
     *
     * @param args Los argumentos de línea de comandos (no utilizados en este caso).
     * @throws Exception Si ocurre algún error durante la generación de archivos.
     */
    public static void main(String[] args) throws Exception {
        // Usa el directorio actual como base
        String baseDir = System.getProperty("user.dir");
        
        // Construir las rutas relativas usando baseDir
        String rutaLexer = Paths.get(baseDir, "src", "parserlexer", "Lexer.flex").toString();
        String[] rutaParser = {
            "-parser", "Parser", Paths.get(baseDir, "src", "parserlexer", "Parser.cup").toString()
        };
        
        // Llamada al método para generar los archivos
        generar(rutaLexer, rutaParser);
    }
    
    /**
     * Método que se encarga de generar los archivos necesarios utilizando JFlex y Java CUP.
     * Luego mueve los archivos generados (Lexer.java, sym.java y Parser.java) a la carpeta de destino.
     * 
     * @param ruta2 La ruta del archivo Lexer.flex para JFlex.
     * @param rutaS Las rutas necesarias para ejecutar Java CUP, incluyendo el archivo Parser.cup.
     * @throws IOException Si ocurre algún error al leer o mover los archivos.
     * @throws Exception Si ocurre algún otro error durante la ejecución de JFlex o Java CUP.
     */
    private static void generar(String rutaLexer, String[] rutaParser) throws IOException, Exception {
        // Generación de archivos con FLEX usando rutas relativas
        File archivo = new File(rutaLexer);
        JFlex.Main.generate(archivo);

        // Generación de archivos con CUP
        java_cup.Main.main(rutaParser);

        // Rutas relativas para los archivos generados
        Path baseDir = Paths.get(System.getProperty("user.dir"));

        // Mover los archivos generados: Lexer.java, sym.java, y Parser.java
        moverArchivo(baseDir.resolve("src/parserlexer/Lexer.java"), baseDir.resolve("src/bin/Lexer.java"));
        moverArchivo(baseDir.resolve("sym.java"), baseDir.resolve("src/bin/sym.java"));
        moverArchivo(baseDir.resolve("Parser.java"), baseDir.resolve("src/bin/Parser.java"));

        // Cambiar el paquete de los archivos generados
        cambiarPaquete(baseDir.resolve("src/bin/Lexer.java"));
        cambiarPaquete(baseDir.resolve("src/bin/sym.java"));
        cambiarPaquete(baseDir.resolve("src/bin/Parser.java"));       
    }

    /**
     * Método para mover archivos desde una ruta de origen a una ruta de destino.
     * Verifica si el archivo de destino ya existe, lo elimina si es necesario y mueve el archivo de origen
     * a la ruta de destino.
     * 
     * @param rutaOrigen La ruta del archivo de origen que se desea mover.
     * @param rutaDestino La ruta de destino donde se moverá el archivo.
     * @throws IOException Si ocurre un error al verificar, eliminar o mover el archivo.
     */    
    private static void moverArchivo(Path rutaOrigen, Path rutaDestino) throws IOException {
        if (Files.exists(rutaOrigen)) {
            if (Files.exists(rutaDestino)) {
                Files.delete(rutaDestino);  // Eliminar archivo destino si ya existe
            }
            Files.move(rutaOrigen, rutaDestino);  // Mover archivo
        }
    }

    /**
     * Método para cambiar el paquete de un archivo .java generado.
     * Este método reemplaza el paquete actual por el nuevo.
     * 
     * @param archivo La ruta del archivo que se desea modificar.
     * @throws IOException Si ocurre un error al modificar el archivo.
     */
    private static void cambiarPaquete(Path archivo) throws IOException {
        // Leer el contenido del archivo
        String contenido = new String(Files.readAllBytes(archivo));
        
        // Reemplazar el paquete "parserlexer" por "main" (o el nombre que desees)
        contenido = contenido.replace("package parserlexer;", "package bin;");
        
        // Guardar el contenido modificado de nuevo en el archivo
        Files.write(archivo, contenido.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }
}
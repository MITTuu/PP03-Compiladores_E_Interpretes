package views;

import bin.Lexer;
import bin.Parser;
import bin.sym;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import utils.AST.ProgramNode;
import utils.MIPS.GeneracionCodigo.CodeGenerator;
import utils.SymbolsTable.SymbolTable;
import utils.SymbolsTable.FunctionSymbol;
import utils.SymbolsTable.VariableSymbol;

/**
 * La clase {@code View_Main} representa la interfaz gráfica principal de la aplicación.
 * Proporciona un editor de texto con numeración de líneas y otras herramientas 
 * relacionadas con la generación, análisis léxico y sintáctico del código fuente.
 * 
 * Incluye funcionalidades para:
 *   - Cargar y guardar archivos.
 *   - Visualizar el código fuente con numeración de líneas.
 *   - Realizar análisis léxico, sintáctico y semántico.
 *   - Mostrar resultados en diferentes componentes de la interfaz.
 * 
 * @author Dylan Montiel Zúñiga
 * @author Joselyn Jiménez Salgado
 * @version 1/23/2025
 */
public class View_Main extends javax.swing.JFrame {

    LineNumberComponent lineNumberComponent;
    ProgramNode program ;
    SymbolTable symbolTable;
    
    /**
     * Constructor de la clase {@code View_Main}.
     * 
     * Inicializa los componentes gráficos de la interfaz y configura el editor de texto 
     */
    public View_Main() {
        initComponents();
        SwingUtilities.invokeLater(() -> {
        this.setLocationRelativeTo(null);
        
        jTextArea_Input.setLineWrap(false); // Desactiva el salto de línea automático
        jTextArea_Input.setWrapStyleWord(false);
        
        Font font = new Font("Consolas", Font.PLAIN, 14);   
        jTextArea_Input.setFont(font);
        
        // Crea el numerador de líneas
        lineNumberComponent = new LineNumberComponent(jTextArea_Input);        
        
        lineNumberComponent.setFont(font);

        // Añádelo como row header al JScrollPane
        jScrollPane1.setRowHeaderView(lineNumberComponent);
        
        // Actualiza las líneas con el scroll
        jScrollPane1.getVerticalScrollBar().addAdjustmentListener(e -> lineNumberComponent.repaint());

        });
    }
    
    /**
     * Abre un cuadro de diálogo para que el usuario seleccione un archivo de texto (.txt) 
     * desde su sistema de archivos. El archivo seleccionado se lee y su contenido 
     * se muestra en un área de texto (JTextArea) en la interfaz gráfica.     
     * 
     * @see JFileChooser
     * @see FileNameExtensionFilter
     * @see JTextArea
     */
    private void loadFile() {
        JFileChooser chooser = new JFileChooser();

        // Crear un filtro para archivos .txt
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        chooser.setFileFilter(filter);

        // Mostrar el diálogo para abrir el file
        int result = chooser.showOpenDialog(null);

        // Verificar si el usuario seleccionó un file
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try {
                String ST = new String(Files.readAllBytes(file.toPath()));
                jTextArea_Input.setText(ST);  // Establecer el contenido del file en el JTextArea
                lineNumberComponent.repaint();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Guarda el contenido del área de texto (`JTextArea`) en un archivo de texto (.txt) 
     * seleccionado por el usuario a través de un cuadro de diálogo.
     *
     * @see JFileChooser
     * @see BufferedWriter
     * @see FileWriter
     * @see JTextArea
     */
    private void saveToFile() {
        String content = jTextArea_Output.getText();
        
        //Abre una ventana de dialogo para elegir la ruta donde se guardará el texto analizado léxicamente.
        JFileChooser fileChooser = new JFileChooser();
        int selection = fileChooser.showSaveDialog(null);

        if (selection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
           // Verificar si el file tiene la extensión .txt
           if (!file.getName().toLowerCase().endsWith(".txt")) {
               file = new File(file.getAbsolutePath() + ".txt");
           }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente en: " + file.getPath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Guardado cancelado.");
        }
    }
    
    private void saveMipsCode(String dataMIPS, String textMIPS) {
        String dataSection = "# --------------------------------------------------\n"
                    + "# Sección de Datos (.data)\n"
                    + "# --------------------------------------------------\n"
                    + ".data \n\n"
                    + dataMIPS;
        String textSection = "\n\n# --------------------------------------------------\n"
                    + "# Carga de valores en registros (.text)\n"
                    + "# --------------------------------------------------\n"
                    + ".text \n\n"
                    + "# La directiva .globl main declara la etiqueta 'main' como símbolo global \n"
                    + "# lo que indica que es el punto de entrada del programa \n"
                    + ".globl main \n\n"
                    + "main: \n\n"
                    + textMIPS;

        String content = dataSection + textSection;
        
        //Abre una ventana de dialogo para elegir la ruta donde se guardará el texto de codigo generado.
        JFileChooser fileChooser = new JFileChooser();
        int selection = fileChooser.showSaveDialog(null);

        if (selection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
           // Verificar si el file tiene la extensión .txt
           if (!file.getName().toLowerCase().endsWith(".asm")) {
               file = new File(file.getAbsolutePath() + ".asm");
           }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente en: " + file.getPath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Guardado cancelado.");
        }
    }

    /**
     * Realiza el análisis sintáctico del código fuente ingresado en el área de texto. Utiliza el 
     * analizador léxico ({@link Lexer}) y el analizador sintáctico ({@link Parser}) para procesar 
     * el texto proporcionado. Realiza las siguientes operaciones:
     *   - Obtiene y analiza el texto ingresado en el área de entrada.
     *   - Detecta errores léxicos y sintácticos, si los hay, y los muestra en el área de salida.
     *   - Genera el árbol sintáctico abstracto (AST) y la tabla de símbolos si no se detectan errores críticos.
     *   - Opcionalmente, muestra el AST y la tabla de símbolos en ventanas separadas si la opción correspondiente 
     *     está habilitada mediante un {@code JCheckBox}.
     * 
     * En caso de errores, se muestra un mensaje en el área de salida.
     */
    private void analizadorSintactico(){
        String expr = jTextArea_Input.getText();

        Parser parser = new Parser(new Lexer(new StringReader(expr)));

        try {
            Symbol result = parser.parse();            
            String salidaAST = "Sin datos para mostrar.";
            
            // Lista de los errores sintácticos acumulados
            List<String> semanticErrorList = new ArrayList<>();
            
            //Funcionalidad tabla de símbolos
            symbolTable = (SymbolTable) parser.getSymbolTable();  
            
            if(result.value != null)
            {
                program = (ProgramNode)result.value;
                
                //Obtiene el arbol sintáctico mediante el metodo abstracto ToString
                salidaAST = program.toString(" ");               
                
                //Obtiene la lista de errores semánticos según el analisis
                //semanticErrorList = program.getSemanticErrorList();//esta lista no esta siendo usada
                // Obtener los errores sintácticos acumulados
                semanticErrorList = parser.getSemanticErrorList();
                                
            }            
            Lexer s =  (Lexer) parser.getScanner();
            
            List<Symbol> lexErrorList = s.lexErrorList;
            // Obtener los errores sintácticos acumulados
            List<String> errorList = parser.getErrorList();

            if (errorList.isEmpty() && semanticErrorList.isEmpty() && lexErrorList.isEmpty() ) {
                jTextArea_Output.setText("Análisis Sintáctico realizado correctamente");
                jTextArea_Output.setForeground(new Color(25, 111, 61));
            } else {
                StringBuilder mensajeErrores = new StringBuilder("ERRORES ENCONTRADOS:\n");
                if(!lexErrorList.isEmpty()) {
                    mensajeErrores.append("\n");
                    mensajeErrores.append("Errores léxicos:").append("\n");
                    
                    for (Symbol sym: lexErrorList){
                        mensajeErrores.append("Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1) + ", Texto: \"" + sym.value + "\"").append("\n");
                    }
                }                
                
                if(!errorList.isEmpty()) {
                    mensajeErrores.append("\n\n");
                    mensajeErrores.append("ERRORES SINTÁCTICOS:").append("\n");
                    for (String error : errorList) {
                        mensajeErrores.append(error).append("\n");
                    }
                }
                
                /*LISTA DE ERRORES SEMÁNTICOS*/
                if(!semanticErrorList.isEmpty()) {
                    mensajeErrores.append("\n\n");
                    mensajeErrores.append("ERRORES SEMÁNTICOS:").append("\n");
                    for (String error : semanticErrorList) {
                        mensajeErrores.append(error).append("\n");
                    }
                }
                jTextArea_Output.setText(mensajeErrores.toString());
                jTextArea_Output.setForeground(Color.red);
                lineNumberComponent.repaint();               
            }

            // --- Prompt resultado ---
            if (errorList.isEmpty() && semanticErrorList.isEmpty() && lexErrorList.isEmpty()) {
                String mensajeExito = "Análisis exitoso: El código ingresado puede ser traducido a código fuente.\n\n"
                                    + "¿Desea generar el código de salida?";

                int opcion = JOptionPane.showOptionDialog(null, mensajeExito, "Análisis Correcto",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                        new Object[]{"Generar Código", "Cerrar"}, "Generar Código");

                if (opcion == JOptionPane.YES_OPTION) {
                    generarCodigoDestino();
                }
            } else {
                StringBuilder mensajeErrores = new StringBuilder("Análisis fallido: El código no puede ser traducido a código fuente.\n");
                JOptionPane.showMessageDialog(null, mensajeErrores.toString(), "Errores Encontrados", JOptionPane.ERROR_MESSAGE);
            }
            
            //Checkbox para hacer opcional mostrar ventanas de tabla de symbolos y arbol sintáctico
            var mostrarInfoExtra = jCheckBox_VerInfoExtra.isSelected();
            if(mostrarInfoExtra){
               showSymbolTable(symbolTable);
               showAST(salidaAST);
            }
            
            
        } catch (Exception ex) {
            Symbol sym = parser.getS();
            jTextArea_Output.setText("Error crítico: " + ex.getMessage());
            jTextArea_Output.setForeground(Color.red);
        }
    }

    /**
     * Este método utiliza un objeto {@link Lexer} para procesar el texto ingresado en el área de texto y realizar el análisis léxico. 
     * El resultado se muestra en el área de salida con información detallada sobre cada token reconocido, incluyendo la línea, columna 
     * y el lexema correspondiente. En caso de que se detecten errores léxicos, estos se listan al final.
     */
    private void analizadorLexico() {
       boolean continuaError = false;
        // Obtiene el texto de entrada desde JTATextoArea
        String expr = jTextArea_Input.getText();
        jTextArea_Output.setForeground(Color.black);
        if(expr.isEmpty()){
            jTextArea_Output.setText("Análisis léxico completado: Archivo vacío.");
            return;
        }
        Lexer lexer = new Lexer(new StringReader(expr));
        
        // Variables para definir el ancho de las columnas
        int anchoColumna1 = 25;
        int anchoColumna2 = 35;
        int anchoColumna3 = 35;

        // Formato dinámico
        String formato = String.format("%%-%ds %%-%ds %%%ds%%n", anchoColumna1, anchoColumna2, anchoColumna3);

        // StringBuilder para construir el texto
        StringBuilder sb = new StringBuilder();
        
        // Linea de Titulos
        sb.append(String.format(formato, "LINEA / COLUMNA ", "SIMBOLO", "LEXEMA"));
        
        try {
            while (true) {
                Symbol symbol = lexer.next_token();
                int numLine = symbol.right + 1;
                int numColumn = symbol.left + 1;
                if (symbol == null || symbol.value == null) {
                    
                    //Obtiene la lista de errores y los contaten al final del documento.
                    List<Symbol> lexErrorList = lexer.lexErrorList;
                    
                    if(!lexErrorList.isEmpty()) {
                        sb.append("\n\n");
                        sb.append("Errores léxicos:").append("\n");

                        for (Symbol sym: lexErrorList){
                            sb.append("Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1) + ", Texto: \"" + sym.value + "\"").append("\n");
                        }
                    }    
                    
                    jTextArea_Output.setText(sb.toString());
                    lineNumberComponent.repaint();
                    return;
                }
              
                //Flag para registrar error solo una vez

                switch (symbol.sym) {
                    case sym.BlockOpening:
                        sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn , "<Apertura de Bloque>" , symbol.value ));
                        break;
                    case sym.BlockClosure:
                        sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn , "<CierreBloque>" , symbol.value ));
                        break;
                    case sym.Integer:
                        sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn , "<Integer>" , symbol.value ));
                        break;
                    case sym.Float:
                        sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn , "<Float>" , symbol.value ));
                        break;
                    case sym.Bool:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Bool>" , symbol.value ));
                       break;
                    case sym.Char:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Char>" , symbol.value ));
                       break;
                    case sym.String:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<String>" , symbol.value ));
                       break;
                    case sym.Identifier:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Identificador>" , symbol.value ));
                       break;
                    case sym.BracketOpening:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<CorcheteApertura>" , symbol.value ));
                       break;
                    case sym.BracketClosure:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<CorcheteCierre>" , symbol.value ));
                       break;
                    case sym.AssignmentSign:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<SignoAsignacion>" , symbol.value ));
                       break;
                    case sym.ParenthesisOpening:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<ParentesisApertura>" , symbol.value ));
                       break;
                    case sym.ParenthesisClosure:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<ParentesisCierre>" , symbol.value ));
                       break;
                    case sym.CharacterLiteral:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<LiteralCaracter>" , symbol.value ));
                       break;
                    case sym.StringLiteral:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<LiteralCadena>" , symbol.value ));
                       break;
                    case sym.IntegerLiteral:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<LiteralEntero>" , symbol.value ));
                       break;   
                    case sym.FloatLiteral:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<LiteralFlotante>" , symbol.value ));
                       break;
                    case sym.BoolLiteral:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<LiteralBool>" , symbol.value ));
                       break;                        
                    case sym.Sum:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Suma>" , symbol.value ));
                       break;
                    case sym.Subtraction:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Resta>" , symbol.value ));
                       break;
                    case sym.Division:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Division>" , symbol.value ));
                       break;
                    case sym.Multiplication:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Multiplicacion>" , symbol.value ));
                       break;
                    case sym.Module:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Modulo>" , symbol.value ));
                       break;
                    case sym.Power:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Potencia>" , symbol.value ));
                       break;
                    case sym.Increment:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Incremento>" , symbol.value ));
                       break;
                    case sym.Decrement:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Decremento>" , symbol.value ));
                       break;
                    case sym.Negative:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Negativo>" , symbol.value ));
                       break;
                    case sym.Less:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Menor>" , symbol.value ));
                       break;
                    case sym.LessEqual:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<MenorIgual>" , symbol.value ));
                       break;
                    case sym.Greater:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Mayor>" , symbol.value ));
                       break;
                    case sym.GreaterEqual:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<MayorIgual>" , symbol.value ));
                       break;
                    case sym.Equal:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Igual>" , symbol.value ));
                       break;
                    case sym.NotEqual:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Diferente>" , symbol.value ));
                       break;
                    case sym.Conjunction:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Conjuncion>" , symbol.value ));
                       break;
                    case sym.Disjunction:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Disyuncion>" , symbol.value ));
                       break;
                    case sym.Negation:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Negacion>" , symbol.value ));
                       break;
                    case sym.If:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<If>" , symbol.value ));
                       break;
                    case sym.Else:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Else>" , symbol.value ));
                       break;
                    case sym.While:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<While>" , symbol.value ));
                       break;
                    case sym.For:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<For>" , symbol.value ));
                       break;
                    case sym.Switch:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Switch>" , symbol.value ));
                       break;
                    case sym.Case:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Case>" , symbol.value ));
                       break;
                    case sym.Default:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Default>" , symbol.value ));
                       break;
                    case sym.Break:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Break>" , symbol.value ));
                       break;
                    case sym.Return:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Return>" , symbol.value ));
                       break;
                    case sym.Colon:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<DosPuntos>" , symbol.value ));
                       break;
                    case sym.Print:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Print>" , symbol.value ));
                       break;
                    case sym.Read:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Read>" , symbol.value ));
                       break;                  
                    case sym.EndSentence:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<FinSentencia>" , symbol.value ));
                       break;
                    case sym.Main:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Main>" , symbol.value ));
                       break;
                    case sym.Comma:
                       sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn ,"<Coma>" , symbol.value ));
                       break;                       
                    default:
                        sb.append(String.format(formato, "Linea: " + numLine + " Columna: " + numColumn , "Símbolo <no controlado>", symbol.value ));
                        break;  
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error durante el análisis: " + e.getMessage());
        }
    }
    
    public static void generarCodigoDestino(){
        System.out.println("generar codigo");
    }    
    
    /**
     * Muestra el contenido de la tabla de símbolos en un cuadro de diálogo con dos tablas:
     * una para las funciones y otra para las variables. Cada tabla se ajusta dinámicamente 
     * al tamaño de su contenido y se incluye un scroll para manejar tablas grandes.
     *
     * @param symbolTable la tabla de símbolos que contiene las funciones y variables a mostrar.
     */ 
    public static void showSymbolTable(SymbolTable symbolTable) {
        // Crear las columnas para la tabla de funciones
        String[] functionColumns = {"Función", "Tipo de Retorno", "Parámetros"};
        DefaultTableModel functionTableModel = new DefaultTableModel(functionColumns, 0);

        // Llenar la tabla de funciones con datos
        for (Map.Entry<String, FunctionSymbol> entry : symbolTable.getFunctionSymbols().entrySet()) {
            FunctionSymbol function = entry.getValue();
            functionTableModel.addRow(new Object[]{
                    function.getName(),
                    function.getReturnType(),
                    function.getParameters()
            });
        }
        JTable functionTable = new JTable(functionTableModel);

        // Ajustar tamaño de la tabla de funciones según su contenido
        functionTable.setFillsViewportHeight(true);
        functionTable.setPreferredScrollableViewportSize(functionTable.getPreferredSize());

        // Crear las columnas para la tabla de variables
        String[] variableColumns = {"Variable", "Tipo", "Scope", "Valor", "Línea", "Dirección de memoria"};
        DefaultTableModel variableTableModel = new DefaultTableModel(variableColumns, 0);

        // Obtener las variables de la tabla de símbolos y ordenarlas por el número de línea
        List<VariableSymbol> variables = new ArrayList<>(symbolTable.getVariableSymbols().values());
        variables.sort(Comparator.comparingInt(VariableSymbol::getDeclarationLine));

        // Llenar la tabla de variables con datos ordenados
        for (VariableSymbol variable : variables) {
            variableTableModel.addRow(new Object[]{
                    variable.getName(),
                    variable.getType(),
                    variable.getScope(),
                    variable.getValue() != null ? variable.getValue().toString() : "null",
                    variable.getDeclarationLine(),
                    variable.getMemoryAddress()
            });
        }
        JTable variableTable = new JTable(variableTableModel);

        // Ajustar tamaño de la tabla de variables según su contenido
        variableTable.setFillsViewportHeight(true);
        variableTable.setPreferredScrollableViewportSize(variableTable.getPreferredSize());

        // Crear paneles con scroll para ambas tablas
        JScrollPane functionScrollPane = new JScrollPane(functionTable);
        functionScrollPane.setBorder(BorderFactory.createTitledBorder("Funciones"));

        JScrollPane variableScrollPane = new JScrollPane(variableTable);
        variableScrollPane.setBorder(BorderFactory.createTitledBorder("Variables"));

        // Crear un panel principal para colocar las tablas
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(functionScrollPane);
        panel.add(Box.createVerticalStrut(5)); 
        panel.add(variableScrollPane);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(900, 500));

        JOptionPane.showMessageDialog(null, scrollPane, "Tabla de Símbolos", JOptionPane.PLAIN_MESSAGE);
    }


    /**
     * Muestra el Árbol Sintáctico Abstracto (AST) en un cuadro de diálogo con un área de texto.
     * El texto del AST se muestra en una fuente monoespaciada y el cuadro incluye un scroll 
     * para manejar contenido extenso.
     *
     * @param arbol una representación en texto del Árbol Sintáctico Abstracto (AST) a mostrar.
     */
    public static void showAST(String arbol) {
        JTextArea textArea = new JTextArea(arbol);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(800, 500));

        JOptionPane.showMessageDialog(null, scrollPane, "Árbol Sintáctico Abstracto (AST)", JOptionPane.PLAIN_MESSAGE);
    }
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel_Body = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Input = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_Output = new javax.swing.JTextArea();
        jButton_LoadFile = new javax.swing.JButton();
        jButton_SintaxAnalyzer = new javax.swing.JButton();
        jButton_LexicalAnalyzer = new javax.swing.JButton();
        label_Titulo = new java.awt.Label();
        jButton_SaveToFile = new javax.swing.JButton();
        jCheckBox_VerInfoExtra = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea_Input.setColumns(20);
        jTextArea_Input.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea_Input.setRows(5);
        jScrollPane1.setViewportView(jTextArea_Input);

        jTextArea_Output.setEditable(false);
        jTextArea_Output.setColumns(20);
        jTextArea_Output.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTextArea_Output.setRows(5);
        jScrollPane2.setViewportView(jTextArea_Output);

        jButton_LoadFile.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton_LoadFile.setText("Cargar Archivo");
        jButton_LoadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LoadFileActionPerformed(evt);
            }
        });

        jButton_SintaxAnalyzer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton_SintaxAnalyzer.setText("Análisis Sintáctico");
        jButton_SintaxAnalyzer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SintaxAnalyzerActionPerformed(evt);
            }
        });

        jButton_LexicalAnalyzer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton_LexicalAnalyzer.setText("Análisis Léxico");
        jButton_LexicalAnalyzer.setMaximumSize(new java.awt.Dimension(120, 30));
        jButton_LexicalAnalyzer.setMinimumSize(new java.awt.Dimension(120, 30));
        jButton_LexicalAnalyzer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LexicalAnalyzerActionPerformed(evt);
            }
        });

        label_Titulo.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        label_Titulo.setText("Analizador Léxico y Sintáctico");

        jButton_SaveToFile.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton_SaveToFile.setText("Guardar Análisis");
        jButton_SaveToFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SaveToFileActionPerformed(evt);
            }
        });

        jCheckBox_VerInfoExtra.setText("Mostrar Tabla de Symbolos y Arbol Sintáctico");

        jButton1.setText("Generar Código MIPS");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_BodyLayout = new javax.swing.GroupLayout(jPanel_Body);
        jPanel_Body.setLayout(jPanel_BodyLayout);
        jPanel_BodyLayout.setHorizontalGroup(
            jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel_BodyLayout.createSequentialGroup()
                .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_BodyLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel_BodyLayout.createSequentialGroup()
                                .addComponent(jButton_LoadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton_SaveToFile, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel_BodyLayout.createSequentialGroup()
                        .addGap(194, 194, 194)
                        .addComponent(jButton_LexicalAnalyzer, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(155, 155, 155)
                        .addComponent(jButton_SintaxAnalyzer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBox_VerInfoExtra)
                        .addGap(118, 118, 118)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 107, Short.MAX_VALUE)))
                .addGap(29, 29, 29))
            .addComponent(jScrollPane2)
        );
        jPanel_BodyLayout.setVerticalGroup(
            jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_SaveToFile, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton_LoadFile))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_LexicalAnalyzer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_SintaxAnalyzer)
                    .addComponent(jCheckBox_VerInfoExtra)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addContainerGap())
        );

        label_Titulo.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel_Body.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleName("JFrame_Parent");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_LoadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LoadFileActionPerformed
        loadFile();
    }//GEN-LAST:event_jButton_LoadFileActionPerformed

    private void jButton_LexicalAnalyzerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LexicalAnalyzerActionPerformed
        analizadorLexico();
    }//GEN-LAST:event_jButton_LexicalAnalyzerActionPerformed

    private void jButton_SaveToFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SaveToFileActionPerformed
        saveToFile();
    }//GEN-LAST:event_jButton_SaveToFileActionPerformed

    private void jButton_SintaxAnalyzerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SintaxAnalyzerActionPerformed
        analizadorSintactico();
    }//GEN-LAST:event_jButton_SintaxAnalyzerActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       if (program == null) {
            JOptionPane.showMessageDialog(null, "El programa necesita realizar el Análisis de Código primero.", "Error", JOptionPane.WARNING_MESSAGE);
       }else{

            //Asigna la tabla de Simbolos al arbol sintáctico abstracto para poder hacer referencia
            //a la misma en el método generateMIPS
            program.setSymbolTable(symbolTable);


            // Instanciar el generador de código
            CodeGenerator cg = new CodeGenerator();
            //Realiza la llamada para generar el codigo MIPS Y crear la estructura .data del CodeGenerator class.
            //Lo realiza mediante el metodo abstracto generateMIPS que recibe la instancia de CodeGenerator y retorna el .text;
            String textMIPS = program.generateMIPS(cg);

            //Obtiene el codigo de la sección .data
            String dataMIPS = cg.getDataSection();

            //Almacena el código en un archivo
            saveMipsCode(dataMIPS, textMIPS);
       }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new View_Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_LexicalAnalyzer;
    private javax.swing.JButton jButton_LoadFile;
    private javax.swing.JButton jButton_SaveToFile;
    private javax.swing.JButton jButton_SintaxAnalyzer;
    private javax.swing.JCheckBox jCheckBox_VerInfoExtra;
    private javax.swing.JPanel jPanel_Body;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea_Input;
    private javax.swing.JTextArea jTextArea_Output;
    private java.awt.Label label_Titulo;
    // End of variables declaration//GEN-END:variables
}

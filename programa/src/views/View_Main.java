package views;

import bin.Lexer;
import bin.Parser;
import bin.sym;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import utils.SymbolTable;
import utils.SymbolTable.FunctionSymbol;
import utils.SymbolTable.VariableSymbol;
import utils.TreeNode;

public class View_Main extends javax.swing.JFrame {

    LineNumberComponent lineNumberComponent;
    /**
     * Creates new form GUI_Main
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
    
    private void analizadorSintactico(){
        String expr = jTextArea_Input.getText();

        Parser parser = new Parser(new Lexer(new StringReader(expr)));

        try {
            parser.parse();            
            
            Lexer s =  (Lexer) parser.getScanner();
            
            List<Symbol> lexErrorList = s.lexErrorList;
            // Obtener los errores acumulados
            List<String> errorList = parser.getErrorList();

            if (errorList.isEmpty() && lexErrorList.isEmpty() ) {
                jTextArea_Output.setText("Análisis Sintáctico realizado correctamente");
                jTextArea_Output.setForeground(new Color(25, 111, 61));
            } else {
                StringBuilder mensajeErrores = new StringBuilder("ERRORES ENCONTRADOS:\n");
                if(!lexErrorList.isEmpty()) {
                    mensajeErrores.append("\n\n");
                    mensajeErrores.append("Errores léxicos:").append("\n");
                    
                    for (Symbol sym: lexErrorList){
                        mensajeErrores.append("Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1) + ", Texto: \"" + sym.value + "\"").append("\n");
                    }
                }                
                
                if(!errorList.isEmpty()) {
                    mensajeErrores.append("\n\n");
                    mensajeErrores.append("Errores sintácticos:").append("\n");
                    for (String error : errorList) {
                        mensajeErrores.append(error).append("\n");
                    }
                }
                
                jTextArea_Output.setText(mensajeErrores.toString());
                jTextArea_Output.setForeground(Color.red);
                lineNumberComponent.repaint();               
            }
            //Funcionalidad tabla de símbolos
            SymbolTable symbolTable = (SymbolTable) parser.getSymbolTable();
            symbolTable.printVariableSymbols();
            
            TreeNode treeNode = (TreeNode) parser.getTreeNode();
            treeNode.printTree("");
            
            showSymbolTable(symbolTable);
            
            showTreeNode(treeNode);
            
        } catch (Exception ex) {
            Symbol sym = parser.getS();
            jTextArea_Output.setText("Error crítico de sintaxis. Linea: " + (sym.right + 1) + " Columna: " + (sym.left + 1) + ", Texto: \"" + sym.value + "\"");
            jTextArea_Output.setForeground(Color.red);
        }

                
    }

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

    // Método para mostrar el contenido de la tabla de símbolos
    public static void showSymbolTable(SymbolTable symbolTable) {
        StringBuilder sb = new StringBuilder();

            sb.append("Funciones:\n");
            for (Map.Entry<String, FunctionSymbol> entry : symbolTable.getFunctionSymbols().entrySet()) {
                sb.append(" Scope: ").append(entry.getValue().getName() ).append(" -> Parametros: " +entry.getValue().getParameters()+ " | Tipo del retorno: " +entry.getValue().getReturnType()).append("\n");
            }

            sb.append("\nVariables:\n");
            for (Map.Entry<String, VariableSymbol> entry : symbolTable.getVariableSymbols().entrySet()) {
                sb.append(" Scope: ").append(entry.getValue().getScope()).append(" -> Nombre: ").append(entry.getValue().getName() +" | Tipo: " +entry.getValue().getType()).append("\n");
            }

        // Mostrar el contenido en un JOptionPane
        JOptionPane.showMessageDialog(null, sb.toString(), "Tabla de Símbolos", JOptionPane.PLAIN_MESSAGE);
    }
    
    // Método para mostrar el contenido de la tabla de símbolos
    public static void showTreeNode(TreeNode treeNode) {
        StringBuilder sb = new StringBuilder();
        
        buildTreeString(sb, treeNode, 0);
        
        JOptionPane.showMessageDialog(null, sb.toString(), "Árbol Sintáctico", JOptionPane.PLAIN_MESSAGE);
    }

    // Método recursivo para construir la cadena de texto del árbol
    private static void buildTreeString(StringBuilder sb, TreeNode node, int level) {
        // Añadir el nodo actual con la indentación correspondiente
        for (int i = 0; i < level; i++) {
            sb.append("         ");
        }
        sb.append(node.getValue()).append("\n");
        
        // Recursión sobre los hijos del nodo actual
        for (TreeNode child : node.getChildren()) {
            buildTreeString(sb, child, level + 1);
        }
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

        javax.swing.GroupLayout jPanel_BodyLayout = new javax.swing.GroupLayout(jPanel_Body);
        jPanel_Body.setLayout(jPanel_BodyLayout);
        jPanel_BodyLayout.setHorizontalGroup(
            jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BodyLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(label_Titulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))
                .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_BodyLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton_SintaxAnalyzer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_LexicalAnalyzer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton_LoadFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_BodyLayout.createSequentialGroup()
                        .addGap(576, 576, 576)
                        .addComponent(jButton_SaveToFile, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel_BodyLayout.setVerticalGroup(
            jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BodyLayout.createSequentialGroup()
                .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel_BodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton_SaveToFile))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel_BodyLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jButton_LoadFile)
                        .addGap(80, 80, 80)
                        .addComponent(jButton_LexicalAnalyzer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jButton_SintaxAnalyzer)))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        label_Titulo.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    private javax.swing.JButton jButton_LexicalAnalyzer;
    private javax.swing.JButton jButton_LoadFile;
    private javax.swing.JButton jButton_SaveToFile;
    private javax.swing.JButton jButton_SintaxAnalyzer;
    private javax.swing.JPanel jPanel_Body;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea_Input;
    private javax.swing.JTextArea jTextArea_Output;
    private java.awt.Label label_Titulo;
    // End of variables declaration//GEN-END:variables
}

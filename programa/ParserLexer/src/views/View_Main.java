package views;

import bin.sym;
import bin.Lexer;
import bin.sym;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author dylan
 */
public class View_Main extends javax.swing.JFrame {

    /**
     * Creates new form GUI_Main
     */
    public View_Main() {
        initComponents();
        this.setLocationRelativeTo(null);
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

        // Mostrar el diálogo para abrir el archivo
        int result = chooser.showOpenDialog(null);

        // Verificar si el usuario seleccionó un archivo
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try {
                String ST = new String(Files.readAllBytes(file.toPath()));
                jTextArea_Input.setText(ST);  // Establecer el contenido del archivo en el JTextArea
            } catch (FileNotFoundException ex) {
                Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(View_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }

    private void saveToFile() {
        string content = jTextArea_Output.getText()
        
        //Abre una ventana de dialogo para elegir la ruta donde se guardará el texto analizado léxicamente.
        JFileChooser fileChooser = new JFileChooser();
        int seleccion = fileChooser.showSaveDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            
           // Verificar si el archivo tiene la extensión .txt
           if (!archivo.getName().toLowerCase().endsWith(".txt")) {
               archivo = new File(archivo.getAbsolutePath() + ".txt");
           }
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
                writer.write(content);
                JOptionPane.showMessageDialog(null, "Archivo guardado exitosamente en: " + archivo.getPath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar el archivo: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Guardado cancelado.");
        }
    }
    
    private void analizadorLexico() {
       int contLinea = 1;
       int contColumna = 1;
       int lexemaLength = 0;
       boolean continuaError = false;
        // Obtiene el texto de entrada desde JTATextoArea
        String expr = jTextArea_Input.getText(); 
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
        
        // Primera Linea
        sb.append(String.format(formato, "Linea " + contLinea , "", ""));
        try {
            while (true) {
                //Tokens token = lexer.yylex();
                Symbol symbol = lexer.next_token();
                if (symbol == null) {
                    // Guardar el resultado en un archivo             
                    jTextArea_Input.setText(sb.toString());
                    return;
                }
                //Registrar longitud de la cadena de caracteres del lexema para calcular la columna
                lexemaLength = symbol.value.toString().length();
                
                //Flag para registrar error solo una vez
                if(continuaError && symbol.sym != sym.Error){
                    continuaError = false;
                }
                switch (symbol.sym) {
                    case sym.NewLine:
                        contLinea++;
                        contColumna = 0;
                        sb.append(String.format(formato, "Linea " + contLinea , "", ""));
                        break;
                    case sym.BlockOpening:
                        sb.append(String.format(formato, "Columna " + contColumna , "<Apertura de Bloque>" , symbol.value ));
                        break;
                    case sym.BlockClosure:
                        sb.append(String.format(formato, "Columna " + contColumna , "<CierreBloque>" , symbol.value ));
                        break;
                    case sym.Integer:
                        sb.append(String.format(formato, "Columna " + contColumna , "<Integer>" , symbol.value ));
                        break;
                    case sym.Float:
                        sb.append(String.format(formato, "Columna " + contColumna , "<Float>" , symbol.value ));
                        break;
                    case sym.Bool:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Bool>" , symbol.value ));
                       break;
                    case sym.Char:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Char>" , symbol.value ));
                       break;
                    case sym.String:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<String>" , symbol.value ));
                       break;
                    case sym.Identifier:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Identificador>" , symbol.value ));
                       break;
                    case sym.BracketOpening:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<CorcheteApertura>" , symbol.value ));
                       break;
                    case sym.BracketClosure:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<CorcheteCierre>" , symbol.value ));
                       break;
                    case sym.AssignmentSign:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<SignoAsignacion>" , symbol.value ));
                       break;
                    case sym.ParenthesisOpening:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<ParentesisApertura>" , symbol.value ));
                       break;
                    case sym.ParenthesisClosure:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<ParentesisApertura>" , symbol.value ));
                       break;
                    case sym.CharacterLiteral:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<LiteralCaracter>" , symbol.value ));
                       break;
                    case sym.StringLiteral:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<LiteralCadena>" , symbol.value ));
                       break;
                    case sym.IntegerLiteral:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<LiteralEntero>" , symbol.value ));
                       break;   
                    case sym.FloatLiteral:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<LiteralFlotante>" , symbol.value ));
                       break;
                    case sym.BoolLiteral:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<LiteralBool>" , symbol.value ));
                       break;                        
                    case sym.Sum:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Suma>" , symbol.value ));
                       break;
                    case sym.Subtraction:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Resta>" , symbol.value ));
                       break;
                    case sym.Division:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Division>" , symbol.value ));
                       break;
                    case sym.Multiplication:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Multiplicacion>" , symbol.value ));
                       break;
                    case sym.Module:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Modulo>" , symbol.value ));
                       break;
                    case sym.Power:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Potencia>" , symbol.value ));
                       break;
                    case sym.Increment:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Incremento>" , symbol.value ));
                       break;
                    case sym.Decrement:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Decremento>" , symbol.value ));
                       break;
                    case sym.Negative:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Negativo>" , symbol.value ));
                       break;
                    case sym.Less:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Menor>" , symbol.value ));
                       break;
                    case sym.LessEqual:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<MenorIgual>" , symbol.value ));
                       break;
                    case sym.Greater:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Mayor>" , symbol.value ));
                       break;
                    case sym.GreaterEqual:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<MayorIgual>" , symbol.value ));
                       break;
                    case sym.Equal:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Igual>" , symbol.value ));
                       break;
                    case sym.NotEqual:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Diferente>" , symbol.value ));
                       break;
                    case sym.Conjunction:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Conjuncion>" , symbol.value ));
                       break;
                    case sym.Disjunction:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Disyuncion>" , symbol.value ));
                       break;
                    case sym.Negation:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Negacion>" , symbol.value ));
                       break;
                    case sym.If:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<If>" , symbol.value ));
                       break;
                    case sym.Else:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Else>" , symbol.value ));
                       break;
                    case sym.While:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<While>" , symbol.value ));
                       break;
                    case sym.For:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<For>" , symbol.value ));
                       break;
                    case sym.Switch:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Switch>" , symbol.value ));
                       break;
                    case sym.Case:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Case>" , symbol.value ));
                       break;
                    case sym.Default:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Default>" , symbol.value ));
                       break;
                    case sym.Break:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Break>" , symbol.value ));
                       break;
                    case sym.Return:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Return>" , symbol.value ));
                       break;
                    case sym.Colon:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<DosPuntos>" , symbol.value ));
                       break;
                    case sym.Print:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Print>" , symbol.value ));
                       break;
                    case sym.Read:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Read>" , symbol.value ));
                       break;
                    case sym.Comment:
                       String valorSinSaltosComentario = symbol.value.toString().replaceAll("[\\r\\n]+", " "); 
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Comentario>" , valorSinSaltosComentario ));
                       break;                    
                    case sym.EndSentence:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<FinSentencia>" , symbol.value ));
                       break;
                    case sym.Main:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Main>" , symbol.value ));
                       break;
                    case sym.Comma:
                       sb.append(String.format(formato, "Columna " + contColumna ,"<Coma>" , symbol.value ));
                       break;                      
                    case sym.Error:
                        if(continuaError){
                            break;
                        }
                        sb.append(String.format(formato, "Columna " + contColumna ,"<ERROR: Símbolo no definido>" , "" ));
                        //Flag para registrar error solo una vez
                        continuaError = true;
                        break;
                    case sym.BlankSpace:
                        break;
                    case sym.EndFile:
                        sb.append(String.format(formato, "<FIN DE ARCHIVO>", "", ""));
                        //Devuelve el resultado del texto analizado al alcanzar el fin del archivo
                        jTextArea_Output.setText(sb.toString());
                        System.out.print("\033[H\033[2J");  
                        System.out.flush();  
                        System.out.print(sb.toString());
                        return;    
                    default:
                        sb.append(String.format(formato, "Columna " + contColumna , "Símbolo <no controlado>", symbol.value ));
                        break;  
                }
                contColumna+= lexemaLength;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error durante el análisis: " + e.getMessage());
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
        setResizable(false);

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
                    .addGroup(jPanel_BodyLayout.createSequentialGroup()
                        .addComponent(label_Titulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton_SaveToFile, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel_BodyLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel_BodyLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(jPanel_BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton_LexicalAnalyzer, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton_LoadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_BodyLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_SintaxAnalyzer)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(35, Short.MAX_VALUE))
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
            .addComponent(jPanel_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // TODO add your handling code here:
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

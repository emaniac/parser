/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grouper;

import java.awt.Frame;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import sun.security.pkcs11.wrapper.Constants;

/**
 *
 * @author Emania
 * 
 * selects text files and creates one summary text file
 */
public class Parser {
   
    //Variables
    ArrayList<File> listFiles;
    ArrayList<String> listText = new ArrayList<String>();
    String name;
    String fileName;
    String dir;
    ArrayList<String> parts;
    
    public void go(){
        selectFiles();      //selects multiple text files
        print();            //prints info about text files
        getText();          //transformes files into String with \par
        String text = "";
        System.out.println("text are "+listText);
        for(int i = 0; i < listFiles.size(); i++){  //
            text += getData(listText.get(i), listFiles.get(i));
        }
        text = makeRTF(text);   //adds header and brackets
        System.out.println("-----------------Vysledny soubor-----------------");
        System.out.println(text);
        createTextFile(text);
        JOptionPane.showMessageDialog(new Frame(), "successfully created \nfile name is: \n"+fileName);
        nul();
    }
    
    
    //selects multiple text files
    public void selectFiles(){
        System.out.println("-----------------selecting files-----------------");
        try { 
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, "Error loading look and feel", e);}
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(new Frame());
        File[] files = chooser.getSelectedFiles();
        listFiles = new ArrayList<File>(Arrays.asList(files));
        Collections.sort(listFiles);
        dir = listFiles.get(0).getPath();
    }
       
    //transformes files into String with \par to local variable listText
    public void getText(){
        System.out.println("-----------------Transforming files into text-----------------");
        for(File file : listFiles){
            try {
                //            String text = "";
//            System.out.println("***getText file " + file);
//            try { text = new Scanner(file, "UTF-8").useDelimiter("\\A").next();    //reads file
//            } catch (FileNotFoundException ex) {Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);}
//            String tmp = "";
//            Scanner scanner = new Scanner(text);
//            while (scanner.hasNextLine()) {
//              String line = scanner.nextLine();
//              tmp += "\\par ";
//              tmp += line;
//              tmp += "\n";
//            }
                List<String> text = Files.readAllLines(file.toPath());
                String tmp = "";
                for(int i = 0; i < text.size(); i++){
                    tmp += "\\par ";
                    tmp += text.get(i);
                    tmp += "\n";
                }
                listText.add(tmp);
            } catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    //makes final text for one text file
    public String getData(String text, File file){
        System.out.println("-----------------Getting data-----------------");
        String output = "";
        output += file.getPath();
        System.out.println("file is "+file);
        output += "\n\\par ";
        output += mySubstring(text);
        System.out.println("text is: "+text);
        System.out.println(output);
        return output;
    }
    
    //returns the important info from String (substring after 'Analyse total')
    public String mySubstring(String text){
        int offset = new String("Analyse Total ").length();
        int index = text.indexOf("Analyse Total ");
        System.out.println("**index is "+index+", offset is "+offset);
        if(text.length() == 0){
            return null;
        } else if(index == -1){
            return text;
        } else {
            return text.substring(index+offset);
        }
    }
    
    //prints info about text files
    public void print(){
        System.out.println("--------------printing info-----------------");
        for(File file : listFiles){
            System.out.println(file.getName());
            System.out.println(file);
        }
        System.out.println("directory = " + dir);
    }

    //adds header and brackets
    private String makeRTF(String text) {
        String output = "";
        output += "{\\rtf1\\ansi\\ansicpg1250\\deff0\\deflang1029{\\fonttbl{\\f0\\fnil MS Sans Serif;}}\n";
        output += "\\viewkind4\\uc1\\pard\\f0\\fs16\n";
        output += "\\par\n";
//        output += text.replace("\n", "\\par\n");
        output += text;
        output += "}";
        return output;
    }
    
    //creates text file 'name'in the directory of variable 'path'
    private void createTextFile(String text){
        String name = listFiles.get(0).getParentFile().getName();
        String path = listFiles.get(0).getParent();
        String file = path + "\\" + name + ".txt";
        
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream(file), "utf-8"));
            Scanner scanner = new Scanner(text);
            while (scanner.hasNextLine()) {
              String line = scanner.nextLine();
              writer.write(line);
              writer.write(Constants.NEWLINE);
            }
        } catch (IOException ex) {
        } finally {
           try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
        fileName = file;
    }
    
    //nulls all global variables
    private void nul() {
        listFiles.clear();
        listText.clear();
        name = null;
        fileName = null;
        dir = null;
        parts.clear();
    }
}

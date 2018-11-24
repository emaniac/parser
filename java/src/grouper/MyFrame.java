/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grouper;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import sun.security.pkcs11.wrapper.Constants;

/**
 *
 * @author Emania
 */
public class MyFrame extends JFrame{
    
    //Constants
    private final int xSize = 350;
    private final int ySize = 600;
    private int fontSize = 15;
    private Font myFont = new Font("SansSerif", Font.BOLD, fontSize);
    private String verze = "2.0";
    private String Log20 = "Zvetseni listu.";
    private String Log19 = "podpora promenlive indexace radku.";
    private String Log18 = "added field Popis.";
    private String Log175 = "updated new formating.";
    private String Log174 = "upravene ft.";
    private String Log173 = "upravene jmeno zakazky.";
    private String Log172 = "datum je vzdy dvouciferne.";
    private String Log171 = "v Parseru opravene nejake vybery, kodovani zmeneno na ANSI.";
    private String Log16 = "v Parseru opravene opakovani textu, vyjimky prazdny soubor a bez fraze analyse total.";
    private String Log15 = "Opraveny termin data a snad i nadpis zakaznik";
    
    //global Components
    private JTabbedPane tabbedPane;
    private JPanel panel3;
    private ArrayList<Frame> components = new ArrayList<Frame>();       //not used
    private JFrame frame;
//    JTextField tf1;
//    JTextArea ta1;
    
    //Variables
    String choosertitle;
    
    //Variables for page 1
    private JPanel panel1;
    private JButton bt1;
    private Grouper grouper;
//    private JFileChooser chooser;   //not used?
    
    //Variables for page 2
    private JPanel panel2;
    private JComboBox markeCB;
    private JComboBox typeCB;
    private JComboBox analyseSchemeCB;
    private JButton bt2;
    private JButton chooseDirBt;
    private JButton startBt;
    private JDatePickerImpl datePicker;
    private JDatePanelImpl datePanel;
    private JTextField ftTf;
    private JTextField fnTf;
    private JLabel ftLb;
    private UtilDateModel model;
    private JFileChooser chooser;
    
    //Variables for page 4
    private JPanel panel4;
    private String help = "verze " + verze + "\n program se vypina pres tlacitko exit v \n 2. zalozce csv finder.";
    
    //others
    private String marke;
    private String ft = "not selected yet";
    private String type;
    private String date;
    private String analyseScheme;
    private String dir;   
    private String fileName = "export.dar";   
    private File file;
    private int year;
    private int month;
    private int day;
    private ArrayList<File> listFiles;
    private ArrayList<Csv> listCsv;
    private String output = "";
    
    
    public MyFrame(){
        setTitle("Parser "+verze);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(xSize, ySize);
        JPanel topPanel = new JPanel();
        topPanel.setLayout( new BorderLayout());
        getContentPane().add( topPanel );

        // Create the tab pages
        createPage1();
        createPage2();
        createPage3();
        createPage4();

        // Create a tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( "text parser", panel1 );
        tabbedPane.addTab( "csv finder", panel2 );
        tabbedPane.addTab( "Page 3", panel3 );
        tabbedPane.addTab( "help", panel4 );
        topPanel.add( tabbedPane, BorderLayout.CENTER );
    }
    
    
    public void comment(){
    /*
//    private String marke;
//    private String ft; dialogový rámeček, implicitně se nabídne název adresáře
//    private String type;
//    private String date;
//    private String analyseScheme;
//    private File file;
    */
    }
    
    public void createPage1(){
        //for Hedvika
        panel1 = new JPanel();
        panel1.setLayout( null );

        bt1 = new JButton("Select text files");
        bt1.setBounds(10, 20, 300, 30);
        panel1.add(bt1);

        bt1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                    new Parser().go();
            }
        });
        
        JButton exitBt = new JButton("exit");
        exitBt.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        exitBt.setBounds(10, 70, 100, 30);
        panel1.add(exitBt);
    }
    
    public void createPage2(){
        
        panel2 = new JPanel();
        panel2.setLayout( null );
        
        
        //Button for selecting csv files
        bt2 = new JButton("select files");
        bt2.setBounds(10, 20, 300, 30);
        panel2.add(bt2);
        bt2.addActionListener((ActionEvent e) -> {
            selectFiles();
            ftTf.setText(ft);
            ft = ftTf.getText();
        });
        
        //Calendar
        model = new UtilDateModel();
        datePanel = new JDatePanelImpl(model);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setBounds(10, 70, 300, 30);
        
        Calendar calendar = Calendar.getInstance();
        Date today = new Date();
        year = gYear(today);
        month = gMonth(today);
        day = gDay(today);
        System.out.println("today's date is "+year+"/"+month+"/"+day);
        model.setDate(year, month-1, day);
        model.setSelected(true);
        panel2.add(datePicker);
        
        //choosing FT
        JLabel lb1 = new JLabel("FT:");
        lb1.setBounds(10, 120, 70, 30);
        panel2.add(lb1);
        ftTf = new JTextField(ft);
        ftTf.setBounds(80, 120, 220, 30);
        ftTf.addActionListener((ActionEvent e) -> {
            ft = ftTf.getText();
        });
        panel2.add(ftTf);
        
        //Create the combo box for type, can be reselected default
        JLabel lb2 = new JLabel("Type:");
        lb2.setBounds(10, 170, 70, 30);
        String[] typeSelections = { "RLF", "Palubní", "ISLP", "WT", "VAS", "KDVDB"};
        type=typeSelections[0];
        typeCB = new JComboBox(typeSelections);
        typeCB.setSelectedIndex(0);
        typeCB.addActionListener((ActionEvent e) -> {
            type=(String)typeCB.getSelectedItem();
        });
        typeCB.setBounds(80, 170, 220, 30);
        panel2.add(lb2);
        panel2.add(typeCB);
        
        //Create the combo box for Marke
        JLabel lb3 = new JLabel("Marke:");
        lb3.setBounds(10, 220, 70, 30);
        String[] markeSelections = { "VW", "VW-Nutzfahrzeuge", "Audi", "Seat", "Skoda" };
        marke=markeSelections[0];
        markeCB = new JComboBox(markeSelections);
        markeCB.setSelectedIndex(0);
        typeCB.addActionListener((ActionEvent e) -> {
            type=(String)typeCB.getSelectedItem();
        });
        markeCB.setBounds(80, 220, 220, 30);
        panel2.add(lb3);
        panel2.add(markeCB);
        
        //Create the combo box for analyseScheme
        JLabel lb4 = new JLabel("Analyse Scheme:");
        lb4.setBounds(10, 270, 100, 30);
        String[] analyseSchemeSelections = { "VW 08", "VW 09", "Audi 08"};
        analyseScheme=analyseSchemeSelections[0];
        analyseSchemeCB = new JComboBox(analyseSchemeSelections);
        analyseSchemeCB.setSelectedIndex(0);
        analyseSchemeCB.addActionListener((ActionEvent e) -> {
            analyseScheme=(String)analyseSchemeCB.getSelectedItem();
        });
        analyseSchemeCB.setBounds(120, 270, 180, 30);
        panel2.add(lb4);
        panel2.add(analyseSchemeCB);
        
        //choosing File Name
        JLabel lb5 = new JLabel("Output file name:");
        lb5.setBounds(10, 320, 100, 30);
        panel2.add(lb5);
        fnTf = new JTextField(fileName);
        fnTf.setBounds(120, 320, 180, 30);
        fnTf.addActionListener((ActionEvent e) -> {
            fileName = fnTf.getText();
            System.out.println("file name changed to "+fileName);
        });
        panel2.add(fnTf);
        
        //Execute button
        startBt = new JButton("start");
        startBt.addActionListener((ActionEvent e) -> {
            page2execute();
        });
        startBt.setBounds(200, 370, 100, 30);
        panel2.add(startBt);
        
        //Exit button
        JButton exitBt = new JButton("exit");
        exitBt.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        exitBt.setBounds(10, 370, 100, 30);
        panel2.add(exitBt);
    }

    public void createPage3(){
        panel3 = new JPanel();
        panel3.setLayout( null );
        JLabel nsyLb = new JLabel("Not implemented yet");
        nsyLb.setBounds(10, 15, 300, 30);
        panel3.add(nsyLb);
    }
    
    public void createPage4(){  //help  
        panel4 = new JPanel();
        panel4.setLayout( null );
        JLabel nsyLb = new JLabel(help);
        nsyLb.setBounds(10, 15, 300, 30);
        panel4.add(nsyLb);
    }
    
    public void page2execute(){
        output = "";
        output += "<?xml version='1.0' encoding='windows-1250' ?>\n";
        output += "<Import>\n";
        
        fileName=fnTf.getText();
        if(fileName.indexOf(".dar")==-1){       //exception
            JOptionPane.showMessageDialog(new Frame(), "Wrong file name\nmust end with .dar");
            System.exit(-1);
        }
        marke = (String)markeCB.getSelectedItem();
        Date selectedDate = (Date) datePicker.getModel().getValue();
        year = gYear(selectedDate);
        month = gMonth(selectedDate);
        day = gDay(selectedDate);
        String sMonth = (month < 10) ? '0' + Integer.toString(month) : Integer.toString(month);
        String sDay = (day < 10) ? '0' + Integer.toString(day) : Integer.toString(day);
        
        date = ""+sDay+"."+sMonth+"."+year;  
        
        for(File file : listFiles){     
            try {
                Csv csv = new Csv(marke, ft, type, date, analyseScheme, file);
                output += csv.getOrder();
            } catch (Exception ex) {
                Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(new Frame(), "Error creating file, contact the administrator\n");
                return;
            }
        }
        output += "</Import>\n";
        createDarFile(output);
        System.out.println(output);
        JOptionPane.showMessageDialog(new Frame(), "successfully created \nfile name is: \n"+fileName);
        System.exit(0);
    };
    
    //selects only csv files
    public void selectFiles(){
        System.out.println("-----------------selecting files-----------------");
        try { 
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, "Error loading look and feel", e);}
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(null, "csv");
        chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        chooser.showOpenDialog(new Frame());
        File[] files = chooser.getSelectedFiles();
        listFiles = new ArrayList<File>(Arrays.asList(files));
        Collections.sort(listFiles);
        if(!listFiles.isEmpty()){
            dir = listFiles.get(0).getParent();
            ft=dir.substring(dir.lastIndexOf("\\")+1);         //indexOf("\\")+1);
            System.out.println("dir is "+dir);
        }
    }
    
    private void createDarFile(String text){
        String name = fileName;
        String path = dir;
        String file = path + "\\" + name;
        
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                  new FileOutputStream(file), "Cp1252"));
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
    
    //redundant, changes text size of all components, not implemented yet
    public void setTextSize(int size){
        fontSize = size;
        for(Frame comp : components){
            
        }
    }
    public int gYear(Date date){
        DateFormat format = new SimpleDateFormat("yyyy");
        return Integer.parseInt(format.format(date));
    }
    public int gMonth(Date date){
        DateFormat format = new SimpleDateFormat("MM");
        return Integer.parseInt(format.format(date));
    }
    public int gDay(Date date){
        DateFormat format = new SimpleDateFormat("dd");
        return Integer.parseInt(format.format(date));
    }
    
}

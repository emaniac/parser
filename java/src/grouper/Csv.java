/*
 * Represents one CSV file and creates String output
 */
package grouper;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Emania
 */
public class Csv {
    
    //selections
    private String marke;
    private String ft;
    private String type;
    private String date;
    private String analyseScheme;
    private File file;
    
    //output
    private String order = "not initialized";  

    public Csv(String marke, String ft, String type, String date, String analyseScheme, File file) throws Exception {
//        System.out.println("constructor");
//        System.out.println("file is "+file.getPath());
        System.out.println("marke is: "+marke+", ft is "+ft+", type is "+type+", date is "+date+", analyseScheme is "+analyseScheme+", file is "+file);
        
        this.marke = marke;
        this.ft = ft;
        this.type = type;
        this.date = date;
        this.analyseScheme = analyseScheme;
        this.file = file;
        order = readString(file, marke, type, date, analyseScheme, ft);
        
    }
    
    public String readString(File file, String markeIn, String typeIn, String dateIn2, String analyseSchemeIn, String ftIn) throws FileNotFoundException, IOException, Exception{
        String vystup = "";
        String vsechno = "";
        String[] lines = new String[500];    //lines of csv file as String, very large
        
        String orderName = "";  //done
        String customer = "";   //done
        String marke = markeIn;      // volitelně z 5 hodnot: VW/VW-Nutzfahrzeuge/Audi/Seat/Skoda
        String catnum = "";     // prazdne
        String type = typeIn;       //výběr z více hodnot: RLF/Palubní/ISLP/WT
        String section = "";    //prazdne
        String dateIn = dateIn2;     //dialogový rámeček, implicitně se nabídne dnešní datum
        String termin = "";     //done
        String nameGe = "";     //možná ještě doplníme
        String name = "";       //prazdne
        String analyseScheme = analyseSchemeIn;  //výběr ze tří hodnot (VW 08/AW 09/Audi 08) podle kombinace polí Marke a Type
        String anxTranslated = "";  //hodnota z řádku 21 + 23
        String anRep = "";
        String an100 = "";
        String an99_95 = "";
        String an94_85 = "";
        String an84_75 = "";
        String an74_50 = "";
        String an0 = "";
        String anTotal = "";
        String ft = ftIn;
        String popis = "";
        String analyseLog = "";
        
        //reader
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"ISO-8859-1"));
        
        //creates String array from csv file
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            int i = 2;
            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
                if(line == null) break;
                System.out.println("line "+i+" is: "+line);
                lines[i] = line;
                i++;
            }
            String everything = sb.toString();
            vsechno = sb.toString();
        } finally {
            br.close();
        }
        
        lines[0] = "";
        
        /*
        
        orderName += getNumber(lines[9], 1);
        orderName += "-";
        orderName += getNumber(lines[4], 1);
//        orderName += "-";
//        orderName += getNumber(lines[3], 1);
        customer += "Porsche Èeská republika s.r.o.";
        termin += termin(getNumber(lines[6], 1));
        
        
        int cisloA1 = Integer.parseInt(getNumber(lines[21+1], 1));        //chyba zadani?
        int cisloA2 = Integer.parseInt(getNumber(lines[23+1], 1));        //v mailu 0
        int cisloA = cisloA1 + cisloA2;
        anxTranslated += cisloA;
        
        int cisloB1 = Integer.parseInt(getNumber(lines[22+1], 1));
        int cisloB2 = Integer.parseInt(getNumber(lines[24+1], 1));
        int cisloB = cisloB1 + cisloB2;
        anRep += cisloB;
        
        an100 += getNumber(lines[25+1], 1);
        an99_95 += getNumber(lines[26+1], 1);
        an94_85 += 0;
        an84_75 += getNumber(lines[27+1], 1);
        an74_50 += 0;
        an0 += getNumber(lines[28+1], 1);
        anTotal += getNumber(lines[19+1], 1); //getNumber(lines[28], 1) //jak mam osetrit?
        popis += getNumber(lines[15+1], 1);
        analyseLog += vsechno;

        */
        
        /* CORRECTION */
        
        
        int idx;
        idx = getLineIdx("Brand and location", lines);
        orderName += getNumber(lines[idx], 1);
        orderName += "-";
        
        idx = getLineIdx("Translation order number", lines);
        orderName += getNumber(lines[idx], 1);
        
        
        
        
        customer += "Porsche Èeská republika s.r.o.";
        
        idx = getLineIdx("Delivery date", lines);
        termin += termin(getNumber(lines[idx], 1));
        
        idx = getLineIdx("Perfect match (words that match perfectly)", lines);
        int cisloA1 = Integer.parseInt(getNumber(lines[idx], 1));        //chyba zadani?
        
        idx = getLineIdx("Context (words that match in context)", lines);
        int cisloA2 = Integer.parseInt(getNumber(lines[idx], 1));        //v mailu 0
        int cisloA = cisloA1 + cisloA2;
        anxTranslated += cisloA;
        
        idx = getLineIdx("File repetitions (words that match across files)", lines);
        int cisloB1 = Integer.parseInt(getNumber(lines[idx], 1));
        
        idx = getLineIdx("Repetitions", lines);
        int cisloB2 = Integer.parseInt(getNumber(lines[idx], 1));
        int cisloB = cisloB1 + cisloB2;
        anRep += cisloB;
        
        
        idx = getLineIdx("100% match (excluding multiple 100% matches)", lines);
        an100 += getNumber(lines[idx], 1);
        
        idx = getLineIdx("95 - 99% match (including multiple 100 % matches)", lines);
        an99_95 += getNumber(lines[idx], 1);
        an94_85 += 0;
        
        idx = getLineIdx("75 - 94% match", lines);
        an84_75 += getNumber(lines[idx], 1);
        an74_50 += 0;
        
        idx = getLineIdx("0 - 74% match", lines);
        an0 += getNumber(lines[idx], 1);
        
        idx = getLineIdx("Total number of words", lines);
        anTotal += getNumber(lines[idx], 1); //getNumber(lines[28], 1) //jak mam osetrit?
        
        idx = getLineIdx("Document title", lines);
        popis += getNumber(lines[idx], 1);
        
        analyseLog += vsechno;
        
        
        /* END OF CORRECTION */
        
        vystup += "<Order>";
        vystup += "<OrderName>"; vystup += orderName; vystup += "</OrderName>\r\n";
        vystup += "<Customer>"; vystup += customer; vystup += "</Customer>\r\n";
        vystup += "<Marke>"; vystup += marke; vystup += "</Marke>\r\n";
        vystup += "<CatNum>";vystup += "</CatNum>\r\n";
        vystup += "<Type>"; vystup += type; vystup += "</Type>\r\n";
        vystup += "<Section>";vystup += "</Section>\r\n";
        vystup += "<DateIn>"; vystup += dateIn; vystup += "</DateIn>\r\n";
        vystup += "<Termin>"; vystup += termin; vystup += "</Termin>\r\n";
        vystup += "<NameGe>"; vystup += "</NameGe>\r\n";   // druhý mail
        vystup += "<Name>"; vystup += popis; vystup += "</Name>\r\n";    // první mail
//        vystup += "<NameGe>"; vystup += popis; vystup += "</NameGe>\r\n";  // marke?
        vystup += "<AnalyseScheme>"; vystup += analyseScheme; vystup += "</AnalyseScheme>\r\n";
        vystup += "<ANXTranslated>"; vystup += anxTranslated; vystup += "</ANXTranslated>\r\n";
        vystup += "<ANRep>"; vystup += anRep; vystup += "</ANRep>\r\n";
        vystup += "<AN100>"; vystup += an100; vystup += "</AN100>\r\n";
        vystup += "<AN99_95>"; vystup += an99_95; vystup += "</AN99_95>\r\n";
        vystup += "<AN94_85>"; vystup += an94_85; vystup += "</AN94_85>\r\n";
        vystup += "<AN84_75>"; vystup += an84_75; vystup += "</AN84_75>\r\n";
        vystup += "<AN74_50>"; vystup += an74_50; vystup += "</AN74_50>\r\n";
        vystup += "<AN0>"; vystup += an0; vystup += "</AN0>\r\n";
        vystup += "<ANTotal>"; vystup += anTotal; vystup += "</ANTotal>\r\n";
        vystup += "<FT>"; vystup += ft; vystup += "</FT>\r\n";  // marke?
        vystup += "<AnalyseLog>"; vystup += analyseLog; vystup += "\r\n</AnalyseLog>\r\n";
        vystup += "</Order>\r\n";
//        System.out.println("vystup komplet: \n******************\n" + vystup + "\n\n ************");
        return vystup;
    }
    
    //returns String on certain position from line of String
    public static String getNumber(String str, int number){
        String text = "";
        int tmp = 0;
            for(int i = 0; i < str.length(); i++){
                if(str.charAt(i) == ';'){
                    tmp++;
                    i++;
                }
                if(tmp == number){
                    text += str.charAt(i);
                }
            }
        return text;
    }
    
    //returns date in right format
    public static String termin(String text) throws Exception{
        String termin = "";
        int pozice = 0;
        String den = "";
        String mesic = "";
        String rok = "";
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == '-') {pozice++; i++;}
            if(text.charAt(i) == ' ') break;
            
            if(pozice == 0) rok += text.charAt(i);
            if(pozice == 1) mesic += text.charAt(i);
            if(pozice == 2) den += text.charAt(i);
        }
        if(mesic.length() == 1) mesic = ("0" + mesic);
        if(mesic.length() == 1) den = ("0" + den);
        termin = (den+'.'+mesic+'.'+rok);
        if(termin.length() != 10) throw new Exception("spatne zadane datum");
        return termin;
    }
    
    // returns the index of the line containing text
    public int getLineIdx(String text, String[] lines){
        for(int i = 0; i < lines.length; i++){
            if(lines[i] != null && lines[i].contains(text)){
                return i;
            }
        }
        System.out.println("ERROR - function getLineIdx did not find substring");
        return -1;
    }

    public String getOrder() {
        return order;
    }

    void touch() {
        System.out.println("I am csv");
    }
    
    
    
}

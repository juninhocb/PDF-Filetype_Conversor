package org.carlosjr;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //cToTxtConverter();

        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        int returnChooser = chooser.showOpenDialog(null);
        if (returnChooser == JFileChooser.APPROVE_OPTION){
            List<File> selectedFiles = Arrays.asList(chooser.getSelectedFiles());
            File folder =  selectedFiles.get(0).getParentFile(); //does not use in the second format case
            if (selectedFiles.size() > 0){
                selectedFiles.forEach(file -> {
                    convertTxtToPDF(file);
                });
            }
        }
    }


    public static void convertTxtToPDF(File file){
        String text = readTextFile(file);
        System.out.println(text);
        try {
            newPdf(text, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void newPdf(String text, File file) throws IOException{
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(doc, page);
        PDFont font = PDType1Font.COURIER;
        contentStream.setFont(font, 14);
        contentStream.beginText();
        contentStream.setNonStrokingColor(Color.black);
        contentStream.moveTextPositionByAmount(20, 750);
        contentStream.drawString(text);
        contentStream.endText();
        contentStream.close();
        doc.save(file.getName().split(".")[0]+".pdf");
        doc.close();
    }

    private static String readTextFile(File file) {
        String finalText = new String();
        try {
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader br = new BufferedReader(fileReader);
            String line;
            while ((line = br.readLine()) != null) {
                finalText += line;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalText;
    }


    public static void convertTxtToPDF2(File folder, File file){
        String textContent = readTextFile2(file);
        String name = file.getName();
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(50, 700);
                contentStream.showText(textContent.replace("\r", "").replace("\n", "").replace("\0", ""));
                contentStream.endText();
            }

            String pdfFileName = name.replace(".txt", ".pdf");
            File pdfFile = new File(folder, pdfFileName);
            document.save(new FileOutputStream(pdfFile));
            System.out.println("Converted file: " + name + " to " + pdfFileName);
        } catch (IOException e) {
            System.out.println("Failed to convert file: " + name);
            e.printStackTrace();
        }
    }


    public static String readTextFile2(File file){
        try {

            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            int charCode;
            while ((charCode = br.read()) != -1){

                if (charCode != 65533 && charCode != 9)
                    sb.append((char) charCode);

            }
            return sb.toString();
        } catch (IOException e) {
            System.out.println("Failed to read file: " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }
    public static void cToTxtConverter(){
        JFileChooser folderChooser = new JFileChooser();
        folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnChooser = folderChooser.showOpenDialog(null);
        if (returnChooser == JFileChooser.APPROVE_OPTION){
            File folder = new File(folderChooser.getSelectedFile().getAbsolutePath());
            List<File> files = Arrays.asList(folder.listFiles());
            files.forEach(file -> file.renameTo(new File(folder , file.getName().replace(".c", ".txt"))));
            System.out.println("Process successfully done!");
        }
    }
}
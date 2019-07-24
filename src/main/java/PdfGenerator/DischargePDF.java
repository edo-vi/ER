package PdfGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Anchor;


public class DischargePDF {

    private static Font courier = new Font(Font.FontFamily.COURIER, 22,
            Font.BOLD);

    private static Font small = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.BLACK);
    private static Font small7 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 7, Font.NORMAL, BaseColor.BLACK);
    private static Font small9 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 9, Font.NORMAL, BaseColor.BLACK);
    private static Font small10 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 10, Font.NORMAL, BaseColor.BLACK);
    private static Font smallBold = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);



    public static void main(String[] args) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Lettera_" + "Marianovic" + "_" + new Date().toString().replace(":", "_").replace(" ", "_" )+ ".pdf"));
            document.open();
            addMetaData(document);
            addContentPage(document);

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("Week report");
        document.addSubject("Summary of the last 7 days with recoveries, prescriptions, drug administrations and vital parameters.");
        document.addKeywords("PDF, report, summary, recovery, parameters");
        document.addAuthor("Marian Statache");
        document.addCreator("Marian Statache");
    }

    private static void addContentPage(Document document) throws DocumentException {
        Image image = null;
        try {
            image = Image.getInstance("src/main/resources/logo.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setAbsolutePosition(400f, 720f);
        image.scaleAbsolute(180, 120);
        document.add(image);

        image = null;
        try {
            image = Image.getInstance("src/main/resources/firma.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setAbsolutePosition(420f, 80f);
        image.scaleAbsolute(80, 40);
        document.add(image);

        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("Azienda ospedaliera di Borgo Roma", small7));
        preface.add(new Paragraph("Reparto terapia intensiva", small7));
        preface.add(new Paragraph("Piazzale Ludovico Antonio Scuro, Verona, VR", small7));
        preface.add(new Paragraph("Primario Dr. Null", small7));
        preface.add(new Paragraph("tel. +39 045 254625", small7));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Data: " + new Date(), small9));
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Lettera di dimissione", courier));

        addEmptyLine(preface, 3);

        Paragraph par = new Paragraph("Cognome:   ", small);
        Anchor anc = new Anchor("Marianovic", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Nome:   ", small);
        anc = new Anchor("Mario", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Data di nascita:   ", small);
        anc = new Anchor("01-01-2000", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Luogo di nascita:   ", small);
        anc = new Anchor("Forette", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("C.F.:   " , small);
        anc = new Anchor("1234", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 2);
        par = new Paragraph("Data inizio ricovero:   ", small);
        anc = new Anchor("ieri", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Diagnosi di ingresso:   ", small);
        anc = new Anchor("tutto male", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 2);
        par = new Paragraph("Data dimissione:   ", small);
        anc = new Anchor("domani", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Esito:   ", small);
        anc = new Anchor("tutto bene", smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 7);
        par = new Paragraph("Il Primario", small10);
        par.setIndentationLeft(420);
        preface.add(par);
        par = new Paragraph("Prof. Dr. Null", small);
        par.setIndentationLeft(400);
        preface.add(par);


        document.add(preface);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
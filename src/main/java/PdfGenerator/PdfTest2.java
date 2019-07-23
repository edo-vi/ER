package PdfGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Anchor;


public class PdfTest2 {

    private static Font courier = new Font(Font.FontFamily.COURIER, 22,
            Font.BOLD);
    private static Font courierC = new Font(Font.FontFamily.COURIER, 18,
            Font.BOLD);
    private static Font courierS = new Font(Font.FontFamily.COURIER, 15,
            Font.BOLD);

    private static Font small = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.BLACK);
    private static Font small7 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 7, Font.NORMAL, BaseColor.BLACK);
    private static Font small9 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 9, Font.NORMAL, BaseColor.BLACK);
    private static Font smallBold = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);

    private static Font roboto = FontFactory.getFont("src/main/resources/Roboto/Roboto-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 20, Font.NORMAL, BaseColor.BLACK);
    private static Font robotoC = FontFactory.getFont("src/main/resources/Roboto/Roboto-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 18, Font.BOLD, BaseColor.BLACK);
    private static Font robotoS = FontFactory.getFont("src/main/resources/Roboto/Roboto-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 15, Font.BOLD, BaseColor.BLACK);



    public static void main(String[] args) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new Date().toString().replace(":", "_")+".pdf"));
            document.open();
            addMetaData(document);
            addTitlePage(document);
            addContent(document);



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

    private static void addTitlePage(Document document)
            throws DocumentException {
        Image image1 = null;
        try {
            image1 = Image.getInstance("logo.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Fixed Positioning
        image1.setAbsolutePosition(400f, 720f);
        //Scale to new height and new width of image
        image1.scaleAbsolute(180, 120);
        document.add(image1);
        Anchor an = new Anchor("Link");
        an.setReference("#C2");
        Paragraph p = new Paragraph(an);
        document.add(p);


        Paragraph preface = new Paragraph();
        preface.add(new Paragraph("Azienda ospedaliera di Borgo Roma", small7));
        preface.add(new Paragraph("Reparto terapia intensiva", small7));
        preface.add(new Paragraph("Piazzale Ludovico Antonio Scuro, Verona, VR", small7));
        preface.add(new Paragraph("Primario Dr. Null", small7));
        preface.add(new Paragraph("tel. +39 045 254625", small7));
        addEmptyLine(preface, 1);
        preface.add(new Paragraph("Data: "+new Date(), small9));
        addEmptyLine(preface, 1);

        preface.add(new Paragraph("Report Riassuntivo Ultimi 7 Giorni", courier));

        addEmptyLine(preface, 1);

        Paragraph paragraph = new Paragraph("Questo documento descrive le informazioni principali sui ricoveri degli ultimi 7 giorni.", small);
        paragraph.setIndentationLeft(5);
        preface.add(paragraph);

        addEmptyLine(preface, 2);
        document.add(preface);

        Anchor anchor = new Anchor("1. Pazienti attualmente in ricovero", small);
        anchor.setReference("#C1");
        preface = new Paragraph(anchor);
        //paragraph.setIndentationLeft(30);
        document.add(preface);
        document.add(paragraph);
        preface = new Paragraph();
        createList(preface, "active");
        document.add(preface);

        anchor = new Anchor("2. Cartelle cliniche chiuse", small);
        anchor.setReference("#C2");
        preface = new Paragraph(anchor);
        //paragraph.setIndentationLeft(30);
        document.add(preface);
        document.add(paragraph);
        preface = new Paragraph();
        createList(preface, "inactive");


        document.add(preface);
        // Start a new page
        document.newPage();
    }

    private static void addContent(Document document) throws DocumentException {

        Paragraph subPara;
        Paragraph subPara2;
        Section subCatPart;

        Anchor anchor = new Anchor("Pazienti attualmente in ricovero", courierC);
        anchor.setName("C1");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        catPart.add(paragraph);



        //while...
        subPara = new Paragraph("Carlo Combi", courierS);
        subCatPart = catPart.addSection(subPara);
        subPara = new Paragraph();
        subPara.add(paragraph);
        subPara.add(new Paragraph("Cognome: ", small));
        subPara.add(new Paragraph("Nome: ", small));
        subPara.add(new Paragraph("Data di nascita: ", small));
        subPara.add(new Paragraph("Luogo di nascita: ", small));
        subPara.add(new Paragraph("C.F.: ", small));
        subPara.add(new Paragraph("Data inizio ricovero: ", small));
        subPara.add(new Paragraph("Diagnosi di ingresso: ", small));
        subPara.add(paragraph);
        subPara.add(new Paragraph("PRESCRIZIONI", smallBold));
        //while...
        subPara.add(paragraph);
        subPara.add(new Paragraph("data+medico+farmaco+quantita+dosi+durata", small));
        subPara.add(paragraph);
        subPara2 = new Paragraph("Somministrazioni", smallBold);
        //while
        subPara2.add(new Paragraph("data, ora, farmaco, dosaggio, note", small));
        subPara2.setIndentationLeft(30);
        //}}
        subPara.add(subPara2);
        subPara.add(paragraph);
        subPara.add(paragraph);

        subPara.setIndentationLeft(20);
        subCatPart.add(subPara);
        subCatPart.setIndentationLeft(10);
        //}


        // now add all this to the document
        document.add(catPart);

        document.newPage();





        anchor = new Anchor("Cartelle cliniche chiuse", courierC);
        anchor.setName("C2");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 2);
        catPart.add(paragraph);


        //while...
        subPara = new Paragraph("Carlo Combi", courierS);
        subCatPart = catPart.addSection(subPara);
        subPara = new Paragraph();
        subPara.add(paragraph);
        subPara.add(new Paragraph("Cognome: ", small));
        subPara.add(new Paragraph("Nome: ", small));
        subPara.add(new Paragraph("Data di nascita: ", small));
        subPara.add(new Paragraph("Luogo di nascita: ", small));
        subPara.add(new Paragraph("C.F.: ", small));
        subPara.add(new Paragraph("Data inizio ricovero: ", small));
        subPara.add(new Paragraph("Diagnosi di ingresso: ", small));
        subPara.add(new Paragraph("Data fine ricovero: ", small));
        subPara.add(new Paragraph("Esito ricovero: ", small));
        subPara.add(paragraph);
        subPara.add(new Paragraph("PRESCRIZIONI", smallBold));
        //while...
        subPara.add(paragraph);
        subPara.add(new Paragraph("data+medico+farmaco+quantita+dosi+durata", small));
        subPara.add(paragraph);
        subPara2 = new Paragraph("Somministrazioni", smallBold);
        //while
        subPara2.add(new Paragraph("data, ora, farmaco, dosaggio, note", small));
        subPara2.setIndentationLeft(30);
        //}}
        subPara.add(subPara2);
        subPara.add(paragraph);
        subPara.add(paragraph);

        subPara.setIndentationLeft(20);
        subCatPart.add(subPara);
        subCatPart.setIndentationLeft(10);
        //}

        // now add all this to the document
        document.add(catPart);

    }


    private static void createList(Paragraph preface, String cmd) {
        List list = new List(false, false, 20);
        //while...
        list.add(new ListItem(new Paragraph("item1", small)));
        list.add(new ListItem(new Paragraph("item1", small)));

        list.setIndentationLeft(50);
        preface.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
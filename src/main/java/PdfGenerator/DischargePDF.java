package PdfGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import Entities.Recovery;
import State.State;
import State.Store;
import State.StringCommand;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Anchor;


public class DischargePDF {

    private final Font courier = new Font(Font.FontFamily.COURIER, 22,
            Font.BOLD);

    private final Font small = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.BLACK);
    private final Font small7 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 7, Font.NORMAL, BaseColor.BLACK);
    private final Font small9 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 9, Font.NORMAL, BaseColor.BLACK);
    private final Font small10 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 10, Font.NORMAL, BaseColor.BLACK);
    private final Font smallBold = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);

    private Store<StringCommand> store;
    private State state;

    public DischargePDF(Store<StringCommand> store) {
        this.store = store;
        this.state = store.poll();
    }

    public void createPDF(String name, Recovery recovery) {
        try {
            String filename = "Lettera_" + name + "_" + new Date().toString().replace(":", "_").replace(" ", "_" );
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Lettera_" + name + "_" + new Date().toString().replace(":", "_").replace(" ", "_" )+ ".pdf"));
            document.open();
            addMetaData(document);
            addContentPage(document, recovery);

            document.close();
            writer.close();
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + filename + ".pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMetaData(Document document) {
        document.addTitle("Week report");
        document.addSubject("Summary of the last 7 days with recoveries, prescriptions, drug administrations and vital parameters.");
        document.addKeywords("PDF, report, summary, recovery, parameters");
        document.addAuthor("Marian Statache");
        document.addCreator("Marian Statache");
    }

    private void addContentPage(Document document, Recovery recovery) throws DocumentException {
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
        Anchor anc = new Anchor(recovery.getPatient().getSurname(), smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Nome:   ", small);
        anc = new Anchor(recovery.getPatient().getName(), smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Data di nascita:   ", small);
        anc = new Anchor(recovery.getPatient().getDateofBirth().toString(), smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Luogo di nascita:   ", small);
        anc = new Anchor(recovery.getPatient().getPlaceOfBirth(), smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("C.F.:   " , small);
        anc = new Anchor(recovery.getPatient().getFiscalCode(), smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 2);
        par = new Paragraph("Data inizio ricovero:   ", small);
        anc = new Anchor(recovery.getStartDate().toString(), smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Diagnosi di ingresso:   ", small);
        anc = new Anchor(recovery.getDiagnosis(), smallBold);
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 2);
        par = new Paragraph("Data dimissione:   ", small);
        try {
            anc = new Anchor(recovery.getEndDate().toString(), smallBold);
        } catch (Recovery.RecoveryNullFieldException e) {}
        par.add(anc);
        par.setIndentationLeft(20);
        preface.add(par);
        addEmptyLine(preface, 1);
        par = new Paragraph("Esito:   ", small);
        try {
            anc = new Anchor(recovery.getDischargeSummary(), smallBold);
        } catch (Recovery.RecoveryNullFieldException e) {}
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

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
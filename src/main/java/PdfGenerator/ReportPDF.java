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

public class ReportPDF {
    private final Font courier = new Font(Font.FontFamily.COURIER, 22,
            Font.BOLD);
    private final Font courierC = new Font(Font.FontFamily.COURIER, 18,
            Font.BOLD);
    private final Font courierS = new Font(Font.FontFamily.COURIER, 15,
            Font.BOLD);

    private final Font small = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.BLACK);
    private final Font small7 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 7, Font.NORMAL, BaseColor.BLACK);
    private final Font small9 = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 9, Font.NORMAL, BaseColor.BLACK);
    private final Font smallBold = FontFactory.getFont("src/main/resources/Crimson_Text/CrimsonText-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);

    private final Font roboto = FontFactory.getFont("src/main/resources/Roboto/Roboto-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 20, Font.NORMAL, BaseColor.BLACK);
    private final Font robotoC = FontFactory.getFont("src/main/resources/Roboto/Roboto-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 18, Font.BOLD, BaseColor.BLACK);
    private final Font robotoS = FontFactory.getFont("src/main/resources/Roboto/Roboto-Regular.ttf",
            BaseFont.IDENTITY_H , BaseFont.EMBEDDED, 15, Font.BOLD, BaseColor.BLACK);
    private final Store<StringCommand> store;
    private final State state;

    public ReportPDF(Store<StringCommand> store) {
        this.store = store;
        this.state = store.poll();
    }

    public void createPDF(String name) throws Exception {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(name + ".pdf"));
        document.open();
        addMetaData(document);
        addTitlePage(document);
        addContent(document);
        document.close();
        writer.close();
        store.update(new StringCommand("ERROR", "May the PDF be with you."));
        Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + name + ".pdf");
    }

    private void addMetaData(Document document) {
        document.addTitle("Week report");
        document.addSubject("Summary of the last 7 days with recoveries, prescriptions, drug administrations and vital parameters.");
        document.addKeywords("PDF, report, summary, recovery, parameters");
        document.addAuthor("Marian Statache");
        document.addCreator("Marian Statache");
    }

    private void addTitlePage(Document document) throws DocumentException {
        Image image = null;
        try {
            image = Image.getInstance("src/main/resources/logo.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Fixed Positioning
        image.setAbsolutePosition(400f, 720f);
        //Scale to new height and new width of image
        image.scaleAbsolute(180, 120);
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

        preface.add(new Paragraph("Report Riassuntivo Ultimi 7 Giorni", courier));

        addEmptyLine(preface, 1);

        Paragraph paragraph = new Paragraph("Questo documento descrive le informazioni principali sui ricoveri degli ultimi 7 giorni.", small);
        paragraph.setIndentationLeft(5);
        preface.add(paragraph);

        addEmptyLine(preface, 2);

        Anchor anchor = new Anchor("1. Pazienti attualmente in ricovero", small);
        anchor.setReference("#C1");
        paragraph = new Paragraph();
        paragraph.add(anchor);
        paragraph.setIndentationLeft(30);
        preface.add(paragraph);
        createList(preface, "active");

        anchor = new Anchor("2. Pazienti dimessi", small);
        anchor.setReference("#C2");
        paragraph = new Paragraph();
        paragraph.add(anchor);
        paragraph.setIndentationLeft(30);
        preface.add(paragraph);
        createList(preface, "inactive");

        document.add(preface);
    }

    private void addContent(Document document) throws DocumentException {

        Paragraph para;
        Anchor target;

        target = new Anchor("Pazienti attualmente in ricovero", courierC);
        target.setName("C1");
        para = new Paragraph();
        para.add(target);

        final Chapter catPart = new Chapter(para, 1);
        state.getPatients().forEach(p -> {

            if (p.isRecovered()) {
                Paragraph paragraph = new Paragraph();
                addEmptyLine(paragraph, 1);
                catPart.add(paragraph);
                catPart.add(paragraph);
                Paragraph subPara = new Paragraph(p.getSurname() + " " + p.getName(), courierS);
                Section subCatPart = catPart.addSection(subPara);
                Paragraph subPara2 = new Paragraph();
                subPara2.add(paragraph);
                Paragraph par = new Paragraph("Cognome:   ", small9);
                Anchor anc = new Anchor(p.getSurname(), small);
                par.add(anc);
                subPara2.add(par);
                par = new Paragraph("Nome:   ", small9);
                anc = new Anchor(p.getName(), small);
                par.add(anc);
                subPara2.add(par);
                par = new Paragraph("Data di nascita:   ", small9);
                anc = new Anchor(p.getDateofBirth().toString(), small);
                par.add(anc);
                subPara2.add(par);
                par = new Paragraph("Luogo di nascita:   ", small9);
                anc = new Anchor(p.getPlaceOfBirth(), small);
                par.add(anc);
                subPara2.add(par);
                par = new Paragraph("C.F.:   " , small9);
                anc = new Anchor(p.getFiscalCode(), small);
                par.add(anc);
                subPara2.add(par);
                par = new Paragraph("Data inizio ricovero:   ", small9);
                anc = new Anchor(p.getActiveRecoveries().get(0).getStartDate().toString(), small);
                par.add(anc);
                subPara2.add(par);
                par = new Paragraph("Diagnosi di ingresso:   ", small9);
                anc = new Anchor(p.getActiveRecoveries().get(0).getDiagnosis(), small);
                par.add(anc);
                subPara2.add(par);
                p.getActiveRecoveries().forEach(r -> {
                    if(r.getPrescriptions().size() > 0) {
                        subPara2.add(paragraph);
                        subPara2.add(new Paragraph("PRESCRIZIONI", smallBold));
                        r.getPrescriptions().forEach(prescription -> {
                            subPara2.add(paragraph);
                            subPara2.add(new Paragraph(prescription.getDate().toString() + "    Dr. " + prescription.getDoctor()
                                    + "    " + prescription.getDrug() + "    " + prescription.getDailyDose() + "mg/mL    " + prescription.getTotalNumberofDoses()
                                    + " al giorno per " + prescription.getDuration() + " giorni", small));
                            if(prescription.getAdministrations().size() > 0) {
                                subPara2.add(paragraph);
                                Paragraph subPara3 = new Paragraph("Somministrazioni", smallBold);
                                prescription.getAdministrations().forEach(a -> {
                                    subPara3.add(new Paragraph(new java.sql.Date(a.getDate().getTime()).toString() + "    Ore:  "
                                            + a.getHour() + "    " + a.getPrescription().getDrug() + "    " + a.getDose() + "mg/mL    Note: " + a.getNotes(), small));
                                    subPara3.setIndentationLeft(10);
                                });
                                subPara2.add(subPara3);
                            }
                        });
                    }
                });
                subPara.add(subPara2); // add SubPara2
                subPara.setIndentationLeft(10);

                subCatPart.setIndentationLeft(10);
            }
        });
        try {
            document.add(catPart);
        } catch (DocumentException e) {
            store.update(new StringCommand("ERROR", "System Error.\nUnlucky"));
        }

        target = new Anchor("Pazienti dimessi", courierC);
        target.setName("C2");
        para = new Paragraph();
        para.add(target);

        Chapter catPart2 = new Chapter(para, 2);

        state.getPatients().forEach(p -> {

            if(p.isDischarged()) {
                Paragraph paragraph = new Paragraph();
                addEmptyLine(paragraph, 1);
                catPart2.add(paragraph);
                catPart2.add(paragraph);
                Paragraph subPara = new Paragraph(p.getSurname() + " " + p.getName(), courierS);
                Section subCatPart = catPart2.addSection(subPara);
                p.getDischargedRecovery().forEach(r -> {
                    Paragraph subPara2 = new Paragraph();
                    subPara2.add(paragraph);
                    Paragraph par = new Paragraph("Cognome:   ", small9);
                    Anchor anc = new Anchor(p.getSurname(), small);
                    par.add(anc);
                    subPara2.add(par);
                    par = new Paragraph("Nome:   ", small9);
                    anc = new Anchor(p.getName(), small);
                    par.add(anc);
                    subPara2.add(par);
                    par = new Paragraph("Data di nascita:   ", small9);
                    anc = new Anchor(p.getDateofBirth().toString(), small);
                    par.add(anc);
                    subPara2.add(par);
                    par = new Paragraph("Luogo di nascita:   ", small9);
                    anc = new Anchor(p.getPlaceOfBirth(), small);
                    par.add(anc);
                    subPara2.add(par);
                    par = new Paragraph("C.F.:   ", small9);
                    anc = new Anchor(p.getFiscalCode(), small);
                    par.add(anc);
                    subPara2.add(par);
                    par = new Paragraph("Data inizio ricovero:   ", small9);
                    anc = new Anchor(r.getStartDate().toString(), small);
                    par.add(anc);
                    subPara2.add(par);
                    par = new Paragraph("Diagnosi di ingresso:   ", small9);
                    anc = new Anchor(r.getDiagnosis(), small);
                    par.add(anc);
                    subPara2.add(par);
                    try {
                        par = new Paragraph("Data fine ricovero:   ", small9);
                        anc = new Anchor(r.getEndDate().toString(), small);
                        par.add(anc);
                        subPara2.add(par);
                    } catch (Recovery.RecoveryNullFieldException e) {
                        store.update(new StringCommand("ERROR", "System Error.\nUnlucky"));
                    }
                    try {
                        par = new Paragraph("Diagnosi d'uscita:   ", small9);
                        anc = new Anchor(r.getDischargeSummary(), small);
                        par.add(anc);
                        subPara2.add(par);
                    } catch (Recovery.RecoveryNullFieldException e) {
                        store.update(new StringCommand("ERROR", "System Error.\nUnlucky"));
                    }
                    subPara2.add(paragraph);
                    if(r.getPrescriptions().size() > 0) {
                        subPara2.add(new Paragraph("PRESCRIZIONI", smallBold));
                        r.getPrescriptions().forEach(prescription -> {
                            subPara2.add(paragraph);
                            subPara2.add(new Paragraph(prescription.getDate().toString() + "    Dr. " + prescription.getDoctor()
                                    + "    " + prescription.getDrug() + "    " + prescription.getDailyDose() + "mg/mL    " + prescription.getTotalNumberofDoses()
                                    + " al giorno per " + prescription.getDuration() + " giorni", small));
                            if(prescription.getAdministrations().size() > 0) {
                                subPara2.add(paragraph);
                                Paragraph subPara3 = new Paragraph("Somministrazioni", smallBold);
                                prescription.getAdministrations().forEach(a -> {
                                    subPara3.add(new Paragraph(new java.sql.Date(a.getDate().getTime()).toString() + "    Ore:  "
                                            + a.getHour() + "    " + a.getPrescription().getDrug() + "    " + a.getDose() + "mg/mL    Note: " + a.getNotes(), small));
                                    subPara3.setIndentationLeft(10);
                                });
                                subPara2.add(subPara3);
                            }
                        });
                    }
                    subPara.add(subPara2); // add SubPara2
                    subPara.setIndentationLeft(10);
                });

                subCatPart.setIndentationLeft(10);
            }
        });
        try {
            document.add(catPart2);
        } catch (DocumentException e) {
            store.update(new StringCommand("ERROR", "System Error.\nUnlucky"));
        }
    }


    private void createList(Paragraph preface, String cmd) {
        List list = new List(false, false, 20);

        if(cmd.equals("active"))
            state.getPatients().forEach(p -> {
                if(p.isRecovered())
                    list.add(new ListItem(new Paragraph(p.getSurname() + " " + p.getName(), small)));
            });
        else state.getPatients().forEach(p -> {
            if(p.isDischarged())
                list.add(new ListItem(new Paragraph(p.getSurname() + " " + p.getName(), small)));
        });

        list.setIndentationLeft(50);
        preface.add(list);
    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
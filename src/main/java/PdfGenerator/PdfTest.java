package PdfGenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfTest
{
    public static void main(String[] args)
    {
        Document document = new Document();
        try
        {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("AddImageExample.pdf"));
            document.open();
            document.add(new Paragraph("Image Example"));



            String imageUrl = "http://www.eclipse.org/xtend/images/java8_logo.png";
            Image image2 = Image.getInstance(new URL(imageUrl));
            document.add(image2);

            document.close();
            writer.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
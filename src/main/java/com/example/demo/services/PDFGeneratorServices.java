package com.example.demo.services;

import com.example.demo.models.Contract;
import com.lowagie.text.*;
import org.springframework.stereotype.Service;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class PDFGeneratorServices {
    public void export(HttpServletResponse response, Contract contract) throws DocumentException {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();
            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontTitle.setSize(18);

            Paragraph paragraph = new Paragraph("Contract", fontTitle);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);

            Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
            fontParagraph.setSize(12);

            Paragraph paragraph1 = new Paragraph("ID Contract: " + contract.getId(), fontParagraph);
            paragraph1.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph2 = new Paragraph("ID Request: " + contract.getId_request(), fontParagraph);
            paragraph2.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph3 = new Paragraph("ID Sender: " + contract.getId_sender(), fontParagraph);
            paragraph3.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph4 = new Paragraph("ID Transporter: " + contract.getId_transporter(), fontParagraph);
            paragraph4.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph5 = new Paragraph("ID Truck: " + contract.getId_truck(), fontParagraph);
            paragraph5.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph6 = new Paragraph("Source Latitude: " + contract.getSource_lat(), fontParagraph);
            paragraph6.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph7 = new Paragraph("Source Longitude: " + contract.getSource_lng(), fontParagraph);
            paragraph7.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph8 = new Paragraph("Destination Latitude: " + contract.getDestination_lat(), fontParagraph);
            paragraph8.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph9 = new Paragraph("Destination Longitude: " + contract.getDestination_lng(), fontParagraph);
            paragraph9.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph10 = new Paragraph("Total Cost: " + contract.getTotalCost(), fontParagraph);
            paragraph10.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph11 = new Paragraph("Goods Type: " + contract.getTypeGoods(), fontParagraph);
            paragraph11.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph12 = new Paragraph("Pay Term: " + contract.getPayTerm(), fontParagraph);
            paragraph11.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph13 = new Paragraph("Distance Free: " + contract.getDistance_free(), fontParagraph);
            paragraph11.setAlignment(Paragraph.ALIGN_LEFT);

            Paragraph paragraph14 = new Paragraph("Distance Full: " + contract.getDistance_full(), fontParagraph);
            paragraph11.setAlignment(Paragraph.ALIGN_LEFT);

            document.add(paragraph);
            document.add(Chunk.NEWLINE);
            document.add(paragraph1);
            document.add(paragraph2);
            document.add(paragraph3);
            document.add(paragraph4);
            document.add(paragraph5);
            document.add(paragraph6);
            document.add(paragraph7);
            document.add(paragraph8);
            document.add(paragraph9);
            document.add(paragraph10);
            document.add(paragraph11);
            document.add(paragraph12);
            document.add(paragraph13);
            document.add(paragraph14);
//            document.newPage();
//            document.add(new Paragraph("Aceasta este o pagina cu text: " + contract.getId().toString()));
//            document.newPage();

        } catch (DocumentException | IOException de) {
            System.err.println(de.getMessage());
        }

        document.close();
    }
}

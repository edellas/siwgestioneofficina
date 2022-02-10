package it.uniroma3.siw.gestioneofficina.util;


import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import it.uniroma3.siw.gestioneofficina.model.Intervento;


public class InterventiPDFExporter {
    private List<Intervento> interventi;

    public InterventiPDFExporter(List<Intervento> interventi) {
        this.interventi = interventi;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.black);
        cell.setBorder(0);
        cell.setPadding(10);
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Codice", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("DataIntervento", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Descrizione", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Prezzo", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Intervento intervento : interventi) {
            table.addCell(String.valueOf(intervento.getTipologiaIntervento().getCodice()));
            table.addCell(String.valueOf(intervento.getDataIntervento()));
            table.addCell(intervento.getTipologiaIntervento().getDescrizione());
            table.addCell(String.valueOf(intervento.getTipologiaIntervento().getCosto()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER_BOLD);
        font.setSize(10);
        font.setColor(Color.black);
        float totale = 0;
        for(Intervento i : this.interventi){
            totale = totale + i.getTipologiaIntervento().getCosto();
        }
        float iva = (totale*22)/100;
        Paragraph p1 = new Paragraph(interventi.get(0).getCliente().getFirstName() + " " + interventi.get(0).getCliente().getLastName(), font);
        p1.setAlignment(Paragraph.ALIGN_TOP);
        p1.setAlignment(Paragraph.ALIGN_RIGHT);
        Paragraph p = new Paragraph("Interventi effettuati", font);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph p3 = new Paragraph("Totale(Senza IVA): € " + String.valueOf(totale),font);
        Paragraph p4 = new Paragraph("IVA: € " + String.valueOf(iva),font);
        Paragraph p5 = new Paragraph("Totale: € " + String.valueOf(totale+iva),font);
        p3.setAlignment(Paragraph.ALIGN_BOTTOM);
        p3.setAlignment(Paragraph.ALIGN_RIGHT);
        p4.setAlignment(Paragraph.ALIGN_BOTTOM);
        p4.setAlignment(Paragraph.ALIGN_RIGHT);
        p5.setAlignment(Paragraph.ALIGN_BOTTOM);
        p5.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(p1);
        document.add(p);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {4f, 4f, 4f, 4f});
        table.setSpacingBefore(60);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);
        document.add(p3);
        document.add(p4);
        document.add(p5);
        document.close();

    }
}
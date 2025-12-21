/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
/**
 *
 * @author P15s
 */
public class Model_Laporan {
    
      // ===== Template umum untuk membuat PDF dari JTable =====
    private static void exportPDF(JTable table, String namaFile, String judulLaporan) throws Exception {

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(namaFile));
        doc.open();

        // Judul
        Font fontJudul = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph judul = new Paragraph(judulLaporan + "\n\n", fontJudul);
        judul.setAlignment(Element.ALIGN_CENTER);
        doc.add(judul);

        // Tabel PDF
        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
        pdfTable.setWidthPercentage(100);

        // Header tabel
        for (int i = 0; i < table.getColumnCount(); i++) {
            PdfPCell header = new PdfPCell(new Phrase(table.getColumnName(i)));
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pdfTable.addCell(header);
        }

        // Data isi tabel
        TableModel model = table.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(row, col);
                pdfTable.addCell(value == null ? "" : value.toString());
            }
        }

        doc.add(pdfTable);
        doc.close();
    }

    // ======================== LAPORAN BARANG MASUK ========================
    public static void cetakBarangMasuk(JTable table) {
        try {
            exportPDF(table, "Laporan_Barang_Masuk.pdf", "Laporan Barang Masuk");
            System.out.println("Laporan Barang Masuk sudah dibuat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================== LAPORAN BARANG KELUAR ========================
    public static void cetakBarangKeluar(JTable table) {
        try {
            exportPDF(table, "Laporan_Barang_Keluar.pdf", "Laporan Barang Keluar");
            System.out.println("Laporan Barang Keluar sudah dibuat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ======================== LAPORAN HASIL PANEN ========================
    public static void cetakHasilPanen(JTable table)  {
        try {
            exportPDF(table, "Laporan_Hasil_Panen.pdf", "Laporan Hasil Panen");
            System.out.println("Laporan Hasil Panen sudah dibuat");
        } catch (Exception e) {
            e.printStackTrace();
 }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.io.FileOutputStream;
import java.io.File;
import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;

import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
/**
 *
 * @author P15s
 */
public class Model_Laporan {
    
  // ================= TEMPLATE EXPORT PDF =================
    private static void exportPDF(JTable table, String namaLaporan) throws Exception {

        // ===== Folder laporan =====
        String folderPath = "laporan";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // ===== Nama file pakai tanggal & jam =====
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String namaFile = folderPath + "/" + namaLaporan + "_" + timeStamp + ".pdf";

        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(namaFile));
        doc.open();

        // ===== WARNA HIJAU AGRITECH =====
        BaseColor hijauAgritech = new BaseColor(46, 125, 50); // hijau elegan

        // ===== JUDUL AGRITECH =====
        Font fontAgritech = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD, hijauAgritech);
        Paragraph agritech = new Paragraph("AGRITECH\n", fontAgritech);
        agritech.setAlignment(Element.ALIGN_CENTER);
        doc.add(agritech);

        // ===== SUB JUDUL =====
        Font fontSub = new Font(Font.FontFamily.HELVETICA, 12);
        Paragraph sub = new Paragraph(
                "Sistem Inventory Pertanian\nSolusi digital untuk manajemen persediaan pertanian\n\n",
                fontSub
        );
        sub.setAlignment(Element.ALIGN_CENTER);
        doc.add(sub);

        // ===== GARIS PEMBATAS =====
        PdfPTable garis = new PdfPTable(1);
        PdfPCell cellGaris = new PdfPCell();
        cellGaris.setBorder(Rectangle.BOTTOM);
        cellGaris.setBorderColor(hijauAgritech);
        cellGaris.setFixedHeight(10);
        cellGaris.setBorderWidthBottom(2);
        cellGaris.setHorizontalAlignment(Element.ALIGN_CENTER);
        garis.addCell(cellGaris);
        doc.add(garis);

        doc.add(new Paragraph("\n"));

        // ===== JUDUL LAPORAN =====
        Font fontJudul = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph judul = new Paragraph(namaLaporan.replace("_", " "), fontJudul);
        judul.setAlignment(Element.ALIGN_CENTER);
        doc.add(judul);

        // ===== TANGGAL CETAK =====
        String tglCetak = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        Font fontTanggal = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC);
        Paragraph tanggal = new Paragraph("Tanggal Cetak: " + tglCetak + "\n\n", fontTanggal);
        tanggal.setAlignment(Element.ALIGN_RIGHT);
        doc.add(tanggal);

        // ===== TABEL DATA =====
        PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
        pdfTable.setWidthPercentage(100);

        // Header tabel
        Font fontHeader = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, BaseColor.WHITE);
        for (int i = 0; i < table.getColumnCount(); i++) {
            PdfPCell header = new PdfPCell(new Phrase(table.getColumnName(i), fontHeader));
            header.setBackgroundColor(hijauAgritech);
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.setPadding(5);
            pdfTable.addCell(header);
        }

        // Isi tabel
        TableModel model = table.getModel();
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object value = model.getValueAt(row, col);
                PdfPCell cell = new PdfPCell(new Phrase(value == null ? "" : value.toString()));
                cell.setPadding(5);
                pdfTable.addCell(cell);
            }
        }

        doc.add(pdfTable);
        doc.close();
        
        // pop up berhasil
        JOptionPane.showMessageDialog(null, "Laporan berhasil dibuat dan siap dicetak:\n" + namaFile, "AGRITECH", JOptionPane.INFORMATION_MESSAGE);
    }

    // ================= LAPORAN BARANG MASUK =================
    public static void cetakBarangMasuk(JTable table) {
        try {
            exportPDF(table, "Laporan_Barang_Masuk");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LAPORAN BARANG KELUAR =================
    public static void cetakBarangKeluar(JTable table) {
        try {
            exportPDF(table, "Laporan_Barang_Keluar");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= LAPORAN HASIL PANEN =================
    public static void cetakHasilPanen(JTable table) {
        try {
            exportPDF(table, "Laporan_Hasil_Panen");
        } catch (Exception e) {
            e.printStackTrace();
 }
    }
}
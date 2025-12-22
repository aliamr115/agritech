/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package frame;
import Class.koneksi;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import Class.Model_Laporan;



public class laporan extends javax.swing.JPanel {
    
private void loadTabelLaporan(String pilihan){
    
    String query = "";
    
    switch (pilihan.toLowerCase()) {
        case "laporan barang masuk":
            query = "SELECT bm.no_masuk, bm.tgl_masuk, "
          + "jb.nama_jenis AS nama_barang, "
          + "dbm.jml_masuk, dbm.subtotal_masuk "
          + "FROM barangmasuk bm "
          + "JOIN detail_barangmasuk dbm "
          + "ON bm.no_masuk = dbm.no_masuk "
          + "JOIN barang b "
          + "ON dbm.kode_barang = b.kode_barang "
          + "JOIN jenisbarang jb "
          + "ON b.kode_jenis = jb.kode_jenis";
break;
 case "laporan barang keluar":
        query = "SELECT bk.no_keluar, bk.tgl_keluar, jb.nama_jenis AS nama_barang, " +
                "dbk.jml_keluar, dbk.subtotal_keluar " +
                "FROM barangkeluar bk " +
                "JOIN detail_barangkeluar dbk ON bk.no_keluar = dbk.no_keluar " +
                "JOIN barang b ON dbk.kode_barang = b.kode_barang " +
                "JOIN jenisbarang jb ON b.kode_jenis = jb.kode_jenis";
        break;
        case "laporan hasil panen":
    query = "SELECT no_panen, tgl_panen, jumlah_panen, total_hasil FROM hasilpanen";
break;
    }
    if (query.isEmpty()){
       JOptionPane.showMessageDialog(this, " Query kosong! +" + pilihan);
            return;
    }
            
    try{
        koneksi konek = new koneksi();
        Connection conn = koneksi.configDB();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        DefaultTableModel model = new DefaultTableModel();
model.setColumnIdentifiers(new Object[]{
    "No Masuk",
    "Tanggal Masuk",
    "Nama Barang",
    "Jumlah Masuk",
    "Subtotal"
});
tblLaporan.setModel(model);
        while (rs.next()){
            model.addRow(new Object[]{
    rs.getInt("no_masuk"),
    rs.getDate("tgl_masuk"),
    rs.getString("nama_barang"),
    rs.getInt("jml_masuk"),
    rs.getBigDecimal("subtotal_masuk")
});        }
    }catch(Exception e){
        JOptionPane.showMessageDialog(this, "Gagal mengambil data" + e.getMessage());
    }
}
    public laporan() {
        initComponents();
        CBlaporan.addActionListener(e -> {
            String pilihan = CBlaporan.getSelectedItem().toString();
            lblMain.setText(pilihan);
            loadTabelLaporan(pilihan);
        }
        );
      
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        CBlaporan = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        Bcetak = new javax.swing.JButton();
        lblMain = new javax.swing.JLabel();
        Bkembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLaporan = new javax.swing.JTable();

        setBackground(new java.awt.Color(138, 195, 153));

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel1.setText("Laporan");

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel2.setText("Pilih jenis laporan");

        CBlaporan.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        CBlaporan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "laporan barang masuk", "laporan barang keluar", "hasil panen" }));
        CBlaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CBlaporanActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(138, 195, 153));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        Bcetak.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        Bcetak.setText("Cetak");
        Bcetak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BcetakActionPerformed(evt);
            }
        });

        lblMain.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        lblMain.setText("Main");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(122, 122, 122)
                .addComponent(lblMain)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(104, Short.MAX_VALUE)
                .addComponent(Bcetak)
                .addGap(102, 102, 102))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(lblMain)
                .addGap(27, 27, 27)
                .addComponent(Bcetak)
                .addGap(19, 19, 19))
        );

        Bkembali.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        Bkembali.setText("Kembali");
        Bkembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BkembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(220, 220, 220))
                    .addComponent(CBlaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Bkembali)
                .addGap(140, 140, 140))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(CBlaporan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(Bkembali)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblLaporan.setBackground(new java.awt.Color(204, 255, 204));
        tblLaporan.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        tblLaporan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblLaporan);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(72, 72, 72)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1017, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(77, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void BkembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BkembaliActionPerformed
        // TODO add your handling code here:
        String pilihan = CBlaporan.getSelectedItem().toString();
        lblMain.setText(pilihan);
        loadTabelLaporan(pilihan);
    }//GEN-LAST:event_BkembaliActionPerformed

    private void CBlaporanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CBlaporanActionPerformed
        // TODO add your handling code here: 
     String pilihan = CBlaporan.getSelectedItem().toString();

    if (pilihan.equals("laporan barang masuk")) {
        lblMain.setText("Laporan Barang Masuk");
    } else if (pilihan.equals("laporan barang keluar")) {
        lblMain.setText("Laporan Barang Keluar");
    } else if (pilihan.equals("hasil panen")) {
        lblMain.setText("Laporan Hasil Panen");
    }

    loadTabelLaporan(lblMain.getText());

    }//GEN-LAST:event_CBlaporanActionPerformed

    private void BcetakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BcetakActionPerformed
        // TODO add your handling code here:
        String menu = lblMain.getText();

        switch (menu) {
            case "Laporan Barang Masuk":
            Model_Laporan.cetakBarangMasuk(tblLaporan);
            break;

            case "Laporan Barang Keluar":
            Model_Laporan.cetakBarangKeluar(tblLaporan);
            break;

            case "Laporan Hasil Panen":
            Model_Laporan.cetakHasilPanen(tblLaporan);
            break;

            default:
            JOptionPane.showMessageDialog(this, "Sikahkan pilih menu laporan!");
            break;
        }
    }//GEN-LAST:event_BcetakActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Bcetak;
    private javax.swing.JButton Bkembali;
    private javax.swing.JComboBox<String> CBlaporan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblMain;
    private javax.swing.JTable tblLaporan;
    // End of variables declaration//GEN-END:variables
}

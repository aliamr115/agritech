/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package frame;

import Class.koneksi;
import Class.Model_User;
import Class.Model_BarangKeluar;
import Class.Model_Barang;
import Class.Model_DetBarangKeluar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.awt.CardLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Statement;
import javax.swing.JTable;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author asus
 */
public class barang_Keluar1 extends javax.swing.JPanel {
    
    String noEdit = "";
    String idEdit = "";
    String tglEdit = "";
    String totalEdit = "";

    /**
     * Creates new form barang_Keluar
     */
    public barang_Keluar1() {
        initComponents();
        
        loadComboBarang();
        showDataBK();
        load_table_BK();
       
    }
    
    void autoID() {
    try {
        Model_BarangKeluar mbk = new Model_BarangKeluar();
        ResultSet rs = mbk.autoID();

        int nomor = 1; // default

        if (rs.next()) {
            nomor = rs.getInt("max_id") + 1;
        }

        String kodeBaru = "BK" + String.format("%03d", nomor);
        tNoKeluar.setText(kodeBaru);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal generate ID: " + e.getMessage());
    }
}

   private void loadComboBarang() {
    try {
        cBarang.removeAllItems();
        cBarang.addItem("-- Pilih Barang --");

        Model_Barang mb = new Model_Barang();
        ResultSet rs = mb.loadNamaBarang();

        while (rs.next()) {
            cBarang.addItem(rs.getString("nama_barang"));
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}


       
    
    private void hitungTotalKeluar() {
    BigDecimal total = BigDecimal.ZERO;
    DefaultTableModel model = (DefaultTableModel) tblDetail.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        total = total.add((BigDecimal) model.getValueAt(i, 3));
    }

    tTotalKeluar.setText(total.toString());
}
    

    
    private void showDataBK() { //menampilkan panel utama dengan button TAMBAH
        mainPanel.removeAll();
        mainPanel.add(dataBK);
        mainPanel.repaint();
        mainPanel.revalidate();
        
        loadComboBarang();
        autoID();
        reset();
        tNoKeluar.setEditable(false);
        tIdUser.setEditable(true);

        
        btnTambah.setVisible(true);
        btnUbah.setVisible(true);
        btnHapus.setVisible(true);
        
        load_table_BK();
    }
    private void cariData() {
    String cari = tCari.getText().trim();
    
    DefaultTableModel model = (DefaultTableModel) tblBK.getModel();
    model.setRowCount(0);

    try {
        String sql = "SELECT * FROM barangkeluar "
                   + "WHERE CAST(no_keluar AS CHAR) LIKE ? "
                   + "OR CAST(id_user AS CHAR) LIKE ? "
                   + "OR tgl_keluar LIKE ? "
                   + "OR CAST(total_keluar AS CHAR) LIKE ?";

        PreparedStatement ps = koneksi.configDB().prepareStatement(sql);
        ps.setString(1, "%" + cari + "%");
        ps.setString(2, "%" + cari + "%");
        ps.setString(3, "%" + cari + "%");
        ps.setString(4, "%" + cari + "%");

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("no_keluar");
            String kodeBK = "BK" + String.format("%03d", id);

            model.addRow(new Object[]{
                kodeBK,                          // 👈 FORMAT BK001
                rs.getInt("id_user"),
                rs.getDate("tgl_keluar"),
                rs.getLong("total_keluar")
            });
        }

        rs.close();
        ps.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error pencarian: " + e.getMessage());
        e.printStackTrace();
    }
}

    
    private void showDetailBK() { //menampilkan panel utama dengan button TAMBAH
        mainPanel.removeAll();
        mainPanel.add(detailBK);
        mainPanel.repaint();
        mainPanel.revalidate();

        eventTableClick();
        btnKembali.setVisible(true);
        
        
        load_table_BK();
    }
    
   private void showTambahBK() {
    mainPanel.removeAll();
    mainPanel.add(tambahBK);
    mainPanel.repaint();
    mainPanel.revalidate();

    reset();               // ✅ Reset dulu
    loadComboBarang();  // ✅ Baru load combo box
       autoID();         // ✅ Generate ID
    
    tNoKeluar.setEditable(false);
    tIdUser.setEditable(true);
    load_table_BK();
}
    
    void load_table_BK() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No Keluar");
    model.addColumn("Tanggal");
    model.addColumn("Nama Barang");
    model.addColumn("Total");

    try {
        Model_BarangKeluar mbk = new Model_BarangKeluar();
        ResultSet rs = mbk.TampilBarangKeluar();

        while (rs.next()) {
            int id = rs.getInt("no_keluar");
            String kodeBK = "BK" + String.format("%03d", id);

            model.addRow(new Object[]{
                kodeBK,                          // BK001
                rs.getDate("tgl_keluar"),
                rs.getString("nama_barang"),     //  BARU
                rs.getLong("total_keluar")
            });
        }
        tblBK.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}


    
     void load_table_datailBK(String noKeluar){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No Keluar");
        model.addColumn("Kode Barang");
        model.addColumn("Jumlah");
        model.addColumn("Subtotal");
        
        
        try {
            //membuat objek user dan mengambil data dari database
            Model_DetBarangKeluar detbk = new Model_DetBarangKeluar();
            ResultSet result =  detbk.TampilDetailBK(noKeluar);
            //loop data baris per baris
            while (result.next()){
                //Tambahkan baris ke dalam tabel model
            model.addRow(new Object[]{
                 result.getString("no_keluar"),
                 result.getString("kode_barang"),
                 result.getInt("jml_keluar"),
                 result.getBigDecimal("subtotal_keluar"),
                  });
        }
           //set model ke JTable
        tblDetail.setModel(model);
        
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void loadKodeBarang() {
    try {
        String sql = "SELECT kode_barang FROM barang";
        Connection conn = koneksi.configDB();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        rs.close();
        st.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage());
    }
}

    
 private void reset() {
    tNoKeluar.setText("");
    tIdUser.setText("");
    tTotalKeluar.setText("");
    jcTglKeluar.setDate(null);
    
    
 }
    
    private void eventTableClick(){ //untuk menampilkan panel UBAH,HAPUS,BATAL saat salah satu data pada JTable diklik 
        tblBK.addMouseListener(new MouseAdapter(){
        @Override
        public void mouseClicked(MouseEvent e){
            int baris = tblBK.getSelectedRow();
            
            if (baris != -1){
                //ambil data dari tabel
                String noKeluar = tblBK.getValueAt(baris, 0).toString();
                String id = tblBK.getValueAt(baris, 1).toString();
                String tgl = tblBK.getValueAt(baris, 2).toString();
                String total = tblBK.getValueAt(baris, 3).toString();
                
                //simpan data
               
                
                dataBK.setVisible(true); //tampilkan panel dataBM(TAMBAH,UBAH,HAPUS)
                
                //otomatis mengisi variabel / textfield
                tNoKeluar.setText(noKeluar);
                tNoKeluar.setEditable(false);
                tIdUser.setText(id);
                tIdUser.setEditable(false);
                
                try{
                    //konversi string ke date
                    java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tgl);
                    jcTglKeluar.setDate(date);
                } catch (Exception ex){
                    System.out.println("Error parsing tanggal: " + ex.getMessage());
                }
                
                tTotalKeluar.setText(total);
                
                tampilkanTblAtas(noKeluar);
                tampilkanTblBawah(noKeluar);
                btnSimpan.setText("Ubah");
                
        }
        }
        });
    }
    
    public void tampilkanTblAtas(String no_keluar) { //menampilkan tabel  atas
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("No Keluar");
    model.addColumn("Tanggal");
    model.addColumn("Total");

    try {
        String sql = "SELECT * FROM barangkeluar WHERE no_keluar = ?";
        PreparedStatement ps = koneksi.configDB().prepareStatement(sql);
        ps.setString(1, no_keluar);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("no_keluar"),
                rs.getString("tgl_keluar"),
                rs.getBigDecimal("total_keluar")
            });
        }

        tblBK2.setModel(model);
    } catch (Exception e) {
        e.printStackTrace();
    }
}   
    
    public void tampilkanTblBawah(String no_keluar) { //menampilkan tabel det  bawah
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Kode Barang");
    model.addColumn("Nama Barang");
    model.addColumn("Jumlah");
    model.addColumn("Subtotal");

    try {
        String sql = "SELECT d.kode_barang, b.nama_barang, d.jml_keluar, d.subtotal_keluar "
                    + "FROM detail_barangkeluar d "
                    + "JOIN barang b ON d.kode_barang = b.kode_barang "
                    + "WHERE d.no_keluar = ?";
        
        PreparedStatement ps = koneksi.configDB().prepareStatement(sql);
        ps.setString(1, no_keluar);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("kode_barang"),
                rs.getString("nama_barang"),
                rs.getInt("jml_keluar"),
                rs.getBigDecimal("subtotal_keluar")
            });
        }

        tblDetail.setModel(model);

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void IDno_Keluar() { 
    try {
        Connection mysqlConfig = koneksi.configDB();
        Statement st = mysqlConfig.createStatement();
        ResultSet rs = st.executeQuery("SELECT MAX(no_keluar) AS max_id FROM barangkeluar");
        
        // ✅ INI YANG PENTING - HARUS ADA rs.next()
        if (rs.next()) {
            String maxID = rs.getString("max_id");
            
            if (maxID == null) {
                tNoKeluar.setText("BK001");
            } else {
                int nomor = Integer.parseInt(maxID.substring(2));
                nomor++;
                
                String kodeBaru = String.format("BK%03d", nomor);
                tNoKeluar.setText(kodeBaru);
            }
        } else {
            tNoKeluar.setText("BK001");
        }
        
        rs.close();
        st.close();
        
    } catch (Exception e) {
        e.printStackTrace();
        tNoKeluar.setText("BK001"); // Default jika error
        
    }
}

    
   public void simpanBarangKeluarDanDetail() {
    Connection conn = null;

    try {
        conn = koneksi.configDB();
        conn.setAutoCommit(false);
        Model_BarangKeluar mbk = new Model_BarangKeluar();
mbk.setIdUser(Integer.parseInt(tIdUser.getText()));
mbk.setTgl_keluar(new java.sql.Date(jcTglKeluar.getDate().getTime()));
mbk.setNama_barang(cBarang.getSelectedItem().toString()); // 👈 DARI COMBO
mbk.setTotal_keluar(Long.parseLong(tTotalKeluar.getText()));
        mbk.TambahBarangKeluar();
        load_table_BK();
        conn.commit();
        JOptionPane.showMessageDialog(this, "Barang keluar berhasil disimpan");
        showDataBK();

    } catch (Exception e) {
        try { 
            if (conn != null) conn.rollback(); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, e.getMessage());
    } finally { // ✅ TAMBAHKAN FINALLY
        try {
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        dataBK = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        btnTambah = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblBK = new javax.swing.JTable();
        btnHapus = new javax.swing.JButton();
        tCari = new javax.swing.JTextField();
        tambahBK = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        btnSimpan = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnBatal = new javax.swing.JButton();
        tNoKeluar = new javax.swing.JTextField();
        tIdUser = new javax.swing.JTextField();
        tTotalKeluar = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jcTglKeluar = new com.toedter.calendar.JDateChooser();
        cBarang = new javax.swing.JComboBox<>();
        detailBK = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        btnKembali = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblBK2 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();
        btnUbah = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        mainPanel.setLayout(new java.awt.CardLayout());

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel9.setText("Data Barang Keluar");

        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));

        btnTambah.setBackground(new java.awt.Color(153, 255, 0));
        btnTambah.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnTambah.setText("TAMBAH");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        tblBK.setBackground(new java.awt.Color(204, 255, 204));
        tblBK.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBKMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblBK);

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        tCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tCariActionPerformed(evt);
            }
        });
        tCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tCariKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout dataBKLayout = new javax.swing.GroupLayout(dataBK);
        dataBK.setLayout(dataBKLayout);
        dataBKLayout.setHorizontalGroup(
            dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataBKLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(300, Short.MAX_VALUE))
            .addGroup(dataBKLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataBKLayout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addGap(18, 18, 18)
                        .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dataBKLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel10))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataBKLayout.createSequentialGroup()
                                .addComponent(btnHapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dataBKLayout.setVerticalGroup(
            dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataBKLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah)
                        .addComponent(btnHapus)
                        .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 40, Short.MAX_VALUE))
        );

        mainPanel.add(dataBK, "card2");

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel11.setText("Tambah Barang Keluar");

        jSeparator11.setForeground(new java.awt.Color(0, 0, 0));

        btnSimpan.setBackground(new java.awt.Color(153, 255, 0));
        btnSimpan.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        tNoKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tNoKeluarActionPerformed(evt);
            }
        });

        tIdUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tIdUserActionPerformed(evt);
            }
        });

        jLabel1.setText("no keluar");

        jLabel2.setText("id user");

        jLabel4.setText("tgl keluar");

        jLabel5.setText("total keluar");

        jLabel3.setText("Kode barang");

        cBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout tambahBKLayout = new javax.swing.GroupLayout(tambahBK);
        tambahBK.setLayout(tambahBKLayout);
        tambahBKLayout.setHorizontalGroup(
            tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahBKLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahBKLayout.createSequentialGroup()
                        .addComponent(btnSimpan)
                        .addGap(30, 30, 30)
                        .addComponent(btnBatal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12))
                    .addGroup(tambahBKLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(tIdUser, javax.swing.GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
                            .addComponent(tNoKeluar)
                            .addComponent(tTotalKeluar)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jcTglKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(119, 119, 119)
                        .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(cBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(tambahBKLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(300, Short.MAX_VALUE))
        );
        tambahBKLayout.setVerticalGroup(
            tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahBKLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel12)
                    .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSimpan)
                        .addComponent(btnBatal)))
                .addGap(63, 63, 63)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tNoKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahBKLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tTotalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jcTglKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 162, Short.MAX_VALUE))
        );

        mainPanel.add(tambahBK, "card2");

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel13.setText("Detail Barang Keluar");

        jSeparator13.setForeground(new java.awt.Color(0, 0, 0));

        btnKembali.setBackground(new java.awt.Color(153, 255, 0));
        btnKembali.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        tblBK2.setBackground(new java.awt.Color(204, 255, 204));
        tblBK2.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBK2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBK2MouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(tblBK2);

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel15.setText("Data Barang Keluar");

        tblDetail.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetailMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblDetail);

        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout detailBKLayout = new javax.swing.GroupLayout(detailBK);
        detailBK.setLayout(detailBKLayout);
        detailBKLayout.setHorizontalGroup(
            detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailBKLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(300, Short.MAX_VALUE))
            .addGroup(detailBKLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailBKLayout.createSequentialGroup()
                        .addComponent(btnKembali)
                        .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(detailBKLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addContainerGap(1145, Short.MAX_VALUE))
                            .addGroup(detailBKLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(btnUbah)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(detailBKLayout.createSequentialGroup()
                        .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(324, Short.MAX_VALUE))))
        );
        detailBKLayout.setVerticalGroup(
            detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailBKLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnKembali)
                        .addComponent(btnUbah)))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(60, 60, 60)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(94, Short.MAX_VALUE))
        );

        mainPanel.add(detailBK, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        autoID();
        showTambahBK();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void tblBKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBKMouseClicked
        
    int row = tblBK.getSelectedRow();
    if (row == -1) return;

    noEdit = tblBK.getValueAt(row, 0).toString();     
    }//GEN-LAST:event_tblBKMouseClicked

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
int baris = tblBK.getSelectedRow();

if (baris == -1) {
    JOptionPane.showMessageDialog(null, "Pilih data yang mau dihapus");
    return;
}

// Ambil BK001 dari tabel
String kodeBK = tblBK.getValueAt(baris, 0).toString();

// Potong "BK" → ambil angka
int noKeluar = Integer.parseInt(kodeBK.substring(2));

Model_BarangKeluar barangKeluar = new Model_BarangKeluar();
barangKeluar.setNo_keluar((noKeluar));
barangKeluar.HapusBarangKeluar();

load_table_BK();


    }//GEN-LAST:event_btnHapusActionPerformed

    private void tCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tCariActionPerformed

    }//GEN-LAST:event_tCariActionPerformed

    private void tCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCariKeyReleased
        cariData();

    }//GEN-LAST:event_tCariKeyReleased

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        simpanBarangKeluarDanDetail();

    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalActionPerformed
        showDataBK();
    }//GEN-LAST:event_btnBatalActionPerformed

    private void tNoKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tNoKeluarActionPerformed

        Model_BarangKeluar barangKeluar = new Model_BarangKeluar();
        IDno_Keluar();
    }//GEN-LAST:event_tNoKeluarActionPerformed

    private void tIdUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tIdUserActionPerformed
        Model_User usr = new Model_User();
        IDno_Keluar();
    }//GEN-LAST:event_tIdUserActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        showDataBK();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void tblBK2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBK2MouseClicked

    }//GEN-LAST:event_tblBK2MouseClicked

    private void tblDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetailMouseClicked

    }//GEN-LAST:event_tblDetailMouseClicked

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed

         IDno_Keluar();
       eventTableClick();
       showTambahBK();
               
        //isi form dari variabel
        tNoKeluar.setText(noEdit);
        tNoKeluar.setEditable(false);
        tIdUser.setText(idEdit);
        tIdUser.setEditable(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
        java.util.Date date = null;
         try {
             date = sdf.parse(tglEdit);
         } catch (ParseException ex) {
             Logger.getLogger(barang_Keluar1.class.getName()).log(Level.SEVERE, null, ex);
         }
        jcTglKeluar.setDate(date);
        tTotalKeluar.setText(totalEdit);
        
        btnSimpan.setText("Ubah"); 
    }//GEN-LAST:event_btnUbahActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cBarang;
    private javax.swing.JPanel dataBK;
    private javax.swing.JPanel detailBK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator9;
    private com.toedter.calendar.JDateChooser jcTglKeluar;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tIdUser;
    private javax.swing.JTextField tNoKeluar;
    private javax.swing.JTextField tTotalKeluar;
    private javax.swing.JPanel tambahBK;
    private javax.swing.JTable tblBK;
    private javax.swing.JTable tblBK2;
    private javax.swing.JTable tblDetail;
    // End of variables declaration//GEN-END:variables
}

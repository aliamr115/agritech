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
public class barang_Keluar extends javax.swing.JPanel {
    
    String noEdit = "";
    String idEdit = "";
    String tglEdit = "";
    String totalEdit = "";

    /**
     * Creates new form barang_Keluar
     */
    public barang_Keluar() {
        initComponents();
        IDno_Keluar();
        showDataBK();
    }
    


    private void hitungTotalKeluar() {
    BigDecimal total = BigDecimal.ZERO;
    DefaultTableModel model = (DefaultTableModel) tblDetail.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        total = total.add((BigDecimal) model.getValueAt(i, 3));
    }

    tTotalKeluar.setText(total.toString());
}
    private void resetInputBarang() {
         tKodeBarang.setText("");
        tHarga.setText("");
        tJumlah.setText("");
        tSubtotal.setText("");
        cNamaBarang.setSelectedIndex(0);
}

    
    private void showDataBK() { //menampilkan panel utama dengan button TAMBAH
        mainPanel.removeAll();
        mainPanel.add(dataBK);
        mainPanel.repaint();
        mainPanel.revalidate();

        
        btnTambah.setVisible(true);
        btnUbah.setVisible(true);
        btnHapus.setVisible(true);
        
        load_table_BK();
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
    
    private void showTambahBK() { //menampilkan panel untuk input tambah jenis
        mainPanel.removeAll();
        mainPanel.add(tambahBK);
        mainPanel.repaint();
        mainPanel.revalidate();

        reset();
    }
    
    void load_table_BK(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No keluar");
        model.addColumn("ID User");
        model.addColumn("Tanggal");
        model.addColumn("Total Keluar");
        
        
        try {
            //membuat objek user dan mengambil data dari database
            Model_BarangKeluar bk = new Model_BarangKeluar();
            ResultSet result =  bk.TampilBarangKeluar();
            //loop data baris per baris
            while (result.next()){
                //Tambahkan baris ke dalam tabel model
            model.addRow(new Object[]{
                 result.getString("no_keluar"),
                 result.getString("id_user"),
                 result.getString("tgl_keluar"),
                 result.getString("total_keluar"),
                  });
        }
           //set model ke JTable
        tblBK.setModel(model);
        
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
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

    
    private void reset(){
        tNoKeluar.setText("");
        tIdUser.setText("");
        cNamaBarang.setSelectedItem("");
        jcTglKeluar.setDate(null); //menggunakan jCalender
        tTotalKeluar.setText("");

        btnSimpan.setText("Simpan");
        
    }
    
        private void cariData(){ //textfield untuk mencari data
        String cari = tCari.getText();
        
        DefaultTableModel model = (DefaultTableModel) tblBK.getModel();
        model.setRowCount(0);
        
        try {
            String sql = "SELECT * FROM barangkeluar WHERE no_keluar LIKE ? OR id_user LIKE ? OR tgl_keluar LIKE ? OR total_keluar LIKE ?";
            PreparedStatement ps = koneksi.configDB().prepareStatement(sql);
            ps.setString(1, "%" + cari + "%");
            ps.setString(2, "%" + cari + "%");
            ps.setString(3, "%" + cari + "%");
            ps.setString(4, "%" + cari + "%");
           
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("no_keluar"),
                    rs.getString("id_user"),
                    rs.getString("tgl_keluar"),
                    rs.getString("total_keluar")
                    
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
         
       
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
                noEdit    = noKeluar;
                idEdit    = id;
                tglEdit   = tgl;
                totalEdit = total;
                
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
                showDetailBK();
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
      public void IDno_Keluar(){ //id otomatis untuk no keluar
        try {
            Connection mysqlConfig = koneksi.configDB();
            Statement st = mysqlConfig.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX(no_keluar) AS max_id FROM barangkeluar");
            
            if (rs.next()){
                String maxID = rs.getString("max_id");
                
                if(maxID == null){
                    tNoKeluar.setText("BK001");
                } else {
                    int nomor = Integer.parseInt(maxID.substring(2));
                    nomor ++;
                    
                    String kodeBaru = String.format("BK%03d", nomor);
                    tNoKeluar.setText(kodeBaru);
                }
            }
            
            rs.close();
            st.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    
     public void simpanBarangKeluarDanDetail() {

    Connection conn = null;

    try {
        conn = koneksi.configDB();
        conn.setAutoCommit(false);

        // HEADER
        String sqlHeader = "INSERT INTO barangkeluar VALUES (?,?,?,?)";
        PreparedStatement psH = conn.prepareStatement(sqlHeader);
        psH.setString(1, tNoKeluar.getText());
        psH.setString(2, tIdUser.getText());
        psH.setDate(3, new java.sql.Date(jcTglKeluar.getDate().getTime()));
        psH.setBigDecimal(4, new BigDecimal(tTotalKeluar.getText()));
        psH.executeUpdate();

        // DETAIL
        String sqlDetail = "INSERT INTO detail_barangkeluar VALUES (?,?,?,?)";
        PreparedStatement psD = conn.prepareStatement(sqlDetail);

        // UPDATE STOK
        String sqlStok = "UPDATE barang SET stok = stok - ? WHERE kode_barang = ?";
        PreparedStatement psStok = conn.prepareStatement(sqlStok);

        for (int i = 0; i < tblDetail.getRowCount(); i++) {

            String kode = tblDetail.getValueAt(i, 0).toString();
            int jumlah = Integer.parseInt(tblDetail.getValueAt(i, 2).toString());
            BigDecimal subtotal = (BigDecimal) tblDetail.getValueAt(i, 3);

            // CEK STOK
            PreparedStatement cek = conn.prepareStatement(
                "SELECT stok FROM barang WHERE kode_barang=?");
            cek.setString(1, kode);
            ResultSet rs = cek.executeQuery();

            if (rs.next() && rs.getInt("stok") < jumlah) {
                throw new Exception("Stok barang " + kode + " tidak cukup!");
            }

            // INSERT DETAIL
            psD.setString(1, tNoKeluar.getText());
            psD.setString(2, kode);
            psD.setInt(3, jumlah);
            psD.setBigDecimal(4, subtotal);
            psD.executeUpdate();

            // KURANGI STOK
            psStok.setInt(1, jumlah);
            psStok.setString(2, kode);
            psStok.executeUpdate();
        }

        conn.commit();
        JOptionPane.showMessageDialog(this, "Barang keluar berhasil disimpan");
        showDataBK();

    } catch (Exception e) {
        try { conn.rollback(); } catch (Exception ex) {}
        JOptionPane.showMessageDialog(this, e.getMessage());
    


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
        btnDetail = new javax.swing.JButton();
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
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        tHarga = new javax.swing.JTextField();
        tJumlah = new javax.swing.JTextField();
        cNamaBarang = new javax.swing.JComboBox<>();
        tKodeBarang = new javax.swing.JTextField();
        tSubtotal = new javax.swing.JTextField();
        jcTglKeluar = new com.toedter.calendar.JDateChooser();
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
        jSeparator1 = new javax.swing.JSeparator();

        setLayout(new java.awt.CardLayout());

        mainPanel.setLayout(new java.awt.CardLayout());

        dataBK.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel9.setText("Data Barang Keluar");

        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));

        btnTambah.setBackground(new java.awt.Color(0, 153, 0));
        btnTambah.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
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

        btnHapus.setBackground(new java.awt.Color(0, 153, 0));
        btnHapus.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
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

        btnDetail.setBackground(new java.awt.Color(0, 153, 0));
        btnDetail.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnDetail.setForeground(new java.awt.Color(255, 255, 255));
        btnDetail.setText("Detail");
        btnDetail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dataBKLayout = new javax.swing.GroupLayout(dataBK);
        dataBK.setLayout(dataBKLayout);
        dataBKLayout.setHorizontalGroup(
            dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataBKLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataBKLayout.createSequentialGroup()
                        .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 969, Short.MAX_VALUE)
                            .addComponent(jSeparator9))
                        .addContainerGap(43, Short.MAX_VALUE))
                    .addGroup(dataBKLayout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addGap(30, 30, 30)
                        .addComponent(btnHapus)
                        .addGap(31, 31, 31)
                        .addComponent(btnDetail)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dataBKLayout.createSequentialGroup()
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51))))))
        );
        dataBKLayout.setVerticalGroup(
            dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataBKLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addGroup(dataBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(44, 44, 44)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 30, Short.MAX_VALUE))
        );

        mainPanel.add(dataBK, "card2");

        tambahBK.setBackground(new java.awt.Color(255, 255, 255));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel11.setText("Tambah Barang Keluar");

        jSeparator11.setForeground(new java.awt.Color(0, 0, 0));

        btnSimpan.setBackground(new java.awt.Color(0, 153, 51));
        btnSimpan.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        btnBatal.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
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

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel1.setText("No Keluar");

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel2.setText("ID User");

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel4.setText("Tanggal");

        jLabel5.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel5.setText("Total Keluar");

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel3.setText("Kode Barang");

        jLabel6.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel6.setText("Nama Barang");

        jLabel7.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel7.setText("Harga");

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel8.setText("Jumlah");

        jLabel16.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel16.setText("Subtotal");

        tJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tJumlahKeyReleased(evt);
            }
        });

        cNamaBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cNamaBarang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cNamaBarangItemStateChanged(evt);
            }
        });

        tKodeBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tKodeBarangKeyReleased(evt);
            }
        });

        tSubtotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tSubtotalKeyReleased(evt);
            }
        });

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
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(tIdUser)
                            .addComponent(tNoKeluar)
                            .addComponent(jcTglKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                            .addComponent(tTotalKeluar, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(tambahBKLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(tambahBKLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addComponent(jLabel3))
                            .addComponent(jLabel8)
                            .addComponent(jLabel7)
                            .addComponent(cNamaBarang, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(tKodeBarang)
                            .addComponent(tHarga)
                            .addComponent(tJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(141, 141, 141))))
            .addGroup(tambahBKLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 940, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(72, Short.MAX_VALUE))
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
                        .addComponent(btnSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBatal, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(63, 63, 63)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tNoKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(5, 5, 5)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cNamaBarang, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                    .addComponent(tIdUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tHarga, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(jcTglKeluar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(0, 0, 0)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tTotalKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(tambahBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16)
                    .addComponent(tSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 247, Short.MAX_VALUE))
        );

        mainPanel.add(tambahBK, "card2");

        detailBK.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel13.setText("Detail Barang Keluar");

        jSeparator13.setForeground(new java.awt.Color(0, 0, 0));

        btnKembali.setBackground(new java.awt.Color(0, 153, 51));
        btnKembali.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnKembali.setForeground(new java.awt.Color(255, 255, 255));
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

        tblDetail.setBackground(new java.awt.Color(204, 255, 204));
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

        btnUbah.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout detailBKLayout = new javax.swing.GroupLayout(detailBK);
        detailBK.setLayout(detailBKLayout);
        detailBKLayout.setHorizontalGroup(
            detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailBKLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailBKLayout.createSequentialGroup()
                        .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel14)
                            .addGroup(detailBKLayout.createSequentialGroup()
                                .addComponent(btnKembali)
                                .addGap(18, 18, 18)
                                .addComponent(btnUbah)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(detailBKLayout.createSequentialGroup()
                        .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 966, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 966, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 966, Short.MAX_VALUE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGap(0, 30, Short.MAX_VALUE))))
        );
        detailBKLayout.setVerticalGroup(
            detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailBKLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailBKLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel14))
                    .addGroup(detailBKLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(detailBKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUbah, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                            .addComponent(btnKembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(54, 54, 54)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel13)
                .addGap(5, 5, 5)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        mainPanel.add(detailBK, "card2");

        add(mainPanel, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        IDno_Keluar();
        showTambahBK();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void tblBKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBKMouseClicked
        
    int row = tblBK.getSelectedRow();
    if (row == -1) return;

    noEdit = tblBK.getValueAt(row, 0).toString();     
    }//GEN-LAST:event_tblBKMouseClicked

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        Model_BarangKeluar barangKeluar = new Model_BarangKeluar();
       //barangKeluar.setNo_keluar(tNoKeluar.getText());

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
       // Model_User usr = new Model_User();
        IDno_Keluar();
    }//GEN-LAST:event_tIdUserActionPerformed

    private void tJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tJumlahKeyReleased
        //otomatis hitung subtotal
        try {
            double harga = Double.parseDouble(tHarga.getText());
            int jumlah = Integer.parseInt(tJumlah.getText());
            double subtotal = harga * jumlah;

            tSubtotal.setText(String.valueOf(subtotal));
        } catch (Exception e) {
            tSubtotal.setText("");
        }
    }//GEN-LAST:event_tJumlahKeyReleased

    private void cNamaBarangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cNamaBarangItemStateChanged
        //digunakan untuk isi otomatis kode barang & harga saat nama barang dpilih
        try {
            String sql = "SELECT kode_barang, harga FROM barang WHERE nama_barang = ?";
            Connection con = koneksi.configDB();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cNamaBarang.getSelectedItem().toString());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tKodeBarang.setText(rs.getString("kode_barang"));
                tHarga.setText(rs.getString("harga"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            reset();
        }
    }//GEN-LAST:event_cNamaBarangItemStateChanged

    private void tKodeBarangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tKodeBarangKeyReleased
        //otomatis terisi saat ketik kode barang
        try {
            String sql = "SELECT nama_barang, harga FROM barang WHERE kode_barang = ?";
            Connection con = koneksi.configDB();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tKodeBarang.getText());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cNamaBarang.setSelectedItem(rs.getString("nama_barang"));
                tHarga.setText(rs.getString("harga"));
            }
        } catch (Exception e) {
            // dikosongkan jika tidak ditemukan
            reset();
        }
    }//GEN-LAST:event_tKodeBarangKeyReleased

    private void tSubtotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tSubtotalKeyReleased

    }//GEN-LAST:event_tSubtotalKeyReleased

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
             Logger.getLogger(barang_Keluar.class.getName()).log(Level.SEVERE, null, ex);
         }
        jcTglKeluar.setDate(date);
        tTotalKeluar.setText(totalEdit);
        
        btnSimpan.setText("Ubah"); 
    }//GEN-LAST:event_btnUbahActionPerformed

    private void btnDetailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailActionPerformed
        // TODO add your handling code here:
         if (noEdit == null || noEdit.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu");
        return;
    }

    tampilkanTblBawah(noEdit);
    }//GEN-LAST:event_btnDetailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnDetail;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cNamaBarang;
    private javax.swing.JPanel dataBK;
    private javax.swing.JPanel detailBK;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator9;
    private com.toedter.calendar.JDateChooser jcTglKeluar;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tHarga;
    private javax.swing.JTextField tIdUser;
    private javax.swing.JTextField tJumlah;
    private javax.swing.JTextField tKodeBarang;
    private javax.swing.JTextField tNoKeluar;
    private javax.swing.JTextField tSubtotal;
    private javax.swing.JTextField tTotalKeluar;
    private javax.swing.JPanel tambahBK;
    private javax.swing.JTable tblBK;
    private javax.swing.JTable tblBK2;
    private javax.swing.JTable tblDetail;
    // End of variables declaration//GEN-END:variables
}

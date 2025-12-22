/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package frame;

import Class.Model_BarangMasuk;
//import Class.Model_User;
//import Class.Model_Barang;
import Class.Model_DetBarangMasuk;
import Class.koneksi;
import java.awt.CardLayout;
import java.awt.event.ItemEvent;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.math.BigDecimal;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author asus
 */
public class barang_Masuk extends javax.swing.JPanel {
    String noEdit = "";
    String idEdit = "";
    String tglEdit = "";
    String totalEdit = "";
    
    /**
     * Creates new form barang_Masuk
     */
    public barang_Masuk() {//konstruktor
        initComponents();
        tSubtotal.setEditable(false);
        tHarga.setEditable(false);
        tKodeBarang.setEditable(false);
        tNoMasuk.setText("AUTO");
        tIdUser.setEditable(false);
        tTotalMasuk.setEditable(true);
        
        tampilDataBarangMasuk();
        tampilkanTabelAtas(noEdit);
        tampilkanTabelBawah(noEdit);
        loadComboBarang();
        load_table_dataBarangMasuk();
        eventTableClick();
    }
 
    private void tampilDataBarangMasuk(){// menampilkan panel utama button tambah
        mainPanel.removeAll();
        mainPanel.add(dataBarangMasuk);
        mainPanel.repaint();
        mainPanel.revalidate();
        
        btnTambah.setVisible(true);
        btnHapus.setVisible(true);
        btnUbah.setVisible(true);
        
        load_table_dataBarangMasuk();
    }
    
    private void tampilDetailBarangMasuk(){ // tampil panel utama
        mainPanel.removeAll();
        mainPanel.add(detailBarangMasuk);
        mainPanel.repaint();
        mainPanel.revalidate();
        
        btnUbah.setVisible(true);
        btnKembali.setVisible(true);
        
        tampilkanTabelAtas(noEdit);
        tampilkanTabelBawah(noEdit);
    }
    
    private void tampilTambahBarangMasuk() { // menampilkan panel untuk input tambah jenis
        mainPanel.removeAll();
        mainPanel.add(tambahBarangMasuk);
        mainPanel.repaint();
        mainPanel.revalidate();
        
        reset();
    }
    
    void load_table_dataBarangMasuk(){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No Masuk");
        model.addColumn("ID User");
        model.addColumn("Tanggal");
        model.addColumn("Total");
        
        try {
            //membuat objek user dan mengambil data dari database
            Model_BarangMasuk dbm = new Model_BarangMasuk();
            ResultSet result =  dbm.tampilDataBarangMasuk();
            //loop data baris per baris
            while (result.next()){
                //Tambahkan baris ke dalam tabel model
            model.addRow(new Object[]{
                 result.getInt("no_masuk"),
                 result.getInt("id_user"),
                 result.getDate("tgl_masuk"),
                 result.getBigDecimal("total_masuk")
            });
        }
           //set model ke JTable
        tblDataBarangMasuk.setModel(model);
        
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    void load_table_detailBarangMasuk(String noMasuk){
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No Masuk");
        model.addColumn("Kode Barang");
        model.addColumn("Jumlah");
        model.addColumn("Subtotal");
        
        try {
            String sql = "SELECT no_masuk, id_user, tgl_masuk, total_masuk FROM barangmasuk";
            Statement st = koneksi.configDB().createStatement();
            ResultSet result =  st.executeQuery(sql);
            //loop data baris per baris
            while (result.next()){
                //Tambahkan baris ke dalam tabel model
            model.addRow(new Object[]{
                 result.getInt("no_masuk"),
                 result.getInt("id_user"),
                 result.getString("tgl_masuk"),
                 result.getInt("total_masuk")
            });
        }
           //set model ke JTable
        tblDetail.setModel(model);
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }       
    public void tampilkanTabelAtas(String noMasuk){//menampilkan tabel atas
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("No Masuk");
        model.addColumn("1D User");
        model.addColumn("Tanggal");
        model.addColumn("Total");
        
        try {
            String sql = "SELECT * FROM barangmasuk WHERE no_masuk =?";
            PreparedStatement ps = koneksi.configDB().prepareStatement(sql);
            ps.setString(1, noMasuk);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("no_masuk"),
                    rs.getString("id_user"),
                    rs.getString("tgl_masuk"),
                    rs.getBigDecimal("total_masuk"),
                });
        }
            tblDetailAtas.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void tampilkanTabelBawah(String noMasuk){ //menampilkan tabel detail bawah
        DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Kode Barang");
            model.addColumn("Nama Barang");
            model.addColumn("Harga");
            model.addColumn("Jumlah");
            model.addColumn("Subtotal");

            try {
                String sql = "SELECT b.kode_barang, b.nama_barang, b.harga, d.jml_masuk, d.subtotal_masuk "
                            + "FROM detail_barangmasuk d "
                            + "JOIN barang b ON d.kode_barang = b.kode_barang "
                            + "WHERE d.no_masuk = ?";
                
            PreparedStatement ps = koneksi.configDB().prepareStatement(sql);
            ps.setString(1, noMasuk);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                model.addRow(new Object[]{
                    rs.getInt("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("harga"),
                    rs.getInt("jml_masuk"),
                    rs.getBigDecimal("subtotal_masuk")
                });
            }
            tblDetailBawah.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadComboBarang(){//kode barang, nama barang, harga muncul otomatis saat dipilih nama barang
        try {
            String sql = "SELECT kode_barang, nama_barang, harga FROM barang";
            Connection con = koneksi.configDB();
            Statement st =  con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            cNamaBarang.removeAllItems();
            while (rs.next()) { 
                cNamaBarang.addItem(rs.getString("nama_barang"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            
            reset();
        }
    }
    
    private void simpanBarangMasukDanDetail(){//digunakan untuk simpan ke 2 tabel
        Connection conn = null;
        PreparedStatement psHeader = null;
        PreparedStatement psDetail = null;
        ResultSet rs = null;
        
        try {
            conn = koneksi.configDB();
            conn.setAutoCommit(false);
            
            //simpan header tanpa no masuk
            String sqlHeader = "INSERT INTO barangmasuk (id_user, tgl_masuk, total_masuk) VALUES (?,?,?)";
            psHeader = conn.prepareStatement(sqlHeader, Statement.RETURN_GENERATED_KEYS);
            psHeader.setInt(1, Integer.parseInt(tIdUser.getText()));
            psHeader.setDate(2, new java.sql.Date(jcTanggal.getDate().getTime()));
            psHeader.setBigDecimal(3, new BigDecimal(tTotalMasuk.getText()));
            psHeader.executeUpdate();
            
            //ambil no masuk auto increment
            rs = psHeader.getGeneratedKeys();
            int noMasuk = 0;
            if (rs.next()) {
                noMasuk = rs.getInt(1);
            }
            
            //tampil ke from
            tNoMasuk.setText(String.valueOf(noMasuk));
            
            //simpan ke detail
            String sqlDetail = "INSERT INTO detail_barangmasuk (no_masuk, kode_barang, jml_masuk, subtotal_masuk) VALUES (?,?,?,?)";
            psDetail = conn.prepareStatement(sqlDetail);
            
            for (int i = 0; i < tblDetailBawah.getRowCount(); i++) {
                psDetail.setInt(1, noMasuk);
                psDetail.setInt(2, Integer.parseInt(tblDetailBawah.getValueAt(i, 1).toString()));//kode barang
                psDetail.setInt(3, Integer.parseInt(tblDetailBawah.getValueAt(i, 2).toString()));//jumlah masuk
                psDetail.setBigDecimal(4, new BigDecimal(tblDetailBawah.getValueAt(i, 3).toString()));//subtotal
                psDetail.executeUpdate();
            }
            conn.commit();
            JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan");
            
            load_table_dataBarangMasuk();
            reset();
            
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {}
            JOptionPane.showMessageDialog(this, "Gagal Menyimpan: " + e.getMessage());
        }
    }
    
    private void hitungSubtotal(){
        try {
            int jumlah = Integer.parseInt(tJumlah.getText());
            int harga = Integer.parseInt(tHarga.getText());
            
            int subtotal = jumlah * harga;
            tSubtotal.setText(String.valueOf(subtotal));
        } catch (NumberFormatException e) {
            tSubtotal.setText("0");
        }
    }
    
    private void reset(){
        tNoMasuk.setText(null);
        tIdUser.setText(null);
        jcTanggal.setDate(null);
        tTotalMasuk.setText(null);
        
        btnSimpan.setText("Simpan");
    }
    
    public void hapusBarangMasuk(int noMasuk){
        try {
            Connection con = koneksi.configDB();
            con.setAutoCommit(false);
            
            PreparedStatement psDetail = con.prepareStatement("DELETE FROM detail_barangmasuk WHERE no_masuk = ?");
            psDetail.setInt(1, noMasuk);
            psDetail.executeUpdate();
            
            PreparedStatement psHeader = con.prepareStatement("DELETE FROM barangmasuk WHERE no_masuk = ?");
            psHeader.setInt(1, noMasuk);
            psHeader.executeUpdate();
            
            con.commit();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Hapus: " + e.getMessage());
        }
    }
    
    private void cariData(){
        String cari = tCari.getText();
        DefaultTableModel model = (DefaultTableModel) tblDataBarangMasuk.getModel();
        model.setRowCount(0);
        
        try {
            String sql = "SELECT * FROM barangmasuk WHERE no_masuk LIKE ? OR id_user LIKE ? OR tgl_masuk LIKE ? OR total_masuk LIKE ?";
            PreparedStatement ps = koneksi.configDB().prepareStatement(sql);
            ps.setString(1, "%" + cari + "%");
            ps.setString(2, "%" + cari + "%");
            ps.setString(3, "%" + cari + "%");
            ps.setString(4, "%" + cari + "%");
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {                
                model.addRow(new Object[]{
                    rs.getString("no_masuk"),
                    rs.getString("id_user"),
                    rs.getString("tgl_masuk"),
                    rs.getString("total_masuk"),
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error : " + e.getMessage());
        }
    }    
    private void hitungTotalMasuk(){
        int total = 0;
        for (int i = 0; i < tblDetailBawah.getRowCount(); i++) {
            total += Integer.parseInt(tblDetailBawah.getValueAt(i, 3).toString());
        }
        tTotalMasuk.setText(String.valueOf(total));
    }   
    
    private void eventTableClick(){// menampilkan panel ubah,hapus,batal saat salah satu data pada JTable diklik
        tblDataBarangMasuk.addMouseListener(new MouseAdapter(){
        public void mouseClicked(MouseEvent e){
            int baris = tblDataBarangMasuk.getSelectedRow();
            
            if (baris != -1) {
                //ambil data dari tabel
                String noMasuk = tblDataBarangMasuk.getValueAt(baris, 0).toString();
                String id = tblDataBarangMasuk.getValueAt(baris, 1).toString();
                String tgl = tblDataBarangMasuk.getValueAt(baris, 2).toString();
                String total = tblDataBarangMasuk.getValueAt(baris, 3).toString();
                
                //simpan data
                noEdit    = noMasuk;
                idEdit    = id;
                tglEdit   = tgl;
                totalEdit = total;
                
                dataBarangMasuk.setVisible(true);//tampil panel data tambah,ubah,hapus
                
                //mengisi otomatis variabel/text
                tNoMasuk.setText(noMasuk);
                tNoMasuk.setEditable(false);
                tIdUser.setText(id);
                tIdUser.setEditable(false);
                
                try{
                    java.util.Date dt = new SimpleDateFormat("yyyy-MM-dd").parse(tgl);
                    jcTanggal.setDate(dt);
                } catch (Exception ex){
                    System.out.println("Error parsing tanggal: " + ex.getMessage());
                }
                
                tTotalMasuk.setText(total);
                tampilkanTabelBawah(noMasuk);
                tampilkanTabelAtas(noMasuk);
                btnSimpan.setText("Ubah");
                tampilDetailBarangMasuk();
            }
        }
        });
    }
    /**z
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        mainPanel = new javax.swing.JPanel();
        dataBarangMasuk = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDataBarangMasuk = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        tCari = new javax.swing.JTextField();
        tambahBarangMasuk = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        btnSimpan = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        tNoMasuk = new javax.swing.JTextField();
        tIdUser = new javax.swing.JTextField();
        tTotalMasuk = new javax.swing.JTextField();
        btnBatalTambah = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        tKodeBarang = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        tHarga = new javax.swing.JTextField();
        tJumlah = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cNamaBarang = new javax.swing.JComboBox<>();
        tSubtotal = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jcTanggal = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblDetail = new javax.swing.JTable();
        detailBarangMasuk = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        btnKembali = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDetailAtas = new javax.swing.JTable();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblDetailBawah = new javax.swing.JTable();
        btnUbah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        dataBarangMasuk.setBackground(new java.awt.Color(138, 195, 153));

        jLabel3.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel3.setText("Data Barang Masuk");

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));

        tblDataBarangMasuk.setBackground(new java.awt.Color(204, 255, 204));
        tblDataBarangMasuk.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        tblDataBarangMasuk.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "No Masuk", "ID User", "Tanggal", "Total"
            }
        ));
        tblDataBarangMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDataBarangMasukMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblDataBarangMasuk);

        btnTambah.setBackground(new java.awt.Color(0, 153, 0));
        btnTambah.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnTambah.setForeground(new java.awt.Color(255, 255, 255));
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
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

        tambahBarangMasuk.setBackground(new java.awt.Color(138, 195, 153));

        jLabel8.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel8.setText("Tambah Barang Masuk");

        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));

        btnSimpan.setBackground(new java.awt.Color(0, 153, 0));
        btnSimpan.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnSimpan.setForeground(new java.awt.Color(255, 255, 255));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSimpanActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel1.setText("No Masuk");

        jLabel2.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel2.setText("ID User");

        jLabel4.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel4.setText("Tanggal  Masuk");

        jLabel10.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel10.setText("Total Masuk");

        tNoMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tNoMasukActionPerformed(evt);
            }
        });

        tIdUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tIdUserActionPerformed(evt);
            }
        });

        tTotalMasuk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tTotalMasukActionPerformed(evt);
            }
        });

        btnBatalTambah.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnBatalTambah.setText("Batal");
        btnBatalTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalTambahActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel12.setText("Kode Barang");

        tKodeBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tKodeBarangActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel13.setText("Nama Barang");

        tHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tHargaActionPerformed(evt);
            }
        });
        tHarga.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tHargaKeyReleased(evt);
            }
        });

        tJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJumlahActionPerformed(evt);
            }
        });
        tJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tJumlahKeyReleased(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel14.setText("Harga");

        jLabel15.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        jLabel15.setText("Jumlah");

        cNamaBarang.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        cNamaBarang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cNamaBarang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cNamaBarangItemStateChanged(evt);
            }
        });

        tSubtotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tSubtotalKeyReleased(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel16.setText("Subtotal");

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
        jScrollPane2.setViewportView(tblDetail);

        javax.swing.GroupLayout tambahBarangMasukLayout = new javax.swing.GroupLayout(tambahBarangMasuk);
        tambahBarangMasuk.setLayout(tambahBarangMasukLayout);
        tambahBarangMasukLayout.setHorizontalGroup(
            tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel8)
                        .addContainerGap(744, Short.MAX_VALUE))
                    .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                        .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(btnSimpan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnBatalTambah))
                            .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(jLabel9))
                            .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(tIdUser)
                                    .addComponent(tNoMasuk)
                                    .addComponent(tTotalMasuk)
                                    .addComponent(jcTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(114, 114, 114)
                        .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14)
                                    .addComponent(jLabel15)
                                    .addComponent(cNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 398, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tambahBarangMasukLayout.createSequentialGroup()
                                        .addComponent(jLabel16)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 902, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        tambahBarangMasukLayout.setVerticalGroup(
            tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSimpan)
                            .addComponent(btnBatalTambah))
                        .addGap(17, 17, 17)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tNoMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(10, 10, 10)
                        .addComponent(tIdUser, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel4)
                        .addGap(10, 10, 10)
                        .addComponent(jcTanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel10)
                        .addGap(10, 10, 10)
                        .addComponent(tTotalMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(tambahBarangMasukLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel12)
                        .addGap(18, 18, 18)
                        .addComponent(tKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel15)
                        .addGap(10, 10, 10)
                        .addComponent(tJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(tambahBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(49, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dataBarangMasukLayout = new javax.swing.GroupLayout(dataBarangMasuk);
        dataBarangMasuk.setLayout(dataBarangMasukLayout);
        dataBarangMasukLayout.setHorizontalGroup(
            dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataBarangMasukLayout.createSequentialGroup()
                .addGroup(dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataBarangMasukLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel3))
                    .addGroup(dataBarangMasukLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tambahBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(dataBarangMasukLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 914, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dataBarangMasukLayout.createSequentialGroup()
                            .addGap(38, 38, 38)
                            .addGroup(dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(dataBarangMasukLayout.createSequentialGroup()
                                    .addComponent(btnTambah)
                                    .addGap(613, 613, 613)
                                    .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 914, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dataBarangMasukLayout.setVerticalGroup(
            dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataBarangMasukLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addGroup(dataBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tambahBarangMasuk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        mainPanel.add(dataBarangMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1098, -1));

        detailBarangMasuk.setBackground(new java.awt.Color(138, 195, 153));
        detailBarangMasuk.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                detailBarangMasukMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel6.setText("Data Barang Masuk");

        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));

        btnKembali.setBackground(new java.awt.Color(153, 255, 0));
        btnKembali.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnKembali.setText("Kembali");
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        tblDetailAtas.setBackground(new java.awt.Color(204, 255, 204));
        tblDetailAtas.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        tblDetailAtas.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDetailAtas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetailAtasMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblDetailAtas);

        jSeparator10.setForeground(new java.awt.Color(0, 0, 0));

        jLabel11.setFont(new java.awt.Font("Franklin Gothic Book", 0, 18)); // NOI18N
        jLabel11.setText("Detail Barang Masuk");

        tblDetailBawah.setBackground(new java.awt.Color(204, 255, 204));
        tblDetailBawah.setFont(new java.awt.Font("Franklin Gothic Book", 0, 12)); // NOI18N
        tblDetailBawah.setModel(new javax.swing.table.DefaultTableModel(
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
        tblDetailBawah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetailBawahMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblDetailBawah);

        btnUbah.setBackground(new java.awt.Color(153, 255, 0));
        btnUbah.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnUbah.setText("Ubah");
        btnUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUbahActionPerformed(evt);
            }
        });

        btnHapus.setBackground(new java.awt.Color(0, 153, 0));
        btnHapus.setFont(new java.awt.Font("Franklin Gothic Book", 1, 12)); // NOI18N
        btnHapus.setForeground(new java.awt.Color(255, 255, 255));
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout detailBarangMasukLayout = new javax.swing.GroupLayout(detailBarangMasuk);
        detailBarangMasuk.setLayout(detailBarangMasukLayout);
        detailBarangMasukLayout.setHorizontalGroup(
            detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailBarangMasukLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailBarangMasukLayout.createSequentialGroup()
                        .addGroup(detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator7, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator10)
                                .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)))
                        .addGap(183, 183, 183))
                    .addGroup(detailBarangMasukLayout.createSequentialGroup()
                        .addComponent(btnKembali)
                        .addGroup(detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(detailBarangMasukLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addContainerGap(883, Short.MAX_VALUE))
                            .addGroup(detailBarangMasukLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(btnUbah)
                                .addGap(30, 30, 30)
                                .addComponent(btnHapus)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(detailBarangMasukLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap(932, Short.MAX_VALUE))))
        );
        detailBarangMasukLayout.setVerticalGroup(
            detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailBarangMasukLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addGroup(detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addGroup(detailBarangMasukLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUbah, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 624, Short.MAX_VALUE))
        );

        mainPanel.add(detailBarangMasuk, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1098, -1));

        add(mainPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1098, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        // TODO add your handling code here:
        tampilDataBarangMasuk();
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void detailBarangMasukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_detailBarangMasukMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_detailBarangMasukMouseClicked

    private void tblDataBarangMasukMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDataBarangMasukMouseClicked
        // TODO add your handling code here:
        int baris = tblDataBarangMasuk.getSelectedRow();
        eventTableClick();
        if (baris != -1) {
            String no = String.valueOf(tblDataBarangMasuk.getValueAt(baris, 0));
            String id = String.valueOf(tblDataBarangMasuk.getValueAt(baris, 1));
            String tgl = String.valueOf(tblDataBarangMasuk.getValueAt(baris, 2));
            String total = String.valueOf(tblDataBarangMasuk.getValueAt(baris, 3));
            
        }
    }//GEN-LAST:event_tblDataBarangMasukMouseClicked

    private void tblDetailAtasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetailAtasMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetailAtasMouseClicked

    private void tblDetailBawahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetailBawahMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetailBawahMouseClicked

    private void btnBatalTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatalTambahActionPerformed
        // TODO add your handling code here:
        tampilDataBarangMasuk();
    }//GEN-LAST:event_btnBatalTambahActionPerformed

    private void btnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSimpanActionPerformed
        // TODO add your handling code here:
        simpanBarangMasukDanDetail();
    }//GEN-LAST:event_btnSimpanActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        tampilTambahBarangMasuk();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void tCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tCariActionPerformed

    private void tNoMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tNoMasukActionPerformed
        // TODO add your handling code here:
        Model_BarangMasuk bm = new Model_BarangMasuk();
    }//GEN-LAST:event_tNoMasukActionPerformed

    private void tKodeBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tKodeBarangActionPerformed
        // TODO add your handling code here:
        try {
            String sql = "SELECT nama_barang, harga FROM barang WHERE kode_barang = ?"; 
            Connection con = koneksi.configDB();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, tKodeBarang.getText());
            ResultSet rs= ps.executeQuery();
            if (rs.next()){
                cNamaBarang.setSelectedItem(rs.getString("nama_barang"));
                tHarga.setText(rs.getString("harga"));
            }
        } catch (Exception e) {
            reset();
        }
    }//GEN-LAST:event_tKodeBarangActionPerformed

    private void tJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJumlahActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJumlahActionPerformed

    private void tSubtotalKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tSubtotalKeyReleased
        // TODO add your handling code here:
        tSubtotal.setEnabled(false);
    }//GEN-LAST:event_tSubtotalKeyReleased

    private void tIdUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tIdUserActionPerformed
        // TODO add your handling code here:
        //Model_User usr = new Model_User();
    }//GEN-LAST:event_tIdUserActionPerformed

    private void tTotalMasukActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tTotalMasukActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tTotalMasukActionPerformed

    private void tHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tHargaActionPerformed

    private void tCariKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tCariKeyReleased
        // TODO add your handling code here:
        cariData();
    }//GEN-LAST:event_tCariKeyReleased

    private void btnUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUbahActionPerformed
        // TODO add your handling code here:
        eventTableClick();
        tampilTambahBarangMasuk();
        
        //isi form
        tNoMasuk.setText(noEdit);
        tNoMasuk.setEditable(false);
        tIdUser.setText(idEdit);
        tIdUser.setEditable(false);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        java.util.Date dt = null;
        
        try {
            dt = sdf.parse(tglEdit);
        } catch (ParseException e) {
            Logger.getLogger(barang_Masuk.class.getName()).log(Level.SEVERE, null, e);
        }
        jcTanggal.setDate(dt);
        tTotalMasuk.setText(totalEdit);
    }//GEN-LAST:event_btnUbahActionPerformed

    private void cNamaBarangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cNamaBarangItemStateChanged
        // TODO add your handling code here:
        if (evt.getStateChange() != ItemEvent.SELECTED) {
           hitungSubtotal();
        }
        if (cNamaBarang.getSelectedItem() == null) {
            return;
            }
  
        try {
            String sql = "SELECT kode_barang, harga FROM barang WHERE nama_barang = ?"; 
            Connection con = koneksi.configDB();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, cNamaBarang.getSelectedItem().toString());
            ResultSet rs= ps.executeQuery();
            if (rs.next()){
                tKodeBarang.setText(rs.getString("kode_barang"));
                tHarga.setText(rs.getString("harga"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            reset();
        }
    }//GEN-LAST:event_cNamaBarangItemStateChanged

    private void tJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tJumlahKeyReleased
        // TODO add your handling code here:
        hitungSubtotal();
    }//GEN-LAST:event_tJumlahKeyReleased

    private void tHargaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tHargaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tHargaKeyReleased

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
         Model_BarangMasuk bm = new Model_BarangMasuk();
        bm.setNo_masuk(tNoMasuk.getText());
        bm.hapusBarangMasuk();
        tampilDataBarangMasuk();
        reset();
    }//GEN-LAST:event_btnHapusActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatalTambah;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnSimpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JButton btnUbah;
    private javax.swing.JComboBox<String> cNamaBarang;
    private javax.swing.JPanel dataBarangMasuk;
    private javax.swing.JPanel detailBarangMasuk;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTable jTable1;
    private com.toedter.calendar.JDateChooser jcTanggal;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tHarga;
    private javax.swing.JTextField tIdUser;
    private javax.swing.JTextField tJumlah;
    private javax.swing.JTextField tKodeBarang;
    private javax.swing.JTextField tNoMasuk;
    private javax.swing.JTextField tSubtotal;
    private javax.swing.JTextField tTotalMasuk;
    private javax.swing.JPanel tambahBarangMasuk;
    private javax.swing.JTable tblDataBarangMasuk;
    private javax.swing.JTable tblDetail;
    private javax.swing.JTable tblDetailAtas;
    private javax.swing.JTable tblDetailBawah;
    // End of variables declaration//GEN-END:variables

}
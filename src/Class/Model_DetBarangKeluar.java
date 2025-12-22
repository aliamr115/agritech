/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;
import Class.koneksi;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * Model untuk Detail Barang Keluar
 * @author user
 */
public class Model_DetBarangKeluar extends koneksi{
    private String no_keluar, kode_barang;
    private int jml_keluar;
    private BigDecimal subtotal_keluar; //DB decimal, BigDecimal cocok untuk stok, harga
    private final Connection koneksi; //FINAL = membuat koneksi 1 kali, hanya bisa diberi nilai 1 kali
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public Model_DetBarangKeluar() { //konstruktor
       koneksi = super.configDB();
    }

    // ==================== GETTER & SETTER ====================
    
    public String getNo_keluar() {
        return no_keluar;
    }

    public void setNo_keluar(String no_keluar) {
        this.no_keluar = no_keluar;
    }

    public String getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public int getJml_keluar() {
        return jml_keluar;
    }

    public void setJml_keluar(int jml_keluar) {
        this.jml_keluar = jml_keluar;
    }

    public BigDecimal getSubtotal_keluar() {
        return subtotal_keluar;
    }

    public void setSubtotal_keluar(BigDecimal subtotal_keluar) {
        this.subtotal_keluar = subtotal_keluar;
    }
   
    // ==================== CRUD METHODS ====================
    
    /**
     * Menambah detail barang keluar ke database
     */
    public void TambahDetailBK(){
        query = "INSERT INTO detail_barangkeluar VALUES(?,?,?,?)";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, no_keluar);
            ps.setString(2, kode_barang);
            ps.setInt(3, jml_keluar);
            ps.setBigDecimal(4, subtotal_keluar);
            
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Detail Barang Keluar berhasil Ditambah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditambah: " + e.getMessage());
        }   
    }
    
    /**
     * Mengubah detail barang keluar di database
     */
    public void UbahDetailBK(){
        query = "UPDATE detail_barangkeluar SET kode_barang=?, jml_keluar=?, subtotal_keluar=? WHERE no_keluar=?";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, kode_barang);
            ps.setInt(2, jml_keluar);
            ps.setBigDecimal(3, subtotal_keluar);
            ps.setString(4, no_keluar);
            
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Detail Barang Keluar berhasil Diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah: " + e.getMessage());
        }
    }
    
    /**
     * Menghapus detail barang keluar dari database
     */
    public void HapusDetailBK(){
        query = "DELETE FROM detail_barangkeluar WHERE no_keluar = ?";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, no_keluar);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Detail Barang Keluar berhasil Di Hapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Di Hapus: " + e.getMessage());
        }
    }
    
    /**
     * Menampilkan detail barang keluar berdasarkan no_keluar
     * @param noKeluar nomor transaksi barang keluar
     * @return ResultSet berisi data detail barang keluar
     */
    public ResultSet TampilDetailBK(String noKeluar){
        try {
            query = "SELECT * FROM detail_barangkeluar WHERE no_keluar=?";
            Connection conn = super.configDB();
            
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, noKeluar);
            
            return ps.executeQuery();
           
        } catch (SQLException e) {
            System.out.println("Error TampilDetailBK: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Menampilkan semua detail barang keluar
     * @return ResultSet berisi semua data detail barang keluar
     */
    public ResultSet TampilSemuaDetailBK(){
        try {
            query = "SELECT * FROM detail_barangkeluar";
            Connection conn = super.configDB();
            
            Statement st = conn.createStatement();
            return st.executeQuery(query);
           
        } catch (SQLException e) {
            System.out.println("Error TampilSemuaDetailBK: " + e.getMessage());
            return null;
        }
    }
}
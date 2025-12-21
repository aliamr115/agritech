  /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import Class.koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
/**
 *
 * @author user
 */
public class Model_JenisBarang extends koneksi{
    private String kode_jenis, nama_jenis; 
     private final Connection koneksi; //FINAL = membuat koneksi 1 kali, hanya bisa diberi nilai 1 kali
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query; //yang akan digunakan untuk mengimport query SQL nya, tambah, hapus, ubah

    public Model_JenisBarang() {
        koneksi = super.configDB();
    }
     

    public String getKode_jenis() {
        return kode_jenis;
    }

    public void setKode_jenis(String kode_jenis) {
        this.kode_jenis = kode_jenis;
    }

    public String getNama_jenis() {
        return nama_jenis;
    }

    public void setNama_jenis(String nama_jenis) {
        this.nama_jenis = nama_jenis;
    }
    
    public void TambahJenis(){
        query = "INSERT INTO jenisbarang VALUES(?,?)";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, kode_jenis);
            ps.setString(2, nama_jenis);
            
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null,"Data Berhasil Ditambah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Data Gagal Ditambah");
        }   
    }
    
    public boolean UbahJenis(){
        
        try {
            String sql = "UPDATE jenisbarang SET nama_jenis=? WHERE kode_jenis=?";
            PreparedStatement ps = koneksi.prepareStatement(sql);
           
            ps.setString(1, nama_jenis);
            ps.setString(2, kode_jenis);  
            ps.executeUpdate();
            ps.close();
            
            JOptionPane.showMessageDialog(null,"Data Berhasil Diubah");
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Data Gagal Diubah");
            return false;
        }
    }
    
    public void HapusJenis(){
         query = "DELETE FROM jenisbarang WHERE kode_jenis = ?";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, kode_jenis);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Data Gagal Dihapus");
        }
    }
    
    public ResultSet TampilJenis(){
        query = "SELECT * FROM jenisbarang";
        
         try {
            st = koneksi.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Data Gagal Ditampilkan");
        }
        return rs;
    }
    
    public ResultSet autoKodeJenis() {
    try {
        String sql = "SELECT MAX(kode_jenis) AS kode_jenis FROM jenisbarang";
        st = koneksi.createStatement();
        rs = st.executeQuery(sql);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal generate kode: " + e.getMessage());
    }
    return rs;
}
    public ResultSet ComboJenis() {
        try {
            query = "SELECT nama_jenis FROM jenisbarang";
            
            st = koneksi.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Eror ComboJenis : " + sQLException);
        }
        return rs;
    }
    
    public ResultSet konversi() {
        try {
            query = "SELECT kode_jenis FROM jenisbarang WHERE nama_jenis = ?";

            ps = koneksi.prepareStatement(query);
            ps.setString(1, nama_jenis);
            rs = ps.executeQuery();
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Eror Konversi : " + sQLException.getMessage());
        }
        return rs;
    }
    
    public ResultSet NamaJenis(String nama) {
    query = "SELECT kode_jenis, nama_jenis FROM jenisbarang WHERE nama_jenis = ?";
    try {
        ps = koneksi.prepareStatement(query);
        ps.setString(1, nama);
        rs = ps.executeQuery();
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error NamaJenis aaaaaa : " + e.getMessage());
    }
    return rs;
    }
}

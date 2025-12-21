/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;
import Class.koneksi;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;
/**
 *
 * @author user
 */
public class Model_DetBarangMasuk extends koneksi{
    private String no_masuk, kode_barang;
    private int jml_masuk;
    private BigDecimal subtotal_masuk;//desimal untuk stok harga
    private final Connection koneksi;//membuat koneksi nilai 1x
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public Model_DetBarangMasuk() {//konstruktor
        koneksi = super.configDB();
    }
    
    public String getNo_masuk() {
        return no_masuk;
    }

    public void setNo_masuk(String no_masuk) {
        this.no_masuk = no_masuk;
    }

    public String getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
    }

    public int getJml_masuk() {
        return jml_masuk;
    }

    public void setJml_keluar(int jml_masuk) {
        this.jml_masuk = jml_masuk;
    }

    public BigDecimal getSubtotal_masuk() {
        return subtotal_masuk;
    }

    public void setSubtotal_masuk(BigDecimal subtotal_masuk) {
        this.subtotal_masuk = subtotal_masuk;
    }
    
    public void ubahDetail(){
        query = "UPDATE detail_barangmasuk SET kode_barang = ?, jml_masuk = ?, subtotal_masuk = ? WHERE no_masuk = ?";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, kode_barang);
            ps.setInt(2, jml_masuk);
            ps.setBigDecimal(3, subtotal_masuk);
            ps.setString(4, no_masuk);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Diubah");
        }
    }
    
    public void hapusDetail(){
        query = "DELETE FROM detail_barangmasuk WHERE no_masuk";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, no_masuk);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");
        }
    }
        
    public ResultSet tampilDetail(String noMasuk){
        try {
            query = "SELECT * FROM detail_barangmasuk WHERE no_masuk = ?";
            Connection con = super.configDB();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, noMasuk);
            
            return ps.executeQuery();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    
    public void tambahDetail(){
        query = "INSERT INTO detail_barangmasuk VALUES (?,?,?,?)";
            try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, kode_barang);
            ps.setInt(2, jml_masuk);
            ps.setBigDecimal(3, subtotal_masuk);
            ps.setString(4, no_masuk);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambah");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditambah");
        }
    }
}

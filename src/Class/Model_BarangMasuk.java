/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import Class.koneksi;
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
public class Model_BarangMasuk extends koneksi{
    private String no_masuk, tgl_masuk;
    private int total_masuk;
    //private Model_User idUser;
    private Connection cn = null;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;
    
public Model_BarangMasuk(){
    cn = super.configDB();
} 

    public String getNo_masuk() {
        return no_masuk;
    }

    public void setNo_masuk(String no_masuk) {
        this.no_masuk = no_masuk;
    }

    public String getTgl_masuk() {
        return tgl_masuk;
    }

    public void setTgl_masuk(String tgl_masuk) {
        this.tgl_masuk = tgl_masuk;
    }

     public int getTotal_masuk() {
        return total_masuk;
    }

    public void setTotal_masuk(int total_masuk) {
        this.total_masuk = total_masuk;
    }

    //public Model_User getIdUser() {
      //  return idUser;
    //}

    //public void setIdUser(Model_User idUser) {
      //  this.idUser = idUser;
    //}
    
    public void tambahBarangMasuk(){
        query = "INSERT INTO barangmasuk VALUES (?, ?, ?)";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, no_masuk);
            ps.setString(2, tgl_masuk);
            ps.setInt(3, total_masuk);
           
            ps.executeUpdate();
            ps.close();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal");
            System.out.println(e);
        }
    }
    
    public void ubahBarangMasuk(){
        try {
            query = "UPDATE barangmasuk SET tgl_masuk = ?, total_masuk  = ? WHERE no_masuk = ?";
            
            ps = cn.prepareStatement(query);
            ps.setString(1, tgl_masuk);
            ps.setInt(2, total_masuk);
            ps.setString(3, no_masuk);
 
            ps.executeUpdate();
            ps.close();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal");
        }
    }
    
    public void hapusBarangMasuk(){
        query = "DELETE FROM barangmasuk WHERE no_masuk = ?";
        try {
            ps = cn.prepareStatement(query);
            ps.setString(1, no_masuk);
            ps.executeUpdate();
            ps.close();
            
            JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal");
        }
    }

    public ResultSet tampilDataBarangMasuk() {
        query = "SELECT * FROM barangmasuk";
        cn = koneksi.configDB();
        try {
            st = cn.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Data Gagal Ditampilkan");
    }
    return rs;
    }
}
    
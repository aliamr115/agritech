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
public class Model_BarangKeluar extends koneksi{
    private String no_keluar, tgl_keluar;
    private Long total_keluar;
    private Model_User idUser;
    private final Connection koneksi;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;
    
    
    public Model_BarangKeluar() {
        koneksi = super.configDB();
    }

    public String getNo_keluar() {
        return no_keluar;
    }

    public void setNo_keluar(String no_keluar) {
        this.no_keluar = no_keluar;
    }

    public String getTgl_keluar() {
        return tgl_keluar;
    }

    public void setTgl_keluar(String tgl_keluar) {
        this.tgl_keluar = tgl_keluar;
    }

    public Long getTotal_keluar() {
        return total_keluar;
    }

    public void setTotal_keluar(Long total_keluar) {
        this.total_keluar = total_keluar;
    }

    public Model_User getIdUser() {
        return idUser;
    }

    public void setIdUser(Model_User idUser) {
        this.idUser = idUser;
    
    }
    
    public void TambahBarangKeluar() {
        query = "INSERT INTO barangkeluar VALUES(?,?);";
        try {
             ps = koneksi.prepareStatement(query);
             ps.setString(1, no_keluar);
             ps.setString(2,tgl_keluar );
             ps.setLong(3,total_keluar );
             ps.executeUpdate();
             ps.close();
             JOptionPane.showMessageDialog(null, "Berhasil");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Gagal" + e);
            }
    }
    
    public void HapusBarangKeluar() {
        query = "DELETE FROM barangkeluar WHERE no_keluar = ?";
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, no_keluar);
            ps.setString(0, tgl_keluar);
            ps.setLong(3,total_keluar );
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Berhasil");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal" + e);
        }
    }
     public void UbahBarangKeluar(){
        query = "update barangkeluar set tgl_keluar=?, total_keluar =? where no_keluar=?;";
        
        try {
            ps = koneksi.prepareStatement(query);
            ps.setString(1, no_keluar);
            ps.setString(2, tgl_keluar);
            ps.setLong(3,total_keluar );
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(null, "Berhasil");
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Gagal");
        }
     }
      public ResultSet TampilBarangKeluar(){
        query = "select * from barangkeluar";
        
        try {
            st = koneksi.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "GAGAL");
        }
        return rs;
        
        
    }
      public ResultSet autoID() {
        try {
            query = "SELECT MAX(no_keluar) AS max_id FROM barangkeluar";
            st = koneksi.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException sQLException) {
           JOptionPane.showMessageDialog(null, "gagal" +sQLException.getMessage());
        }
        return rs;
    }
}
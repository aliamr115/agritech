package Class;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.util.Date;

public class Model_BarangKeluar extends koneksi {

    private int no_keluar;
    private int idUser;
    private Date tgl_keluar;
    private String nama_barang;
    private Long total_keluar;

    private final Connection conn;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;

    public Model_BarangKeluar() {
        conn = super.configDB();
    }

    // ================= GETTER & SETTER =================

    public int getNo_keluar() {
        return no_keluar;
    }

    public void setNo_keluar(int no_keluar) {
        this.no_keluar = no_keluar;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Date getTgl_keluar() {
        return tgl_keluar;
    }

    public void setTgl_keluar(Date tgl_keluar) {
        this.tgl_keluar = tgl_keluar;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public Long getTotal_keluar() {
        return total_keluar;
    }

    public void setTotal_keluar(Long total_keluar) {
        this.total_keluar = total_keluar;
    }

    // ================= CRUD =================

    public void TambahBarangKeluar() {
        query = "INSERT INTO barangkeluar (id_user, tgl_keluar, nama_barang, total_keluar) VALUES (?, ?, ?, ?)";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, idUser);
            ps.setDate(2, new java.sql.Date(tgl_keluar.getTime())); // DATE FIX
            ps.setString(3, nama_barang);
            ps.setLong(4, total_keluar);

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Data barang keluar berhasil ditambahkan");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal insert: " + e.getMessage());
        }
    }

    public void UbahBarangKeluar() {
        query = "UPDATE barangkeluar SET tgl_keluar = ?, nama_barang = ?, total_keluar = ? WHERE no_keluar = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setDate(1, new java.sql.Date(tgl_keluar.getTime()));
            ps.setString(2, nama_barang);
            ps.setLong(3, total_keluar);
            ps.setInt(4, no_keluar);

            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Data barang keluar berhasil diubah");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal update: " + e.getMessage());
        }
    }

    public void HapusBarangKeluar() {
        query = "DELETE FROM barangkeluar WHERE no_keluar = ?";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, no_keluar);
            ps.executeUpdate();
            ps.close();

            JOptionPane.showMessageDialog(null, "Data barang keluar berhasil dihapus");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal hapus: " + e.getMessage());
        }
    }

    public ResultSet TampilBarangKeluar() {
        try {
            query = "SELECT * FROM barangkeluar ORDER BY no_keluar ASC";
            st = conn.createStatement();
            rs = st.executeQuery(query);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error tampil: " + e.getMessage());
        }
        return rs;
    }

    public ResultSet autoID() {
        try {
            query = "SELECT MAX(no_keluar) AS max_id FROM barangkeluar";
            st = conn.createStatement();
            rs = st.executeQuery(query);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal autoID: " + e.getMessage());
        }
        return rs;
    }
}
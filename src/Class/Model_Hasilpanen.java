/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;
import javax.sql.*;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
/**
 *
 * @author P15s
 */
public class Model_Hasilpanen {
  Connection con = koneksi.configDB();

    public double hitungPengeluaran(int idUser, String namaBarang) {
        double total = 0;
        try {
            String sql =
                "SELECT IFNULL(SUM(d.total),0) AS total " +
                "FROM barang_keluar bk " +
                "JOIN detail_barang_keluar d ON bk.id_keluar = d.id_keluar " +
                "WHERE bk.id_user = ? AND d.nama_barang = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setString(2, namaBarang);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (Exception e) {
            System.out.println("Error hitung pengeluaran: " + e);
        }
        return total;
    }

    public void simpan(int idUser, String namaBarang, double totalPanen) {

        double pengeluaran = hitungPengeluaran(idUser, namaBarang);
        double panenBersih = totalPanen - pengeluaran;

        try {
            String sql =
                "INSERT INTO hasil_panen " +
                "(id_user, nama_barang, total_panen, total_pengeluaran_barang, panen_bersih) " +
                "VALUES (?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUser);
            ps.setString(2, namaBarang);
            ps.setDouble(3, totalPanen);
            ps.setDouble(4, pengeluaran);
            ps.setDouble(5, panenBersih);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Error simpan panen: " + e);
        }
    }
}

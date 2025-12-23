/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author user
 */
public class Model_Barang extends koneksi{ //Model : digunakan untuk mendeklarasikan atribut" apa saja yang ada diDB
    private String  nama_barang, satuan;
    private int harga, stok, kode_barang, kode_jenis;
    private final Connection conn;
    private PreparedStatement ps;
    private Statement st;
    private ResultSet rs;
    private String query;
    

    public Model_Barang() {
        conn = super.configDB();
    }
    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(int kode_barang) {
        this.kode_barang = kode_barang;
    }

    public int getKode_jenis() {
        return kode_jenis;
    }

    public void setKode_jenis(int kode_jenis) {
        this.kode_jenis = kode_jenis;
    }
   
    
    public void tambahBarang() {
        try {
            query = "INSERT INTO barang (kode_jenis, nama_barang, satuan, harga, stok) "
                    + "VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(query);
//            ps.setInt(1, kode_barang);
            ps.setInt(1, kode_jenis);
            ps.setString(2, nama_barang);
            ps.setString(3, satuan);
            ps.setInt(4, harga);
            ps.setInt(5, stok);
            
            ps.executeUpdate();
            ps.close();
            
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error tambah: " + sQLException.getMessage());
        }
    }
    
    public void ubahBarang() {
        try {
            query = "UPDATE barang SET kode_jenis=?, nama_barang=?, satuan=?, harga=?, stok=? "
                    + "WHERE kode_barang=?";
            
            ps = conn.prepareStatement(query);
            ps.setInt(1, kode_jenis);
            ps.setString(2, nama_barang);
            ps.setString(3, satuan);
            ps.setInt(4, harga);
            ps.setInt(5, stok);
            ps.setInt(6, kode_barang);
            
            ps.executeUpdate();
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Error update: " + sQLException.getMessage());
        }
    }
    
    public void hapusBarang() {
        try {
         query = "DELETE FROM barang WHERE kode_barang = ?";
           
            ps = conn.prepareStatement(query);
            ps.setInt(1, kode_barang);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "DaTA BERASIL DIHAPUS!");
            
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "DaTA GAGAL DIHAPUS: " + sQLException.getMessage());
           
    }
    }
    
    public ResultSet TampilBarang() {
        query =  "SELECT b.kode_barang, b.kode_jenis, j.nama_jenis, " +
         "b.nama_barang, b.satuan, b.harga, b.stok " +
         "FROM barang b " +
         "JOIN jenisbarang j ON b.kode_jenis = j.kode_jenis " +
         "ORDER BY b.kode_barang ASC";
        
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Data gagal di tampilkan : " + sQLException.getMessage());
        }
        return rs;
    }
    
    public  ResultSet DetailBarang(int id) {
        query = "SELECT * FROM barang WHERE kode_barang = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "Eror : " + sQLException.getMessage());
        }
        return rs;
    }
    
    public ResultSet cariJenis(String key) {
        query = "SELECT * FROM barang WHERE nama_barang LIKE ? "
                + "OR satuan LIKE ? "
                + "OR CAST(stok AS CHAR) LIKE ? "
                + "OR CAST(harga AS CHAR) LIKE ? "
                + "OR kode_barang = ?";
        
        try {
            ps = conn.prepareStatement(query);
            key = "%" + key + "%";
            
            ps.setString(1, key); // nama barang
            ps.setString(2, key); // satuan
            ps.setString(3, key); // stok
            ps.setString(4, key); // harga
            ps.setString(5, key); // kode barang
            
            rs = ps.executeQuery();

        } catch (SQLException sQLException) {
            JOptionPane.showMessageDialog(null, "gagal Cari data : " + sQLException.getMessage());
        }
        return rs;
    }
    public ResultSet loadNamaBarang() {
    try {
        query = "SELECT kode_barang, nama_barang FROM barang ORDER BY nama_barang ASC";
        ps = conn.prepareStatement(query);
        rs = ps.executeQuery();
        return rs;

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal load nama barang: " + e.getMessage());
        return null;
    }
}

    
}


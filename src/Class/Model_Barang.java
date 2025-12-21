/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;

/**
 *
 * @author user
 */
public class Model_Barang extends koneksi{ //Model : digunakan untuk mendeklarasikan atribut" apa saja yang ada diDB
    private String kode_barang,  nama_barang, satuan, jenis_barang;
    private int harga, stok;
    
    private final Connection koneksi; 
    private PreparedStatement ps;
    private ResultSet rs;
    private String query;
    

    public Model_Barang() { //konstuktor
        koneksi = super.configDB();
    }

    public String getKode_barang() {
        return kode_barang;
    }

    public void setKode_barang(String kode_barang) {
        this.kode_barang = kode_barang;
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

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public String getJenis_barang() {
        return jenis_barang;
    }

    public void setJenis_barang(String jenis_barang) {
        this.jenis_barang = jenis_barang;
    }

   
    
    public void tambahBarang() {
        try {
            query = "INSERT INTO barang (kode_barang, kode_jenis, nama_barang, satuan, harga, stok) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            ps = koneksi.prepareStatement(query);
            ps.setString(1, kode_barang);
            ps.setString(2, jenis_barang);
            ps.setString(3, nama_barang);
            ps.setString(4, satuan);
            ps.setInt(5, harga);
            ps.setInt(6, stok);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error tambah: " + e.getMessage());
        }
    }
    
    public void ubahBarang() {
        try {
            query = "UPDATE barang SET kode_jenis=?, nama_barang=?, satuan=?, harga=?, stok=? "
                    + "WHERE kode_barang=?";
            
            ps = koneksi.prepareStatement(query);
            ps.setString(1, jenis_barang);
            ps.setString(2, nama_barang);
            ps.setString(3, satuan);
            ps.setInt(4, harga);
            ps.setInt(5, stok);
            ps.setString(6, kode_barang);
            
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Berhasil Diubah!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error update: " + e.getMessage());
        }
    }
    
    public void hapusBarang() {
        try {
         query = "DELETE FROM barang WHERE kode_barang = ?";
           
            ps = koneksi.prepareStatement(query);
            ps.setString(1, kode_barang);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "DaTA BERASIL DIHAPUS!");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DaTA GAGAL DIHAPUS: " + e.getMessage());
           
    }
    }
}


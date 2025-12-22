/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author user
 */
public class Model_HasilPanen {

    private String id_panen, tgl_panen;
    private double jml_panen, total_pengeluaran_barang, panen_bersih;
    private Model_User idUser;
    
    public String getId_panen() {
        return id_panen;
    }

    public void setId_panen(String id_panen) {
        this.id_panen = id_panen;
    }

    public String getTgl_panen() {
        return tgl_panen;
    }

    public void setTgl_panen(String tgl_panen) {
        this.tgl_panen = tgl_panen;
    }

    public double getJml_panen() {
        return jml_panen;
    }

    public void setJml_panen(double jml_panen) {
        this.jml_panen = jml_panen;
    }

    public double getTotal_pengeluaran_barang() {
        return total_pengeluaran_barang;
    }

    public void setTotal_pengeluaran_barang(double total_pengeluaran_barang) {
        this.total_pengeluaran_barang = total_pengeluaran_barang;
    }

    public double getPanen_bersih() {
        return panen_bersih;
    }

    public void setPanen_bersih(double panen_bersih) {
        this.panen_bersih = panen_bersih;
    }

    public Model_User getIdUser() {
        return idUser;
    }

    public void setIdUser(Model_User idUser) {
        this.idUser = idUser;
    }
    
}

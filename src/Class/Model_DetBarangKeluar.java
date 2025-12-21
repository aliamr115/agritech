/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Class;

/**
 *
 * @author user
 */
public class Model_DetBarangKeluar {

    private Model_BarangKeluar bk; 
    private Model_Barang brg;
    private int jml_keluar;
    private double subtotal_keluar;
    
    public Model_BarangKeluar getBk() {
        return bk;
    }

    public void setBk(Model_BarangKeluar bk) {
        this.bk = bk;
    }

    public Model_Barang getBrg() {
        return brg;
    }

    public void setBrg(Model_Barang brg) {
        this.brg = brg;
    }

    public int getJml_keluar() {
        return jml_keluar;
    }

    public void setJml_keluar(int jml_keluar) {
        this.jml_keluar = jml_keluar;
    }

    public double getSubtotal_keluar() {
        return subtotal_keluar;
    }

    public void setSubtotal_keluar(double subtotal_keluar) {
        this.subtotal_keluar = subtotal_keluar;
    }
}

package app.model;

// Bu satırı dosyanın en üstüne ekle
import java.io.Serializable;

// Sınıf tanımını "implements Serializable" ekleyerek güncelle
public class Student implements Serializable {

    // İyi bir pratik olarak bunu da eklemek önerilir.
    private static final long serialVersionUID = 1L;

    public String ogrenciNo;
    public String adSoyad;
    public String bolum;

    public Student(String ogrenciNo, String adSoyad, String bolum) {
        this.ogrenciNo = ogrenciNo;
        this.adSoyad = adSoyad;
        this.bolum = bolum;
    }
}
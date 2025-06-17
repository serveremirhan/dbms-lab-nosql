package app.store;

import app.model.Student;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadLocalRandom;

public class RedisStore {

    private static Jedis jedis;
    private static Gson gson = new Gson();

    public static void init() {
        // localhost'taki Redis sunucusuna bağlan (varsayılan port 6379)
        jedis = new Jedis("redis://localhost:6379");

        // Veritabanının boş olup olmadığını kontrol et
        if (jedis.dbSize() > 0) {
            System.out.println("Redis zaten dolu, veri eklenmeyecek.");
            return;
        }

        System.out.println("Redis'e 10,000 rastgele öğrenci kaydı ekleniyor...");
        String[] departments = {"Bilgisayar Mühendisliği", "Elektrik Mühendisliği", "Makine Mühendisliği", "Tıp", "Hukuk"};
        String[] names = {"Kerem", "Sude", "Mert", "Gizem", "Ozan", "Pelin", "Barış", "Aslı"};
        String[] surnames = {"Korkmaz", "Eren", "Polat", "Keskin", "Yıldız"};

        for (int i = 0; i < 10000; i++) {
            String studentNo = "2025" + String.format("%06d", i);
            String randomName = names[ThreadLocalRandom.current().nextInt(names.length)];
            String randomSurname = surnames[ThreadLocalRandom.current().nextInt(surnames.length)];
            String randomDepartment = departments[ThreadLocalRandom.current().nextInt(departments.length)];

            Student student = new Student(studentNo, randomName + " " + randomSurname, randomDepartment);
            
            // Student nesnesini JSON string'ine çevir
            String studentJson = gson.toJson(student);
            
            // Redis'e ekle. Anahtar: "student:12345", Değer: "{...}" (JSON)
            jedis.set("student:" + studentNo, studentJson);
        }
        System.out.println("Redis'e veri ekleme tamamlandı.");
    }

    public static Student get(String studentNo) {
        // Redis'ten anahtara karşılık gelen JSON string'ini al
        String studentJson = jedis.get("student:" + studentNo);

        if (studentJson != null) {
            // Eğer veri varsa, JSON'dan tekrar Student nesnesine çevir
            return gson.fromJson(studentJson, Student.class);
        } else {
            return null;
        }
    }
}
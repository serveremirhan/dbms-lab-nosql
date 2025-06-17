package app;

import static spark.Spark.*;
import com.google.gson.Gson;

// Gerekli tüm store sınıflarını import ediyoruz
import app.store.MongoStore;
import app.store.HazelcastStore;
import app.store.RedisStore; // YENİ EKLENDİ

import app.model.Student;

public class Main {
    public static void main(String[] args) {

        port(8081);
        Gson gson = new Gson();

        // 1. MongoDB'yi hazırla
        System.out.println("MongoDB veritabanı hazırlanıyor...");
        MongoStore.init();
        System.out.println("MongoDB hazır.");
        
        // 2. Hazelcast'i hazırla
        System.out.println("Hazelcast veritabanı hazırlanıyor...");
        HazelcastStore.init();
        System.out.println("Hazelcast hazır.");

        // 3. YENİ EKLENEN KISIM: Redis'i hazırla
        System.out.println("Redis veritabanı hazırlanıyor...");
        RedisStore.init();
        System.out.println("Redis hazır.");


        // -------- MongoDB ENDPOINT'İ --------
        get("/nosql-lab-mon/:param", (req, res) -> {
            res.type("application/json");
            String param = req.params(":param");
            String studentId = null;

            if (param != null && param.startsWith("student_no=")) {
                studentId = param.substring("student_no=".length());
            }

            if (studentId == null || studentId.isEmpty()) {
                res.status(400);
                return "{\"error\": \"Gecersiz parametre formati. '.../student_no=XXXX' seklinde olmali.\"}";
            }
            
            Student student = MongoStore.get(studentId);

            if (student != null) {
                return gson.toJson(student);
            } else {
                res.status(404);
                return "{\"error\": \"Ogrenci bulunamadi: " + studentId + "\"}";
            }
        });


        // -------- Hazelcast ENDPOINT'İ --------
        get("/nosql-lab-hz/:param", (req, res) -> {
            res.type("application/json");
            String param = req.params(":param");
            String studentId = null;

            if (param != null && param.startsWith("student_no=")) {
                studentId = param.substring("student_no=".length());
            }

            if (studentId == null || studentId.isEmpty()) {
                res.status(400);
                return "{\"error\": \"Gecersiz parametre formati. '.../student_no=XXXX' seklinde olmali.\"}";
            }

            Student student = HazelcastStore.get(studentId);

            if (student != null) {
                return gson.toJson(student);
            } else {
                res.status(404);
                return "{\"error\": \"Ogrenci bulunamadi: " + studentId + "\"}";
            }
        });
        
        
        // -------- YENİ EKLENEN KISIM: Redis ENDPOINT'İ --------
        get("/nosql-lab-rd/:param", (req, res) -> {
            res.type("application/json");
            String param = req.params(":param");
            String studentId = null;

            if (param != null && param.startsWith("student_no=")) {
                studentId = param.substring("student_no=".length());
            }

            if (studentId == null || studentId.isEmpty()) {
                res.status(400);
                return "{\"error\": \"Gecersiz parametre formati. '.../student_no=XXXX' seklinde olmali.\"}";
            }

            // TEK FARK: RedisStore'dan veri çekiyoruz
            Student student = RedisStore.get(studentId);

            if (student != null) {
                return gson.toJson(student);
            } else {
                res.status(404);
                return "{\"error\": \"Ogrenci bulunamadi: " + studentId + "\"}";
            }
        });


        System.out.println("Sunucu http://localhost:8080 adresinde başlatıldı. Tüm endpointler aktif.");
    }
}
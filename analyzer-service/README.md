# Troubleshooting Project Spring Boot: dari "JVM target 5" sampai Jalan dengan H2

Catatan ini merangkum dua error yang muncul saat membuka project Spring Boot baru dari Spring Initializr di IntelliJ IDEA, lengkap dengan cara menelusuri penyebab dan solusinya.

**Inti pelajaran:** pesan error yang paling terlihat belum tentu menunjukkan penyebab yang sebenarnya.

## Ringkasan

| # | Gejala | Penyebab | Solusi |
|---|--------|----------|--------|
| 1 | `Cannot compile module 'demo' configured for JVM target 5` | IntelliJ 2026.1 gagal mengimpor project dengan Maven 3.10.0 | Turunkan Maven Wrapper ke 3.9.16, lalu reload Maven |
| 2 | `zsh: no such file or directory: ./mvnw` | Perintah dijalankan dari folder yang salah | Masuk ke folder `demo` dulu |
| 3 | `Failed to determine a suitable driver class` | Ada JPA, tetapi belum ada database | Tambahkan dependency H2 |

## Lingkungan yang Dipakai

| Komponen | Versi |
|----------|-------|
| IntelliJ IDEA | 2026.1 (build 261.22158.277) |
| JDK | BellSoft Liberica 25 |
| Spring Boot | 4.1.2 |
| Spring Cloud | 2025.1.4 |
| Maven Wrapper | 3.10.0, diganti menjadi 3.9.16 |
| H2 Database | 2.4.240 (versi diatur oleh Spring Boot) |

## Masalah 1: IntelliJ Meminta JVM Target 5

### Gejala

Saat build di IntelliJ, muncul pesan berikut:

```text
java: Please add JDK 8 or later to Project Structure | Platform Settings | SDKs.
The added JDK should support cross-compilation for JVM target 5.

java: Cannot compile module 'demo' configured for JVM target 5:
the specified fallback SDK version 17 does not support the required jvm target 5.
```

Muncul juga error yang terlihat tidak berhubungan:

```text
1 error
======================
Full classname legend:
======================
ArtifactHandlerManager:   "org.apache.maven.artifact.handler.manager.ArtifactHandlerManager"
DefaultArtifactFactory:   "org.apache.maven.artifact.factory.DefaultArtifactFactory"
DefaultPlexusContainer$1: "org.codehaus.plexus.DefaultPlexusContainer$1"
```

### Jebakannya

Pesan itu menyuruh kita menambahkan JDK, padahal JDK bukan masalahnya:

- `pom.xml` sudah berisi `<java.version>25</java.version>`.
- JDK 25 sudah terpasang dan sudah dipilih sebagai SDK project.
- Java 5 sudah sangat lama dan tidak mungkin berasal dari project Spring Boot 4.

Jadi, memasang JDK lama atau menurunkan versi Java tidak akan menyelesaikan masalah ini.

### Cara Menelusuri

1. **Cek konfigurasi project.** `pom.xml` sudah benar, jadi angka 5 tidak berasal dari sana.
2. **Cari petunjuk yang janggal.** Ada folder bernama persis `${project.build.directory}` di dalam `demo`. Artinya, properti Maven tidak pernah diterjemahkan oleh IntelliJ.
3. **Baca log IntelliJ.** Di macOS, lokasinya `~/Library/Logs/JetBrains/IntelliJIdea2026.1/idea.log`. Di log ada error lengkapnya:

   ```text
   [Guice/MissingImplementation]: No implementation for ArtifactHandlerManager was bound.
   at org.jetbrains.idea.maven.server.Maven3XServerEmbedder.customizeComponents(...)
   ```

4. **Bandingkan dengan terminal.** Perintah `./mvnw -q -o validate` berhasil dengan Maven 3.10.0. Jadi, Maven dan `pom.xml` tidak rusak. Yang gagal adalah Maven server milik IntelliJ.

### Penyebab

```text
IntelliJ 2026.1 memakai Maven dari wrapper (3.10.0)
        ↓
Maven server IntelliJ gagal menemukan ArtifactHandlerManager
        ↓
Impor project gagal, pom.xml tidak terbaca
        ↓
Pengaturan java.version=25 tidak sampai ke IntelliJ
        ↓
Modul memakai nilai bawaan: JVM target 5
```

Penyebab teknis yang paling mungkin: Maven 3.10.0 mendaftarkan komponen ini lewat Sisu (`META-INF/sisu/javax.inject.Named`), sedangkan Maven 3.9.16 masih mendaftarkannya lewat `META-INF/plexus/components.xml`. Kesimpulan ini didapat dari membandingkan isi file jar kedua versi, bukan dari membaca source code IntelliJ.

### Solusi

**1. Ubah versi Maven Wrapper**

Di file `demo/.mvn/wrapper/maven-wrapper.properties`, ubah `distributionUrl` dari:

```properties
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.10.0/apache-maven-3.10.0-bin.zip
```

menjadi:

```properties
distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.16/apache-maven-3.9.16-bin.zip
```

**2. Hapus folder sisa impor yang gagal**

Tanda kutip tunggal wajib dipakai supaya shell tidak menerjemahkan `$`:

```bash
cd demo
rm -r './${project.build.directory}'
```

**3. Reload di IntelliJ**

Buka tool window **Maven**, lalu klik **Reload All Maven Projects**. Setelah itu jalankan **Build > Rebuild Project**.

Jika masih muncul target 5, jalankan **File > Invalidate Caches** satu kali.

### Cek Hasilnya

```bash
./mvnw -v
```

Hasilnya harus menunjukkan `Apache Maven 3.9.16`.

> **Catatan:** versi IntelliJ yang lebih baru mungkin sudah mendukung Maven 3.10.0. Ini belum diuji. Jika IntelliJ kamu berbeda versi, kamu mungkin tidak mengalami masalah ini.

## Masalah 2: `./mvnw` Tidak Ditemukan

### Gejala

```text
➜  trainingSpringBoot19Simulasi git:(master) ✗ ./mvnw test
zsh: no such file or directory: ./mvnw
```

### Penyebab

File `mvnw` ada di folder `demo`, bukan di root repository.

### Solusi

```bash
cd demo
./mvnw test
```

**Tip:** `./` berarti "di folder saat ini". Gunakan `pwd` dan `ls` untuk memastikan posisi kamu.

## Masalah 3: Test Gagal karena Belum Ada Database

### Gejala

Build sudah berhasil, tetapi `./mvnw test` gagal. Log-nya sangat panjang, jadi cari bagian ini:

```text
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class
```

### Cara Membaca Stack Trace

Saat log panjang, fokus pada dua tempat:

1. Blok `APPLICATION FAILED TO START`, karena Spring Boot menjelaskan masalahnya di sana.
2. Baris `Caused by:` yang paling bawah, karena biasanya itulah akar masalahnya.

Baris yang menyebut `entityManagerFactory` hanyalah efek lanjutan dari error tersebut.

### Penyebab

```text
spring-boot-starter-data-jpa ada di pom.xml
        ↓
Spring Boot menyiapkan koneksi database saat aplikasi start
        ↓
Tidak ada spring.datasource.url dan tidak ada driver database
        ↓
Aplikasi gagal start
```

Test `contextLoads` memang menyalakan seluruh aplikasi. Karena itu, test gagal walaupun project belum punya kode JPA. Log juga menunjukkan `Found 0 JPA repository interfaces`.

### Solusi: Tambahkan H2

H2 adalah database in-memory yang berjalan di dalam aplikasi, jadi tidak perlu instalasi server. Tambahkan ke dalam `<dependencies>` di `pom.xml`:

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Beberapa hal yang perlu diperhatikan:

- `<version>` tidak perlu ditulis, karena sudah diatur oleh Spring Boot.
- `runtime` berarti H2 dipakai saat aplikasi berjalan, bukan saat kode dikompilasi.
- `application.properties` tidak perlu diubah. Spring Boot otomatis membuat database saat menemukan H2.

Setelah mengubah `pom.xml`, klik **Reload All Maven Projects** di IntelliJ.

### Cek Hasilnya

Jalankan test:

```bash
./mvnw test
```

Hasil yang diharapkan:

```text
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Di log juga akan terlihat aplikasi tersambung ke H2:

```text
HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:...
Database driver: H2 JDBC Driver
Database version: 2.4.240
```

Lalu jalankan aplikasinya:

```bash
./mvnw spring-boot:run
```

Hasil yang diharapkan:

```text
Tomcat started on port 8080 (http) with context path '/'
Started DemoApplication in ... seconds
```

Tekan `Ctrl + C` untuk menghentikan aplikasi.

> **`http://localhost:8080/` menampilkan 404?** Itu normal. Aplikasi sudah berjalan, tetapi belum ada controller yang menangani URL tersebut.

### Keterbatasan H2

Data H2 in-memory **hilang setiap aplikasi di-restart**, dan nama databasenya dibuat acak setiap kali start. H2 cocok untuk belajar dan testing. Untuk data yang harus tersimpan, pakai database seperti PostgreSQL.

## Pelajaran untuk Peserta

1. **Jangan langsung percaya saran di pesan error.** "Tambahkan JDK" hanya efek samping dari impor Maven yang gagal.
2. **Cari error yang pertama terjadi.** Satu kegagalan di awal bisa memunculkan banyak error lain.
3. **Perhatikan hal yang janggal.** Target Java 5 dan folder `${project.build.directory}` adalah petunjuk penting.
4. **Bandingkan IDE dengan terminal.** Jika terminal berhasil tetapi IDE gagal, masalahnya kemungkinan ada di IDE.
5. **Baca log di tempat yang tepat.** Ini berlaku untuk `idea.log`, blok `APPLICATION FAILED TO START`, dan baris `Caused by:` terakhir.
6. **Ubah satu hal, lalu verifikasi.** Dengan begitu, kita tahu perubahan mana yang benar-benar menyelesaikan masalah.

## Daftar Perintah

Jalankan dari dalam folder `demo`:

```bash
./mvnw -v               # cek versi Maven dan Java
./mvnw test-compile     # kompilasi kode utama dan test
./mvnw test             # jalankan test
./mvnw spring-boot:run  # jalankan aplikasi
```

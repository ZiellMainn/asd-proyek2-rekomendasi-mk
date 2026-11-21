import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/** Membaca ketiga berkas input dan mengisi graph kurikulum. */
public class FileReaderUtil {

    /** Baca semua baris yang tidak kosong dari file ke dalam MyLinkedList. */
    public static MyLinkedList<String> bacaBaris(String path) throws IOException {
        MyLinkedList<String> baris = new MyLinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    baris.add(line);
                }
            }
        }
        return baris;
    }

    /**
     * LANGKAH 1: matakuliah.csv -> vertex graph.
     * Format: Kode;Nama;sks;semester;ganjil/genap (baris pertama = header, dilewati).
     */
    public static void bacaMataKuliah(String path, GraphKurikulum graph) throws IOException {
        MyLinkedList<String>.Cursor c = bacaBaris(path).cursor();
        if (c.hasNext()) c.next(); // lewati header
        while (c.hasNext()) {
            String[] k = c.next().split(";");
            if (k.length < 5) continue;
            graph.tambahMK(new MataKuliah(
                    k[0].trim(), k[1].trim(),
                    Integer.parseInt(k[2].trim()),
                    Integer.parseInt(k[3].trim()),
                    k[4].trim()));
        }
    }

    /**
     * LANGKAH 2: prasyarat.csv -> sisi graph.
     * Format: prasyarat;matakuliah, contoh "KAL;AL" berarti AL membutuhkan KAL.
     */
    public static void bacaPrasyarat(String path, GraphKurikulum graph) throws IOException {
        MyLinkedList<String>.Cursor c = bacaBaris(path).cursor();
        if (c.hasNext()) c.next(); // lewati header
        while (c.hasNext()) {
            String[] k = c.next().split(";");
            if (k.length < 2) continue;
            graph.tambahPrasyarat(k[0].trim(), k[1].trim());
        }
    }

    /**
     * LANGKAH 3: mhs_xx.txt -> tandai MK yang sudah lulus.
     * Format:
     *   semester_berikutnya=genap
     *   lulus=RPL,PDAS,...
     * @return "ganjil" atau "genap"
     */
    public static String bacaMahasiswa(String path, GraphKurikulum graph) throws IOException {
        String periode = "";
        MyLinkedList<String>.Cursor c = bacaBaris(path).cursor();
        while (c.hasNext()) {
            String line = c.next().replace("\\_", "_");
            int posisiSama = line.indexOf('=');
            if (posisiSama < 0) continue;
            String kunci = line.substring(0, posisiSama).trim().toLowerCase();
            String nilai = line.substring(posisiSama + 1).trim();

            if (kunci.equals("semester_berikutnya")) {
                periode = nilai.toLowerCase();
            } else if (kunci.equals("lulus") && !nilai.isEmpty()) {
                for (String kode : nilai.split(",")) {
                    MataKuliah mk = graph.cari(kode.trim());
                    if (mk != null) {
                        mk.lulus = true;
                    } else {
                        System.out.println("Peringatan: kode MK tidak dikenal: " + kode.trim());
                    }
                }
            }
        }
        return periode;
    }
}

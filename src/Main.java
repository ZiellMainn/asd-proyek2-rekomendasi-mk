/**
 * Proyek 2 ASD - Sistem Rekomendasi Pengambilan Mata Kuliah.
 *
 * Cara pakai:
 *   java Main matakuliah.csv prasyarat.csv mhs_xx.txt
 * Tanpa argumen, program memakai data/matakuliah.csv, data/prasyarat.csv,
 * dan data/mhs_01.txt.
 *
 * Alur program:
 *   LANGKAH 1  Baca matakuliah.csv  -> setiap MK menjadi vertex graph
 *   LANGKAH 2  Baca prasyarat.csv   -> setiap baris menjadi edge graph
 *   LANGKAH 3  Baca mhs_xx.txt      -> tandai MK yang sudah lulus
 *   LANGKAH 4  Cek setiap MK dengan SistemRekomendasi
 *   LANGKAH 5  Cetak daftar MK yang bisa dan belum bisa diambil
 */
public class Main {

    public static void main(String[] args) {
        String fileMK = "data/matakuliah.csv";
        String filePrasyarat = "data/prasyarat.csv";
        String fileMhs = "data/mhs_01.txt";

        if (args.length >= 3) {
            fileMK = args[0];
            filePrasyarat = args[1];
            fileMhs = args[2];
        } else if (args.length > 0) {
            System.out.println("Pemakaian: java Main matakuliah.csv prasyarat.csv mhs_xx.txt");
            return;
        }

        GraphKurikulum graph = new GraphKurikulum();
        String periode;
        try {
            FileReaderUtil.bacaMataKuliah(fileMK, graph);          // LANGKAH 1
            FileReaderUtil.bacaPrasyarat(filePrasyarat, graph);    // LANGKAH 2
            periode = FileReaderUtil.bacaMahasiswa(fileMhs, graph); // LANGKAH 3
        } catch (Exception e) {
            System.out.println("Gagal membaca berkas: " + e.getMessage());
            return;
        }

        if (!periode.equals("ganjil") && !periode.equals("genap")) {
            System.out.println("Isi semester_berikutnya harus 'ganjil' atau 'genap'.");
            return;
        }

        SistemRekomendasi sistem = new SistemRekomendasi(graph, periode);
        sistem.proses();                                            // LANGKAH 4
        cetak(fileMhs, sistem);                                     // LANGKAH 5
    }

    static void cetak(String fileMhs, SistemRekomendasi sistem) {
        System.out.println("File mahasiswa      : " + fileMhs);
        System.out.println("Semester berikutnya : " + sistem.getPeriode());
        System.out.println();

        MyLinkedList<MataKuliah> bisa = sistem.getBisaDiambil();
        System.out.println("Daftar MK yang bisa diambil:");
        int totalSks = 0;
        int nomor = 1;
        for (MyLinkedList<MataKuliah>.Cursor c = bisa.cursor(); c.hasNext();) {
            MataKuliah mk = c.next();
            System.out.println(nomor + ". " + mk.kode + " " + mk.nama + " " + mk.sks + " SKS");
            totalSks += mk.sks;
            nomor++;
        }
        if (bisa.isEmpty()) {
            System.out.println("(tidak ada)");
        }
        System.out.println("Total: " + totalSks + " SKS");
        System.out.println();

        MyLinkedList<String> belum = sistem.getBelumBisaDiambil();
        System.out.println("Daftar MK yang belum bisa diambil semester depan:");
        for (MyLinkedList<String>.Cursor c = belum.cursor(); c.hasNext();) {
            System.out.println(c.next());
        }
        if (belum.isEmpty()) {
            System.out.println("(tidak ada)");
        }
    }
}

/**
 * Berisi ATURAN pengambilan mata kuliah (sesuai instruksi proyek).
 *
 * Sebuah MK bisa diambil semester depan jika lolos 4 pengecekan:
 *   1. MK belum lulus.
 *   2. MK ditawarkan pada semester berikutnya (ganjil/genap).
 *   3. Semua prasyarat, langsung maupun tidak langsung, sudah lulus.
 *   4. Khusus Skripsi: semua MK wajib lain sudah lulus.
 * MK yang lolos cek 1-2 tapi gagal cek 3 atau 4 masuk daftar
 * "belum bisa diambil" beserta alasannya.
 */
public class SistemRekomendasi {

    private final GraphKurikulum graph;
    private final String periode;      // "ganjil" atau "genap"

    private final MyLinkedList<MataKuliah> bisaDiambil = new MyLinkedList<>();
    private final MyLinkedList<String> belumBisaDiambil = new MyLinkedList<>();

    public SistemRekomendasi(GraphKurikulum graph, String periode) {
        this.graph = graph;
        this.periode = periode;
    }

    /** Jalankan pengecekan untuk setiap MK di kurikulum (urut sesuai matakuliah.csv). */
    public void proses() {
        for (MyLinkedList<MataKuliah>.Cursor c = graph.getDaftarMK().cursor(); c.hasNext();) {
            MataKuliah mk = c.next();

            // Cek 1 & 2: sudah lulus atau tidak ditawarkan -> tidak perlu ditampilkan
            if (mk.lulus || !mk.ditawarkanPada(periode)) {
                continue;
            }

            String alasan = cariAlasanTidakBisa(mk);
            if (alasan == null) {
                bisaDiambil.add(mk);
            } else {
                belumBisaDiambil.add(mk.kode + ": " + alasan);
            }
        }
    }

    /** Alasan sebuah MK belum bisa diambil, atau null jika bisa diambil. */
    private String cariAlasanTidakBisa(MataKuliah mk) {
        if (mk.kode.equalsIgnoreCase("SKR")) {
            // Cek 4: aturan khusus Skripsi
            MyLinkedList<MataKuliah> sisa = graph.belumLulusSelain(mk);
            if (!sisa.isEmpty()) {
                return "semua MK wajib harus lulus dulu (kurang "
                        + sisa.size() + " MK: " + sisa.join(", ") + ")";
            }
            return null;
        }

        // Cek 3: prasyarat langsung & tidak langsung (DFS pada graph)
        MyLinkedList<MataKuliah> kurang = graph.prasyaratBelumLulus(mk);
        if (!kurang.isEmpty()) {
            return "belum lulus " + kurang.join(", ");
        }
        return null;
    }

    public MyLinkedList<MataKuliah> getBisaDiambil() {
        return bisaDiambil;
    }

    public MyLinkedList<String> getBelumBisaDiambil() {
        return belumBisaDiambil;
    }

    public String getPeriode() {
        return periode;
    }
}

/**
 * Node (vertex) pada graph kurikulum.
 * Setiap mata kuliah menyimpan daftar prasyarat langsungnya (adjacency list).
 */
public class MataKuliah {

    String kode;
    String nama;
    int sks;
    int semester;
    String periode;          // "Ganjil", "Genap", atau "Keduanya"
    boolean lulus;

    /** Edge graph: MK yang harus lulus sebelum MK ini (prasyarat langsung). */
    MyLinkedList<MataKuliah> prasyarat = new MyLinkedList<>();

    /** Penanda sudah dikunjungi saat DFS (seperti wasVisited pada materi Graph). */
    boolean wasVisited = false;

    public MataKuliah(String kode, String nama, int sks, int semester, String periode) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.periode = periode;
    }

    /** Apakah MK ditawarkan pada periode ("ganjil"/"genap") tertentu. */
    public boolean ditawarkanPada(String periodeBerikutnya) {
        return periode.equalsIgnoreCase("Keduanya")
                || periode.equalsIgnoreCase(periodeBerikutnya);
    }

    @Override
    public String toString() {
        return kode;
    }
}

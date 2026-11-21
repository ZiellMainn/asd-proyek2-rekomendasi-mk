/**
 * Graph berarah (directed graph) kurikulum, direpresentasikan dengan adjacency list.
 * Vertex = mata kuliah, edge = hubungan prasyarat.
 * Edge disimpan dari MK ke prasyaratnya, supaya pertanyaan
 * "apa saja syarat MK ini?" cukup dijawab dengan DFS dari MK tersebut.
 */
public class GraphKurikulum {

    private MyLinkedList<MataKuliah> daftarMK = new MyLinkedList<>();

    /** Tambah vertex. */
    public void tambahMK(MataKuliah mk) {
        daftarMK.add(mk);
    }

    /** Sequential search berdasarkan kode MK: O(V). */
    public MataKuliah cari(String kode) {
        for (MyLinkedList<MataKuliah>.Cursor c = daftarMK.cursor(); c.hasNext();) {
            MataKuliah mk = c.next();
            if (mk.kode.equalsIgnoreCase(kode)) {
                return mk;
            }
        }
        return null;
    }

    /** Tambah edge: kodeMK membutuhkan kodePrasyarat. */
    public void tambahPrasyarat(String kodePrasyarat, String kodeMK) {
        MataKuliah syarat = cari(kodePrasyarat);
        MataKuliah mk = cari(kodeMK);
        if (syarat == null || mk == null) {
            System.out.println("Peringatan: kode tidak dikenal pada prasyarat "
                    + kodePrasyarat + " -> " + kodeMK);
            return;
        }
        if (!mk.prasyarat.contains(syarat)) {
            mk.prasyarat.add(syarat);
        }
    }

    public MyLinkedList<MataKuliah> getDaftarMK() {
        return daftarMK;
    }

    /**
     * Kumpulkan SEMUA prasyarat (langsung maupun tidak langsung) yang belum lulus
     * dengan DFS. Hasil disimpan post-order: prasyarat paling dasar ditulis lebih dulu.
     * Contoh: KALJ -> [AL, KA] karena KA butuh AL.
     */
    public MyLinkedList<MataKuliah> prasyaratBelumLulus(MataKuliah mk) {
        MyLinkedList<MataKuliah> hasil = new MyLinkedList<>();
        mk.wasVisited = true;
        for (MyLinkedList<MataKuliah>.Cursor c = mk.prasyarat.cursor(); c.hasNext();) {
            dfs(c.next(), hasil);
        }
        resetVisited(); // seperti materi: reset flag setelah penelusuran selesai
        return hasil;
    }

    private void dfs(MataKuliah v, MyLinkedList<MataKuliah> hasil) {
        if (v.wasVisited) {
            return;              // sudah dikunjungi, jangan dicatat dua kali
        }
        v.wasVisited = true;     // tandai
        if (v.lulus) {
            return;              // sudah lulus: rantai di atasnya pasti sudah terpenuhi
        }
        for (MyLinkedList<MataKuliah>.Cursor c = v.prasyarat.cursor(); c.hasNext();) {
            dfs(c.next(), hasil); // telusuri lebih dalam (rekursif)
        }
        hasil.add(v);            // post-order
    }

    /** Kembalikan semua wasVisited ke false. */
    private void resetVisited() {
        for (MyLinkedList<MataKuliah>.Cursor c = daftarMK.cursor(); c.hasNext();) {
            c.next().wasVisited = false;
        }
    }

    /** Daftar MK selain 'kecuali' yang belum lulus (untuk aturan Skripsi). */
    public MyLinkedList<MataKuliah> belumLulusSelain(MataKuliah kecuali) {
        MyLinkedList<MataKuliah> hasil = new MyLinkedList<>();
        for (MyLinkedList<MataKuliah>.Cursor c = daftarMK.cursor(); c.hasNext();) {
            MataKuliah mk = c.next();
            if (mk != kecuali && !mk.lulus) {
                hasil.add(mk);
            }
        }
        return hasil;
    }
}

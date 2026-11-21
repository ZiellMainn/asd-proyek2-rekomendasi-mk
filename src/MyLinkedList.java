/**
 * Singly linked list generik buatan sendiri (tanpa java.util).
 * Dipakai untuk menyimpan daftar node graph, adjacency list prasyarat,
 * dan daftar hasil rekomendasi.
 */
public class MyLinkedList<T> {

    private class Node {
        T data;
        Node next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    /** Tambah elemen di akhir list: O(1) karena menyimpan tail. */
    public void add(T data) {
        Node baru = new Node(data);
        if (head == null) {
            head = baru;
            tail = baru;
        } else {
            tail.next = baru;
            tail = baru;
        }
        size++;
    }

    /** Ambil elemen ke-index: O(n). */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index " + index + ", size " + size);
        }
        Node p = head;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.data;
    }

    /** Cek apakah list memuat elemen (pakai equals): O(n). */
    public boolean contains(T data) {
        for (Node p = head; p != null; p = p.next) {
            if (p.data.equals(data)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Gabungkan isi list menjadi teks, dipisah separator. */
    public String join(String separator) {
        StringBuilder sb = new StringBuilder();
        for (Node p = head; p != null; p = p.next) {
            sb.append(p.data);
            if (p.next != null) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * Iterasi sederhana tanpa java.util.Iterator:
     * for (Cursor c = list.cursor(); c.hasNext();) { T x = c.next(); }
     */
    public Cursor cursor() {
        return new Cursor();
    }

    public class Cursor {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
    }
}

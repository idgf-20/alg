package hash.p3;

class MyClosedHashtableForInts  {
    protected int[] tagArray;
    private int[] bucketArray;
    protected int size;
    protected int capacity;
    static final int EMPTY = 0;
    static final int OCCUPIED = 1;
    static final int DELETED = 2;

    public int size() { return size; }
    public int getCapacity() { return capacity; }
    public boolean isEmpty() { return size() == 0; }

    MyClosedHashtableForInts(int aCapacity) {
        this.capacity = aCapacity;
        tagArray = new int[capacity];
        bucketArray = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            tagArray[i] = EMPTY;
            bucketArray[i] = Integer.MIN_VALUE;
        }
        size = 0;
    }

    /* Die Hashfunktion */
    public int getInitialHashIndex(int key) {
        return Math.abs(key % capacity); // Modulo-Operation für gleichmäßige Verteilung
    }

    /* sucht in der Hashtabelle nach Eintrag mit Schlüssel key und liefert
       den zugehörigen Index oder -1 zurück */
    public int searchForKey(int key) {
        int hashIndex = getInitialHashIndex(key);
        int originalIndex = hashIndex;

        while (tagArray[hashIndex] != EMPTY) {
            if (tagArray[hashIndex] == OCCUPIED && bucketArray[hashIndex] == key) {
                return hashIndex; // Schlüssel gefunden
            }
            hashIndex = (hashIndex + 1) % capacity; // Lineares Sondieren
            if (hashIndex == originalIndex) {
                break; // Hashtable wurde komplett durchlaufen
            }
        }
        return -1; // Schlüssel nicht gefunden
    }

    /* Fügt einen Schlüssel in die Hashtabelle ein */
    public void add(int key) {
        if (size == capacity) {
            throw new IllegalStateException("Hash table is full");
        }

        int hashIndex = getInitialHashIndex(key);

        while (tagArray[hashIndex] == OCCUPIED) {
            if (bucketArray[hashIndex] == key) {
                return; // Schlüssel existiert bereits, nichts tun
            }
            hashIndex = (hashIndex + 1) % capacity; // Lineares Sondieren
        }

        tagArray[hashIndex] = OCCUPIED;
        bucketArray[hashIndex] = key;
        size++;
    }

    /* Entfernt einen Schlüssel aus der Hashtabelle */
    public boolean remove(int key) {
        int hashIndex = searchForKey(key);
        if (hashIndex != -1) {
            tagArray[hashIndex] = DELETED; // Markiere den Slot als gelöscht
            bucketArray[hashIndex] = Integer.MIN_VALUE;
            size--;
            return true;
        }
        return false; // Schlüssel nicht gefunden
    }

    /* Prüft, ob ein Schlüssel in der Hashtabelle existiert */
    public boolean find(int key) {
        return searchForKey(key) != -1;
    }

    /* Druckt den Zustand der Hashtabelle */
    public void printTable() {
        for (int i = 0; i < getCapacity(); i++) {
            switch (tagArray[i]) {
                case EMPTY:
                    System.out.print("[  ]");
                    break;
                case OCCUPIED:
                    System.out.print("(");
                    System.out.print(bucketArray[i]);
                    System.out.print(")");
                    break;
                case DELETED:
                    System.out.print("{");
                    System.out.print("}");
                    break;
            }
            System.out.print(" ");
        }
        System.out.println();
    }

    public static void testRoutines() {
        // Testfälle für die Hashtabelle
        MyClosedHashtableForInts ht = new MyClosedHashtableForInts(11);
        ht.add(1);
        ht.add(2);
        ht.add(3);
        ht.add(4);
        ht.add(5);
        ht.add(6);
        ht.add(7);
        ht.add(8);
        ht.add(12);
        ht.add(15);
        ht.printTable(); // Erwartet: (1) (2) (3) (4) (5) (6) (7) (8) (12) (15) [  ]

        ht.add(16);
        ht.printTable(); // Erwartet: (16) (1) (2) (3) (4) (5) (6) (7) (8) (12) (15)

        System.out.println(ht.find(9)); // Erwartet: false
        System.out.println(ht.find(3)); // Erwartet: true

        ht.remove(3);
        ht.remove(6);
        ht.remove(8);
        ht.printTable(); // Erwartet: (16) (1) (2) {} (4) (5) {} (7) {} (12) (15)
    }

    public static void main(String[] args) {
        testRoutines();
    }
}

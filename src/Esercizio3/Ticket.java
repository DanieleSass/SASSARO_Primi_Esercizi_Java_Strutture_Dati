package Esercizio3;

public class Ticket implements  Comparable<Ticket>{
    private String id;
    private String descrizione;
    private Livello livello;
    private Long timestampArrivo;

    public Ticket(String i, String d, Livello l, Long t){
        id=i;
        descrizione=d;
        livello=l;
        timestampArrivo=t;
    }

    public Livello getLivello() {
        return livello;
    }

    @Override
    public int compareTo(Ticket o) {
        return 0;
    }
}

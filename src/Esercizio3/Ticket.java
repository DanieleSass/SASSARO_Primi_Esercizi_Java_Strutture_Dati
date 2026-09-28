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

    public Long getTimestampArrivo() {
        return timestampArrivo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getId() {
        return id;
    }

    @Override
    public int compareTo(Ticket o) {

        // output <0->this viene prima dell'oggetto passato come parametro->ha priorità + alta
        //output = 0 sono uguali
        //output > 0->this va dopo l'oggetto parametro->ha meno priorità

        //ordinal guarda la posizione
        //0 è il primo, quindi Critico, 3 è Basso pk è ultimo
        int confronto = Integer.compare(this.livello.ordinal(), o.livello.ordinal());

        //se non sono uguali
        if(confronto!=0){
            return confronto;
        }

        //se hanno lo stesso livello
        //controlla chi ha il timestamp + vecchio
        int confrontoTimestampVecchio = Long.compare(this.timestampArrivo, o.timestampArrivo);
        return confrontoTimestampVecchio;
    }
}

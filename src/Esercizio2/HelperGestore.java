package Esercizio2;

import java.lang.foreign.ValueLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

public class HelperGestore {
    private ArrayList<RichiestaHttp> storico = new ArrayList<RichiestaHttp>();
    private LinkedList<RichiestaHttp> slidingWindow = new LinkedList<RichiestaHttp>();
    private HashSet<String> sospetti = new HashSet<String>();
    private TreeSet<Long> classifica = new TreeSet<Long>();

    public void AggiungiRichiesta (RichiestaHttp ric){

        //logica che permette di restituire direttamente
        //le strutture dati così come sono
        //senza operazioni successive pk tutti i calcolo/operazioni sono eseguite qui

        //apparte il punto numero 1


        storico.add(ric);

        slidingWindow.addLast(ric);
        if (slidingWindow.size()>10){
            //rimuove la prima
            slidingWindow.removeFirst();
        }

        if(ric.getStatusCode()>=400 && ric.getStatusCode()<=599){
            sospetti.add(ric.getIp());
        }
        classifica.add(ric.getTempoRispostaMs());
    }


    //punto1
    public ArrayList<RichiestaHttp> UltimeRichieste(int n){
        ArrayList<RichiestaHttp> daReturnare = new ArrayList<>();
        for (int i = storico.size() -1; i>=0 && daReturnare.size()<n; i--) {
            if(storico.get(i).getStatusCode()>=500){
                daReturnare.add(storico.get(i));
            }
        }

        return daReturnare;

    }

    //90 percentile indica il valore per cui il 90% dei dati
    //si trova al di sotto (o uguale) e solo il 10% dei dati lo supera

    public Long CalcoloPercentile90(){
        int totElementi = classifica.size();
        // i dati devono essere per forza in ordine
        //motivo per cui si usa tree set

        //si trova elemento a indice 9/10, cioè dividendo /10 e moltiplicando *9
        //oppure moltiplicando *0.9
        //e poi si sottrae 1 perchè si tiene conto degli indici (partono da 0)
        //e non più del numero di elementi

        int indicePercentile90 =(int) Math.round(totElementi*0.9) -1;

        //primo elemento = più piccolo, la root
        Long valoreAttuale = classifica.first();

        //siccome non ha indice ma si scorre ad albero,
        //con istruzione dentro al for prende il figlio del nodo in cui siamo il cui
        //valore è più grande e lo riassegno alla variabile

        //facendo così per un numero <indice> di volte arrivo al valore percentile 90
        for (int i =0;i<indicePercentile90;i++){
            valoreAttuale=classifica.higher(valoreAttuale);
        }

        return valoreAttuale;
    }

    public String Report(){
        String daReturnare ="";
        Long calcoloPercentile = CalcoloPercentile90();

        daReturnare+="Numero Richiesta: "+ storico.size()+"\n";
        daReturnare+="Numero IP sospetti totali e distinti: "+sospetti.size()+"\n";

        daReturnare+=" Calcolo Percentile 90: "+calcoloPercentile+"\n";

        int numeroIpSospettiNellaSlidingWindow=0;
        for (RichiestaHttp r : slidingWindow){
            if(sospetti.contains(r.getIp())){
                numeroIpSospettiNellaSlidingWindow++;
            }
        }
        daReturnare+="Nelle ultime 10 richieste ci sono " + numeroIpSospettiNellaSlidingWindow+ " di IP sospetti\n";

        return daReturnare;
    }

    //metodi usati come get dal main per avere le liste su cui sono già state effettuate le operazioni
    public ArrayList<RichiestaHttp> Storico(){
        return storico;
    }
    public LinkedList<RichiestaHttp> SlidingWindow(){
        return slidingWindow;
    }
    public HashSet<String> IPSospetti(){
        return sospetti;
    }
    public TreeSet<Long> ClassificaTempi(){
        return classifica;
    }

}

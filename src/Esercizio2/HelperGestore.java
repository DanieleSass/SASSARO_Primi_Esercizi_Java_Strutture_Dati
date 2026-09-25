package Esercizio2;

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

        if(ric.getStatusCode()>400 && ric.getStatusCode()<=599){
            sospetti.add(ric.getIp());
        }
    }


    //punto1
    public ArrayList<RichiestaHttp> UltimeRichieste(int n){
        int ciclo = storico.size()-n;
        ArrayList<RichiestaHttp> daReturnare = new ArrayList<>();
        for (int i = storico.size() -1; i < ciclo; i--) {
            if(storico.get(i).getStatusCode()>=500){
                daReturnare.add(storico.get(i));
            }
        }

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

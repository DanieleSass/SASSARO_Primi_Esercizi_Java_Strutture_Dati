package Esercizio2;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.IO.print;
import static java.lang.IO.println;

public class MainEsercizio2 {
    public static void main(){
        String[] ip=
        {"192.168.1.10", "10.0.3.20", "172.16.4.40","192.168.2.10","10.0.3.30", "185.220.5.50"};

        String[] path=
        {
         "index.html", "./cartella1", "/privato", "/users/user383", "path4"
        };

        //prende il time stamp attuale e dopo lo uso per generare quello delle richiestehttp
        long timeStampAttuale = System.currentTimeMillis();

        Random random = new Random(20);

        RichiestaHttp richiesta;
        HelperGestore gestore = new HelperGestore();
        for (int i = 0; i < 40; i++) {
            String ipScelto = ip[random.nextInt(ip.length)];
            String pathScelto = path[random.nextInt(path.length)];
            int codice = random.nextInt(600);
            long tempo = random.nextInt(10, 10000);
            long timeStamp = timeStampAttuale + i*10;

            richiesta = new RichiestaHttp(ipScelto, pathScelto, codice, tempo, timeStamp);
            println(richiesta);
            gestore.AggiungiRichiesta(richiesta);
        }
        println();
        println();
        println("Richieste: " + gestore.Storico().size());

        println();

        int n=5;
        println("ultime " + n + " richieste con codice >=500");
        ArrayList<RichiestaHttp> errori = gestore.UltimeRichieste(n);
        if (errori.isEmpty()) {
            println("Nessuna richiesta con codice>= 500 trovata");
        } else {
            for (RichiestaHttp req : errori) {
                println(req);
            }
        }
        println();

        println("Sliding Window con ultimi 10 elementi");
        for (RichiestaHttp req : gestore.SlidingWindow()) {
            println(req);
        }
        println();

        println("ip sospetti con hashset");
        println("Totale ip sospettii: " + gestore.IPSospetti().size());
        for (String ipp : gestore.IPSospetti()) {
            println("- " + ipp);
        }
        println();


        println("classifica tempi con treeset");

        //potrebbe essere < del numero di richieste se impiega lo stesso tempo
        println("Numero di tempiregistrati: " + gestore.ClassificaTempi().size());
        println("Tempo di risposta al 90 Percentile: " + gestore.CalcoloPercentile90());
        println();

        println("Report finale: ");
        println(gestore.Report());
    }
}

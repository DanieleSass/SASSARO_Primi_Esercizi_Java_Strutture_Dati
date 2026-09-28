package Esercizio1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

import static java.lang.IO.print;
import static java.lang.IO.println;

public class MainEsercizio1 {
    public static void main(){

        //per il bonus
        ArrayList<LetturaSensore> lista = new ArrayList<LetturaSensore>();


        //compresi tra -127 e 128
        Integer b1 = 50;
        Integer b2 = 50;
        //fuori range della cache
        Integer b3 = 200;
        Integer b4 = 200;

        println("Test con valori a 50 (in cache):");
        println(LetturaSensore.ConfrontaBatteriaNo(b1, b2));
        println(LetturaSensore.ConfrontaBatteriaSi(b1, b2));

        println("\nTest con valori a 200 (fuori cache):");
        System.out.println(LetturaSensore.ConfrontaBatteriaNo(b3, b4)); //=false
        System.out.println( LetturaSensore.ConfrontaBatteriaSi(b3, b4)); //true
        System.out.println();

        //esempio di prova fatto da chat
        String[] pacchetti = {
                "temp=23.5;umid=61;ts=1732000000;batt_low=false",
                "temp=19.2;umid=45;ts=1732000100;batt_low=true",
                "temp=xx.xx;umid=50;ts=1732000200",
                "temp=30.1;umid=150;ts=1732000300",
                "temp=25.0;ts=1732000400;batt_low=false",
                "umid=80;ts=1732000500;batt_low=true",
                "temp=abc;umid=xyz;ts=invalid",
                "temp=15.8;umid=70;ts=1732000700;batt_low=false"
        };

        int lettureValide = 0;
        int erroriParsing = 0;

        double sommaTemperaturaValide = 0;
        int contatoreTemepraturaValide = 0;
        double mediaTemperaturaValide = 0;

        for (int i = 0; i < pacchetti.length; i++) {
            println("Pacchetto n" + (i + 1) + ": " + pacchetti[i]);
            Optional<LetturaSensore> optionalSensore= LetturaSensore.parsePacchetto(pacchetti[i]);

            //non è empty
            if(optionalSensore.isPresent()){
                lettureValide++;
                LetturaSensore sensore = optionalSensore.get();

                lista.add(sensore);

                if(sensore.getTemperatura() != null){
                    sommaTemperaturaValide+=sensore.getTemperatura();
                    contatoreTemepraturaValide++;

                    //lista.add(sensore);
                }

                println("Lettura OK: " + sensore);
            }
            else{
                erroriParsing++;
                println("Lettura con errore");
            }

            println();
        }

        println("Pacchetti totali: " + pacchetti.length);
        println("Letture valide: " + lettureValide);
        println("Letture con errore: " + erroriParsing);
        if(contatoreTemepraturaValide>0){
            println("Media delle temperatura valide: "+String.format("%.2f",sommaTemperaturaValide/  contatoreTemepraturaValide ));
        }
        else{
            println("Nessuna temperatura valida registrata");
        }

        println();


                //nullsLast mette i valori null alla fine
        lista.sort(Comparator.comparing(LetturaSensore::getTemperatura, Comparator.nullsLast(Comparator.reverseOrder())));
        //li confronta per il metodo getTemperatura->quindi x temperatura, invertendo ordine->decrescente

        println("Lista ordinata x temp decrescente");
        for (LetturaSensore s : lista) {
            println(s);
        }
    }

}

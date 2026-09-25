package Esercizio3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.PriorityQueue;

import static java.lang.IO.println;

public class MainEsercizio3 {
    public static void main(){
        String file = "ticket.csv";
        String fileOutput = "report_lavorazione.txt";
        Path path = Paths.get(file);
        Path pathOutput = Paths.get(fileOutput);

        PriorityQueue<Ticket> coda = new PriorityQueue<Ticket>();

        ArrayList<String> logErrori = new ArrayList<String>();


        if(!Files.exists(path)){
            println("File di input non esistente");
            return;
        }

        if(!Files.exists(pathOutput)){
            println("File di output non esistente");
            return;
        }

        try(BufferedReader reader = Files.newBufferedReader(path)){
            String riga;
            int contRiga = 0;
            boolean intestazione = true;

            while ((riga=reader.readLine()).trim() != null){
                //riga = reader.readLine().trim();
                //non posso leggerla qua e metterla anche nella condizione sennò
                //ogni volta salto una riga

                //salta la prima riga con le legende/intestazioni
                if(intestazione==true){
                    intestazione = !intestazione;
                    continue;
                }

                String[] dati = riga.split(",");
                if(dati.length==4){
                    //se ci sono tutti i dati...
                    //ora fa controllo di validità per esempio sul livello ecc

                    String id = dati[0].trim();
                    String descrizione = dati[1].trim();
                    String livello = dati[2].trim();
                    String timestamp = dati[3].trim();

                    Long timeStamp = null;
                    Livello livelloConvertito = null;

                    //provo a convertire livello e timestamp nel enum e in Long
                    try{
                        timeStamp = Long.parseLong(timestamp);
                    }
                    catch (Exception e){
                        logErrori.add("Id: + "+ id+" Riga: "+ riga+ ": Timestamp non valido: "+ timestamp);
                        println(e.getMessage());
                    }

                    try{
                        livelloConvertito = Livello.ConvertiDaStringa(livello.toUpperCase());
                    }
                    catch (Exception e){
                        println(e.getMessage());
                        logErrori.add("Id: + "+ id+" Riga: "+ riga+ ": Livello non valido "+ livello);
                    }

                    if(livelloConvertito == null ){
                        println("Errore nel livello di priorità");
                        continue;
                    }

                    if(timeStamp == null ){
                        println("Errore nel timeStamp di arrivo");
                        continue;
                    }

                    Ticket t = new Ticket(id, descrizione, livelloConvertito, timeStamp);
                    coda.add(t);

                }
            }

            //scrittura su file di output

            try(BufferedWriter writer = Files.newBufferedWriter(pathOutput)){
                writer.write(String.format("Id", "Durata", "Descrizione", "TimeStamp"));

                int tempoTotale = 0;
                int totaleTicket = 0;
                int lvlAltoCriticoDiFila = 0;

                while(!coda.isEmpty()){
                    Ticket tt = coda.poll();  //estra con prioritò
                    Livello lvl = tt.getLivello();

                    //controllo gestione max 5 lvl alti/critici di fila e dopo pausa
                    if (lvl==Livello.Alto || lvl==Livello.Critico){
                        lvlAltoCriticoDiFila++;
                        if(lvlAltoCriticoDiFila>5){
                            //allora aspetta i 10 minuti prima di continuare
                            tempoTotale+=10;
                        }
                    }
                    else{
                        lvlAltoCriticoDiFila=0;
                    }

                    //misura la durata normale
                    int durata=Livello.OttieniDurataDaLivello(lvl);
                    tempoTotale+=durata;
                    totaleTicket++;
                }

            }
            catch (Exception e){
                println(e.getMessage());
            }



        }
        catch (Exception e){
            println(e.getMessage());
        }
    }
}

package Esercizio3;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.PriorityQueue;

import static java.lang.IO.println;

public class MainEsercizio3 {
    public static void main(){
        String file = "src/Esercizio3/ticket.csv";
        String fileOutput = "src/Esercizio3/report_lavorazione.txt";
        Path path = Paths.get(file);
        Path pathOutput = Paths.get(fileOutput);

        PriorityQueue<Ticket> coda = new PriorityQueue<Ticket>();

        //per il "bonus"
        PriorityQueue<Ticket> codaCritici = new PriorityQueue<Ticket>();

        ArrayList<String> logErrori = new ArrayList<String>();


        if(!Files.exists(path)){
            println("File di input non esistente");
            return;
        }

        try{
            //blocco try with resources=
            //garantisce chiusura automatica di file
            //al termine del blocco
            //evita memory leak o file rimasti aperti
            try(BufferedReader reader = Files.newBufferedReader(path)){
                String riga;
                boolean intestazione = true;

                while ((riga=reader.readLine()) != null){
                    riga=riga.trim();
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
                            logErrori.add("Id: "+ id+" Riga: "+ riga+ ": Livello non valido "+ livello);
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

                        if(t.getLivello()==Livello.Critico){
                            codaCritici.add(t);
                            if(codaCritici.size()>2){
                                println("Problema: ci sono più di 2 CRITICI in coda ("+ codaCritici.size()+" Critici in attesa  )");
                            }
                        }


                    }
                }
        }
        catch (FileNotFoundException e){
            println(e.getMessage());
        }
        catch (IOException e){
            println(e.getMessage());
        }

        //stampo in console dei log di errori
            for (String err: logErrori){
                println(err);
            }


            //scrittura su file di output

            try(BufferedWriter writer = Files.newBufferedWriter(pathOutput)){

                //intestazione
                //segnaposti ->numero lunghezza minima della colonna->se più corto aggiunge spazi vuoti x riempire
                //s=string  d=decimal per numeri interi o long

                writer.write(String.format("%6s | %8s | %10s | %40s | %20s\n", "ID", "LIVELLO", "DURATA(m)", "DESCRIZIONE", "COMPLETAMENTO"));

                int tempoTotale = 0;
                int totaleTicket = 0;
                int lvlAltoCriticoDiFila = 0;

                int cntCritico= 0;
                int cntAlto =0;
                int cntMedio=0;
                int cntBasso=0;

                while(!coda.isEmpty()){
                    Ticket tt = coda.poll();  //estra con prioritò
                    Livello lvl = tt.getLivello();

                    if(lvl==Livello.Critico){
                        codaCritici.remove(tt);
                    }

                    //misura la durata normale
                    int durata=Livello.OttieniDurataDaLivello(lvl);
                    tempoTotale+=durata;
                    totaleTicket++;

                    println(String.format("Lavorazione ticket %s (Livello %s) - Durata: %d min | Tempo totale: %d min",
                            tt.getId(), lvl, durata, tempoTotale));

                    //riga del ticket
                    writer.write(String.format("%6s | %8s | %10d | %40s | %d\n", tt.getId(), lvl, durata, tt.getDescrizione(), tempoTotale));


                    //controllo gestione max 5 lvl alti/critici di fila e dopo pausa
                    if (lvl==Livello.Alto || lvl==Livello.Critico){
                        lvlAltoCriticoDiFila++;
                        if(lvlAltoCriticoDiFila==5){
                            //allora aspetta i 10 minuti prima di continuare
                            tempoTotale+=10;
                            println("\n Pausa per 5 livelli ALTI/CRITICI di FILA -> +10 minuti \n");
                            //resetto anche il contatore di quelli alti/critici di file
                            lvlAltoCriticoDiFila=0;
                        }
                    }
                    else{
                        lvlAltoCriticoDiFila=0;
                    }

                    switch (lvl){
                        case Alto:
                            cntAlto++;
                            break;
                        case Medio:
                            cntMedio++;
                            break;
                        case Basso:
                            cntBasso++;
                            break;
                        case Critico:
                            cntCritico++;
                            break;
                    }

               }
                writer.write("\nRiepilogo finale:\n");
                writer.write("Ticket totali:" +totaleTicket+"\n");
                writer.write(String.format("Tempo totale minuti: "+ tempoTotale+"\n"));
                writer.write(String.format("Tempo totale ore: %.2f\n", tempoTotale / 60.0));

                writer.write("CRITICO: " + cntCritico + " biglietti \n");
                writer.write("ALTO: " + cntAlto + " biglietti \n");
                writer.write("MEDIO: " + cntMedio + " biglietti \n");
                writer.write("BASSO: " + cntBasso + " biglietti \n");

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

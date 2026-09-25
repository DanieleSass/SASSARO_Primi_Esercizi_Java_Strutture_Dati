package Esercizio1;

import java.lang.foreign.StructLayout;
import java.util.Optional;

public class LetturaSensore
{
private Double temperatura;
private Integer umiditaPercentuale;
private Long timestampUnix;
private Boolean batteriaScarica;

public LetturaSensore (){
    temperatura=null;
    umiditaPercentuale=null;
    timestampUnix=null;
    batteriaScarica=null;
}

public LetturaSensore(Double temperatura, Integer umiditaPercentuale, Long timestampUnix, Boolean batteriaScarica) {
    this.temperatura = temperatura;
    this.umiditaPercentuale = ControllaValiditaUmidita(umiditaPercentuale);
    this.timestampUnix = timestampUnix;
    this.batteriaScarica = batteriaScarica;
}

public Integer ControllaValiditaUmidita(Integer umiditaPercentuale){
    if(umiditaPercentuale != null && (umiditaPercentuale<0 || umiditaPercentuale>100)){
        throw new LetturaInvalidaException("Valore di umidita fuori range");
    }
    return umiditaPercentuale;
}

public static Optional<LetturaSensore> parsePacchetto(String raw) {
    LetturaSensore sensore = null;

    Double temp = null;
    Integer umid = null;
    Long time = null;
    Boolean batt = null;

    String[] dati = raw.trim().split(";");
    //con [1] prende il valore dopo "=" quindi il numero/bool e non la parola/legenda
    temp = Double.parseDouble((dati[0].split(";"))[1]);
    umid = Integer.parseInt((dati[1].split(";"))[1]);
    time = Long.parseLong((dati[2].split(";"))[1]);
    batt = Boolean.parseBoolean((dati[3].split(";"))[1]);

    sensore = new LetturaSensore(temp, umid, time, batt);

    return null;
}

public static String ConfrontaBatteria(Integer int1, Integer int2){
    if(int1 == int2){
        return "I due valori sono uguali";
    }
    else{
        return "I due valori sono diversi";
    }
}

}

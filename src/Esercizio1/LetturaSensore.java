package Esercizio1;

import java.util.Optional;
import static java.lang.IO.println;

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
    //this.umiditaPercentuale = ControllaValiditaUmidita(umiditaPercentuale);
    this.umiditaPercentuale=umiditaPercentuale;
    this.timestampUnix = timestampUnix;
    this.batteriaScarica = batteriaScarica;
}

    public Boolean getBatteriaScarica() {
        return batteriaScarica;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public Integer getUmiditaPercentuale() {
        return umiditaPercentuale;
    }

    public Long getTimestampUnix() {
        return timestampUnix;
    }

    public static Integer ControllaValiditaUmidita(Integer umiditaPercentuale){
    if(umiditaPercentuale != null && (umiditaPercentuale<0 || umiditaPercentuale>100)){
        throw new LetturaInvalidaException("Valore di umidita fuori range ("+ umiditaPercentuale+")");
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

    for (String valori : dati){
        String[] chiaveValore = valori.split("=");
        if(chiaveValore.length!= 2){
            continue;   //salta quelle che non hanno xxx=yyy
        }
        String chiave = chiaveValore[0];
        String valore = chiaveValore[1];
        try{
            switch (chiave.trim().toLowerCase()){
                case "temp":
                    temp = Double.valueOf(valore);
                    break;
                case "umid":
                    try {
                        Integer valoree = Integer.valueOf(valore);
                        umid = ControllaValiditaUmidita(valoree);
                    }
                    catch (LetturaInvalidaException e){
                        println(e.getMessage());
                        umid = null;
                    }

                    break;
                case "ts":
                    time = Long.valueOf(valore);
                    break;
                case "batt_low":
                    batt = Boolean.valueOf(valore);
                    break;
            }
        }catch (NumberFormatException e){
            println(e.getMessage());
        }
        //catch (LetturaInvalidaException ee){
            //println(ee.getMessage());
          //  umid = null;
        //}

    }

    try{
        sensore = new LetturaSensore(temp, umid, time, batt);
        return Optional.of(sensore);
    }catch (Exception e){
        println(e.getMessage());
        return  Optional.empty();
    }

}

public static Boolean ConfrontaBatteriaNo(Integer int1, Integer int2)
{
    return int1 == int2;
}

    public static Boolean ConfrontaBatteriaSi(Integer int1, Integer int2)
    {
    return int1.equals(int2);
}

@Override
public String toString(){
    return "LetturaSensore: " + "temp=" + temperatura + ", umid=" + umiditaPercentuale +
            ", timestamp=" + timestampUnix + ", batt=" + batteriaScarica;
}
}

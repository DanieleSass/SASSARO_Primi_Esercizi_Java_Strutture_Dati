package Esercizio2;

import java.util.ArrayList;

public class RichiestaHttp{
    private String ip;
    private String path;
    private int statusCode;
    private long tempoRispostaMs;
    private long timestamp;

    public RichiestaHttp(String ip, String path, int codice, long tempo, long timestamp){
        this.ip = ip;
        this.path = path;
        statusCode = codice;
        tempoRispostaMs = tempo;
        this.timestamp = timestamp;
    }

    public String getIp() {
        return ip;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getTempoRispostaMs() {
        return tempoRispostaMs;
    }

    @Override
    public String toString(){
        return "IP: "+ ip+ " Path: "+path + " Codice: "+ statusCode+ " tempo Risposta: "+ tempoRispostaMs+ " timeStamp: "+ timestamp;
    }

}

package Esercizio3;

public enum Livello {
    Critico,
    Alto,
    Medio,
    Basso;

    public static Livello ConvertiDaStringa(String str){
        switch (str){
            case "CRITICO":
                return Critico;
            case "ALTO":
                return Alto;
            case "MEDIO":
                return Medio;
            case "BASSO":
                return Basso;
            default:
                return null;
        }
    }
    public static int OttieniDurataDaLivello(Livello l){
        switch (l){
            case Critico:
                return 15;
            case Alto:
                return 30;
            case Medio:
                return 60;
            case Basso:
                return 120;
            default:
                return 0;
        }
    }

}

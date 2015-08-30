package br.com.lle.stockoptionsanalysis.mobile;

import com.google.gson.Gson;

public class Constants {

    private Constants() {}
	
	public static Gson gson = new Gson();

    public static String LOG_APP_NAME = "SOA_LOG";

    public static String JSON_ALERTAS_STOP = "";

    public static int HORA_COMECO = 9; // as 10 horas comeca

    // TODO: Corrigir o valor
    public static int HORA_FIM = 18; // termina as 17 hs

    // TODO: Lembrar de verificar o tempo quando for para producao
    public static int QUINZE_MIN_MILIS = 15*60*1000;

    //public static int QUINZE_MIN_MILIS = 60*1000;

}

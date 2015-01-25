package br.com.lle.stockoptionsanalysis.mobile.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import br.com.lle.sata.mobile.core.to.CotacaoAtivoTO;
import br.com.lle.stockoptionsanalysis.mobile.interfaces.dao.IAlertaAtivoDAO;

/**
 * Created by lynton on 01/01/2015.
 */
public class AlertaAtivoDAO implements IAlertaAtivoDAO {

    private Context ctx;
    private String SHARED_PREF_NAME = "userPreferences";
    private String SHARED_PREF_ALERTA_ATIVO_KEY = "alertasAtivo";

    public AlertaAtivoDAO(Context ctx) {
        this.ctx = ctx;
    }
    @Override
    public List<CotacaoAtivoTO> getCotacoesAtivo() {
        List<CotacaoAtivoTO> cotacoes = new ArrayList<CotacaoAtivoTO>();
        cotacoes.add(new CotacaoAtivoTO());
        cotacoes.add(new CotacaoAtivoTO());
        cotacoes.get(0).setCodigo("PETR4");
        cotacoes.get(1).setCodigo("PETRA12");
        SharedPreferences sp = ctx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Gson g = new Gson();
        editor.putString(SHARED_PREF_ALERTA_ATIVO_KEY, g.toJson(cotacoes));
        editor.commit();
        // recupera do shared preferences
        String jsonStr = sp.getString(SHARED_PREF_ALERTA_ATIVO_KEY, "");
        List<CotacaoAtivoTO> cotacoes2 = g.fromJson(jsonStr, new TypeToken<List<CotacaoAtivoTO>>(){}.getType());
        return cotacoes2;
    }
}

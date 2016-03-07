package br.com.lle.stockoptionsanalysis.mobile.interfaces.js;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.lle.sata.mobile.core.interfaces.IBuscaCotacao;
import br.com.lle.sata.mobile.core.interfaces.IBuscaCotacaoOpcao;
import br.com.lle.sata.mobile.core.robo.BVMFBuscaCotacao;
import br.com.lle.sata.mobile.core.robo.BVMFBuscaCotacaoOpcao;
import br.com.lle.sata.mobile.core.to.CotacaoOpcaoTO;
import br.com.lle.sata.mobile.core.util.BlackScholes;
import br.com.lle.stockoptionsanalysis.mobile.Constants;
import br.com.lle.stockoptionsanalysis.mobile.alert.StockAlert;
import br.com.lle.stockoptionsanalysis.mobile.to.CoordenadaTO;
import br.com.lle.stockoptionsanalysis.mobile.util.GCMUtil;

import static br.com.lle.stockoptionsanalysis.mobile.Constants.gson;

public class MainJavaScriptInterface {

	private Activity activity;
	
	public MainJavaScriptInterface(Activity activity) {
        this.activity = activity;
	}
	
	private double rounding(double valor, int casasDecimais) {
		BigDecimal bd = new BigDecimal(valor).setScale(casasDecimais, BigDecimal.ROUND_HALF_EVEN);
		return bd.doubleValue();
	}
	
	@JavascriptInterface
	public String resultado(boolean ehCall, double precoExercicio, double inicioAcao, double fimAcao,
					double intervaloAcao, int duracao, double volatilidade, double taxaJuros) {
		volatilidade = rounding((double) volatilidade / 100, 4);
		taxaJuros = rounding((double) taxaJuros / 100, 4);
		List<CoordenadaTO> coordenadas = calculaBS(ehCall, precoExercicio,inicioAcao, fimAcao, intervaloAcao, duracao, volatilidade, taxaJuros);
		return gson.toJson(coordenadas);
//		return "[{\"x\":10,\"y\":30},{\"x\":20,\"y\":25},{\"x\":30,\"y\":20}]";
	}
	
	// calcula o BS
	public static List<CoordenadaTO> calculaBS(boolean ehCall, double precoExerc, double inicioAcao, double fimAcao, 
								double intervaloAcao, int duracao, double volatilidade, double taxaDeJuros) {
		List<CoordenadaTO> coordenadas = new ArrayList<CoordenadaTO>();

		for (double acao= inicioAcao; acao <= fimAcao; acao+=intervaloAcao) {
			BigDecimal bd = new BigDecimal(acao).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			double qtdDiasVencEmAnos = BlackScholes.getQtdDiasEmAnos(duracao);
			double precoOpc = BlackScholes.blackScholes(ehCall, acao, precoExerc, qtdDiasVencEmAnos, taxaDeJuros, volatilidade);
			BigDecimal opcao = new BigDecimal(precoOpc).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			CoordenadaTO coordenada = new CoordenadaTO();
			coordenada.setX(bd.doubleValue());
			coordenada.setY(opcao.doubleValue());
			coordenadas.add(coordenada);
		}
		return coordenadas;
	}
	
	@JavascriptInterface
	public String getCotacao(String ativo) {
		try {
			IBuscaCotacao bc = new BVMFBuscaCotacao();
            if (ativo.equalsIgnoreCase("REGID"))
                return GCMUtil.getGCMRegID(activity.getApplicationContext());
			return bc.getCotacao(ativo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-9999,99";
	}
	
	@JavascriptInterface
	public String getPrecoOpcao(boolean ehCall, double precoAcao, double precoExerc, 
			int qtdDiasVenc, double volatilidade, double taxaJuros) {
		try {
			volatilidade = rounding((double) volatilidade / 100, 4);
			taxaJuros = rounding((double) taxaJuros / 100, 4);
			BigDecimal pa = new BigDecimal(precoAcao).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal pe = new BigDecimal(precoExerc).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal volat = new BigDecimal(volatilidade).setScale(4, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal txJuros = new BigDecimal(taxaJuros).setScale(4, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal po = BlackScholes.blackScholes(ehCall, pa, pe, qtdDiasVenc, volat, txJuros).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			return String.valueOf(po);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-7777,77";
	}
	
	@JavascriptInterface
	public String getVolatilidade(boolean ehCall, double precoAcao, double precoExerc, 
			double precoOpcao, int qtdDiasVenc, double taxaJuros) {
		try {
			taxaJuros = rounding((double) taxaJuros / 100, 4);
			BigDecimal pa = new BigDecimal(precoAcao).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal pe = new BigDecimal(precoExerc).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal po = new BigDecimal(precoOpcao).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal txJuros = new BigDecimal(taxaJuros).setScale(4, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal volat = BlackScholes.calculaVolatilidade(ehCall, pa, pe, qtdDiasVenc, po, txJuros);
			volat = volat.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			return String.valueOf(volat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-8888,88";
	}

    @JavascriptInterface
     public String getCotacoesOpcoes(String codigoAtivo, boolean ehCall) {
            List<CotacaoOpcaoTO> cotacoesOpcoes = new ArrayList<CotacaoOpcaoTO>();
        IBuscaCotacaoOpcao bco = new BVMFBuscaCotacaoOpcao();
        cotacoesOpcoes = bco.getCotacoesOpcoes(codigoAtivo, ehCall);
        return gson.toJson(cotacoesOpcoes);
    }

    @JavascriptInterface
    public String getCotacaoOpcao(String codigoOpcao) {
        IBuscaCotacaoOpcao bco = new BVMFBuscaCotacaoOpcao();
        CotacaoOpcaoTO co = bco.getCotacaoOpcao(codigoOpcao);
        return gson.toJson(co);
    }

    @JavascriptInterface
    public void atualizarAlertas(String jsonAlertas) {
        // atualiza a constante que contém os alertas no formato json
        Constants.JSON_ALERTAS_STOP = jsonAlertas;
        // verifica se tem algum alerta para notificar
        StockAlert sa = new StockAlert(this.activity);
        sa.stockAlert();
    }

    @JavascriptInterface
    public String getVencimentoOpcoes() {
        IBuscaCotacaoOpcao bco = new BVMFBuscaCotacaoOpcao();
        //return gson.toJson(bco.getVencimentosOpcoes());
        return "";
    }

}

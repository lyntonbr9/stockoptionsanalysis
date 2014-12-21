package br.com.lle.stockoptionsanalysis.mobile.interfaces;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.webkit.JavascriptInterface;
import br.com.br.sata.mobile.core.interfaces.IBuscaCotacao;
import br.com.br.sata.mobile.core.robo.BuscaCotacao;
import br.com.br.sata.mobile.core.util.BlackScholes;
import br.com.lle.stockoptionsanalysis.mobile.Constants;
import br.com.lle.stockoptionsanalysis.mobile.to.CoordenadaTO;

public class MainJavaScriptInterface {
	
	private Activity activity;
	
	public MainJavaScriptInterface(Activity activity) {
		this.activity = activity;
	}
	
	@JavascriptInterface
	public String resultado(boolean ehCall, double precoExercicio, double inicioAcao, double fimAcao,
					double intervaloAcao, int duracao, double volatilidade, double taxaJuros) {
		List<CoordenadaTO> coordenadas = calculaBS(ehCall, precoExercicio,inicioAcao, fimAcao, intervaloAcao, duracao, 1, 10, volatilidade, taxaJuros);
		return Constants.gson.toJson(coordenadas);
//		return "[{\"x\":10,\"y\":30},{\"x\":20,\"y\":25},{\"x\":30,\"y\":20}]";
	}
	
	// calcula o BS
	public static List<CoordenadaTO> calculaBS(boolean ehCall, double precoExerc, double inicioAcao, double fimAcao, 
								double intervaloAcao, int duracao, int ultimoDia, int intervaloDia,
								double volatilidade, double taxaDeJuros) {
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
			IBuscaCotacao bc = new BuscaCotacao();
			return bc.getCotacao(ativo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-9999,99";
	}
	
	@JavascriptInterface
	public String getPrecoOpcao(boolean ehCall, double precoAcao, double precoExerc, 
			int qtdDiasVenc, double volatilidade, double taxaDeJuros) {
		try {
			BigDecimal pa = new BigDecimal(precoAcao).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal pe = new BigDecimal(precoExerc).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal volat = new BigDecimal(volatilidade).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal txJuros = new BigDecimal(taxaDeJuros).setScale(4, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal po = BlackScholes.blackScholes(ehCall, pa, pe, qtdDiasVenc, volat, txJuros).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			return String.valueOf(po);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-7777,77";
	}
	
	@JavascriptInterface
	public String getVolatilidade(boolean ehCall, double precoAcao, double precoExerc, 
			double precoOpcao, int qtdDiasVenc, double taxaDeJuros) {
		try {
			BigDecimal pa = new BigDecimal(precoAcao).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal pe = new BigDecimal(precoExerc).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal po = new BigDecimal(precoOpcao).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal txJuros = new BigDecimal(taxaDeJuros).setScale(4, BigDecimal.ROUND_HALF_EVEN);
			BigDecimal volat = BlackScholes.calculaVolatilidade(ehCall, pa, pe, qtdDiasVenc, po, txJuros).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			return String.valueOf(volat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "-8888,88";
	}

}

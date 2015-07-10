if(!soa) {
    var soa = {

        getCotacao: function(codigoAtivo) {
			if (IS_MOBILE_RUNNING)
				return window.JSInterface.getCotacao(codigoAtivo);
			else
				return '11,66';
        },

        getCotacoesOpcoes: function(codigoAtivo, ehCall) {
			if (IS_MOBILE_RUNNING)
				return JSON.parse(window.JSInterface.getCotacoesOpcoes(codigoAtivo, ehCall));
			else
				return [{codigo: 'PETRG12', precoExercicio: '12,16', dataVencimento: '20/07/2015', ehCall: 'true'}];
        },

        getVolatilidade: function(ehCall, pa, pe, precoOpcao, qtdDiasVenc, txjuros) {
			if (IS_MOBILE_RUNNING)
				return window.JSInterface.getVolatilidade(ehCall, pa, pe, precoOpcao, qtdDiasVenc, txjuros);
			else
				return '54.00';
        },
		
		getPrecoOpcao: function(ehCall, pa, pe, qtdDiasVenc, volat, txjuros) {
			if (IS_MOBILE_RUNNING)
				return window.JSInterface.getPrecoOpcao(ehCall, pa, pe, qtdDiasVenc, volat, txjuros);
			else
				return '0.23';
		},
		
		getResultado: function(ehCall, pe, pia, pfa, interv, qtdDias, volat, txjuros) {
			if (IS_MOBILE_RUNNING)
				return JSON.parse(window.JSInterface.resultado(ehCall, pe, pia, pfa, interv, qtdDias, volat, txjuros));
			else
				return JSON.parse("[{\"x\":10,\"y\":30},{\"x\":20,\"y\":25},{\"x\":30,\"y\":20}]");
		},
		
		atualizarAlertas: function(alertas) {
			if (IS_MOBILE_RUNNING)
				window.JSInterface.atualizarAlertas(JSON.stringify(alertas));
		}
		
    }
}
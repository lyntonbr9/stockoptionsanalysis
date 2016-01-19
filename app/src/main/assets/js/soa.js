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
		},
		
		getVencimentoOpcoes: function() {
			//if (IS_MOBILE_RUNNING)
			//	return JSON.parse(window.JSInterface.getVencimentoOpcoes());
			//else	
				return JSON.parse("{\"A\":\"18/01/2016\",\"B\":\"15/02/2016\",\"C\":\"21/03/2016\",\"D\":\"18/04/2016\",\"E\":\"16/05/2016\",\"F\":\"20/06/2016\",\"G\":\"18/07/2016\",\"H\":\"15/08/2016\",\"I\":\"19/09/2016\",\"J\":\"17/10/2016\",\"K\":\"21/11/2016\",\"L\":\"19/12/2016\",\"M\":\"18/01/2016\",\"N\":\"15/02/2016\",\"O\":\"21/03/2016\",\"P\":\"18/04/2016\",\"Q\":\"16/05/2016\",\"R\":\"20/06/2016\",\"S\":\"18/07/2016\",\"T\":\"15/08/2016\",\"U\":\"19/09/2016\",\"V\":\"17/10/2016\",\"W\":\"21/11/2016\",\"X\":\"19/12/2016\"}");
		}
		
    }
}
if(!soa) {
		
    var soa = {
		CALLS: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L'],
		getTipoOpcao: function(codigoOpcao) {
			var letraOpcao = codigoOpcao.substr(4,1).toUpperCase();
			for (var i = 0; i < this.CALLS.length; i++) {
				if (letraOpcao === this.CALLS[i])
					return true; // CALL
			}
			return false; // PUT
		},
		// deprecated
        getCotacao: function(codigoAtivo) {
			if (IS_MOBILE_RUNNING)
				return window.JSInterface.getCotacao(codigoAtivo);
			else {
				
				var promessa = bc.getCotacao(codigoAtivo);
				return promessa;
				//return '14.94';
			}
        },
		
		getCotacaoAcao: function(codigoAcao) {
			if (IS_MOBILE_RUNNING)
				return window.JSInterface.getCotacao(codigoAcao);
			else {
				
				if(VERSAO_PRO) {
					var promessa = bcp.getCotacao(codigoAcao);
				} else {
					var promessa = bc.getCotacaoAcao(codigoAcao);
				}
				
				return promessa;
				//return '14.94';
			}
        },
		
        getCotacoesOpcoes: function(codigoAtivo, ehCall) {
			//if (IS_MOBILE_RUNNING)
			//	return JSON.parse(window.JSInterface.getCotacoesOpcoes(codigoAtivo, ehCall));
			//else
			//	return [{codigo: 'PETRP1', precoExercicio: '5,00', dataVencimento: '18/04/2016', ehCall: 'true'}];
        },
		
		getCotacaoOpcao: function(codigoOpcao, codigoAcao) {
			if (IS_MOBILE_RUNNING) {
				var strCo = window.JSInterface.getCotacaoOpcao(codigoOpcao);
				console.log(strCo);
				return JSON.parse(strCo);
			} else {
				
				if(VERSAO_PRO) {
					var promessa = bcp.getCotacaoOpcao(codigoOpcao);
				} else {
					var promessa = bc.getCotacaoOpcao(codigoOpcao, codigoAcao, this.getTipoOpcao(codigoOpcao));
				}
				
				return promessa;
				//return {codigo: 'PETRD15', precoExercicio: '15.00', dataVencimento: '17/04/2017', ehCall: 'true'};
			}
			/*
			if (IS_MOBILE_RUNNING) {
				var strCo = window.JSInterface.getCotacaoOpcao(codigoOpcao);
				console.log(strCo);
				return JSON.parse(strCo);
			} else {
				var promessa = bc.getCotacaoOpcao(codigoOpcao, codigoAcao, this.getTipoOpcao(codigoOpcao));
				return promessa;
				//return {codigo: 'PETRD15', precoExercicio: '15.00', dataVencimento: '17/04/2017', ehCall: 'true'};
			}*/
        },

        getVolatilidade: function(ehCall, pa, pe, precoOpcao, qtdDiasVenc, txjuros) {
			var tipoOpcao = ehCall ? 'call' : 'put';
			txjuros = (txjuros/100);
			//s, k, t, r, op, callPut
			return bs.getVolatility(pa, pe, bs.getTimeToExp(qtdDiasVenc), txjuros, precoOpcao, tipoOpcao) * 100;
        },
		
		getPrecoOpcao: function(ehCall, pa, pe, qtdDiasVenc, volat, txjuros) {
			var tipoOpcao = ehCall ? 'call' : 'put';
			volat = (volat/100);
			txjuros = (txjuros/100);
			//s, k, t, v, r, callPut
			return bs.blackScholes(pa, pe, bs.getTimeToExp(qtdDiasVenc), volat, txjuros, tipoOpcao);
		},
		
		getResultado: function(ehCall, pe, pia, pfa, interv, qtdDias, volat, txjuros) {
			var valoresBS = [];
			var tipoOpcao = ehCall ? 'call' : 'put';
			volat = (volat/100);
			txjuros = (txjuros/100);
			for (var acao=pia; acao <= pfa; acao+=interv) {
				var precoOpcao = bs.blackScholes(acao, pe, bs.getTimeToExp(qtdDias), volat, txjuros, tipoOpcao);
				precoOpcao = Math.round(precoOpcao*100) / 100;
				valoresBS.push({
					x: acao,
					y: precoOpcao
				});
			}
			return valoresBS;
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
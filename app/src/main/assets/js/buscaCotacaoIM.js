if (!bc) {
	
	//var URL_PATTERN = 'http://www.infomoney.com.br/api/opcoes?hash=cfcd208495d565ef66e7dff9f98764da&ch=00829ae26f8332c7c84fce65c8293d06';
	
	
	var URL_PATTERN = 'http://www.infomoney.com.br/api/opcoes?ch=302d9af8da629c0a64c348fbdbe44a62&hash=cfcd208495d565ef66e7dff9f98764da';
	
	//var regExpOpcoes = /<MaturityDate>([\s\S]*?)<\/Symbol>/g;
	
	var MESES_ANO = ['2017-01-01', '2017-02-01', '2017-03-01', '2017-04-01', '2017-05-01', '2017-06-01',
					'2017-07-01', '2017-08-01', '2017-09-01', '2017-10-01', '2017-11-01', '2017-12-01']
	
	var MESES_REFER_OPCOES = {
		A: MESES_ANO[0], B: MESES_ANO[1], C: MESES_ANO[2], D: MESES_ANO[3], E: MESES_ANO[4], F: MESES_ANO[5],
		G: MESES_ANO[6], H: MESES_ANO[7], I: MESES_ANO[8], J: MESES_ANO[9], K: MESES_ANO[10], L: MESES_ANO[11],
		M: MESES_ANO[0], N: MESES_ANO[1], O: MESES_ANO[2], P: MESES_ANO[3], Q: MESES_ANO[4], R: MESES_ANO[5],
		S: MESES_ANO[6], T: MESES_ANO[7], U: MESES_ANO[8], V: MESES_ANO[9], W: MESES_ANO[10], X: MESES_ANO[11]
	}
	
	var bc = {
		_getOptionFromResponse: function(codigoOpcao, response) {
			for (var i = 0; i < response.Options.length; i++) {
				if (codigoOpcao.toUpperCase() == response.Options[i].Symbol.toUpperCase()) {
					return response.Options[i];
				}
			}
		},
		_getAjaxPostConfig: function(codigoAcao, dataMesOpcao, ehCall) {
			return {
				url: URL_PATTERN,
				type: 'post',
				data: {
					maturityMonthYear: dataMesOpcao,
					orderBy: '1',
					isAscendenting: 'true',
					showOnlyNegotiatedFilter: 'false',
					perPage:'100',
					page: '1',
					StockCode:codigoAcao,
					PutOrCall: (ehCall) ? '1': '0'
				},
				headers: {
					'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
				}
			}
		},
		getCotacaoAcao: function(codigoAcao) {
			
			var mesVencimento = MESES_REFER_OPCOES['L'];
			
			// tanto faz recuperar dados das CALLs ou PUTs
			var ajaxPostConfig = this._getAjaxPostConfig(codigoAcao, mesVencimento, true);

			var jqxhr = $.ajax(ajaxPostConfig).then(function(response) {
				console.log(response);
				
				return {
					fechamento: response.Options[0].StockCurrentPrice
				}
			});
			return jqxhr;
		},
		getCotacaoOpcao: function(codigoOpcao, codigoAcao, ehCall) {
			
			//var ajaxPostConfig = this._getAjaxPostConfig('PETR4', '2017-06-01T00%3A00%3A00', true);
			
			var letraOpcao = codigoOpcao.substring(4, 5).toUpperCase();
			
			var mesVencimento = MESES_REFER_OPCOES[letraOpcao];
			
			var ajaxPostConfig = this._getAjaxPostConfig(codigoAcao, mesVencimento, ehCall);
			
			var pesquisaOpcao = this._getOptionFromResponse;

			var jqxhr = $.ajax(ajaxPostConfig).then(function(response) {
				console.log(response);
				var opcaoEscolhida = pesquisaOpcao(codigoOpcao, response);
				var dataVenc = opcaoEscolhida.MaturityDate.replace(/T00:00:00/g, '');
				dataVenc = dataVenc.replace(/-/g, '');
				return {
					codigo: codigoOpcao,
					codigoAcao: codigoAcao,
					fechamento: opcaoEscolhida.Price,
					precoExercicio: opcaoEscolhida.StrikePrice,
					ehCall: ehCall,
					dataVencimento: dataVenc
				}				
			});
			return jqxhr;
		}
	}
}
if (!bc) {
	
	var MESES_ANO = ['2017-01-01', '2017-02-01', '2017-03-01', '2017-04-01', '2017-05-01', '2017-06-01',
					'2017-07-01', '2017-08-01', '2017-09-01', '2017-10-01', '2017-11-01', '2017-12-01']
	
	var MESES_REFER_OPCOES = {
		A: MESES_ANO[0], B: MESES_ANO[1], C: MESES_ANO[2], D: MESES_ANO[3], E: MESES_ANO[4], F: MESES_ANO[5],
		G: MESES_ANO[6], H: MESES_ANO[7], I: MESES_ANO[8], J: MESES_ANO[9], K: MESES_ANO[10], L: MESES_ANO[11],
		M: MESES_ANO[0], N: MESES_ANO[1], O: MESES_ANO[2], P: MESES_ANO[3], Q: MESES_ANO[4], R: MESES_ANO[5],
		S: MESES_ANO[6], T: MESES_ANO[7], U: MESES_ANO[8], V: MESES_ANO[9], W: MESES_ANO[10], X: MESES_ANO[11]
	}
	
	var bc = {
		URL_PATTERN: 'http://www.bolsafinanceira.com/cotacoes/get_datafeed?codigo={0}',
		_getAjaxGetConfig: function(codigoAtivo) {
			
			return {
				url: this.URL_PATTERN.replace('{0}', codigoAtivo),
				type: 'get',
				//dataType: 'jsonp',
				//jsonp: false,
				//jsonpCallback: 'jsonpCallback',
				//jsonp: false,     //this will be added in the query as parameter
				//jsonpCallback: 'jsonp_reply',
				//contentType: "text/plain; charset=utf-8"
				contentType: "application/json; charset=utf-8"
			}
		},
		getCotacaoAcao: function(codigoAcao) {
			
			var jqxhr = $.ajax(this._getAjaxGetConfig(codigoAcao)).then(function(response) {
				console.log(response);
				
				var dadosAcao = response.split(',');
				return {
					fechamento: Number(dadosAcao[9])
				}
				
			});
			return jqxhr;
			
		},
		getCotacaoOpcao: function(codigoOpcao, codigoAcao, ehCall) {
			
			var jqxhr = $.ajax(this._getAjaxGetConfig(codigoOpcao)).then(function(response) {
				console.log(response);
				var dadosOpcao = response.split(',');
				var dadosPrecoExer = dadosOpcao[2].split(' ');
				var prExcercicio = dadosPrecoExer[dadosPrecoExer.length-1];
				return {
					codigo: codigoOpcao,
					fechamento: Number(dadosOpcao[9]),
					precoExercicio: Number(prExcercicio),
					dataVencimento: '2017-07-17',
					Bk: {}
				};
			});
			return jqxhr;
		}
	}
}
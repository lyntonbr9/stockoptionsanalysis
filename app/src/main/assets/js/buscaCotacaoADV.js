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
		URL_PATTERN: 'http://br.advfn.com/bolsa-de-valores/bovespa/{0}/cotacao',
		getCotacaoAcao: function(codigoAcao) {
			
			var jqxhr = $.get(this.URL_PATTERN.replace('{0}', codigoAcao)).then(function(response) {	
				
				//var re = /<\s*td align="center"[^>]*>(.*?)<\s*\/\s*td>/g;
				var regExpSpanQuotes = /<\s*span id='quote[^>]*>(.*?)<\s*\/\s*span>/g;
				var valores = response.match(regExpSpanQuotes);
				//console.log(response);
				//console.log(valores);
				var cotacao = valores[9].replace(/<\s*span id='quote[^>]*>/g,'');
				cotacao = cotacao.replace('</span>', '');
				cotacao = cotacao.replace(',', '.');
				return {
					fechamento: Number(cotacao)
				}
			});
			return jqxhr;
		},
		//TODO: Falta implementar o dia do vencimento
		getCotacaoOpcao: function(codigoOpcao, codigoAcao, ehCall) {
			
			var jqxhr = $.get(this.URL_PATTERN.replace('{0}', codigoAcao)).then(function(response) {	
				
				//var re = /<\s*td align="center"[^>]*>(.*?)<\s*\/\s*td>/g;
				var regExpSpanQuotes = /<\s*span id='quote[^>]*>(.*?)<\s*\/\s*span>/g;
				var valores = response.match(regExpSpanQuotes);
				//console.log(response);
				//console.log(valores);
				var cotacao = valores[9].replace(/<\s*span id='quote[^>]*>/g,'');
				cotacao = cotacao.replace('</span>', '');
				cotacao = cotacao.replace(',', '.');
				return {
					fechamento: Number(cotacao)
				}
			});
			
			/*
			return {
					codigo: codigoOpcao,
					codigoAcao: codigoAcao,
					fechamento: opcaoEscolhida.Price,
					precoExercicio: opcaoEscolhida.StrikePrice,
					ehCall: ehCall,
					dataVencimento: dataVenc
				}
			*/
			
			return jqxhr;
		}
	}
}
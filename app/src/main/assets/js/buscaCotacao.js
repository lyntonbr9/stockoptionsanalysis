if (!bc) {
	
	var bc = {
		URL_PATTERN: 'http://br.advfn.com/bolsa-de-valores/bovespa/{0}/cotacao',
		getCotacaoAcao: function(codigoAcao) {
			
			var jqxhr = $.get(this.URL_PATTERN.replace('{0}', codigoAcao)).then(function(response) {	
				
				//console.log(response);
				var regExpSpanQuotes = /<\s*span id='quote[^>]*>(.*?)<\s*\/\s*span>/g;
				var valores = response.match(regExpSpanQuotes);
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
		getCotacaoOpcao: function(codigoOpcao, codigoAcao, ehCall) {
			
			var jqxhr = $.get(this.URL_PATTERN.replace('{0}', codigoOpcao)).then(function(response) {	
				
				//console.log(response);
				var regExpSpanQuotes = /<\s*span id='quote[^>]*>(.*?)<\s*\/\s*span>/g;
				var spanQuotes = response.match(regExpSpanQuotes);
				
				var cotacao = spanQuotes[9].replace(/<\s*span id='quote[^>]*>/g,'');
				cotacao = cotacao.replace('</span>', '');
				cotacao = cotacao.replace(',', '.');
				
				var regExpTdAlignCenter = /<td align="center">(.*?)<\/td>/g;
				var tdAlignCenters = response.match(regExpTdAlignCenter);
				
				var strike = tdAlignCenters[6].replace('<td align="center">', '');
				strike = strike.replace('</td>', '');
				strike = strike.replace(',', '.');
				
				var dtVencimento =  tdAlignCenters[7].replace('<td align="center">', '');
				dtVencimento = dtVencimento.replace('</td>', '');
				dtVencimento = dtVencimento.substring(0,4) + dtVencimento.substring(5,7) + (Number(dtVencimento.substring(8,10)) + 1);
				return {
					codigo: codigoOpcao,
					codigoAcao: codigoAcao,
					fechamento: Number(cotacao),
					precoExercicio: Number(strike),
					ehCall: ehCall,
					dataVencimento: dtVencimento,
					Bk: { Bd:[], Ak:[] }
				}
			});
			
			return jqxhr;
		}
	}
}
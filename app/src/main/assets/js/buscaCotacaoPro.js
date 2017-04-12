if (!bcp) {
	
	var URL_PATTERN = 'https://mdgateway03.easynvest.com.br/iwg/snapshot/?t=webgateway&c=141793&q={0},29,0,10';
	
	var bcp = {
		getCotacao: function(ativo) {
			var url = URL_PATTERN.replace('{0}', ativo);

			var jqxhr = $.get(url).then(function(response) {
				console.log(response);
				return {
					fechamento: Number(response.Value[0].Ps.P) || Number(response.Value[0].Ps.CP)
				};
			});
			return jqxhr;
		},
		getCotacaoOpcao: function(codigoOpcao) {
			var url = URL_PATTERN.replace('{0}', codigoOpcao);
			
			var jqxhr = $.get(url).then(function(response) {
				console.log(response);
				return {
					codigo: codigoOpcao,
					fechamento: Number(response.Value[0].Ps.P) || Number(response.Value[0].Ps.CP),
					precoExercicio: Number(response.Value[0].Ps.StrikePrice),
					dataVencimento: response.Value[0].Ps.StD,
					Bk: response.Value[0].Bk
				};
			});
			return jqxhr;
		}
	}
}
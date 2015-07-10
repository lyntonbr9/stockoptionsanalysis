if(!soaTB) {
    var soaTB = {

        addCotacoesOpcoes: function(idTable, cotacoesOpcoes) {
            for (var i=0, length=cotacoesOpcoes.length; i < length; i++) {
                var linha = i + 1;
                var co = cotacoesOpcoes[i];
                co.fechamento = co.fechamento || "";
                co.valorExtrinseco = co.valorExtrinseco || "";
                co.valorIntrinseco = co.valorIntrinseco || "";
                co.volatilidadeImplicita = co.volatilidadeImplicita || "";
                $("#"+idTable).append('<tr>' +
                    '<td onclick="simularOpcao(' + i +  ')" ><a href="#pgSimulacao">' + co.codigo + '</a></td>' +
                    '<td>' + co.precoExercicio + '</td>' +
                    '<td class="PRAZO_' + linha + '"> d</td>' +
                    '<td class="PR_' + linha + '">' + co.fechamento + '</td>' +
                    '<td class="VE_' + linha + '">' + co.valorExtrinseco + '</td>' +
                    '<td class="VI_' + linha + '">' + co.valorIntrinseco + '</td>' +
                    '<td class="VOLAT_' + linha + '">' + co.volatilidadeImplicita + '</td>' +
                    '<td class="VR_' + linha + '"></td>' +
                    '</tr>');
            }
        },
		
		createTableAlertas: function(idTable, alertas) {
			var tdStatus = '';
            for (var i=0, length=alertas.length; i < length; i++) {
                var al = alertas[i];
                al.codigoAtivo = al.codigoAtivo || "";
                al.precoStopLow = al.precoStopLow || "";
                al.precoStopHigh = al.precoStopHigh || "";
                al.habilitado = al.habilitado != 'undefined' ? al.habilitado : true;
				if (al.habilitado)
					tdStatus = '<td onclick="mudarStatusAlerta(' + i +  ')" class="ON">ON</td>';
				else	
					tdStatus = '<td onclick="mudarStatusAlerta(' + i +  ')" class="OFF">OFF</td>';
                $("#"+idTable).append('<tr>' +
                    '<td>' + al.codigoAtivo + '</td>' +
                    '<td>' + al.precoStopLow + '</td>' +
					'<td>' + al.precoStopHigh + '</td>' +
                    tdStatus +
                    '<td><img src="img/lixeira.jpg" style="width:20px;height:20px" onclick="removerAlerta(' + i +  ')" /></td>' +
                    '</tr>');
            }
        },

        clearTable: function(idTable) {
            $("#"+idTable).find("tr:gt(0)").remove();
        }
    }
}
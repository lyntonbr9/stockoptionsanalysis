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

        clearTable: function(idTable) {
            $("#"+idTable).find("tr:gt(0)").remove();
        }
    }
}
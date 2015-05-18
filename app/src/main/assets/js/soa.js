if(!soa) {
    var soa = {

        getCotacao: function(codigoAtivo) {
            return window.JSInterface.getCotacao(codigoAtivo);
        },

        getCotacoesOpcoes: function(codigoAtivo, ehCall) {
            return JSON.parse(window.JSInterface.getCotacoesOpcoes(codigoAtivo, ehCall));
        },

        getVolatilidade: function(ehCall, pa, pe, precoOpcao, qtdDiasVenc, txjuros) {
            return window.JSInterface.getVolatilidade(ehCall, pa, pe, precoOpcao, qtdDiasVenc, txjuros);
        }
    }
}
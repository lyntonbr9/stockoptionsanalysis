package br.com.lle.stockoptionsanalysis.mobile.interfaces.dao;

import java.util.List;
import br.com.lle.sata.mobile.core.to.CotacaoAtivoTO;

/**
 * Created by lynton on 01/01/2015.
 */
@Deprecated
public interface IAlertaAtivoDAO {

    List<CotacaoAtivoTO> getCotacoesAtivo();
}

package com.financeiro.nfedfe.service;

import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.Nfe;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.ConsultaDFeEnum;
import br.com.swconsultoria.nfe.dom.enuns.PessoaEnum;
import br.com.swconsultoria.nfe.dom.enuns.StatusEnum;
import br.com.swconsultoria.nfe.exception.NfeException;
import br.com.swconsultoria.nfe.schema.resnfe.ResNFe;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNfeProc;
import br.com.swconsultoria.nfe.util.ObjetoUtil;
import br.com.swconsultoria.nfe.util.XmlNfeUtil;
import br.com.swconsultoria.nfe.dom.Evento;
import br.com.swconsultoria.nfe.dom.enuns.ManifestacaoEnum;
import br.com.swconsultoria.nfe.schema.envConfRecebto.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envConfRecebto.TRetEnvEvento;
import br.com.swconsultoria.nfe.util.ManifestacaoUtil;
import com.financeiro.nfedfe.entity.Empresa;
import com.financeiro.nfedfe.entity.NotasFiscais;
import com.financeiro.nfedfe.exception.SistemaException;
import com.financeiro.nfedfe.repository.EmpresaRepository;
import com.financeiro.nfedfe.util.ConfiguracoesNFE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.financeiro.nfedfe.util.CompactaDescompactaXML.compactarXML;

@Service
@Slf4j
public class DistNfService {

    private final EmpresaRepository empresaRepository;
    private final NfService nfService;

    public DistNfService(EmpresaRepository empresaRepository, NfService nfService) {
        this.empresaRepository = empresaRepository;
        this.nfService = nfService;
    }

    public void consultaNotasDist() throws JAXBException, NfeException, CertificadoException, IOException {
        List<Empresa> empresas = empresaRepository.findAll();
        for (Empresa empresa : empresas){
            efetuaConsulta(empresa);
        }
    }
    public void efetuaConsulta(Empresa empresa) throws CertificadoException, NfeException, IOException, JAXBException {

        ConfiguracoesNfe configuracao = ConfiguracoesNFE.criarConfiguracao(empresa);
        List<String> ListaNotasManifestar = new ArrayList<>();
        List<NotasFiscais> ListaNotasSalvar = new ArrayList<>();

        boolean verificaNotas = true;

        while(verificaNotas) {
            RetDistDFeInt retornoConsulta = null;

            try {
                //erro aqui
                retornoConsulta = Nfe.distribuicaoDfe(configuracao
                        , PessoaEnum.JURIDICA
                        , empresa.getCpfCnpj()
                        , ConsultaDFeEnum.NSU
                        , ObjetoUtil.verifica(empresa.getNsu()).orElse("000000000000000"));
            } catch (Exception e) {
                log.error("Error in Nfe.distribuicaoDfe", e);
            }

            if (!retornoConsulta.getCStat().equals(StatusEnum.DOC_LOCALIZADO_PARA_DESTINATARIO.getCodigo())) {
                if (retornoConsulta.getCStat().equals(StatusEnum.CONSUMO_INDEVIDO.getCodigo())) {
                    break;
                } else {
                    throw new SistemaException("Erro ao pesquisar notas: " + retornoConsulta.getCStat() + " - " + retornoConsulta.getXMotivo());
                }
            }



            populaLista(empresa, retornoConsulta, ListaNotasManifestar, ListaNotasSalvar);

            verificaNotas = !retornoConsulta.getUltNSU().equals(retornoConsulta.getMaxNSU());
            empresa.setNsu(retornoConsulta.getUltNSU());
        }

        empresaRepository.save(empresa);
        nfService.salvarNf(ListaNotasSalvar);
        manifestaListaNotas(ListaNotasManifestar,empresa,configuracao);
    }

    private static void populaLista(Empresa empresa, RetDistDFeInt retornoConsulta, List<String> ListaNotasManifestar, List<NotasFiscais> ListaNotasSalvar) throws IOException, JAXBException {
        for (RetDistDFeInt.LoteDistDFeInt.DocZip doc : retornoConsulta.getLoteDistDFeInt().getDocZip()) {
            String xml = XmlNfeUtil.gZipToXml(doc.getValue());
            log.info("Xml: " + xml);
            log.info("Xml: " + doc.getSchema());
            log.info("Xml: " + doc.getNSU());

            switch (doc.getSchema()) {
                case "resNFe_v1.01.xsd":
                    ResNFe resNFe = XmlNfeUtil.xmlToObject(xml, ResNFe.class);
                    String chave = resNFe.getChNFe();
                    ListaNotasManifestar.add(chave);
                    break;
                case "procNFe_v4.00.xsd":
                    TNfeProc nfe = XmlNfeUtil.xmlToObject(xml, TNfeProc.class);
                    NotasFiscais notasFiscais = new NotasFiscais();
                    notasFiscais.setChave(nfe.getNFe().getInfNFe().getId().substring(3));
                    notasFiscais.setEmpresa(empresa);
                    notasFiscais.setSchema(doc.getSchema());
                    notasFiscais.setCnpjEmitente(nfe.getNFe().getInfNFe().getEmit().getCNPJ());
                    notasFiscais.setNomeEmitente(nfe.getNFe().getInfNFe().getEmit().getXNome());
                    notasFiscais.setValor(new BigDecimal(nfe.getNFe().getInfNFe().getTotal().getICMSTot().getVNF()));
                    notasFiscais.setXml(compactarXML(xml));
                    ListaNotasSalvar.add(notasFiscais);
                    break;
                default:
                    break;
            }
        }
    }

    private void manifestaListaNotas(List<String> chaves,Empresa empresa, ConfiguracoesNfe configuracoesNfe) throws NfeException {            //Agora o evento pode aceitar uma lista de Manifestções para envio em Lote.
        for (String chave : chaves) {

            Evento manifesta = new Evento();
            manifesta.setChave(chave);
            manifesta.setCnpj(empresa.getCpfCnpj());
            manifesta.setMotivo("Manifestação Notas Resumo");
            manifesta.setDataEvento(LocalDateTime.now());
            manifesta.setTipoManifestacao(ManifestacaoEnum.CIENCIA_DA_OPERACAO);

            //Monta o Evento de Manifestação
            TEnvEvento enviEvento = ManifestacaoUtil.montaManifestacao(manifesta, configuracoesNfe);

            //Envia o Evento de Manifestação
            TRetEnvEvento retorno = Nfe.manifestacao(configuracoesNfe, enviEvento, true);

            if(!retorno.getCStat().equals(StatusEnum.EVENTO_VINCULADO)){
                log.error("Erro ao manifestar chave: "
                        + chave + " com erro: "
                        + retorno.getCStat() + " - "
                        + retorno.getXMotivo());
            }
        }
    }
}

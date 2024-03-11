package com.financeiro.nfedfe.util;

import br.com.swconsultoria.certificado.Certificado;
import br.com.swconsultoria.certificado.CertificadoService;
import br.com.swconsultoria.certificado.exception.CertificadoException;
import br.com.swconsultoria.nfe.dom.ConfiguracoesNfe;
import br.com.swconsultoria.nfe.dom.enuns.EstadosEnum;
import com.financeiro.nfedfe.entity.Empresa;

public class ConfiguracoesNFE {

    public static ConfiguracoesNfe criarConfiguracao(Empresa empresa) throws CertificadoException {

        Certificado certificado = CertificadoService.certificadoPfxBytes(empresa.getCertificadoDigital(),empresa.getSenhaCertificadoDigital());

        return ConfiguracoesNfe.criarConfiguracoes(
                EstadosEnum.valueOf(empresa.getUf()),
                empresa.getAmbiente(),
                certificado,
                "C:\\Users\\godmn\\Desktop\\nfe-dfe\\schemas");
    }
}

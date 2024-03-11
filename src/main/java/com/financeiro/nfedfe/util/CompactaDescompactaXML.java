package com.financeiro.nfedfe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

public class CompactaDescompactaXML {

    public static byte[] compactarXML(String xml) throws IOException {
        byte[] data = xml.getBytes(StandardCharsets.UTF_8);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);

        try (DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(outputStream, new Deflater())) {
            deflaterOutputStream.write(data);
        }

        return outputStream.toByteArray();
    }
}

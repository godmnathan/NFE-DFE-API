package com.financeiro.nfedfe.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class FileToBase64 {
    private static String fileToBase64(String filePath) throws IOException {
        byte[] fileContent = Files.readAllBytes(new File(filePath).toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }
}

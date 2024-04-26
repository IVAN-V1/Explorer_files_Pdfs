package com.ministudio.explorer_files_pdfs;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class explorer {

    public static List<File> getPdfFiles() {

        List<File> pdfFiles = new ArrayList<>();
        File documentsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS); // Carpeta de documentos

        File[] files = documentsDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".pdf")) {
                    pdfFiles.add(file);
                }
            }
        }
        return pdfFiles;
    }

}

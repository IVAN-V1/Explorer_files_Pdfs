package com.ministudio.explorer_files_pdfs.view;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.appbar.MaterialToolbar;
import com.ministudio.explorer_files_pdfs.R;

public class activity_pdf_viewer extends AppCompatActivity {


    private PDFView pdfView;
    Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);



        pdfView = findViewById(R.id.pdf_viewer);

        pdfUri = getIntent().getParcelableExtra("PDF_URI");
        loadPdf(pdfUri);



        MaterialToolbar toolbar = findViewById(R.id.pdf_viewer_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        toolbar = findViewById(R.id.pdf_viewer_toolbar);
        String pdfFileName = getFileNameFromUri(pdfUri);

        // Establece el nombre del archivo como título en el Appbar
        toolbar.setTitle(pdfFileName);

    }



    private void loadPdf(Uri pdfUri) {
        pdfView.fromUri(pdfUri)
                .load();
        pdfView.setBackgroundColor(getResources().getColor(R.color.gris)); // Establece el fondo blanco


    }


    // Método para obtener el nombre del archivo desde el Uri
    @SuppressLint("Range")
    private String getFileNameFromUri(Uri uri) {

        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;


    }
}
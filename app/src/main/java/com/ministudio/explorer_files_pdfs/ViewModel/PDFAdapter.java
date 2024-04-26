package com.ministudio.explorer_files_pdfs.ViewModel;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ministudio.explorer_files_pdfs.R;
import com.ministudio.explorer_files_pdfs.view.activity_pdf_viewer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.PDFViewHolder>{

    private List<File> pdfFiles;

    private Context context;

    public PDFAdapter(List<File> pdfFiles, Context context) {
        this.pdfFiles = pdfFiles;
        this.context= context;
    }


    @NonNull
    @Override
    public PDFViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_view, parent, false);

        return new PDFViewHolder(view);
    }





    @Override
    public void onBindViewHolder(@NonNull PDFViewHolder holder, int position) {
        File pdfFile = pdfFiles.get(position);


        // Establece el nombre del archivo PDF
        holder.nameTextView.setText(pdfFile.getName());

        // Obtiene el tamaño del archivo en KB y muestra en el TextView
        long fileSizeInBytes = pdfFile.length();
        long fileSizeInKB = fileSizeInBytes / 1024; // Tamaño en KB
        holder.pesoTextView.setText("Tamaño: " + fileSizeInKB + " KB");



        // Obtiene la fecha de modificación y muestra en el TextView
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = sdf.format(pdfFile.lastModified());
        holder.fechaTextView.setText(formattedDate);



// Asigna un clic para abrir la actividad para mostrar el PDF
        holder.frameLayout.setOnClickListener(v -> {
            Context context = v.getContext();
            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);

            Intent intent = new Intent(context, activity_pdf_viewer.class);
            intent.putExtra("PDF_URI", uri);
            context.startActivity(intent);
        });




        // Asigna un clic para abrir la actividad para mostrar el PDF
        holder.image_BCompartir.setOnClickListener(v -> {
            // Crea un Uri para el archivo PDF
            Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);

            // Crea un Intent para compartir el archivo PDF
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.setType("application/pdf");

            // Inicia la actividad para compartir
            context.startActivity(Intent.createChooser(shareIntent, "Compartir PDF"));
        });




        // Asigna un clic para abrir el diálogo de confirmación para borrar el PDF
        holder.image_Borrar.setOnClickListener(v -> {

                    new MaterialAlertDialogBuilder(context)
                            .setTitle("Confirmación")
                            .setIcon(R.drawable.danger)
                            .setMessage("¿Quieres borrar este PDF?")
                            .setPositiveButton("Sí", (dialog, which) -> {

                                // Borra el archivo PDF
                                boolean deleted = pdfFile.delete();


                                if (deleted) {
                                    // Si se ha borrado correctamente, muestra un mensaje de éxito
                                    Toast.makeText(context, "PDF borrado exitosamente", Toast.LENGTH_SHORT).show();

                                    pdfFiles.remove(pdfFile);
                                    notifyDataSetChanged();


                                } else {
                                    // Si no se pudo borrar, muestra un mensaje de error
                                    Toast.makeText(context, "Error al borrar el PDF", Toast.LENGTH_SHORT).show();
                                }


                            })


                            .setNegativeButton("No", (dialog, which) -> {
                                // Cierra el diálogo sin hacer nada
                                dialog.dismiss();
                            })
                            .show();
                }

        );



    }






    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }


    public static class PDFViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView pesoTextView;
        TextView fechaTextView;
        FrameLayout frameLayout;

        ImageButton image_BCompartir;
        ImageButton image_Borrar;

        public PDFViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            pesoTextView=itemView.findViewById(R.id.size);
            fechaTextView=itemView.findViewById(R.id.fecha);
            frameLayout=itemView.findViewById(R.id.frame_);
            image_BCompartir =itemView.findViewById(R.id.campartir_pdf);
            image_Borrar =itemView.findViewById(R.id.Borrar);

        }
    }



}



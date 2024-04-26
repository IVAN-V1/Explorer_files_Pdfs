package com.ministudio.explorer_files_pdfs.view

import android.content.Context
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.ministudio.explorer_files_pdfs.ViewModel.PDFAdapter
import com.ministudio.explorer_files_pdfs.R
import com.ministudio.explorer_files_pdfs.explorer

class MainActivity : AppCompatActivity() {

    val  calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)



        val animacion=findViewById<LottieAnimationView>(R.id._no_hay_pdf)
        val mensaje=findViewById<TextView>(R.id.text_pdfs)



        val pdfFiles = explorer.getPdfFiles()
// Verifica cuántos archivos PDF hay
        val numberOfPDFs = pdfFiles.size

        val pdfAdapter = PDFAdapter(
            pdfFiles,
            this
        )


// Establece el adaptador solo si hay archivos PDF disponibles
        if (numberOfPDFs > 0) {

            val recyclerView = findViewById<RecyclerView>(R.id.archivosPDF)
            recyclerView.layoutManager = LinearLayoutManager(this)

            recyclerView.adapter = pdfAdapter



            animacion.setVisibility(View.INVISIBLE)
            mensaje.setVisibility(View.INVISIBLE)

        } else {


            animacion.setVisibility(View.VISIBLE)
            mensaje.setVisibility(View.VISIBLE)


        }




        val appBar: MaterialToolbar = findViewById(R.id.Toolbar_Material)
        appBar.setNavigationOnClickListener {
            onBackPressed()
        }


        appBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.organizar_fecha -> {
                    // Crea un MaterialDatePicker para la selección de fecha
                    val datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Selecciona una fecha")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build()

                    // Configura la acción cuando se selecciona una fecha
                    datePicker.addOnPositiveButtonClickListener { selection ->
                        // Ajusta la zona horaria al valor UTC antes de obtener la fecha
                        calendar.setTimeZone(TimeZone.getTimeZone("UTC"))
                        calendar.setTimeInMillis(selection)

                    }

                    datePicker.show(supportFragmentManager, "DatePicker")

                    true
                }
                else -> false
            }
        }





        appBar.setNavigationIcon(R.drawable.otras)
        val sharedPreferences = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()




        val chipNombre: Chip = findViewById(R.id.nombre)
        val chipFecha: Chip = findViewById(R.id.fecha)


        chipNombre.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pdfFiles.sortBy { it.name }
                editor.putBoolean("nombreSelected", true)
                editor.apply()
            } else {
                editor.putBoolean("nombreSelected", false)
                editor.apply()
            }

            pdfAdapter.notifyDataSetChanged()
        }



        chipFecha.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pdfFiles.sortByDescending { it.lastModified() }
                editor.putBoolean("fechaSelected", true)
                editor.apply()
            } else {
                editor.putBoolean("fechaSelected", false)
                editor.apply()
            }
            pdfAdapter.notifyDataSetChanged()
        }


// Recuperar el estado de los Chips al iniciar la actividad
        val nombreSelected = sharedPreferences.getBoolean("nombreSelected", false)
        val fechaSelected = sharedPreferences.getBoolean("fechaSelected", false)

        chipNombre.isChecked = nombreSelected
        chipFecha.isChecked = fechaSelected



    }
}
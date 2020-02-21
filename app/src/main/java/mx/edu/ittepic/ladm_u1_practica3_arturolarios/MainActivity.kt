package mx.edu.ittepic.ladm_u1_practica3_arturolarios

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    var vector : Array<Int> = Array(10, {0})

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        grantPermission()

        btnAssign.setOnClickListener {
            insertValue()
        }

        btnShow.setOnClickListener {
            showArray()
        }

        btnSave.setOnClickListener {
            saveArray()
        }

        btnOpen.setOnClickListener {
            openArray()
        }
    }

    fun insertValue()
    {
        try
        {
            if(txtValue.text.toString().isEmpty())
            {
                message("ERROR", "No deje el campo del valor vacio")
                return
            }

            if(txtPosition.text.toString().isEmpty())
            {
                message("ERROR", "No deje el campo de posición vacio")
                return
            }

            var position = txtPosition.text.toString().toInt()
            var value = txtValue.text.toString().toInt()

            if(position < 0 || position > 9)
            {
                message("ERROR", "La posición del vector debe estar entre 0 y 9")
                return
            }

            vector[position] = value
            message("ATENCION", "Se insertó correctamente el valor")
        }
        catch(error : NumberFormatException)
        {
            message("Error", "Debes de ingresar solo numeros enteros")
        }
    }

    fun showArray()
    {
        var cad = convertToFormat(true)

        message("VECTOR", cad.replace("$", " - "))
    }

    fun grantPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                                                            Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }
    }

    fun thereisSD() : Boolean
    {
        var estado = Environment.getExternalStorageState()

        if(estado != Environment.MEDIA_MOUNTED)
        {
            return false
        }

        return true
    }

    fun openArray()
    {
        try
        {
            if(!thereisSD())
            {
                message("ATENCION!!", "No se encontró la memoria externa")
                return
            }

            if(txtNameToOpen.text.toString().isEmpty())
            {
                message("ERROR", "No deje el campo nombre vacio")
                return
            }

            var name = txtNameToOpen.text.toString()
            var path = Environment.getExternalStorageDirectory()
            var dataFile = File(path.absolutePath, name)
            var input = BufferedReader(InputStreamReader(FileInputStream(dataFile)))

            var data = input.readLine()
            convertToFormat(false, data)

            input.close()
            message("ATENCION", "Se abrió correctamente el archivo")
        }
        catch(error : IOException)
        {
            message("ERROR!", error.message.toString())
        }
    }

    fun saveArray()
    {
        try
        {
            if(!thereisSD())
            {
                message("ATENCION!!", "No se encontró la memoria externa")
                return
            }

            if(txtNameToSave.text.toString().isEmpty())
            {
                message("ERROR", "No deje el campo nombre vacio")
                return
            }

            var name = txtNameToSave.text.toString()
            var path = Environment.getExternalStorageDirectory()
            var dataFile = File(path.absolutePath, name)
            var output = OutputStreamWriter(FileOutputStream(dataFile))

            var data = convertToFormat(true)

            output.write(data)
            output.flush()
            output.close()

            message("ATENCION!", "Se guardó correctamente")
        }
        catch(error : IOException)
        {
            message("ERROR!", error.message.toString())
        }
    }

    fun convertToFormat(action : Boolean, data : String = "") : String
    {
        if (action)
        {
            var data = ""
            (0..9).forEach {
                if(it == 9)
                {
                    data += vector[it].toString()
                }
                else
                {
                    data += vector[it].toString() + "$"
                }
            }
            return data
        }

        var vector2 = data.split("$")

        (0..9).forEach {
            vector[it] = vector2[it].toInt()
        }

        return ""
    }

    fun message(title : String, text : String)
    {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(text)
            .setPositiveButton("OK"){d, i->}
            .show()
    }
}

package it.matteopellegrino.ksll.sample.dialog

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText

/**
 * Custom [AlertDialog.Builder] with input text field
 *
 * @author Matteo Pellegrino matteo.pelle.pellegrino@gmail.com
 */
class AddLibDialogBuilder(context: Context): AlertDialog.Builder(context) {
    val input: EditText = EditText(context)

    init{
        setTitle("Add new library")
        input.inputType = InputType.TYPE_TEXT_VARIATION_URI
        setView(input)
        setNegativeButton("Cancel"){dialog, _ -> dialog.cancel() }
    }
}
package de.pse.kit.studywithme.ui.component

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePicker(
    context: Context,
    onChange: (String) -> Unit = {},
    singleLine: Boolean = true
) {
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date.value = "$dayOfMonth/$month/$year"
        }, year, month, day
    )

    /**
    MyApplicationTheme3() {
    var expanded by remember { mutableStateOf(false) }
    Box(
    modifier = Modifier
    .fillMaxSize()
    .wrapContentSize(Alignment.TopStart)
    ) {
    Text(
    date.value, modifier = Modifier
    .fillMaxWidth()
    .clickable(onClick = { datePickerDialog.show() })
    .background(MaterialTheme.colorScheme.surface)
    )
    DropdownMenu(
    expanded = expanded,
    onDismissRequest = { expanded = false },
    modifier = Modifier
    .fillMaxWidth()
    .background(MaterialTheme.colorScheme.surface)
    ) {
    datePickerDialog.show()
    }
    }
    }
     */

    MyApplicationTheme {
        var expanded by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = date.value,
            onValueChange = {
                date.value = it
                onChange(it)
            },
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.TopStart),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                textColor = androidx.compose.material.MaterialTheme.colors.onSurface,
                backgroundColor = androidx.compose.material.MaterialTheme.colors.surface,
                cursorColor = Color.Black,
                disabledTextColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant,
                focusedBorderColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant,
                focusedLabelColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant,
                unfocusedBorderColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant,
                unfocusedLabelColor = androidx.compose.material.MaterialTheme.colors.secondaryVariant
            ),
            label = {
                Text(
                    date.value, modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { datePickerDialog.show() })
                        .background(MaterialTheme.colorScheme.surface)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    datePickerDialog.show()
                }
            },
            singleLine = singleLine
        )
    }
}


@Preview
@Composable
fun DatePickerPreview() {
    DatePicker(context = LocalContext.current)
}
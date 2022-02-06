package de.pse.kit.studywithme.ui.component

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import java.util.*


@Composable
fun DatePicker (modifier: Modifier, context: Context) {
    val year: Int
    val month: Int
    val day: Int
    val color: Int = 4

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("") }
    val datePickerDialog = DatePickerDialog(
        context,
        color,
        { _: DatePicker, year, month, dayOfMonth ->
            date.value = "$dayOfMonth.${month + 1}.$year"
        },
        year,
        month,
        day
    )

    MyApplicationTheme3() {
        Card(
            border = BorderStroke(color = androidx.compose.ui.graphics.Color.Black, width = Dp.Hairline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .size(50.dp)
            ) {
                Text(
                    date.value, modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { datePickerDialog.show() }
                        .padding(24.dp), fontSize = 20.sp
                )
            }
        }
    }
}


@Preview
@Composable
fun DatePickerPreview() {
    DatePicker(LocalContext.current)
}
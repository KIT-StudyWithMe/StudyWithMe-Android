package de.pse.kit.studywithme.ui.component

import android.app.TimePickerDialog
import android.content.Context
import android.widget.TimePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.pse.kit.myapplication.ui.theme.MyApplicationTheme3
import java.util.*

@Composable
fun TimePicker(modifier: Modifier, context: Context) {
    val hour: Int
    val minute: Int

    val calendar = Calendar.getInstance()
    hour = calendar.get(Calendar.HOUR_OF_DAY)
    minute = calendar.get(Calendar.MINUTE)

    val time = remember { mutableStateOf("") }
    val timePickerDialog = TimePickerDialog(
        context,
        {_, hour: Int, minute: Int ->
            time.value = "$hour:$minute"
        },
        hour,
        minute,
        false
    )

    MyApplicationTheme3 {
        Card(
            border = BorderStroke(color = Color.Black, width = Dp.Hairline)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .size(50.dp)
            ) {
                Text(
                    time.value, modifier = androidx.compose.ui.Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .clickable { timePickerDialog.show() }
                        .padding(24.dp), fontSize = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun TimePickerPreview() {
    TimePicker(LocalContext.current)
}

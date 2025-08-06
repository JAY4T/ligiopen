package com.admin.ligiopen.utils.composables

import android.app.DatePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.admin.ligiopen.R
import com.admin.ligiopen.utils.screenWidth
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
val dateFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DatePicker(
    date: LocalDate,
    onChangeDate: (date: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val defaultEndLocalDate = LocalDate.now()
    val defaultEndMillis = defaultEndLocalDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()

    @RequiresApi(Build.VERSION_CODES.O)
    fun showDatePicker(isStart: Boolean) {
        val initialDate = LocalDate.now()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                if(selectedDate.isAfter(LocalDate.now())) {
                    Toast.makeText(context, "Invalid date", Toast.LENGTH_SHORT).show()
                } else {
                    onChangeDate(selectedDate)
                }
            },

            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        )

        defaultEndMillis?.let { datePicker.datePicker.maxDate = it }

        datePicker.show()
    }

    IconButton(onClick = { showDatePicker(true) }) {
        Icon(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            modifier = Modifier
                .size(screenWidth(x = 24.0))
        )
    }
}

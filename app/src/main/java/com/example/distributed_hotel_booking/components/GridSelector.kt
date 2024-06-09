package com.example.distributed_hotel_booking.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun GridSelector(
    rows: Int,
    columns: Int,
    selectedValue: Int,
    onValueSelected: (Int) -> Unit,
    cellContent: @Composable (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (i in 0 until rows) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (j in 0 until columns) {
                    val value = (i * columns) + j + 1
                    cellContent(value)
                }
            }
        }
    }
}

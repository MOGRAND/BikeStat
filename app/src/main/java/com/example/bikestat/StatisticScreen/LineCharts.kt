package com.example.bikestat.StatisticsScreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.bikestat.SideOperations.DoubleOperations
import com.example.bikestat.SideOperations.TimeConv
import com.example.bikestat.ui.theme.MainOrange
import java.util.Calendar

@Preview
@Composable
fun LineChartField() {
    val steps = 5
    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val currentDate = "${TimeConv.formatToTwoCharacters(day)}/${
        TimeConv.formatToTwoCharacters(month + 1)
    }/$year"
    val dateList = TimeConv.getFiveDaysDateList(currentDate = currentDate)
    val distanceList = RealmOperations.getAListOfFiveDaysDistance(dateList)
    Log.d("MyLog", distanceList[0].toFloat().toString())
    val pointData = listOf(
        Point(0f, (distanceList[4]).toFloat()),
        Point(1f, (distanceList[3]).toFloat()),
        Point(2f, (distanceList[2]).toFloat()),
        Point(3f, (distanceList[1]).toFloat()),
        Point(4f, (distanceList[0]).toFloat()),
    )

    val xAxisData = AxisData.Builder()
        .startDrawPadding(6.dp)
        .axisStepSize(80.dp)
        .backgroundColor(Color.White)
        .steps(pointData.size - 1)
        .labelData {
            val ind = 4 - it
            dateList[ind].substringBefore("/")
        }
        .labelAndAxisLinePadding(20.dp)
        .axisLineColor(MainOrange)
        .axisLabelColor(Color.Black)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.White)
        .labelAndAxisLinePadding(25.dp)
        .labelData { i ->
            val yScale = distanceList.max().toDouble() / steps
            DoubleOperations.roundDoubleToTwoDecimalPlaces(i * yScale.toDouble())
        }
        .axisLineColor(MainOrange)
        .axisLabelColor(Color.Black)
        .build()
    val lineChartData = LineChartData(
        paddingRight = 0.dp,
        containerPaddingEnd = 0.dp,
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointData,
                    LineStyle(
                        color = MainOrange,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = MainOrange
                    ),
                    SelectionHighlightPoint(color = MainOrange),
                    ShadowUnderLine(
                        alpha = 0.5f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MainOrange,
                                Color.Transparent
                            )
                        )
                    ),
                )
            )
        ),
        backgroundColor = Color.White,///
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color = MainOrange)

    )
    Row(modifier = Modifier.fillMaxWidth().height(200.dp).background(Color.White)){
        LineChart(modifier = Modifier.width(450.dp).height(300.dp).background(Color.White).padding(0.dp), lineChartData =lineChartData)
    }
}
package com.example.bikestat.HistoryScreen

import com.example.bikestat.SideOperations.DoubleOperations
import com.example.bikestat.SideOperations.StringOperations
import com.example.bikestat.SideOperations.TimeConv
import com.example.bikestat.ui.theme.MainOrange
import com.example.bikestat.ui.theme.dificultyEazyColor
import com.example.bikestat.ui.theme.dificultyHardColor
import com.example.bikestat.ui.theme.dificultyMediumColor
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bikestat.RealmDatabase.RealmOperations
import com.example.bikestat.RealmDatabase.RouteModel


@Composable
fun HistoryScreenView(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar()
        RouteHistoryColumn()
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MainOrange),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "История поездок", fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun RouteHistoryColumn() {
    val routeList = RealmOperations.queryListOfRouteHistoryFromRealm().reversed()
    if (routeList.size >= 1){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            routeList.forEach {routeHistory ->
                HistoryRouteCard(routeHistory)
            }
        }
    }else{
        Text(text = "Тут ничего нет")
    }
}

@Composable
fun HistoryRouteCard(routeHistory: RouteModel) {
    val isOpened = remember {
        mutableStateOf(false)
    }
    val difficultyColor = when (routeHistory.realDifficulty) {
        "Легко" -> {
            dificultyEazyColor
        }

        "Средне" -> {
            dificultyMediumColor
        }

        else -> {
            dificultyHardColor
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 6.dp, end = 6.dp, bottom = 3.dp, top = 3.dp)
            .clickable {
                isOpened.value = !isOpened.value
            },
        colors = CardDefaults.cardColors(
            containerColor = difficultyColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = StringOperations.cutTheString(routeHistory.title, 12), fontSize = 25.sp, color = Color.Black, fontWeight = FontWeight.Bold)
                Text(text = routeHistory.date, fontSize = 25.sp, color = Color.Black)
            }
            DataViewLine(
                firstMainData = "${DoubleOperations.roundDoubleToTwoDecimalPlaces(routeHistory.distanceInKM)} км",
                firstMirrorData = "Расстояние",
                secondMainData = TimeConv.formatTime(routeHistory.timeInSec),
                secondMirrorData = "Время"
            )
            AnimatedVisibility(
                visible = isOpened.value,) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)) {
                    DataViewLine(
                        firstMainData = "${DoubleOperations.roundDoubleToTwoDecimalPlaces(routeHistory.maximumSpeedInKMH)} км/ч",
                        firstMirrorData = "Макc. скорость",
                        secondMainData = "${DoubleOperations.roundDoubleToTwoDecimalPlaces(routeHistory.averageSpeedInKMH)} км/ч",
                        secondMirrorData = "Ср. скорость",
                    )
                    DataViewLine(
                        firstMainData = "${routeHistory.minPulse}/${routeHistory.maxPulse}/${routeHistory.avgPulse}",
                        firstMirrorData = "Пульс мин/макс/ср",
                        secondMainData = "${DoubleOperations.roundDoubleToTwoDecimalPlaces(routeHistory.spentCalories)} ккал",
                        secondMirrorData = "Калории",
                    )
                    DataViewLine(
                        firstMainData = routeHistory.estimatedDifficulty,
                        firstMirrorData = "Расчетная сложность",
                        secondMainData = routeHistory.realDifficulty,
                        secondMirrorData = "Реальная сложность",
                    )
                }
            }
        }
    }
}

@Composable
fun DataViewLine(firstMainData: String, firstMirrorData: String,secondMainData: String, secondMirrorData: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        DataViewField(mainData = firstMainData, mirrorData = firstMirrorData)
        DataViewField(mainData = secondMainData, mirrorData = secondMirrorData)
    }
}

@Composable
fun DataViewField(mainData: String, mirrorData: String) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(170.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = mainData, fontSize = 25.sp, color = Color.Black)
        Text(text = mirrorData, fontSize = 15.sp, color = Color.Black)
    }
}
package com.example.bikestat.StatisticScreen

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bikestat.HistoryScreen.DataViewLine
import com.example.bikestat.HomeScreen.HomeScreenViewModel
import com.example.bikestat.Navigation.ScreenRoutes
import com.example.bikestat.R
import com.example.bikestat.SideOperations.StringOperations
import com.example.bikestat.SideOperations.TimeConv
import com.example.bikestat.StatisticsScreen.LineChartField
import com.example.bikestat.StatisticsScreen.StatisticsScreenViewModel
import com.example.bikestat.ui.theme.MainOrange
import com.example.bikestat.ui.theme.MainTransparentBlack
import com.example.bikestat.ui.theme.dificultyEazyColor
import com.example.bikestat.ui.theme.dificultyHardColor
import com.example.bikestat.ui.theme.dificultyMediumColor
import java.util.Calendar
import java.util.Date

@Composable
fun StatisticsScreenView(navController: NavController) {
    val isCreatingPlan = remember {
        mutableStateOf(false)
    }
    val routesPlanList = remember {
        mutableStateOf(RealmOperations.queryListOfRoutePlanFromRealm().reversed().toMutableList())
    }


    Box(modifier = Modifier.fillMaxSize().background(Color.White), contentAlignment = Alignment.Center,) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar()
            StatisticColumn(isCreatingPlan = isCreatingPlan, routesPlanList = routesPlanList, navController = navController)
        }
    }
    if (isCreatingPlan.value) {
        CreatePlanConfirmField(
            isCreatingPlan = isCreatingPlan,
            routesPlanList = routesPlanList,
        )
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
        Text(text = "Статистика", fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun StatisticColumn(
    isCreatingPlan: MutableState<Boolean>,
    routesPlanList: MutableState<MutableList<RouteModel>>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        ScheduleFild()
        CreatePlanField(isCreatingPlan = isCreatingPlan)

        if (routesPlanList.value.size != 0) {
            routesPlanList.value.forEach {
                PlanRouteCard(planRoute = it, navController = navController)
            }
        } else {
            Text(text = "Тут ничего нет")
        }
    }
}

@Composable
fun ScheduleFild() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .background(Color.White)
            .padding(start = 6.dp, end = 6.dp, bottom = 3.dp, top = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White, contentColor = Color.White),
        border = BorderStroke(2.dp, MainOrange),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
            Text(text = "Расстояние за последние 5 суток:", fontSize = 20.sp, color = Color.Black, modifier = Modifier.padding(start = 6.dp))
            LineChartField()
            Text(text = "Рекомендованная сложность: ${StatisticsScreenViewModel.getARecommendedDifficulty()}", fontSize = 20.sp, color = Color.Black, modifier = Modifier.padding(start = 6.dp))
        }
    }
}

@Composable
fun CreatePlanField(isCreatingPlan: MutableState<Boolean>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(start = 6.dp, end = 6.dp, bottom = 3.dp, top = 3.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(2.dp, MainOrange),
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(text = "Запланировать поездку", fontSize = 20.sp, color = Color.Black)

            Card(
                modifier = Modifier
                    .size(50.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MainOrange
                )
            ) {
                IconButton(onClick = {
                    isCreatingPlan.value = true
                }, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        painter = painterResource(id = R.drawable.create_icon),
                        contentDescription = "BackButton",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun CreatePlanConfirmField(
    isCreatingPlan: MutableState<Boolean>,
    routesPlanList: MutableState<MutableList<RouteModel>>
) {
    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

    val planingRouteDistanceInKM = remember {
        mutableStateOf("")
    }
    val planingRouteTimeInMinutes = remember {
        mutableStateOf("")
    }
    val planingRouteDate = remember {
        mutableStateOf(
            "${TimeConv.formatToTwoCharacters(day)}/${
                TimeConv.formatToTwoCharacters(month + 1)
            }/$year"
        )
    }
    val planingRouteTitle = remember {
        mutableStateOf("")
    }
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MainTransparentBlack)
            .clickable {
                isCreatingPlan.value = false
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier
                .width(350.dp)
                .height(480.dp)
                .clickable { },
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "План поездки",
                    fontSize = 30.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
                TitleTextField(routePlanMutableTitle = planingRouteTitle)
                IntTextField(
                    routeIntDataMutable = planingRouteDistanceInKM,
                    title = "Расстояние(в км)"
                )
                IntTextField(routeIntDataMutable = planingRouteTimeInMinutes, title = "Время(в минутах)")

                DatePickerField(mutableDate = planingRouteDate)

                CompleteButton(
                    isCreatingPlan = isCreatingPlan,
                    planingTitle = planingRouteTitle,
                    planingDistance = planingRouteDistanceInKM,
                    planingTime = planingRouteTimeInMinutes,
                    planingDate = planingRouteDate,
                    routesPlanList = routesPlanList
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun IntTextField(routeIntDataMutable: MutableState<String>, title: String) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = routeIntDataMutable.value,
        onValueChange = { newText ->
            if ((newText.toDoubleOrNull() != null || newText == "") && newText.length < 25) {
                routeIntDataMutable.value = newText
            }
        },
        textStyle = TextStyle(fontSize = 20.sp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = MainOrange,
            unfocusedIndicatorColor = MainOrange

        ),
        label = { Text(text = title, color = MainOrange) },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TitleTextField(routePlanMutableTitle: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = routePlanMutableTitle.value,
        onValueChange = { newText ->
            if (newText.length < 25) {
                routePlanMutableTitle.value = newText
            }
        },
        textStyle = TextStyle(fontSize = 20.sp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            focusedIndicatorColor = MainOrange,
            unfocusedIndicatorColor = MainOrange

        ),
        label = { Text(text = "Назовите поездку", color = MainOrange) },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
    )
}

@Composable
fun CompleteButton(
    isCreatingPlan: MutableState<Boolean>,
    planingTitle: MutableState<String>,
    planingDistance: MutableState<String>,
    planingTime: MutableState<String>,
    planingDate: MutableState<String>,
    routesPlanList: MutableState<MutableList<RouteModel>>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(
            modifier = Modifier
                .width(150.dp)
                .height(50.dp)
        )
        Button(
            onClick = {
                StatisticsScreenViewModel.insertRouteToRealm(
                    title = planingTitle.value,
                    planingDistance = planingDistance.value,
                    planingTimeInMinutes = planingTime.value.toString(),
                    planingDate = planingDate.value,
                )
                routesPlanList.value =
                    RealmOperations.queryListOfRoutePlanFromRealm().reversed().toMutableList()
                Log.d("MyLog", "${routesPlanList.value}")
                isCreatingPlan.value = false
            },
            modifier = Modifier
                .width(170.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MainOrange
            ),
            shape = RoundedCornerShape(10.dp),
        ) {
            Text(text = "Сохранить", fontSize = 20.sp, color = Color.White)
        }
    }
}

@Composable
fun DatePickerField(
    mutableDate: MutableState<String>
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            mutableDate.value = "${TimeConv.formatToTwoCharacters(dayOfMonth)}/${
                TimeConv.formatToTwoCharacters(month + 1)
            }/$year"
        }, year, month, day
    )


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Button(
            onClick = {
                datePickerDialog.show()
            },
            modifier = Modifier
                .width(180.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(2.dp, MainOrange)
        ) {
            Text(text = "Выбрать дату", fontSize = 20.sp, color = MainOrange)
        }
        Text(
            text = mutableDate.value,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.width(130.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlanRouteCard(planRoute: RouteModel, navController: NavController) {
    val isOpened = remember{
        mutableStateOf(false)
    }
    val difficultyColor = when (planRoute.estimatedDifficulty) {
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
            .combinedClickable(
                onClick = { isOpened.value = !isOpened.value },
                onLongClick = {
                    HomeScreenViewModel.mainedRoute = planRoute
                    navController.navigate(route = ScreenRoutes.HomeScreen.route)
                }
            )

            .padding(start = 6.dp, end = 6.dp, bottom = 3.dp, top = 3.dp),
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
                Row(modifier = Modifier.width(170.dp), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = StringOperations.cutTheString(planRoute.title, 11),
                        fontSize = 25.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Row(modifier = Modifier.width(170.dp), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = planRoute.date,
                        fontSize = 25.sp,
                        color = Color.Black,
                    )
                }

            }
            DataViewLine(
                firstMainData = "${planRoute.planingDistanceInKM} км",
                firstMirrorData = "Расстояние",
                secondMainData = TimeConv.formatTime(
                    planRoute.planingTimeInSec.toString().substringBefore(".").toInt()
                ),
                secondMirrorData = "Время",
            )
            AnimatedVisibility(visible = isOpened.value) {
                DataViewLine(
                    firstMainData = planRoute.estimatedDifficulty,
                    firstMirrorData = "Расчетная сложность",
                    secondMainData = "${planRoute.planingAverageSpeedInKMH} км/ч",
                    secondMirrorData = "Средняя скорость"
                )
            }
        }
    }
}








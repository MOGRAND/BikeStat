package com.example.bikestat.HomeScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.bikestat.Location.getLocation
import com.example.bikestat.R
import com.example.bikestat.RealmDatabase.RouteModel
import com.example.bikestat.SideOperations.BitmapDescriptorConv
import com.example.bikestat.SideOperations.DoubleOperations
import com.example.bikestat.SideOperations.TimeConv
import com.example.bikestat.SideOperations.VectorOperations
import com.example.bikestat.ui.theme.MainOrange
import com.example.bikestat.ui.theme.MainTransparentBlack
import com.example.bikestat.ui.theme.MainTransparentBlue
import com.example.bikestat.ui.theme.MainTransparentBlueForCicrlce
import com.google.android.gms.maps.MapView

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraMoveStartedReason
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun HomeScreenView(navController: NavController) {

    val context = LocalContext.current

    val mainedRoute = HomeScreenViewModel.mainedRoute

    //Data Mutable State
    val currentLocation = remember {
        mutableStateOf(LatLng(55.7, 37.6))
    }
    val routeMutableTitle = remember {
        mutableStateOf("")
    }
    val routeMutableList = remember {
        mutableStateOf(mutableListOf<LatLng>())
    }
    val speedMutableList = remember {
        mutableStateOf(mutableListOf(0.0))
    }
    val newLocation = getLocation(context = context)

    val averageSpeedInKMH = remember {
        mutableStateOf(0.0)
    }
    val timeInSec = remember {
        mutableStateOf(0)
    }
    val distanceInKM = remember {
        mutableStateOf(0.0)
    }

    // Boolean Mutable State
    val isSaving = remember {
        mutableStateOf(false)
    }
    val isBuildingRoute = remember {
        mutableStateOf(false)
    }
    val isCameraLocked = remember {
        mutableStateOf(false)
    }
    val isConfirmed = remember {
        mutableStateOf(if (mainedRoute.isPlaning == true) true else false)
    }


    // Google Maps Mutable State
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLocation.value, 17f)
    }

    if (newLocation.latitude != 0.0 && newLocation.longitude != 0.0) {
        currentLocation.value = LatLng(newLocation.latitude, newLocation.longitude)
    }

    LaunchedEffect(Unit) {
        while (true) {
            val localRouteMutableList = routeMutableList.value
            if (currentLocation.value.latitude != 55.7 && currentLocation.value.longitude != 37.6 && isBuildingRoute.value) {
                timeInSec.value += 1
                localRouteMutableList += LatLng(
                    currentLocation.value.latitude,
                    currentLocation.value.longitude
                )
            }
            routeMutableList.value = localRouteMutableList
            if (routeMutableList.value.size >= 2 && isBuildingRoute.value) {
                val currentDistance = VectorOperations.getVectorLengthInKMByTwoCord(
                    routeMutableList.value[routeMutableList.value.size - 2],
                    routeMutableList.value[routeMutableList.value.size - 1]
                )
                distanceInKM.value += currentDistance
                val localSpeedMutableList = speedMutableList.value
                localSpeedMutableList += currentDistance * 3600
                speedMutableList.value = localSpeedMutableList
            }
            if (isBuildingRoute.value && timeInSec.value != 0) {
                averageSpeedInKMH.value = (distanceInKM.value / timeInSec.value * 3600)
            }
            delay(1000) // Задержка в 1 секунду
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Bottom,
        ) {
            GoogleMapsView(
                currentLocation = currentLocation,
                routeList = routeMutableList,
                cameraPositionState = cameraPositionState,
                isCameraLocked = isCameraLocked,
            )
            TripDataField(
                averageSpeedInKMH = averageSpeedInKMH,
                timeInSec = timeInSec,
                distanceInKM = distanceInKM
            )
            StartAndConfirmButton(
                isBuildingRoute = isBuildingRoute,
                isSaving = isSaving,
                isCameraLocked = isCameraLocked
            )
        }
    }
    if (isSaving.value) {
        ConfirmSaveField(
            navController = navController,
            isSaving = isSaving,
            routeMutableTitle = routeMutableTitle,
            routeMutableDistanceInKM = distanceInKM,
            routeMutableTimeInSec = timeInSec,
            routeMutableAverageSpeed = averageSpeedInKMH,
            routeMaximumSpeed = speedMutableList.value.max(),
            speedMutableList = speedMutableList,
            routeMutableList = routeMutableList,
            route = mainedRoute
        )
    }
    if (isConfirmed.value) {
        ConfirmField(
            isConfirmed = isConfirmed,
            route = mainedRoute,
            isBuildingRoute = isBuildingRoute)
    }
}

@Composable
fun GoogleMapsView(
    currentLocation: MutableState<LatLng>,
    routeList: MutableState<MutableList<LatLng>>,
    cameraPositionState: CameraPositionState,
    isCameraLocked: MutableState<Boolean>
) {
    val context = LocalContext.current

    if (isCameraLocked.value) {
        LaunchedEffect(currentLocation.value) {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(currentLocation.value, 18f)
            delay(5000)//задержка 5 секнд
        }
    }




    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(570.dp), // 400
        cameraPositionState = cameraPositionState
    ) {
        if (cameraPositionState.position.zoom < 15f) {
            Marker(
                state = MarkerState(currentLocation.value),
                title = "Ваши координаты",
                snippet = "${DoubleOperations.roundDoubleToTwoDecimalPlaces(currentLocation.value.latitude)} / ${DoubleOperations.roundDoubleToTwoDecimalPlaces(currentLocation.value.longitude)}",
                icon = BitmapDescriptorConv.bitmapDescriptorFromVector(
                    context,
                    R.drawable.user_icon
                )
            )
        }

        if (cameraPositionState.position.zoom >= 15f) {
            Circle(
                center = currentLocation.value,
                fillColor = MainTransparentBlueForCicrlce,
                radius = 50.0,
                strokeColor = MainTransparentBlue,
                strokeWidth = 10.0f
            )
        }

        Polyline(
            points = routeList.value.toList(),
            color = MainTransparentBlue,
            width = 30.0f
        )
    }
}

@Composable
fun StartAndConfirmButton(
    isBuildingRoute: MutableState<Boolean>,
    isSaving: MutableState<Boolean>,
    isCameraLocked: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray,
                contentColor = Color.White
            )
        ) {
            IconButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    isCameraLocked.value = !isCameraLocked.value
                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = if (isCameraLocked.value == true) R.drawable.locked_icon else R.drawable.unlocked_icon),
                    contentDescription = null
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.Bottom)
                .padding(12.dp)
                .size(90.dp),
            shape = CircleShape,
            containerColor = MainOrange,
            onClick = {
                isBuildingRoute.value = !isBuildingRoute.value
            }
        ) {
            Text(
                text = if (isBuildingRoute.value) "СТОП" else "СТАРТ",
                fontSize = 20.sp,
                color = Color.White
            )
        }
        Card(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray,
                contentColor = Color.White
            )
        ) {
            IconButton(
                modifier = Modifier.fillMaxSize(),
                onClick = {
                    isSaving.value = true

                }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.confirm_icon),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun TripDataField(
    averageSpeedInKMH: MutableState<Double>,
    timeInSec: MutableState<Int>,
    distanceInKM: MutableState<Double>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DataField(
            dataMessage = DoubleOperations.roundDoubleToTwoDecimalPlaces(averageSpeedInKMH.value),
            descriptionMessage = "км.ч"
        )
        DataField(dataMessage = TimeConv.formatTime(timeInSec.value), descriptionMessage = "")
        DataField(
            dataMessage = DoubleOperations.roundDoubleToTwoDecimalPlaces(distanceInKM.value),
            descriptionMessage = "км"
        )
    }
}

@Composable
fun DataField(dataMessage: String, descriptionMessage: String) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .wrapContentWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            text = dataMessage,
            fontSize = 40.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(text = descriptionMessage, fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun ConfirmSaveField(
    navController: NavController,
    isSaving: MutableState<Boolean>,
    routeMutableTitle: MutableState<String>,
    routeMutableDistanceInKM: MutableState<Double>,
    routeMutableTimeInSec: MutableState<Int>,
    routeMutableAverageSpeed: MutableState<Double>,
    routeMaximumSpeed: Double,
    speedMutableList: MutableState<MutableList<Double>>,
    routeMutableList: MutableState<MutableList<LatLng>>,
    route: RouteModel

) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MainTransparentBlack)
            .clickable {
                isSaving.value = false
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Card(
            modifier = Modifier
                .width(350.dp)
                .height(250.dp)
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
                    text = "Закончить поездку?",
                    fontSize = 30.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
                TitleTextField(routeMutableTitle = routeMutableTitle)


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
                            HomeScreenViewModel.insertOrUpdateRouteToRealm(
                                isPlaning = route.isPlaning,
                                title = routeMutableTitle.value,
                                distanceInKM = routeMutableDistanceInKM.value,
                                timeInSec = routeMutableTimeInSec.value,
                                averageSpeedInKMH = routeMutableAverageSpeed.value,
                                maximumSpeedInKMH = routeMaximumSpeed,
                                id = route.id
                            )

                            isSaving.value = false
                            routeMutableTitle.value = ""
                            routeMutableDistanceInKM.value = 0.0
                            routeMutableTimeInSec.value = 0
                            routeMutableAverageSpeed.value = 0.0
                            speedMutableList.value = mutableListOf(0.0)
                            routeMutableList.value = mutableListOf()


                        },
                        modifier = Modifier
                            .width(170.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainOrange
                        ),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(text = "Завершить", fontSize = 20.sp, color = Color.White)
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TitleTextField(routeMutableTitle: MutableState<String>) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        value = routeMutableTitle.value,
        onValueChange = { newText ->
            if (newText.length < 25) {
                routeMutableTitle.value = newText
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
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
    )
}

@Composable
fun TopBar(routeTitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MainOrange),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = routeTitle, fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun ConfirmField(
    isConfirmed: MutableState<Boolean>,
    route: RouteModel,
    isBuildingRoute: MutableState<Boolean>
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(MainTransparentBlack)
            .clickable {
                isConfirmed.value = false
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Card(
            modifier = Modifier
                .width(350.dp)
                .height(400.dp)
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
                    text = "Начать запланированную поездку?",
                    fontSize = 30.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Расчетная длина: ${route.planingDistanceInKM} км",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Расчетное время: ${TimeConv.formatTime(route.planingTimeInSec.toInt())}",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Расчетная сложность: ${route.estimatedDifficulty}",
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            isConfirmed.value = false
                            HomeScreenViewModel.mainedRoute = RouteModel()
                        },
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(2.dp, MainOrange)
                    ) {
                        Text(text = "Отмена", fontSize = 20.sp, color = MainOrange)
                    }
                    Button(
                        onClick = {
                            isConfirmed.value = false
                            isBuildingRoute.value = true
                        },
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MainOrange
                        ),
                        shape = RoundedCornerShape(10.dp),
                    ) {
                        Text(text = "Начать", fontSize = 20.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
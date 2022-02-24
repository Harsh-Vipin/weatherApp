package com.harsh.weatherforcaster

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import com.skydoves.landscapist.glide.GlideImage

import com.harsh.weatherforcaster.repository.Repository
import com.harsh.weatherforcaster.ui.theme.WeatherAppTheme


class MainActivity : ComponentActivity() {

private lateinit var repository:Repository

private lateinit var city:MutableState<String>
private lateinit var time:MutableState<String>


override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    MainPage()
                }
            }
        }


}





@Composable
fun MainPage(){

    city = rememberSaveable {
        mutableStateOf("kannur")
    }



    Column(modifier = Modifier
        .padding(start = 10.dp, end = 10.dp)
        .verticalScroll(rememberScrollState())) {
        SearchBar()
        WeatherInfo()

    }

}

@Composable
fun WeatherInfo(){

    repository = Repository(city.value)

    var CityText = rememberSaveable{ mutableStateOf("City")}
    var CountryText = rememberSaveable{ mutableStateOf("Country")}
    var temp = rememberSaveable{ mutableStateOf(0.0)}
    var tempFeelsLike = rememberSaveable{ mutableStateOf(0.0)}
    var wind = rememberSaveable{ mutableStateOf(0.0)}

    var condition = rememberSaveable{ mutableStateOf("Clear")}
    time = rememberSaveable{ mutableStateOf("0000-00-00 00:00")}
    var imageUrl = rememberSaveable{ mutableStateOf("")}

    repository.weatherLiveData.observe(this, Observer {
        condition.value = it.current.condition.text
        imageUrl.value = it.current.condition.icon
        CityText.value = it.location.name
        CountryText.value = it.location.country
        temp.value = it.current.tempC
        tempFeelsLike.value = it.current.feelslikeC
        wind.value = it.current.windMph

        time.value = it.location.localtime
        time.value = it.location.localtime

    })

    Row(modifier = Modifier.padding(10.dp)){
        Text(
            text = CityText.value+ ",",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
            modifier = Modifier.padding(start=4.dp)
        )
        Text(
            text = CountryText.value,
            fontWeight = FontWeight.Light,
            fontSize = 25.sp,
            modifier = Modifier.padding(start=3.dp)
        )
    }

    Card (
        shape = RoundedCornerShape(20.dp),
        backgroundColor = colorResource(R.color.Blue),
        contentColor = Color.White,
        modifier =Modifier.padding(15.dp)
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.padding(15.dp))

            GlideImage(
                imageModel = imageUrl.value.replace("//","https://"),
                modifier = Modifier.size(70.dp,70.dp)
            )
            Text(
                text = condition.value,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                modifier = Modifier.padding(bottom = 5.dp)
            )
            Text(
                text = time.value,
                fontWeight = FontWeight.Light,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(
                text = "${temp.value.toInt()}°",
                fontWeight = FontWeight.SemiBold,
                fontSize = 100.sp
            )

            Spacer(modifier = Modifier.padding(5.dp))

            Spacer(modifier = Modifier
                .padding(bottom = 4.dp, top = 5.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(colorResource(id = R.color.Blue)))
            Column(modifier = Modifier.fillMaxWidth(1f)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    WeatherDetailBox(R.drawable.wind,"WIND","${wind.value} km/j")
                    WeatherDetailBox(R.drawable.temperature,"FEELS LIKE","${tempFeelsLike.value}°")
                }
                Spacer(modifier = Modifier
                    .padding(bottom = 8.dp, top = 0.dp, start = 0.dp))
                Spacer(modifier = Modifier
                    .padding(bottom = 0.dp, top = 0.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colorResource(id = R.color.Blue)))
                Spacer(modifier = Modifier
                    .padding(bottom = 5.dp, top = 0.dp, start = 0.dp))

                Spacer(modifier = Modifier
                    .padding(bottom = 100.dp, top = 0.dp, start = 0.dp))

            }
        }

    }
}

@Composable
fun SearchBar(){
    val focusRequester = remember {
        FocusRequester()
    }
    val searchBar = rememberSaveable {
        mutableStateOf(false)
    }

    val  searchTextTop = rememberSaveable {
        mutableStateOf("")
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            searchBar.value = !searchBar.value
            if(!searchBar.value && city.value.isEmpty()){
                city.value = searchTextTop.value.replace(" ","_")
            }
        }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_search_24),
                    contentDescription = "SEARCH"
                )
        }

        if(searchBar.value){

            OutlinedTextField(
                value = searchTextTop.value ,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(R.color.Blue)
                ),
                onValueChange = {
                    searchTextTop.value = it
                                } ,
                shape = RoundedCornerShape(percent = 50),
                modifier = Modifier
                    .padding(0.dp)
                    .focusRequester(
                        focusRequester
                    )
                    .height(45.dp)
                    .weight(1f),
                placeholder = {
                    Text("Enter a city name",fontSize = 10.sp)
                },
                textStyle = TextStyle(fontSize = 10.sp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType =  KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        city.value = searchTextTop.value.replace(" ","_")
                        searchBar.value = false
                    }
                )

            )
        }




    }




}



@Composable
fun WeatherDetailBox(pic:Int, title:String, content:String){

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,modifier= Modifier

    ) {
            Image(painter = painterResource(id = pic ),
                contentDescription = title,
                modifier = Modifier
                    .width(25.dp)
                    .height(25.dp)
            )
            Spacer(modifier = Modifier.padding(4.dp))

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.W300,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.padding(3.dp))
                Text(
                    text = content,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )

            }
        }
}











}

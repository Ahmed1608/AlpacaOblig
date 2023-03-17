package com.example.ahmeaah_oblig2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ahmeaah_oblig2.ui.AlpacaViewModel


val districts = listOf("District 1", "District 2", "District 3")


@Composable
fun StartScreen(alpacaViewModel: AlpacaViewModel = viewModel()) {
    val alpacaUiState by alpacaViewModel.stateFlow.collectAsState()
    FinalScreen(
                alpacas = alpacaUiState.alpacas,
                alpacaUiState.selectedDistrict
            )


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalScreen(
    alpacas: List<Alpaca>?,
    selectedDistrict: Int,
    alpacaViewModel: AlpacaViewModel = viewModel(),
) {

    var expanded by remember { mutableStateOf(false) }





    Column {


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }

            ) {

                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = districts[selectedDistrict - 1],
                    onValueChange = { },
                    label = { Text("Velg district") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    }
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    districts.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {


                                when (selectionOption) {
                                    "District 1" -> {

                                        alpacaViewModel.updateDistrict(1)
                                    }
                                    "District 2" -> {
                                        alpacaViewModel.updateDistrict(2)

                                    }
                                    "District 3" -> {
                                        alpacaViewModel.updateDistrict(3)
                                    }
                                }
                                expanded = false
                            })

                    }
                }
            }


        }


        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (alpacas != null) {
                items(alpacas.size) { alpaca ->
                    Mycard(alpaca = alpacas[alpaca])
                }
            }
        }
    }
}

@Composable
fun Mycard(alpaca: Alpaca, alpacaViewModel: AlpacaViewModel = viewModel()) {

    Card(
        Modifier
            .fillMaxWidth(0.9f)
            .height(300.dp)
            .padding(top = 8.dp, bottom = 8.dp)


    ) {
        Box(

            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val state = alpacaViewModel.stateFlow.collectAsState()
                val districtVotes = state.value.votes



                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color(color = android.graphics.Color.parseColor(alpaca.color)))
                )
                Text(
                    fontSize = 20.sp,
                    text = alpaca.name
                )
                AsyncImage(
                    model = alpaca.img,
                    contentDescription = "alpacas",
                    contentScale = ContentScale.Crop,            // crop the image if it's not a square
                    modifier = Modifier
                        .size(170.dp)
                        .clip(CircleShape)                       // clip to the circle shape
                )

                Text(

                    fontSize = 20.sp,
                    text = "Leader: ${alpaca.leader}"
                )
                Text(

                    fontSize = 20.sp,
                    text = "Votes: ${districtVotes[alpaca.id.toInt() - 1]} - ${
                        state.value.percentage[alpaca.id.toInt()-1]
                        
                    }%"


                )

            }

        }


    }
    Spacer(Modifier.height(10.dp))

}
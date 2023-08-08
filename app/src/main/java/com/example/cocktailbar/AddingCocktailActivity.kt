package com.example.cocktailbar

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cocktailbar.ui.theme.CocktailBarTheme
import com.example.cocktailbar.ui.theme.SuperLightGrey

class AddingCocktailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CocktailBarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    addingCocktailScreen(onBackPressed = { onBackPressed() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addingCocktailScreen(onBackPressed: () -> Unit) {
    var showAddingDialog by remember { mutableStateOf(false) }
    var chips = remember { mutableStateListOf<String>() }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val recipe = remember { mutableStateOf("") }
    val nameError = remember { mutableStateOf(false) }

    val context = LocalContext.current
    var databaseHelper = DatabaseHelper(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(SuperLightGrey, shape = RoundedCornerShape(50.dp))
                .size(175.dp)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.placeholder),
                contentDescription = "placeholder",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        CocktailNameTextField(
            value = name.value,
            onValueChange = { name.value = it },
            isError = nameError.value
        )
        if (nameError.value) {
            Text(
                text = "Add title",
                color = Color.Red,
                modifier = Modifier
                    .padding(start = 16.dp, bottom = 4.dp)
                    .fillMaxWidth()
            )
        }
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            singleLine = false,
            label = { Text("Description", color = Color.Black) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            ),
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(150.dp)
        )
        Text(
            text = "Optional field",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 4.dp)
                .fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            chips.forEachIndexed { index, chipText ->
                Chip(text = chipText, onClick = { chips.removeAt(index) })
            }

            IconButton(onClick = {
                showAddingDialog = true
            }) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "add",
                    tint = Color.Blue
                )
            }
        }
        OutlinedTextField(
            value = recipe.value,
            onValueChange = { recipe.value = it },
            singleLine = false,
            label = { Text("Recipe", color = Color.Black) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Color.Black,
                cursorColor = Color.Black,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black
            ),
            modifier = Modifier
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(150.dp)
        )
        Text(
            text = "Optional field",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 4.dp)
                .fillMaxWidth()
        )
        Button(
            onClick = {
                if (name.value.isBlank()) {
                    nameError.value = true
                } else if (chips.isEmpty()) {
                    Toast.makeText(
                        context,
                        "Add ingredients!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val cocktail = CocktailEntity(
                        name = name.value,
                        description = description.value,
                        recipe = recipe.value,
                        ingredients = chips.toList()
                    )
                    databaseHelper.insertCocktail(cocktail)
                    onBackPressed()
                }
            },
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 5.dp)
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White,
            )
        ) {
            Text(text = "Save")
        }
        Button(
            onClick = { onBackPressed() },
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Blue, shape = MaterialTheme.shapes.extraLarge),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Blue,
            )
        ) {
            Text(text = "Cancel")
        }
    }

    if (showAddingDialog) {
        addingDialog(onCloseClicked = { showAddingDialog = false }, chips)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean
) {
    val borderColor = if (isError) Color.Red else Color.Black
    val textColor = if (isError) Color.Red else Color.Black
    val hintColor = if (isError) Color.Red else Color.Black

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Title", color = textColor) },
        placeholder = {
            Text(
                "Cocktail name",
                color = hintColor
            )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            cursorColor = textColor,
            focusedBorderColor = borderColor,
            unfocusedBorderColor = borderColor
        ),
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            .fillMaxWidth()
            .height(60.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun addingDialog(onCloseClicked: () -> Unit, chips: SnapshotStateList<String>) {
    val names = remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onCloseClicked.invoke() },
        title = {
            Text(
                text = "Add ingredient",
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = { onCloseClicked.invoke() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(1.dp, Color.Blue, shape = MaterialTheme.shapes.extraLarge),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Blue,
                )
            ) {
                Text(text = "Cancel")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    chips.add(names.value)
                    onCloseClicked.invoke()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White,
                )
            ) {
                Text(text = "Add")
            }
        },
        text = {
            OutlinedTextField(
                value = names.value,
                onValueChange = { names.value = it },
                label = { Text("Ingredient", color = Color.Black) },
                placeholder = { Text("Ingredient's name", color = Color.Black) },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black,
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(70.dp)
            )
        },
        containerColor = Color.White
    )
}

@Composable
fun Chip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(8.dp)
            .border(1.dp, Color.Blue, shape = MaterialTheme.shapes.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "  $text")
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.Blue
                )
            }
        }
    }
}
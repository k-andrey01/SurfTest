package com.example.cocktailbar

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cocktailbar.ui.theme.CocktailBarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CocktailBarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    nonEmptyScreen()
                }
            }
        }
    }
}

@Composable
fun mainScreenEmpty() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.cocktails_empty),
            contentDescription = "emptyListImg",
            modifier = Modifier.size(300.dp)
        )
        Text(
            text = "My Cocktails",
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp,
                lineHeight = 47.2.sp
            )
        )
        Spacer(modifier = Modifier.height(75.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val context = LocalContext.current
            Text(text = "Add your first\ncocktail here")
            Image(
                painter = painterResource(R.drawable.arrow),
                contentDescription = "arrowDown",
                modifier = Modifier.height(75.dp)
            )
            IconButton(
                onClick = {
                    val intent = Intent(context, AddingCocktailActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier.size(100.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "AddCocktail",
                    tint = Color.Blue,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }
}

@Composable
fun nonEmptyScreen() {
    val context = LocalContext.current
    val databaseHelper = DatabaseHelper(context)
    val cocktails = databaseHelper.getAllCocktails()

    LazyColumn {
        items(cocktails) { cocktail ->
            CocktailItem(cocktail)
        }
    }
}

@Composable
fun CocktailItem(cocktail: CocktailEntity) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Name: ${cocktail.name}")
            Text(text = "Description: ${cocktail.description}")
            Text(text = "Recipe: ${cocktail.recipe}")
            Text(text = "Ingredients: ${cocktail.ingredients.joinToString()}")
        }
    }
}
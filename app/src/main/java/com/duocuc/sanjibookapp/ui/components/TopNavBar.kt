package com.duocuc.sanjibookapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.res.painterResource
import com.duocuc.sanjibookapp.R
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(

    title: String = "El libro de Sanji",
    onBurgerClick: () -> Unit = {},
    backgroundColor: Color = Color(0xFF243B94),
    titleColor: Color = Color.White
) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    textAlign = TextAlign.Center
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBurgerClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menú",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = backgroundColor
        )
    )
}
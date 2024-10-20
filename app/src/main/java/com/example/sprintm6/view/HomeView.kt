package com.example.sprintm6.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.sprintm6.components.LoadingCard
import com.example.sprintm6.components.Space
import com.example.sprintm6.viewModel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: UserViewModel) {
    Scaffold(
        topBar ={
            TopAppBar(
                title = { Text( "simple Rest API + Room",color = Color.White) },
                actions ={
                    IconButton(
                        onClick = { viewModel.addUser() }
                    ) {
                        Icon(Icons.Default.Add, "Add", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF356337)
                )
            )
        }
    ){
        ContentHomeView(it, viewModel)
    }
}

@Composable
fun ContentHomeView(
    paddingValues: PaddingValues,
    viewModel: UserViewModel
) {
    val users by viewModel.users.collectAsState(listOf())
    val isLoading by viewModel.isLoading.collectAsState(false)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(8.dp)

    ) {
        var userCount = users.size
        if (isLoading) userCount++

        items(count = userCount) {index ->
            var auxIndex = index
            if (isLoading){
                if (auxIndex == 0)
                    return@items LoadingCard()
                auxIndex--
            }
            val user = users[auxIndex]
            Card(
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(1.dp),
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Image(
                        modifier = Modifier.size(50.dp),
                        painter = rememberAsyncImagePainter(
                            model =user.thumbnail
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight
                    )
                    Space()
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("${user.name} ${user.lastName}")
                        Text(user.city)
                    }
                    Space()
                    IconButton(
                        onClick ={ viewModel.deleteUser(user) }
                    ) {
                       Icon(Icons.Default.Delete, "Delete")
                    }
                }
            }
        }
    }
}
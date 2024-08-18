package com.example.contact.connect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contact.ui.theme.ContactTheme

@Composable
fun ConnectScreen(
    state: ConnectState,
    onAction: (ConnectAction) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAA3A6))
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFF44336),
                            Color(0xFFE91E63)
                        )
                    )
                )
        ){}
        Column(
            modifier = Modifier
                .offset(y = 150.dp)
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 60.dp, // Adjust these values as needed
                        topEnd = 60.dp
                    )
                )
                .background(Color.White)
                .padding(16.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(25.dp))
            Text(
                text = "Choose  a  name" ,
                style = TextStyle(
                    color = Color(0xFFE91E63) ,
                    fontSize = 30.sp ,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(60.dp))
            TextField(
                value = state.name ,
                onValueChange = {
                    onAction(ConnectAction.onNameChange(it))
                } ,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFFBBD2),
                    focusedLabelColor =  Color(0xFFE91E63),
                    focusedTextColor = Color(0xFFE91E63) ,
                    focusedIndicatorColor = Color(0xFFE91E63),
                    unfocusedLabelColor = Color(0xFFE91E63),
                    unfocusedIndicatorColor = Color(0xFFE91E63),
                    unfocusedContainerColor = Color(0xFFFFBBD2),
                    unfocusedTextColor = Color(0xFFFFBBD2),
                    unfocusedPlaceholderColor = Color(0xFFFF0E0E),
                    focusedPlaceholderColor = Color(0xFFFF0E0E),
                    disabledContainerColor = Color.White ,
                    cursorColor = Color(0xFFE91E63) ,

                ),
                placeholder = {
                    Text(text = "Enter a name")
                } ,
                modifier = Modifier.fillMaxWidth(0.9f),
            )
            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = {
                    onAction(ConnectAction.onConnectClick)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Transparent)
                    .padding(0.dp) // Adjust padding if needed
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFFF44336),
                                    Color(0xFFE91E63)
                                )
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Connect",
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(25.dp))
            if(state.errorMessage != null){
                Text(
                    text = state.errorMessage,
                    color = MaterialTheme.colorScheme.error ,
                    modifier = Modifier.align(Alignment.Start).padding(start = 25.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConnectScreenPreview(){
    ContactTheme {
        ConnectScreen(
            state = ConnectState(
                errorMessage = "Error not found 404"
            ) ,
            onAction = {}
        )
    }
}
package ui.elements

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
@Preview
fun PasswordField(onTextChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    var value by remember { mutableStateOf("") }
    TextField(value, onValueChange = {value = it; onTextChanged(it)}, visualTransformation = PasswordVisualTransformation(), modifier = modifier)
}
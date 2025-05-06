package ui.elements

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
@Preview
fun PasswordField(value: MutableState<String>, onTextChanged: (String) -> Unit, modifier: Modifier = Modifier) {
    TextField(value.value, onValueChange = {value.value = it; onTextChanged(it)}, visualTransformation = PasswordVisualTransformation(), modifier = modifier)
}
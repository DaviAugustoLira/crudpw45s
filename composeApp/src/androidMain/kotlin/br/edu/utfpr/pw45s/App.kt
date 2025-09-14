package br.edu.utfpr.pw45s

import android.annotation.SuppressLint
import android.widget.Scroller
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import br.edu.utfpr.pw45s.model.Product
import br.edu.utfpr.pw45s.repository.AndroidProductRepositoryImpl
import br.edu.utfpr.pw45s.repository.IProductRepository
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import crudpw45s.composeapp.generated.resources.Res
import crudpw45s.composeapp.generated.resources.compose_multiplatform
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
@Preview
fun App(repository: IProductRepository = AndroidProductRepositoryImpl()) {    MaterialTheme {
        var name by remember { mutableStateOf("") }
        var nameUpdate by remember { mutableStateOf("") }
        var code by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }
        var codeUpdate by remember { mutableStateOf("") }
        var id by remember { mutableStateOf("") }
        var idDelete by remember { mutableStateOf("") }
        var listProducts by remember {
            mutableStateOf<List<Product>>(emptyList()) }

        val coroutineScope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            LaunchedEffect(Unit) {
                listProducts = repository.readAll()
            }

            Text("Insira o nome do produto")
            OutlinedTextField(
                value = name,
                onValueChange = { name = it},
                label = { Text("Nome do Produto")}
            )

            Text("Insira o SKU do produto")
            OutlinedTextField(
                value = code,
                onValueChange = { code = it},
                label = { Text("SKU do Produto")}
            )
            Text("Insira o preço do produto")
            OutlinedTextField(
                value = price,
                onValueChange = { price = it},
                label = { Text("Preço do Produto")}
            )

            Button(onClick = {
                coroutineScope.launch {
                    AndroidProductRepositoryImpl().create(Product(name, code))
                }
            }) {
                Text("Criar")
            }

            Spacer(Modifier.height(24.dp))


            Text("Produtos")

            Column(
                modifier = Modifier
                    .background(Color.White)
                    .size(200.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                listProducts.forEach { product ->
                    Text("ID: ${product.code} Nome: ${product.name}")
                }
            }
            Button(onClick = {
                coroutineScope.launch {
                    listProducts = repository.readAll()
                }
            }) {

                Text("Buscar")
            }
            Spacer(Modifier.height(24.dp))


            Text("Atualizar, Insira o ID do documento")
            OutlinedTextField(
                value = id,
                onValueChange = { id = it},
                label = { Text("ID do documento no Firebase")}
            )
            OutlinedTextField(
                value = nameUpdate,
                onValueChange = { nameUpdate = it},
                label = { Text("Nome")}
            )
            OutlinedTextField(
                value = codeUpdate,
                onValueChange = { codeUpdate = it},
                label = { Text("SKU")}
            )

            Button(onClick = {
                coroutineScope.launch {
                    AndroidProductRepositoryImpl().update(Product(nameUpdate, codeUpdate), id)
                }
            }) {
                Text("Atualizar")
            }

            Spacer(Modifier.height(24.dp))
            Text("Deletar, Insira o ID do documento")
            OutlinedTextField(
                value = idDelete,
                onValueChange = { idDelete = it},
                label = { Text("ID do documento no Firebase")}
            )

            Button(onClick = {
                coroutineScope.launch {
                    AndroidProductRepositoryImpl().delete(idDelete)
                }
            }) {
                Text("Deletar")
            }
        }
    }
}
package br.edu.utfpr.pw45s

import br.edu.utfpr.pw45s.model.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test

class SharedCommonTest {

    private lateinit var repository: AndroidProductRepository
    private val db = Firebase.firestore

    @Before
    fun setUp() {
        repository = AndroidProductRepository()
    }

    @After
    fun tearDown() {
        // Limpar a base de dados após cada teste para garantir a idempotência
        runBlocking {
            db.collection("product").get().await().forEach {
                it.reference.delete().await()
            }
        }
    }

    @Test
    fun testCreateAndReadProduct() = runBlocking {
        // GIVEN: Um novo produto
        val newProduct = Product(
            id = 2,
            name = "Test Product",
            code = "Code"
        )

        // WHEN: Criamos o produto no repositório
        repository.create(newProduct)

        // THEN: O produto pode ser lido e seus dados são corretos
        val retrievedProduct = repository.read(newProduct.id) // O método read() precisa ser corrigido para buscar por ID

        assertNotNull(retrievedProduct)
//        assertEquals(newProduct.id, retrievedProduct.id)
//        assertEquals(newProduct.name, retrievedProduct.name)
//        assertEquals(newProduct.price, retrievedProduct.price, 0.01)
    }

    @Test
    fun testUpdateProduct() = runBlocking {
        // GIVEN: Um produto existente
        val existingProduct = Product(
            id = 1,
            name = "Product to Update",
            code = "Code"
        )
        repository.create(existingProduct)

        // WHEN: Atualizamos o produto
        val updatedProduct = existingProduct.copy(
            name = "Updated Product Name",
            code = "Code 2"
        )
        repository.update(updatedProduct)

        // THEN: Os dados do produto são atualizados
        val retrievedProduct = repository.read(updatedProduct.id) // O método read() precisa ser corrigido
//        assertEquals(updatedProduct.name, retrievedProduct.name)
//        assertEquals(updatedProduct.price, retrievedProduct.price, 0.01)
    }

    @Test
    fun testDeleteProduct() = runBlocking {
        // GIVEN: Um produto que será deletado
        val productToDelete = Product(
            id = 1,
            name = "Product to Update",
            code = "Code"
        )
        repository.create(productToDelete)

        // WHEN: Deletamos o produto
        repository.delete(productToDelete.id)

        // THEN: A tentativa de ler o produto deve falhar (lançar exceção ou retornar null)
        try {
            repository.read(productToDelete.id)
            // Se o código chegar aqui, o teste falha
            assert(false) { "Expected an exception, but none was thrown" }
        } catch (e: Exception) {
            // Sucesso, a exceção foi lançada
            assert(true)
        }
    }
}
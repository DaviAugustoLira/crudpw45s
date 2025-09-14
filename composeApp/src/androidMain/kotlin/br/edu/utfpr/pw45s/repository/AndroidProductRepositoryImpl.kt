package br.edu.utfpr.pw45s.repository

import br.edu.utfpr.pw45s.model.Product
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.model.Document
import kotlinx.coroutines.tasks.await

class AndroidProductRepositoryImpl : IProductRepository {
    private val database = Firebase.firestore
    override suspend fun create(product: Product) {
        try{
            database.collection("product").add(product).await()
        }catch (e: RuntimeException){
            throw RuntimeException(e)
        }
    }

    override suspend fun readAll(): List<Product> {
        val snapshot = database.collection("product")
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(Product::class.java)
        }
    }

    override suspend fun update(product: Product, document: String) {
        database.collection("product")
            .document(document)
            .set(product).await()
    }

    override suspend fun delete(document: String) {
        val response = database.collection("product").document(document)
            .get().await()

        database.collection("product").document(response.id).delete().await()
    }
}
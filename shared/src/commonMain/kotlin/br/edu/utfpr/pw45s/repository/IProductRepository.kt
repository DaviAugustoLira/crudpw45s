package br.edu.utfpr.pw45s.repository

import br.edu.utfpr.pw45s.model.Product

interface IProductRepository {
    suspend fun create(product: Product)
    suspend fun readAll(): List<Product>
    suspend fun update(product: Product, document: String);
    suspend fun delete(document: String);
}
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movilecibershield.data.model.product.Product
import com.example.movilecibershield.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository? = null
) : ViewModel() {

    private var allProducts: List<Product> = emptyList()

    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = productRepository?.getProducts()

            isLoading = false

            result?.let { res ->
                res.data?.let { productList ->
                    allProducts = productList
                    products = productList
                }

                res.error?.let { error ->
                    errorMessage = error
                }
            }
        }
    }

    fun searchProducts(query: String) {
        products = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter {
                it.nombre.contains(query, ignoreCase = true)
            }
        }
    }
}
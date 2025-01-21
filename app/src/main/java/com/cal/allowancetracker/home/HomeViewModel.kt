import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cal.allowancetracker.data.Transaction
import com.cal.allowancetracker.data.TransactionsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.Date
import kotlin.random.Random

class HomeViewModel(private val transactionsRepository: TransactionsRepository) : ViewModel() {

    private val _transactions = MutableStateFlow(emptyList<Transaction>())
    val transactions = _transactions.asStateFlow()

    private val _balance = MutableStateFlow(0.0)
    var balance = _balance.asStateFlow()

    init {
        getPurchases()
        getBalance()
    }

    private fun getPurchases() {
        viewModelScope.launch {
            transactionsRepository.getTransactions().collectLatest {
                _transactions.tryEmit(it)
            }
        }
    }

    private fun getBalance() {
        viewModelScope.launch {
            transactionsRepository.getBalance().collectLatest {
                _balance.tryEmit(it)
            }
        }
    }

    fun addDeposit(amount: Double) {
        if (amount > 0) {
            viewModelScope.launch {
                supervisorScope {
                    launch {
                        transactionsRepository.insertTransaction(Transaction(id = Random.nextInt(0, Int.MAX_VALUE), amount = amount, description = "Deposit", isDeposit = true, date = Date()))
                    }
                }
                transactionsRepository.getBalance().collectLatest {
                    _balance.tryEmit(it)
                }
            }
        }
    }
}


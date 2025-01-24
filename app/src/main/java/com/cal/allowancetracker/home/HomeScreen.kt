import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cal.allowancetracker.AllowanceTrackerTopAppBar
import com.cal.allowancetracker.AppViewModelProvider
import com.cal.allowancetracker.navigation.NavigationDestination
import com.cal.allowancetracker.R
import com.cal.allowancetracker.data.Transaction
import com.cal.allowancetracker.purchase.formatDateToString
import com.cal.allowancetracker.purchase.formattedAmount
import com.cal.allowancetracker.ui.theme.AllowanceTrackerTheme
import java.util.Currency
import java.util.Date
import java.util.Locale

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val balance by viewModel.balance.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AllowanceTrackerTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.purchase_entry_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            transactionList = transactions,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.fillMaxSize(),
            contentPadding = innerPadding,
            balance = String.format("%.2f", balance),
            updateBalance = {
                viewModel.addDeposit(it)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeBody(
    transactionList: List<Transaction>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    balance: String,
    updateBalance: (Double) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        val openBalanceDialog = remember { mutableStateOf(false) }

        Row(modifier = Modifier.padding(contentPadding)) {
            Column(Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
                Text(
                    stringResource(R.string.balance, balance),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
            Column(Modifier.align(Alignment.CenterVertically)) {
                Button(onClick = {
                    openBalanceDialog.value = true
                }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add_deposit)
                    )
                }
            }

        }
        if (transactionList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_purchases_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            TransactionList(
                transactionList = transactionList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }

        if (openBalanceDialog.value) {
            var amount by remember { mutableStateOf("") }
            AlertDialog(onDismissRequest = { openBalanceDialog.value = false }) {
                Card {
                    Row(Modifier.padding(10.dp)) {
                        Text(text = stringResource(id = R.string.add_deposit))
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.CenterHorizontally),
                        value = amount.toString(),
                        leadingIcon = { Text(Currency.getInstance(Locale.getDefault()).symbol) },
                        onValueChange = { amount = it },
                        label = { Text(text = "Amount")},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(
                            onClick = { openBalanceDialog.value = false },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                        TextButton(
                            onClick = {
                                updateBalance(amount.toDouble())
                                openBalanceDialog.value = false },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text(stringResource(R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionList(
    transactionList: List<Transaction>,
    onItemClick: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = transactionList, key = { it.id }) { purchase ->
            TransactionItem(purchase = purchase,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(purchase) })
        }
    }
}

@Composable
private fun TransactionItem(
    purchase: Transaction, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
        ) {
            Text(
                text = formatDateToString(purchase.date),
                style = MaterialTheme.typography.titleSmall
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = purchase.description,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(Modifier.weight(0.05f))
                Text(
                    text = purchase.formattedAmount(),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyPreview() {
    AllowanceTrackerTheme {
        HomeBody(listOf(
            Transaction(1, 10.0, false, "Lunch", date = Date()),
            Transaction(2, 20.0, false, "Game", date = Date()),
            Transaction(3, 15.25, false, "Fun thing that takes up a whole bunch of space omg", date = Date()),

            ), onItemClick = {}, balance = "1000", updateBalance = {})
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBodyEmptyListPreview() {
    AllowanceTrackerTheme {
        HomeBody(listOf(), onItemClick = {}, balance = "100.0", updateBalance = { })
    }
}

@Preview(showBackground = true)
@Composable
fun PurchaseItemPreview() {
    AllowanceTrackerTheme {
        TransactionItem(
            Transaction(1, 10.0, false, "Lunch", date = Date()),
        )
    }
}

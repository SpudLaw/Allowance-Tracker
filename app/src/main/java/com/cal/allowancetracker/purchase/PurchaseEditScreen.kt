import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cal.allowancetracker.AllowanceTrackerTopAppBar
import com.cal.allowancetracker.AppViewModelProvider
import com.cal.allowancetracker.R
import com.cal.allowancetracker.navigation.NavigationDestination
import com.cal.allowancetracker.purchase.PurchaseEditViewModel
import com.cal.allowancetracker.purchase.PurchaseEntryBody
import com.cal.allowancetracker.ui.theme.AllowanceTrackerTheme
import kotlinx.coroutines.launch

object PurchaseEditDestination : NavigationDestination {
    override val route = "purchase_edit"
    override val titleRes = R.string.purchase_edit_title
    const val purchaseIdArg = "purchaseId"
    val routeWithArgs = "$route/{$purchaseIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PurchaseEditViewModel
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            AllowanceTrackerTopAppBar(
                title = stringResource(PurchaseEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(Modifier.fillMaxHeight()) {
            Spacer(modifier = Modifier.weight(1f))
            PurchaseEntryBody(
                purchaseUiState = viewModel.purchaseUiState,
                onItemValueChange = viewModel::updateUiState,
                onSaveClick = {
                    coroutineScope.launch {
                        viewModel.saveTransaction()
                        navigateBack()
                    }
                },
                modifier = Modifier
                    .padding(
                        start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                        end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                        top = innerPadding.calculateTopPadding()
                    )
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

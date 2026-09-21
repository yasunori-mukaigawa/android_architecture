package jp.co.nsco.basearchitecture.feature.license.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.ui.dialog.AppDialog
import jp.co.nsco.basearchitecture.core.ui.dialog.DialogUiState
import jp.co.nsco.basearchitecture.feature.license.presentation.common.LicenseRoutes
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

/**
 * この要素。
 *
 * この責務 は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
@Composable
fun LicenseRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LicenseListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogPositiveButtonText = stringResource(R.string.sample_dialog_ok)
    val dialogState = remember { mutableStateOf<DialogUiState?>(null) }

    LaunchedEffect(viewModel) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    is LicenseListUiEffect.NavigateToDetail -> {
                        navController.navigate(LicenseRoutes.detail(effect.id))
                    }

                    LicenseListUiEffect.NavigateBack -> {
                        navController.popBackStack()
                    }

                    is LicenseListUiEffect.ShowDialog -> {
                        dialogState.value = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = dialogPositiveButtonText
                        )
                    }
                }
            }
        }

        viewModel.sendEvent(LicenseListUiEvent.Initialize)
    }

    dialogState.value?.let { dialog ->
        AppDialog(
            dialogUiState = dialog,
            onPositiveClick = {
                dialogState.value = null
            },
            onDismissRequest = {
                dialogState.value = null
            }
        )
    }

    LicenseListScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}







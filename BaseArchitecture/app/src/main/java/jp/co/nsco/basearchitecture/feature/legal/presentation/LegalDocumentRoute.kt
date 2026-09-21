package jp.co.nsco.basearchitecture.feature.legal.presentation

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
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType
import jp.co.nsco.basearchitecture.feature.legal.ui.LegalDocumentScreen
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

/**
 * 法務文書画面の Route Composable。
 *
 * 本 Composable は、Navigation / ViewModel / Effect / Dialog と
 * Stateless Screen を接続する責務を持つ。
 *
 * ■ 提供する責務
 *   ViewModel の取得
 *   UiState の購読
 *   初期表示 Event の通知
 *   UiEffect の購読
 *   Navigation の実行
 *   Dialog 表示状態の管理
 *   LegalDocumentScreen への State / Event 接続
 *
 * ■ 設計上の意図
 *   LegalDocumentScreen を純粋な表示 Composable として保つため、
 *   ViewModel や NavController への依存は Route 側に閉じ込める。
 *
 *   Screen は uiState と onEvent のみを受け取り、
 *   状態管理、画面遷移、副作用処理を直接知らない。
 *
 * ■ 注意
 *   Dialog の表示状態は、一回性の UiEffect を受けた Route 側で保持する。
 *   Dialog 表示を UiState に含めないことで、State 復元や再描画による
 *   意図しない再表示を避ける。
 *
 * @param type 表示対象の法務文書種別。
 * @param navController 画面遷移を制御する NavController。
 * @param modifier 画面全体に適用する Modifier。
 * @param viewModel 法務文書画面の ViewModel。
 */
@Composable
fun LegalDocumentRoute(
    type: LegalDocumentType,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LegalDocumentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val dialogPositiveButtonText = stringResource(R.string.sample_dialog_ok)
    val dialogState = remember { mutableStateOf<DialogUiState?>(null) }

    LaunchedEffect(viewModel, type) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    LegalDocumentUiEffect.NavigateBack -> {
                        navController.popBackStack()
                    }

                    is LegalDocumentUiEffect.ShowDialog -> {
                        dialogState.value = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = dialogPositiveButtonText
                        )
                    }
                }
            }
        }

        viewModel.sendEvent(LegalDocumentEvent.OnAppear(type))
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

    LegalDocumentScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}

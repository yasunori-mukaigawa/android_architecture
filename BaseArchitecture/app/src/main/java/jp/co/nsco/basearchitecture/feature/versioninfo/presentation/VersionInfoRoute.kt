package jp.co.nsco.basearchitecture.feature.versioninfo.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.external.ExternalUriOpener
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.ui.dialog.AppDialog
import jp.co.nsco.basearchitecture.core.ui.dialog.DialogUiState
import jp.co.nsco.basearchitecture.feature.versioninfo.ui.VersionInfoScreen
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch

/**
 * VersionInfo 画面の Route Composable。
 *
 * 本 Composable は、Navigation / ViewModel / 外部URI起動 / Dialog と
 * Stateless Screen を接続する責務を持つ。
 *
 * ■ 提供する責務
 *   ViewModel の取得
 *   UiState の購読
 *   初期表示 Event の通知
 *   UiEffect の購読
 *   戻る遷移の実行
 *   外部URI起動の実行
 *   外部URI起動失敗時の Dialog 表示
 *   VersionInfoScreen への State / Event 接続
 *
 * ■ 設計上の意図
 *   VersionInfoScreen を純粋な表示 Composable として保つため、
 *   ViewModel、NavController、ExternalUriOpener、DialogState への依存は Route 側に閉じ込める。
 *
 *   Screen は uiState と onEvent のみを受け取り、
 *   状態管理、画面遷移、外部URI起動、副作用処理を直接知らない。
 *
 *   外部URI起動は Android の Context / Event に近い処理であるため、
 *   ViewModel ではなく Route 側で実行する。
 *
 * ■ 注意
 *   ExternalUriOpener は Composable から直接 constructor injection できないため、
 *   Hilt EntryPoint 経由で取得している。
 *
 *   ViewModel からは「外部URIを開いてほしい」という Effect のみを発行し、
 *   実際に開けたかどうかの判定と失敗Dialogの表示は Route 側で行う。
 *
 * @param navController 画面遷移を制御する NavController。
 * @param modifier 画面全体に適用する Modifier。
 * @param viewModel VersionInfo 画面の ViewModel。
 */
@Composable
fun VersionInfoRoute(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: VersionInfoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val opener = remember(context) {
        EntryPointAccessors.fromApplication(
            context,
            VersionInfoEntryPoint::class.java
        ).externalUriOpener()
    }
    val dialogPositiveButtonText = stringResource(R.string.sample_dialog_ok)
    val externalOpenErrorTitle = stringResource(R.string.version_info_external_open_error_title)
    val externalOpenErrorMessage = stringResource(R.string.version_info_external_open_error_message)
    val dialogState = remember { mutableStateOf<DialogUiState?>(null) }

    LaunchedEffect(viewModel) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    VersionInfoUiEffect.NavigateBack -> {
                        navController.popBackStack()
                    }

                    is VersionInfoUiEffect.OpenExternalUri -> {
                        if (opener.open(effect.uri) is AppResult.Failure) {
                            dialogState.value = DialogUiState(
                                title = externalOpenErrorTitle,
                                message = externalOpenErrorMessage,
                                positiveButtonText = dialogPositiveButtonText
                            )
                        }
                    }

                    is VersionInfoUiEffect.ShowDialog -> {
                        dialogState.value = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = dialogPositiveButtonText
                        )
                    }
                }
            }
        }

        viewModel.sendEvent(VersionInfoEvent.OnAppear)
    }

    dialogState.value?.let { dialog ->
        AppDialog(
            dialogUiState = dialog,
            onPositiveClick = { dialogState.value = null },
            onDismissRequest = { dialogState.value = null }
        )
    }

    VersionInfoScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = viewModel::sendEvent
    )
}

/**
 * VersionInfoRoute から利用する Hilt EntryPoint。
 *
 * Composable は通常の constructor injection ができないため、
 * Application スコープに登録された ExternalUriOpener を取得するために利用する。
 *
 * ■ 注意
 *   EntryPoint は便利だが、乱用すると依存関係が見えづらくなる。
 *   ViewModel に持たせるべき依存か、Route 側で実行すべき Android 依存処理かを判断して使う。
 */
@EntryPoint
@InstallIn(SingletonComponent::class)
interface VersionInfoEntryPoint {

    /**
     * 外部URIを開く Opener を取得する。
     *
     * @return 外部URI起動を行う Opener。
     */
    fun externalUriOpener(): ExternalUriOpener
}

package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.ui.dialog.AppDialog
import jp.co.nsco.basearchitecture.core.ui.dialog.DialogUiState
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigation
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleDrawerContent
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleRoutes
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleTopBar
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineStart

/**
 * Sample History 画面の Route Composable。
 *
 * 本 Composable は、Navigation / ViewModel / Drawer / Dialog と
 * Stateless Screen を接続する責務を持つ。
 *
 * ■ 提供する責務
 *   ViewModel の取得
 *   UiState の購読
 *   初期表示 Event の通知
 *   UiEffect の購読
 *   Bottom Navigation の遷移実行
 *   Drawer の開閉制御
 *   Drawer item からの画面遷移
 *   Dialog 表示状態の管理
 *   SampleHistoryScreen への State / Event 接続
 *
 * ■ 設計上の意図
 *   SampleHistoryScreen を純粋な表示 Composable として保つため、
 *   ViewModel、NavController、DrawerState、DialogState への依存は Route 側に閉じ込める。
 *
 *   Screen は uiState と onEvent のみを受け取り、
 *   状態管理、画面遷移、副作用処理を直接知らない。
 *
 * ■ 注意
 *   Dialog の表示状態は、一回性の UiEffect を受けた Route 側で保持する。
 *   Dialog 表示を UiState に含めないことで、State 復元や再描画による
 *   意図しない再表示を避ける。
 *
 * @param navController 画面遷移を制御する NavController。
 * @param viewModel Sample History 画面の ViewModel。
 */
@Composable
fun SampleHistoryRoute(
    navController: NavController,
    viewModel: SampleHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val dialogPositiveButtonText = stringResource(R.string.sample_dialog_ok)
    var dialogState by remember { mutableStateOf<DialogUiState?>(null) }

    LaunchedEffect(viewModel) {
        launch(start = CoroutineStart.UNDISPATCHED) {
            viewModel.uiEffect.collect { effect ->
                when (effect) {
                    SampleHistoryUiEffect.NavigateHome -> {
                        navController.navigate(SampleRoutes.List) {
                            popUpTo(SampleRoutes.List) { inclusive = true }
                        }
                    }

                    SampleHistoryUiEffect.NavigateSettings -> {
                        navController.navigate(SampleRoutes.Settings)
                    }

                    is SampleHistoryUiEffect.ShowDialog -> {
                        dialogState = DialogUiState(
                            title = effect.title,
                            message = effect.message,
                            positiveButtonText = dialogPositiveButtonText
                        )
                    }
                }
            }
        }

        viewModel.sendEvent(SampleHistoryUiEvent.Initialize)
    }

    dialogState?.let { dialog ->
        AppDialog(
            dialogUiState = dialog,
            onPositiveClick = { dialogState = null },
            onDismissRequest = { dialogState = null }
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            SampleDrawerContent(
                onItemClick = { item ->
                    coroutineScope.launch { drawerState.close() }
                    item.destinationRoute?.let(navController::navigate)
                }
            )
        }
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            topBar = {
                SampleTopBar(
                    titleResId = R.string.sample_history_title,
                    onMenuClick = { coroutineScope.launch { drawerState.open() } }
                )
            },
            bottomBar = {
                SampleBottomNavigation(
                    selectedItem = SampleBottomNavigationItem.History,
                    onItemClick = { item ->
                        when (item) {
                            SampleBottomNavigationItem.Home -> {
                                viewModel.sendEvent(SampleHistoryUiEvent.HomeClicked)
                            }

                            SampleBottomNavigationItem.Settings -> {
                                viewModel.sendEvent(SampleHistoryUiEvent.SettingsClicked)
                            }

                            SampleBottomNavigationItem.History -> Unit
                        }
                    }
                )
            }
        ) { paddingValues ->
            SampleHistoryScreen(
                modifier = Modifier.padding(paddingValues),
                uiState = uiState,
                onEvent = viewModel::sendEvent
            )
        }
    }
}

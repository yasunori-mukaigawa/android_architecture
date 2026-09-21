package jp.co.nsco.basearchitecture.feature.legal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.ui.loading.LoadingContent
import jp.co.nsco.basearchitecture.core.ui.markdown.MarkdownDocumentView
import jp.co.nsco.basearchitecture.feature.legal.presentation.LegalDocumentEvent
import jp.co.nsco.basearchitecture.feature.legal.presentation.LegalDocumentScreenState
import jp.co.nsco.basearchitecture.feature.legal.presentation.LegalDocumentUiState

/**
 * 法務文書画面の Stateless Screen。
 *
 * 本 Composable は、LegalDocumentUiState に基づいて
 * 法務文書画面を描画し、ユーザー操作を LegalDocumentEvent として通知する責務を持つ。
 *
 * ■ 提供する責務
 *   画面全体の Scaffold 構成
 *   TopAppBar の表示
 *   戻る操作の Event 通知
 *   Loading / Loaded / Error 状態ごとの表示切り替え
 *   Markdown 本文の表示
 *   再試行操作の Event 通知
 *
 * ■ 設計上の意図
 *   本 Composable は Stateless Screen として定義し、
 *   ViewModel、NavController、UseCase、Repository を直接扱わない。
 *
 *   画面状態は LegalDocumentUiState として外から受け取り、
 *   ユーザー操作は LegalDocumentEvent として外へ通知する。
 *
 *   これにより、状態管理や副作用処理は Route / ViewModel 側に閉じ込め、
 *   Screen は表示責務に集中できる。
 *
 * ■ 注意
 *   Navigation や Dialog 表示などの一回性処理は本 Composable では行わない。
 *   それらは LegalDocumentRoute が UiEffect を購読して実行する。
 *
 * @param uiState 法務文書画面の UI 状態。
 * @param onEvent ユーザー操作や画面イベントを通知するコールバック。
 * @param modifier 画面全体に適用する Modifier。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalDocumentScreen(
    uiState: LegalDocumentUiState,
    onEvent: (LegalDocumentEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.title,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(LegalDocumentEvent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        when (uiState.screenState) {
            LegalDocumentScreenState.Loading -> {
                LoadingContent(
                    isLoading = true,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    // Loading専用表示のため、背面contentは空にする。
                }
            }

            LegalDocumentScreenState.Loaded -> {
                MarkdownContent(
                    paddingValues = paddingValues,
                    markdown = uiState.markdown
                )
            }

            LegalDocumentScreenState.Error -> {
                ErrorContent(
                    paddingValues = paddingValues,
                    message = uiState.errorMessage.orEmpty(),
                    onRetryClick = { onEvent(LegalDocumentEvent.RetryClicked) }
                )
            }
        }
    }
}

/**
 * 法務文書本文の表示。
 *
 * Markdown 形式の本文をスクロール可能な領域に表示する。
 *
 * ■ 設計上の意図
 *   Markdown の描画は core.ui.markdown.MarkdownDocumentView に委譲する。
 *
 *   本 Composable は、画面内の余白、背景、スクロール領域の構成だけを担当し、
 *   Markdown の解析・描画詳細は扱わない。
 *
 * @param paddingValues Scaffold から渡される内側余白。
 * @param markdown 表示する Markdown 本文。
 */
@Composable
private fun MarkdownContent(
    paddingValues: PaddingValues,
    markdown: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        MarkdownDocumentView(
            markdown = markdown,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * 法務文書読み込み失敗時の表示。
 *
 * エラーメッセージと再試行ボタンを表示する。
 *
 * ■ 設計上の意図
 *   エラー表示は UiState の errorMessage を表示するだけに留める。
 *
 *   再試行時の実処理は Screen では行わず、
 *   onRetryClick を通じて Event として ViewModel へ通知する。
 *
 * @param paddingValues Scaffold から渡される内側余白。
 * @param message 表示するエラーメッセージ。
 * @param onRetryClick 再試行ボタン押下時のコールバック。
 */
@Composable
private fun ErrorContent(
    paddingValues: PaddingValues,
    message: String,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(96.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetryClick) {
            Text(text = stringResource(R.string.legal_retry))
        }
    }
}

/**
 * 法務文書画面の Preview。
 *
 * Loaded 状態の Markdown 表示を確認するための Preview。
 */
@Preview(showBackground = true)
@Composable
private fun LegalDocumentScreenPreview() {
    LegalDocumentScreen(
        uiState = LegalDocumentUiState(
            screenState = LegalDocumentScreenState.Loaded,
            title = "プライバシーポリシー",
            markdown = "# プライバシーポリシー\n\n> 本書はサンプルです。\n\n## 1. 取得する情報"
        ),
        onEvent = {}
    )
}

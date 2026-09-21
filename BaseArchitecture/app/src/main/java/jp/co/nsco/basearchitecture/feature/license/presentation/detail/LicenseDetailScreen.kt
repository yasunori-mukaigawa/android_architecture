package jp.co.nsco.basearchitecture.feature.license.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.ui.loading.LoadingContent

/**
 * ライセンス詳細画面の Stateless Screen。
 *
 * 本 Composable は、LicenseDetailUiState に基づいて
 * ライセンス詳細画面を描画し、ユーザー操作を LicenseDetailUiEvent として通知する。
 *
 * ■ 提供する責務
 *   画面全体のレイアウト構成
 *   TopAppBar の表示
 *   戻る操作の Event 通知
 *   Loading / Error / Content 表示の切り替え
 *   ライセンス詳細情報の表示
 *
 * ■ 設計上の意図
 *   本 Composable は Stateless Screen として定義し、
 *   ViewModel、NavController、UseCase、Repository を直接扱わない。
 *
 *   画面状態は LicenseDetailUiState として外から受け取り、
 *   ユーザー操作は LicenseDetailUiEvent として外へ通知する。
 *
 *   これにより、状態管理や副作用処理は Route / ViewModel 側に閉じ込め、
 *   Screen は表示責務に集中できる。
 *
 * ■ 注意
 *   Navigation や Dialog 表示などの一回性処理は本 Composable では行わない。
 *   それらは LicenseDetailRoute が UiEffect を購読して実行する。
 *
 * @param uiState ライセンス詳細画面の UI 状態。
 * @param onEvent ユーザー操作や画面イベントを通知するコールバック。
 * @param modifier 画面全体に適用する Modifier。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseDetailScreen(
    uiState: LicenseDetailUiState,
    onEvent: (LicenseDetailUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.license_detail_title),
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { onEvent(LicenseDetailUiEvent.BackClicked) }) {
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

        when {
            uiState.isLoading -> {
                LoadingContent(
                    isLoading = true,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Loading専用表示のため、背面contentは空にする。
                }
            }

            uiState.errorMessage != null -> {
                LicenseDetailInfoFooter(
                    text = uiState.errorMessage,
                    modifier = Modifier.padding(24.dp)
                )
            }

            else -> {
                LicenseDetailContent(
                    uiState = uiState,
                    modifier = Modifier.padding(24.dp)
                )
            }
        }
    }
}

/**
 * ライセンス詳細本文を表示する。
 *
 * ライブラリ名、ライセンス種別、著作権表記、ライセンス本文を
 * カード形式で表示する。
 *
 * ■ 設計上の意図
 *   詳細表示の構造を LicenseDetailScreen 本体から分離し、
 *   画面状態の分岐と本文レイアウトの責務を分ける。
 *
 *   ライセンス本文は長文になりやすいため、
 *   本文領域だけを縦スクロール可能にしている。
 *
 * @param uiState ライセンス詳細画面の UI 状態。
 * @param modifier 詳細表示領域に適用する Modifier。
 */
@Composable
private fun LicenseDetailContent(
    uiState: LicenseDetailUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LicenseDetailDroidIcon()
                    Spacer(modifier = Modifier.size(18.dp))
                    Text(
                        text = uiState.name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                LicenseDetailPropertyRow(
                    label = stringResource(R.string.license_type_label),
                    value = uiState.licenseName
                )
                Spacer(modifier = Modifier.height(20.dp))
                LicenseDetailPropertyRow(
                    label = stringResource(R.string.license_copyright_label),
                    value = uiState.copyright
                )

                Spacer(modifier = Modifier.height(28.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(28.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(520.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(20.dp)
                ) {
                    Text(
                        text = uiState.licenseText,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        LicenseDetailInfoFooter(text = stringResource(R.string.license_detail_info_footer))
    }
}

/**
 * ライセンス詳細の項目行を表示する。
 *
 * @param label 項目名。
 * @param value 項目値。
 */
@Composable
private fun LicenseDetailPropertyRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.36f)
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.64f)
        )
    }
}

/**
 * ライセンス詳細ヘッダーに表示するアイコン。
 *
 * 現在はアプリの launcher foreground を利用している。
 * 将来的にライブラリ別アイコンや固定のライセンスアイコンへ変更する場合は、
 * 本 Composable のみを差し替えればよい。
 */
@Composable
private fun LicenseDetailDroidIcon() {
    Box(
        modifier = Modifier
            .size(70.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(50.dp)
        )
    }
}

/**
 * ライセンス詳細画面の補足情報を表示する Footer。
 *
 * エラー時の簡易メッセージ表示と、通常表示時の補足メッセージ表示で利用する。
 *
 * @param text 表示する補足テキスト。
 * @param modifier Footerに適用する Modifier。
 */
@Composable
private fun LicenseDetailInfoFooter(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.size(14.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/**
 * ライセンス詳細画面の Preview。
 *
 * 通常表示状態のレイアウト確認に利用する。
 */
@Preview(showBackground = true)
@Composable
private fun LicenseDetailScreenPreview() {
    LicenseDetailScreen(
        uiState = LicenseDetailUiState(
            name = "AndroidX Core",
            licenseName = "Apache License 2.0",
            copyright = "Copyright The Android Open Source Project",
            licenseText = "Apache License\nVersion 2.0, January 2004\n\nTERMS AND CONDITIONS"
        ),
        onEvent = {}
    )
}
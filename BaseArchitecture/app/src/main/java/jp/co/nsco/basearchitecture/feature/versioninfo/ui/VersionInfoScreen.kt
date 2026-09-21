package jp.co.nsco.basearchitecture.feature.versioninfo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.SystemUpdate
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material.icons.outlined.Update
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.VersionCheckStatus
import jp.co.nsco.basearchitecture.feature.versioninfo.presentation.VersionInfoEvent
import jp.co.nsco.basearchitecture.feature.versioninfo.presentation.VersionInfoScreenState
import jp.co.nsco.basearchitecture.feature.versioninfo.presentation.VersionInfoUiState

/**
 * VersionInfo 画面の Stateless Screen。
 *
 * VersionInfoScreen は、VersionInfoUiState に基づいて
 * アプリ情報・バージョン情報・確認状態・操作ボタンを描画する。
 *
 * ■ 提供する責務
 *   TopAppBar の表示
 *   Loading / Loaded / Error 表示の切り替え
 *   アプリ概要情報の表示
 *   バージョン情報の表示
 *   バージョン確認状態の表示
 *   外部リンク・再試行操作の Event 通知
 *
 * ■ 設計上の意図
 *   本 Composable は Stateless Screen として定義し、
 *   ViewModel、NavController、UseCase、Repository、ExternalUriOpener を直接扱わない。
 *
 *   画面状態は VersionInfoUiState として外から受け取り、
 *   ユーザー操作は VersionInfoEvent として外へ通知する。
 *
 *   これにより、状態管理、画面遷移、外部URI起動、副作用処理は
 *   Route / ViewModel 側に閉じ込め、Screen は表示責務に集中できる。
 *
 * @param uiState VersionInfo 画面の UI 状態。
 * @param onEvent ユーザー操作や画面イベントを通知するコールバック。
 * @param modifier 画面全体に適用する Modifier。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VersionInfoScreen(
    uiState: VersionInfoUiState,
    onEvent: (VersionInfoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.version_info_title),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(VersionInfoEvent.BackClicked) }) {
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
    ) { padding ->
        when (uiState.screenState) {
            VersionInfoScreenState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            VersionInfoScreenState.Error,
            VersionInfoScreenState.Loaded -> {
                VersionInfoLoadedContent(
                    modifier = Modifier.padding(padding),
                    uiState = uiState,
                    onEvent = onEvent
                )
            }
        }
    }
}

/**
 * VersionInfo 画面の読み込み完了・エラー時コンテンツを表示する。
 *
 * Error 状態でも、取得済み情報がある場合は同じレイアウトで表示できるよう、
 * Loaded / Error の表示本体を共通化している。
 *
 * @param uiState VersionInfo 画面の UI 状態。
 * @param onEvent ユーザー操作を通知するコールバック。
 * @param modifier コンテンツに適用する Modifier。
 */
@Composable
private fun VersionInfoLoadedContent(
    uiState: VersionInfoUiState,
    onEvent: (VersionInfoEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderCard(uiState)
        VersionCard(uiState)
        StatusCard(uiState)
        ActionButtons(uiState, onEvent)
    }
}

/**
 * アプリ概要カードを表示する。
 *
 * @param uiState VersionInfo 画面の UI 状態。
 */
@Composable
private fun HeaderCard(uiState: VersionInfoUiState) {
    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(44.dp)
                )
            }

            Spacer(Modifier.size(18.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = uiState.appName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = uiState.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = stringResource(R.string.version_info_channel_format, uiState.channel),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * 現在バージョン・最新バージョン・ビルド番号などの情報カードを表示する。
 *
 * @param uiState VersionInfo 画面の UI 状態。
 */
@Composable
private fun VersionCard(uiState: VersionInfoUiState) {
    AppCard {
        Text(
            text = stringResource(R.string.version_info_current_section),
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(12.dp))
        InfoRow(
            icon = Icons.Outlined.Tag,
            label = stringResource(R.string.version_info_current_version),
            value = uiState.currentVersion
        )
        InfoRow(
            icon = Icons.Outlined.SystemUpdate,
            label = stringResource(R.string.version_info_latest_version),
            value = uiState.latestVersion ?: "-"
        )
        InfoRow(
            icon = Icons.Outlined.Info,
            label = stringResource(R.string.version_info_build_number),
            value = uiState.buildNumber
        )
        InfoRow(
            icon = Icons.Outlined.Update,
            label = stringResource(R.string.version_info_last_checked_at),
            value = uiState.lastCheckedAt
        )
    }
}

/**
 * バージョン確認状態カードを表示する。
 *
 * 状態に応じて、アイコン・色・表示文言を切り替える。
 *
 * @param uiState VersionInfo 画面の UI 状態。
 */
@Composable
private fun StatusCard(uiState: VersionInfoUiState) {
    val color = when (uiState.status) {
        VersionCheckStatus.Latest -> MaterialTheme.colorScheme.primary
        VersionCheckStatus.UpdateAvailable -> MaterialTheme.colorScheme.tertiary
        VersionCheckStatus.CheckFailed -> MaterialTheme.colorScheme.error
    }

    val icon = when (uiState.status) {
        VersionCheckStatus.Latest -> Icons.Outlined.CheckCircle
        VersionCheckStatus.UpdateAvailable -> Icons.Outlined.SystemUpdate
        VersionCheckStatus.CheckFailed -> Icons.Outlined.ErrorOutline
    }

    AppCard {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color
            )
            Spacer(Modifier.size(12.dp))
            Column {
                Text(
                    text = uiState.statusTitle,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = uiState.statusMessage,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * VersionInfo 画面の操作ボタンを表示する。
 *
 * UiState の表示フラグに応じて、
 * ストアを開く、再試行、アプリ情報ページを開くボタンを表示する。
 *
 * @param uiState VersionInfo 画面の UI 状態。
 * @param onEvent ユーザー操作を通知するコールバック。
 */
@Composable
private fun ActionButtons(
    uiState: VersionInfoUiState,
    onEvent: (VersionInfoEvent) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (uiState.showOpenStoreButton) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(VersionInfoEvent.OpenStoreClicked) }
            ) {
                Text(stringResource(R.string.version_info_open_store))
            }
        }

        if (uiState.showRetryButton) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(VersionInfoEvent.RetryClicked) }
            ) {
                Text(stringResource(R.string.version_info_retry))
            }
        }

        if (uiState.showOpenAppInfoButton) {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onEvent(VersionInfoEvent.OpenAppInfoPageClicked) }
            ) {
                Text(stringResource(R.string.version_info_open_app_info))
            }
        }
    }
}

/**
 * VersionInfo 画面で利用する共通カード。
 *
 * @param content カード内に表示する Composable。
 */
@Composable
private fun AppCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

/**
 * アイコン付きの情報行を表示する。
 *
 * @param icon 左側に表示するアイコン。
 * @param label 項目名。
 * @param value 項目値。
 */
@Composable
private fun InfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.size(12.dp))
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * VersionInfo 画面の更新あり状態 Preview。
 */
@Preview(showBackground = true)
@Composable
private fun VersionInfoScreenPreview() {
    VersionInfoScreen(
        uiState = VersionInfoUiState(
            screenState = VersionInfoScreenState.Loaded,
            appName = "Base Architecture Sample",
            description = "アプリ情報とバージョンを確認できます。",
            channel = "Production",
            currentVersion = "v1.2.3",
            latestVersion = "v1.2.4",
            buildNumber = "10203",
            lastCheckedAt = "2026/06/07 10:31",
            status = VersionCheckStatus.UpdateAvailable,
            statusTitle = "最新版ではありません",
            statusMessage = "新しいバージョンが利用できます。Google Playから最新版を確認してください。",
            showOpenStoreButton = true
        ),
        onEvent = {}
    )
}
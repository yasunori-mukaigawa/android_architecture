package jp.co.nsco.basearchitecture.feature.sample.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.theme.AppThemeId
import jp.co.nsco.basearchitecture.core.theme.AppThemeMode
import jp.co.nsco.basearchitecture.core.ui.loading.LoadingContent
import jp.co.nsco.basearchitecture.ui.theme.BaseArchitectureTheme

/**
 * Sample Settings 画面の Stateless Screen。
 *
 * 本 Composable は、SampleSettingsUiState に基づいて
 * Sample設定とテーマ設定の画面を描画し、ユーザー操作を
 * SampleSettingsUiEvent として通知する。
 *
 * ■ 提供する責務
 *   設定保存エラーの表示
 *   通知設定Switchの表示
 *   テーマモード選択肢の表示
 *   テーマプリセット選択肢の表示
 *   ログイン時確認設定Switchの表示
 *   キャッシュ保持設定Switchの表示
 *   設定変更操作の Event 通知
 *
 * ■ 設計上の意図
 *   本 Composable は Stateless Screen として定義し、
 *   ViewModel、NavController、UseCase、Repository を直接扱わない。
 *
 *   画面状態は SampleSettingsUiState として外から受け取り、
 *   ユーザー操作は SampleSettingsUiEvent として外へ通知する。
 *
 *   これにより、状態管理や副作用処理は Route / ViewModel 側に閉じ込め、
 *   Screen は表示責務に集中できる。
 *
 * @param uiState Sample Settings 画面の UI 状態。
 * @param onEvent ユーザー操作や画面イベントを通知するコールバック。
 * @param modifier 画面全体に適用する Modifier。
 */
@Composable
fun SampleSettingsScreen(
    uiState: SampleSettingsUiState,
    onEvent: (SampleSettingsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LoadingContent(
        isLoading = uiState.isLoading,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            uiState.screenErrorMessage?.let { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            SampleSettingsSection(
                title = stringResource(R.string.sample_settings_section_general),
                rows = listOf(
                    SampleSettingsRowState(
                        icon = Icons.Outlined.Notifications,
                        title = stringResource(R.string.sample_settings_notification_title),
                        description = stringResource(R.string.sample_settings_notification_description),
                        checked = uiState.notificationEnabled,
                        onCheckedChange = {
                            onEvent(SampleSettingsUiEvent.NotificationChanged(it))
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            SampleThemeSettingsSection(
                uiState = uiState,
                onThemeModeClick = {
                    onEvent(SampleSettingsUiEvent.ThemeModeChanged(it))
                },
                onThemePresetClick = {
                    onEvent(SampleSettingsUiEvent.ThemePresetChanged(it))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SampleSettingsSection(
                title = stringResource(R.string.sample_settings_section_security),
                rows = listOf(
                    SampleSettingsRowState(
                        icon = Icons.Outlined.Security,
                        title = stringResource(R.string.sample_settings_confirm_login_title),
                        description = stringResource(R.string.sample_settings_confirm_login_description),
                        checked = uiState.confirmOnLoginEnabled,
                        onCheckedChange = {
                            onEvent(SampleSettingsUiEvent.ConfirmOnLoginChanged(it))
                        }
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            SampleSettingsSection(
                title = stringResource(R.string.sample_settings_section_data),
                rows = listOf(
                    SampleSettingsRowState(
                        icon = Icons.Outlined.Cached,
                        title = stringResource(R.string.sample_settings_keep_cache_title),
                        description = stringResource(R.string.sample_settings_keep_cache_description),
                        checked = uiState.keepCacheEnabled,
                        onCheckedChange = {
                            onEvent(SampleSettingsUiEvent.KeepCacheChanged(it))
                        }
                    )
                )
            )
        }
    }
}

/**
 * 設定項目セクションを表示する。
 *
 * 複数の設定行を Card としてまとめて表示する。
 *
 * @param title セクションタイトル。
 * @param rows セクション内に表示する設定行一覧。
 * @param modifier セクションに適用する Modifier。
 */
@Composable
private fun SampleSettingsSection(
    title: String,
    rows: List<SampleSettingsRowState>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            rows.forEachIndexed { index, row ->
                SampleSettingsRow(row = row)

                if (index != rows.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 14.dp),
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

/**
 * Switch付きの設定行を表示する。
 *
 * @param row 表示する設定行の状態。
 * @param modifier 行に適用する Modifier。
 */
@Composable
private fun SampleSettingsRow(
    row: SampleSettingsRowState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = row.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(34.dp)
        )

        Spacer(modifier = Modifier.width(18.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = row.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = row.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Switch(
            checked = row.checked,
            onCheckedChange = row.onCheckedChange
        )
    }
}

/**
 * テーマ設定セクションを表示する。
 *
 * テーマモードとテーマプリセットの選択肢を表示し、
 * 選択操作を呼び出し側へ通知する。
 */
@Composable
private fun SampleThemeSettingsSection(
    uiState: SampleSettingsUiState,
    onThemeModeClick: (AppThemeMode) -> Unit,
    onThemePresetClick: (AppThemeId) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp)) {
            Text(
                text = stringResource(R.string.sample_settings_section_display),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.sample_settings_theme_mode_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            uiState.themeModeOptions.forEach { option ->
                ThemeModeOptionRow(
                    option = option,
                    onClick = { onThemeModeClick(option.mode) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            Text(
                text = stringResource(R.string.sample_settings_theme_preset_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            uiState.themePresetOptions.forEach { option ->
                ThemePresetOptionRow(
                    option = option,
                    onClick = { onThemePresetClick(option.themeId) }
                )
            }
        }
    }
}

/**
 * テーマモード選択行を表示する。
 *
 * @param option 表示するテーマモード選択肢。
 * @param onClick 選択時のコールバック。
 */
@Composable
private fun ThemeModeOptionRow(
    option: ThemeModeOptionUiState,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = option.selected, onClick = onClick)
        Text(text = option.label, style = MaterialTheme.typography.bodyLarge)
    }
}

/**
 * テーマプリセット選択行を表示する。
 *
 * @param option 表示するテーマプリセット選択肢。
 * @param onClick 選択時のコールバック。
 */
@Composable
private fun ThemePresetOptionRow(
    option: ThemePresetOptionUiState,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = option.selected, onClick = onClick)

        Column(modifier = Modifier.weight(1f)) {
            Text(text = option.label, fontWeight = FontWeight.Bold)
            Text(
                text = option.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Switch付き設定行の表示状態。
 *
 * @property icon 設定項目アイコン。
 * @property title 設定項目タイトル。
 * @property description 設定項目説明。
 * @property checked Switchの選択状態。
 * @property onCheckedChange Switch変更時のコールバック。
 */
private data class SampleSettingsRowState(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
)

/**
 * Sample Settings 画面の通常表示 Preview。
 */
@Preview(showBackground = true)
@Composable
private fun SampleSettingsScreenPreview_Normal() {
    BaseArchitectureTheme {
        SampleSettingsScreen(
            uiState = SampleSettingsUiState(
                isLoading = false,
                notificationEnabled = true,
                confirmOnLoginEnabled = true,
                keepCacheEnabled = true,
                screenErrorMessage = null
            ),
            onEvent = {}
        )
    }
}

/**
 * Sample Settings 画面の読み込み中 Preview。
 */
@Preview(showBackground = true)
@Composable
private fun SampleSettingsScreenPreview_Loading() {
    BaseArchitectureTheme {
        SampleSettingsScreen(
            uiState = SampleSettingsUiState.initial(),
            onEvent = {}
        )
    }
}

/**
 * Sample Settings 画面のエラー表示 Preview。
 */
@Preview(showBackground = true)
@Composable
private fun SampleSettingsScreenPreview_Error() {
    BaseArchitectureTheme {
        SampleSettingsScreen(
            uiState = SampleSettingsUiState(
                isLoading = false,
                notificationEnabled = true,
                confirmOnLoginEnabled = false,
                keepCacheEnabled = true,
                screenErrorMessage = stringResource(R.string.sample_error_message_storage)
            ),
            onEvent = {}
        )
    }
}
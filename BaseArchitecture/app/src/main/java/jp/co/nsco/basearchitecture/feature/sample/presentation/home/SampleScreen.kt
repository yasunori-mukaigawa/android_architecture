package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.ui.loading.LoadingContent
import jp.co.nsco.basearchitecture.ui.theme.BaseArchitectureTheme

/**
 * Sample Home 画面の Stateless Screen。
 *
 * 本 Composable は、SampleUiState に基づいて
 * Sample Home 画面を描画する。
 *
 * ■ 提供する責務
 *   Homeヘッダーカードの表示
 *   読み込み状態の表示
 *   画面イベント通知口の提供
 *
 * ■ 設計上の意図
 *   本 Composable は Stateless Screen として定義し、
 *   ViewModel、NavController、UseCase、Repository を直接扱わない。
 *
 *   画面状態は SampleUiState として外から受け取り、
 *   ユーザー操作が必要になった場合は SampleUiEvent として外へ通知する。
 *
 *   これにより、状態管理や副作用処理は Route / ViewModel 側に閉じ込め、
 *   Screen は表示責務に集中できる。
 *
 * @param uiState Sample Home 画面の UI 状態。
 * @param onEvent ユーザー操作や画面イベントを通知するコールバック。
 * @param modifier 画面全体に適用する Modifier。
 */
@Composable
fun SampleScreen(
    uiState: SampleUiState,
    onEvent: (SampleUiEvent) -> Unit,
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
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            SampleGreetingCard(
                greetingMessage = uiState.greetingMessage,
                dateText = uiState.dateText,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Sample Home 画面の挨拶カードを表示する。
 *
 * ■ 提供する責務
 *   挨拶文言の表示
 *   補足文言の表示
 *   日付表示
 *   装飾アイコンの表示
 *
 * ■ 設計上の意図
 *   Home 画面のカード表示を private Composable として切り出し、
 *   SampleScreen 本体の見通しを保つ。
 *
 *   本 Composable は表示専用であり、
 *   状態更新や画面遷移は行わない。
 *
 * @param greetingMessage 表示する挨拶文言。
 * @param dateText 表示する日付文字列。
 * @param modifier カードに適用する Modifier。
 */
@Composable
private fun SampleGreetingCard(
    greetingMessage: String,
    dateText: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Business,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.width(22.dp))

                Column {
                    Text(
                        text = greetingMessage,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.sample_home_greeting_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = dateText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

/**
 * Sample Home 画面の通常表示 Preview。
 */
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview_Normal() {
    BaseArchitectureTheme {
        SampleScreen(
            uiState = SampleUiState(
                isLoading = false,
                greetingMessage = stringResource(R.string.sample_home_greeting_morning),
                dateText = "2025年5月20日（火）"
            ),
            onEvent = {}
        )
    }
}

/**
 * Sample Home 画面の読み込み中 Preview。
 */
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview_Loading() {
    BaseArchitectureTheme {
        SampleScreen(
            uiState = SampleUiState(
                isLoading = true,
                greetingMessage = stringResource(R.string.sample_home_greeting_afternoon),
                dateText = "2025年5月20日（火）"
            ),
            onEvent = {}
        )
    }
}

/**
 * Sample Home 画面の夜表示 Preview。
 */
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview_Evening() {
    BaseArchitectureTheme {
        SampleScreen(
            uiState = SampleUiState(
                isLoading = false,
                greetingMessage = stringResource(R.string.sample_home_greeting_evening),
                dateText = "2025年5月20日（火）"
            ),
            onEvent = {}
        )
    }
}
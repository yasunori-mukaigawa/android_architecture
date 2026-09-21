package jp.co.nsco.basearchitecture.core.ui.loading

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * ローディング状態に応じてコンテンツ上へ進捗表示を重ねる Composable。
 *
 * 本 Composable は、通常の画面コンテンツを表示した上で、
 * isLoading が true の場合に中央へ CircularProgressIndicator を表示する責務を持つ。
 *
 * ローディング中は透明な overlay を配置し、
 * 背面コンテンツへのクリック操作を抑止する。
 *
 * @param isLoading true の場合、コンテンツ上に CircularProgressIndicator を表示する。
 * @param modifier この Composable 全体に適用する Modifier。
 * @param content 通常時、およびローディング中の背面に表示するコンテンツ。
 */
@Composable
fun LoadingContent(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        content()

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {}
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * アプリ共通の余白定義。
 *
 * 各画面で余白値を直接散在させず、意味のあるspacing scaleを参照できるようにする。
 */
data class BaseSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp
)

/** Defaultテーマで使用する標準spacing。 */
val DefaultSpacing = BaseSpacing()

/** ComposeのCompositionLocalとして提供する現在テーマのspacing。 */
val LocalBaseSpacing = staticCompositionLocalOf { DefaultSpacing }

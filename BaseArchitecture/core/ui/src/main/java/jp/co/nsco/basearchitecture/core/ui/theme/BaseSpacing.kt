package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * アプリ共通の余白定義。
 *
 * BaseSpacing は、画面内の padding、margin、要素間隔などで使用する
 * 標準的な spacing scale を表す。
 *
 * ■ 提供する責務
 *   極小余白の定義
 *   小余白の定義
 *   中余白の定義
 *   大余白の定義
 *   特大余白の定義
 *
 * ■ 設計上の意図
 *   各画面で `4.dp`、`8.dp`、`16.dp` などを直接書き続けると、
 *   画面ごとの余白ルールにばらつきが出やすくなる。
 *
 *   BaseSpacing を通じて余白の意味を揃え、
 *   テーマ単位で spacing を差し替えられるようにする。
 *
 * @property xs 極小余白。
 * @property sm 小余白。
 * @property md 中余白。
 * @property lg 大余白。
 * @property xl 特大余白。
 */
data class BaseSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp
)

/**
 * Default テーマで使用する標準 spacing。
 */
val DefaultSpacing = BaseSpacing()

/**
 * Dashboard テーマで使用する spacing。
 *
 * 現時点では Default と同じ値を使用するが、
 * テーマごとに余白設計を変えたい場合はここで調整する。
 */
val DashboardSpacing = BaseSpacing(
    xs = 4.dp,
    sm = 8.dp,
    md = 16.dp,
    lg = 24.dp,
    xl = 32.dp
)

/**
 * CompositionLocal として提供するアプリ共通 spacing。
 *
 * BaseAppTheme で ThemePreset の spacing を提供し、
 * 各 Composable は BaseTheme.spacing 経由で現在テーマの spacing を参照する。
 */
val LocalBaseSpacing = staticCompositionLocalOf { DefaultSpacing }
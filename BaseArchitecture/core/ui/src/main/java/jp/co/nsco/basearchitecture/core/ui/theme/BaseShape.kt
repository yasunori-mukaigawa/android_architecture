package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Default テーマで使用する角丸定義。
 *
 * アプリ標準のカード、ボタン、コンテナなどに使用する。
 */
val DefaultShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

/**
 * Dashboard テーマで使用する角丸定義。
 *
 * Default テーマよりやや大きめの角丸を定義し、
 * Dashboard 用の視覚的な印象を分ける。
 */
val DashboardShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(28.dp)
)
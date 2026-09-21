package jp.co.nsco.basearchitecture.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Defaultテーマで使用する角丸定義。
 *
 * 案件固有の形状ルールがある場合は、アプリ側のThemePresetで差し替える。
 */
val DefaultShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)

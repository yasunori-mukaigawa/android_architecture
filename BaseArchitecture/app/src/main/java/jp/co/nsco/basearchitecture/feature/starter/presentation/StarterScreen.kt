package jp.co.nsco.basearchitecture.feature.starter.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.co.nsco.basearchitecture.ui.theme.BaseArchitectureTheme

/**
 * Starter画面の純粋な表示Composable。
 *
 * StarterScreenはViewModelやUseCaseを知らず、Routeから受け取った表示値だけを描画する。
 * 案件開始時は、このComposableを画面固有のUiStateとイベント通知へ置き換える。
 *
 * ■ 設計上の意図
 *   Screenを状態管理から分離し、Previewで画面を直接確認できる形を維持する。
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun StarterScreen(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = title) })
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/** Starter画面の標準表示Preview。 */
@Preview(showBackground = true)
@Composable
private fun StarterScreenPreview() {
    BaseArchitectureTheme {
        StarterScreen(
            title = "Starter",
            message = "Template is ready."
        )
    }
}

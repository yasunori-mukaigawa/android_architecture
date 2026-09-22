package jp.co.nsco.basearchitecture.feature.license.presentation.list

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

/** License一覧画面のPlaceholder表示Composable。 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LicenseScreen(
    title: String,
    message: String,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = title) }) }
    ) { paddingValues ->
        Column(
            modifier = modifier.fillMaxSize().padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

/** License一覧PlaceholderのPreview。 */
@Preview(showBackground = true)
@Composable
private fun LicenseScreenPreview() {
    BaseArchitectureTheme {
        LicenseScreen("ライセンス", "ライセンスの仮画面です。")
    }
}

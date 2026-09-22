package jp.co.nsco.basearchitecture.feature.sample.presentation.history

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
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigation
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem

/** Sample History画面の表示Composable。 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SampleHistoryScreen(
    title: String,
    message: String,
    onBottomNavigationItemClick: ((SampleBottomNavigationItem) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(text = title) }) },
        bottomBar = {
            onBottomNavigationItemClick?.let { onClick ->
                SampleBottomNavigation(
                    selectedItem = SampleBottomNavigationItem.History,
                    onItemClick = onClick
                )
            }
        }
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

/** Sample History画面のPreview。 */
@Preview(showBackground = true)
@Composable
private fun SampleHistoryScreenPreview() {
    BaseArchitectureTheme {
        SampleHistoryScreen("Sample History", "Sample Historyの仮画面です。")
    }
}

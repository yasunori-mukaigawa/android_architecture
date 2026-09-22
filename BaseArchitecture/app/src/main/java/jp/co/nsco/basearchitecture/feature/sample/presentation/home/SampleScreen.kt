package jp.co.nsco.basearchitecture.feature.sample.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigation
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleBottomNavigationItem
import jp.co.nsco.basearchitecture.feature.sample.presentation.common.SampleTopBar
import jp.co.nsco.basearchitecture.ui.theme.BaseArchitectureTheme

/**
 * Sample Home画面の表示Composable。
 *
 * ScreenはViewModel、UseCase、Repository、NavControllerを知らず、
 * Routeから受け取った表示値だけを描画する。
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SampleScreen(
    titleResId: Int,
    message: String,
    onMenuClick: () -> Unit = {},
    onBottomNavigationItemClick: ((SampleBottomNavigationItem) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            SampleTopBar(
                titleResId = titleResId,
                onMenuClick = onMenuClick
            )
        },
        bottomBar = {
            onBottomNavigationItemClick?.let { onClick ->
                SampleBottomNavigation(
                    selectedItem = SampleBottomNavigationItem.Home,
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

/** Sample Home画面のPreview。 */
@Preview(showBackground = true)
@Composable
private fun SampleScreenPreview() {
    BaseArchitectureTheme {
        SampleScreen(
            titleResId = R.string.sample_home_title,
            message = "Sample Homeの仮画面です。"
        )
    }
}

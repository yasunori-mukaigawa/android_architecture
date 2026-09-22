package jp.co.nsco.basearchitecture.feature.sample.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType
import jp.co.nsco.basearchitecture.feature.legal.presentation.LegalDocumentRoutes
import jp.co.nsco.basearchitecture.feature.license.presentation.common.LicenseRoutes
import jp.co.nsco.basearchitecture.feature.versioninfo.presentation.VersionInfoRoutes

/**
 * Sample Feature共通のTopAppBar。
 *
 * 表示とメニュー操作の通知だけを担当し、Drawerの開閉は呼び出し側へ委譲する。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleTopBar(
    titleResId: Int,
    onMenuClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(titleResId),
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = stringResource(R.string.sample_drawer_title)
                )
            }
        },
        actions = {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

/**
 * Sample Feature共通のNavigation Drawer内容。
 *
 * Drawer項目の表示と押下通知だけを担当し、遷移処理はRouteへ委譲する。
 */
@Composable
fun SampleDrawerContent(
    onItemClick: (SampleDrawerItem) -> Unit
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = stringResource(R.string.sample_drawer_title),
            modifier = Modifier.padding(24.dp),
            fontWeight = FontWeight.Bold
        )

        sampleDrawerItems().forEach { item ->
            NavigationDrawerItem(
                label = { Text(text = stringResource(item.labelResId)) },
                selected = false,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

/**
 * Sample Feature共通のBottom Navigation。
 *
 * 選択状態の表示と押下通知だけを担当し、実際のNavigationはRouteへ委譲する。
 */
@Composable
fun SampleBottomNavigation(
    selectedItem: SampleBottomNavigationItem,
    onItemClick: (SampleBottomNavigationItem) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        SampleBottomNavigationItem.entries.forEach { item ->
            NavigationBarItem(
                selected = item == selectedItem,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelResId)
                    )
                },
                label = { Text(text = stringResource(item.labelResId)) }
            )
        }
    }
}

/** Sample FeatureのBottom Navigation項目。 */
enum class SampleBottomNavigationItem(
    val labelResId: Int,
    val icon: ImageVector
) {
    /** 操作履歴画面。 */
    History(R.string.sample_bottom_history, Icons.Outlined.History),

    /** Sample Home画面。 */
    Home(R.string.sample_bottom_home, Icons.Outlined.Home),

    /** Sample Settings画面。 */
    Settings(R.string.sample_bottom_settings, Icons.Outlined.Settings)
}

/** Navigation Drawerの表示項目。 */
data class SampleDrawerItem(
    val labelResId: Int,
    val icon: ImageVector,
    val destinationRoute: String? = null
)

/** Sample Featureから遷移できるDrawer項目を定義する。 */
private fun sampleDrawerItems(): List<SampleDrawerItem> {
    return listOf(
        SampleDrawerItem(
            labelResId = R.string.sample_drawer_privacy_policy,
            icon = Icons.Outlined.Description,
            destinationRoute = LegalDocumentRoutes.document(LegalDocumentType.PrivacyPolicy)
        ),
        SampleDrawerItem(
            labelResId = R.string.sample_drawer_terms,
            icon = Icons.Outlined.Gavel,
            destinationRoute = LegalDocumentRoutes.document(LegalDocumentType.TermsOfService)
        ),
        SampleDrawerItem(
            labelResId = R.string.sample_drawer_licenses,
            icon = Icons.AutoMirrored.Outlined.Article,
            destinationRoute = LicenseRoutes.List
        ),
        SampleDrawerItem(
            labelResId = R.string.sample_drawer_version,
            icon = Icons.Outlined.Info,
            destinationRoute = VersionInfoRoutes.VersionInfo
        )
    )
}

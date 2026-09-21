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
 * Sample Feature 共通の TopAppBar。
 *
 * SampleTopBar は、Sample Feature 内の複数画面で利用する
 * ヘッダー表示を共通化する。
 *
 * ■ 提供する責務
 *   画面タイトルの表示
 *   Navigation Drawer を開くためのメニュー操作通知
 *   通知アイコンの表示
 *
 * ■ 設計上の意図
 *   Sample Home / History / Settings で共通する TopAppBar 表示を
 *   画面ごとに重複実装しないようにする。
 *
 *   本 Composable は表示とクリック通知のみを担当し、
 *   Drawer の開閉状態や通知アイコン押下時の処理内容は呼び出し側へ委譲する。
 *
 * @param titleResId 表示する画面タイトルの文字列リソースID。
 * @param onMenuClick メニューアイコン押下時のコールバック。
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
 * Sample Feature 共通の Navigation Drawer 内容。
 *
 * SampleDrawerContent は、Sample Feature から遷移できる補助画面や
 * 関連情報画面への導線を表示する。
 *
 * ■ 提供する責務
 *   Drawer タイトルの表示
 *   Drawer item 一覧の表示
 *   Drawer item 押下通知
 *
 * ■ 設計上の意図
 *   Drawer の表示項目を sampleDrawerItems() に集約し、
 *   UI 側では一覧を描画するだけにする。
 *
 *   遷移処理そのものは本 Composable では行わず、
 *   選択された SampleDrawerItem を呼び出し側へ通知する。
 *
 * @param onItemClick Drawer item 押下時のコールバック。
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
                label = {
                    Text(text = stringResource(item.labelResId))
                },
                selected = false,
                onClick = { onItemClick(item) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

/**
 * Sample Feature 共通の Bottom Navigation。
 *
 * SampleBottomNavigation は、Sample Home / History / Settings の
 * 主要画面を切り替えるための下部Navigationを表示する。
 *
 * ■ 提供する責務
 *   Bottom Navigation の表示
 *   選択中Itemの表示反映
 *   Bottom Navigation item 押下通知
 *
 * ■ 設計上の意図
 *   Sample Feature 内の主要画面切り替えUIを共通化する。
 *
 *   本 Composable は選択状態の表示とクリック通知のみを担当し、
 *   実際の Navigation 実行は Route 側へ委譲する。
 *
 * @param selectedItem 現在選択中のBottom Navigation item。
 * @param onItemClick Bottom Navigation item 押下時のコールバック。
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
                onClick = {
                    onItemClick(item)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.labelResId)
                    )
                },
                label = {
                    Text(text = stringResource(item.labelResId))
                }
            )
        }
    }
}

/**
 * Sample Feature の Bottom Navigation item。
 *
 * SampleBottomNavigationItem は、Sample Feature の主要画面である
 * History / Home / Settings を表す。
 *
 * ■ 設計上の意図
 *   Bottom Navigation の表示ラベルとアイコンを enum にまとめることで、
 *   UI 実装側の分岐や重複を減らす。
 *
 * @property labelResId 表示ラベルの文字列リソースID。
 * @property icon 表示アイコン。
 */
enum class SampleBottomNavigationItem(
    val labelResId: Int,
    val icon: ImageVector
) {
    /**
     * 操作履歴画面。
     */
    History(R.string.sample_bottom_history, Icons.Outlined.History),

    /**
     * Sample Home 画面。
     */
    Home(R.string.sample_bottom_home, Icons.Outlined.Home),

    /**
     * Sample Settings 画面。
     */
    Settings(R.string.sample_bottom_settings, Icons.Outlined.Settings)
}

/**
 * Sample Feature の Drawer item。
 *
 * SampleDrawerItem は、Drawer に表示する1項目を表す。
 *
 * ■ 設計上の意図
 *   Drawer 表示に必要なラベル、アイコン、遷移先routeを1つのモデルにまとめる。
 *
 *   destinationRoute が null の場合は、遷移先を持たない項目として扱える。
 *
 * @property labelResId 表示ラベルの文字列リソースID。
 * @property icon 表示アイコン。
 * @property destinationRoute 遷移先route。遷移先がない場合は null。
 */
data class SampleDrawerItem(
    val labelResId: Int,
    val icon: ImageVector,
    val destinationRoute: String? = null
)

/**
 * Sample Feature の Drawer item 一覧を生成する。
 *
 * ■ 提供する責務
 *   プライバシーポリシー画面への導線定義
 *   利用規約画面への導線定義
 *   OSSライセンス画面への導線定義
 *   バージョン情報画面への導線定義
 *
 * ■ 設計上の意図
 *   Drawer の項目定義を Composable 内に直書きせず、
 *   一覧生成処理として分離する。
 *
 *   Sample Feature は、Legal / License / VersionInfo Feature への導線を持つため、
 *   各Featureの Routes を利用して destinationRoute を生成する。
 *
 * @return Drawer に表示する item 一覧。
 */
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
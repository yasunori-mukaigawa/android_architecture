package jp.co.nsco.basearchitecture.feature.legal.presentation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/** 法務文書FeatureのPlaceholder Graphを登録する。 */
fun NavGraphBuilder.legalDocumentGraph(navController: NavController) {
    composable(
        route = LegalDocumentRoutes.Document,
        arguments = listOf(
            navArgument(LegalDocumentRoutes.ArgType) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val type = backStackEntry.arguments
            ?.getString(LegalDocumentRoutes.ArgType)
            ?.let(LegalDocumentType::fromRouteValue)
            ?: LegalDocumentType.PrivacyPolicy

        LegalDocumentRoute(
            documentType = type,
            navController = navController
        )
    }
}

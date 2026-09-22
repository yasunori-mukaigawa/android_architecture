package jp.co.nsco.basearchitecture.feature.legal.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.feature.legal.domain.LegalDocumentType

/** 法務文書画面のRoute。NavControllerはRouteに留める。 */
@Composable
fun LegalDocumentRoute(
    documentType: LegalDocumentType,
    navController: NavController
) {
    LegalDocumentScreen(
        title = stringResource(R.string.legal_document_title),
        message = stringResource(R.string.legal_document_placeholder),
        documentType = documentType
    )
}

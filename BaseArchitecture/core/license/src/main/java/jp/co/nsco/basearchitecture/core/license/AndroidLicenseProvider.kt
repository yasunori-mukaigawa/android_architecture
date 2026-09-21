package jp.co.nsco.basearchitecture.core.license

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.error.AppError
import jp.co.nsco.basearchitecture.core.result.AppResult

/**
 * Android の raw resource から OSS ライセンス情報を取得する LicenseProvider 実装。
 *
 * 本クラスは、Google Play services OSS Licenses Plugin などで生成された
 * third_party_license_metadata / third_party_licenses を読み取り、
 * アプリ内で扱う LicenseInfo / LicenseDetail に変換する責務を持つ。
 *
 * ■ 提供する責務
 *   OSS ライセンス一覧の取得
 *   OSS ライセンス詳細の取得
 *   生成済み raw resource の読取
 *   生成済みライセンスが存在しない場合の fallback 提供
 *   Android raw resource 依存の隠蔽
 *
 * ■ 設計上の意図
 *   呼び出し側へ Context / Resources / raw resource 名などの Android 実装詳細を漏らさない。
 *
 *   ライセンス表示画面や UseCase は LicenseProvider の契約のみを参照し、
 *   ライセンス情報が生成ファイル由来か fallback 由来かを意識しない。
 *
 *   開発初期や OSS ライセンス生成設定が未整備の状態でも、
 *   fallbackLicenses によりライセンス画面を動作確認できるようにする。
 *
 * @param context アプリケーション Context。
 *                raw resource 読取に使用するため、ApplicationContext を注入する。
 */
class AndroidLicenseProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) : LicenseProvider {

    /**
     * 生成済みライセンスリソースが存在しない場合に使用する fallback ライセンス一覧。
     *
     * 開発初期や OSS ライセンス生成処理が未導入の状態でも、
     * ライセンス画面の表示確認を行えるようにするための代替データ。
     */
    private val fallbackLicenses = listOf(
        LicenseDetail(
            id = "androidx-core",
            name = "AndroidX Core",
            licenseName = APACHE_LICENSE_NAME,
            copyright = ANDROID_OPEN_SOURCE_COPYRIGHT,
            licenseText = APACHE_LICENSE_TEXT
        ),
        LicenseDetail(
            id = "jetpack-compose",
            name = "Jetpack Compose",
            licenseName = APACHE_LICENSE_NAME,
            copyright = ANDROID_OPEN_SOURCE_COPYRIGHT,
            licenseText = APACHE_LICENSE_TEXT
        ),
        LicenseDetail(
            id = "room",
            name = "Room",
            licenseName = APACHE_LICENSE_NAME,
            copyright = ANDROID_OPEN_SOURCE_COPYRIGHT,
            licenseText = APACHE_LICENSE_TEXT
        ),
        LicenseDetail(
            id = "hilt",
            name = "Hilt",
            licenseName = APACHE_LICENSE_NAME,
            copyright = ANDROID_OPEN_SOURCE_COPYRIGHT,
            licenseText = APACHE_LICENSE_TEXT
        ),
        LicenseDetail(
            id = "kotlin-coroutines",
            name = "Kotlin Coroutines",
            licenseName = APACHE_LICENSE_NAME,
            copyright = "非同期プログラミング用の Kotlin ライブラリ",
            licenseText = APACHE_LICENSE_TEXT
        ),
        LicenseDetail(
            id = "navigation-compose",
            name = "Navigation Compose",
            licenseName = APACHE_LICENSE_NAME,
            copyright = ANDROID_OPEN_SOURCE_COPYRIGHT,
            licenseText = APACHE_LICENSE_TEXT
        ),
        LicenseDetail(
            id = "material3",
            name = "Material 3",
            licenseName = APACHE_LICENSE_NAME,
            copyright = ANDROID_OPEN_SOURCE_COPYRIGHT,
            licenseText = APACHE_LICENSE_TEXT
        )
    )

    /**
     * OSS ライセンス一覧を取得する。
     *
     * 生成済み raw resource が存在する場合は、その内容を読み取って一覧化する。
     * 生成済み raw resource が存在しない場合、または読み取り結果が空の場合は、
     * fallbackLicenses を使用する。
     *
     * 一覧表示では全文ライセンスを不要とするため、
     * LicenseDetail から LicenseInfo へ変換して返す。
     *
     * @return OSS ライセンス一覧の取得結果。
     *         成功時は LicenseInfo の一覧を返す。
     */
    override fun getLicenses(): AppResult<List<LicenseInfo>> {
        val licenses = loadGeneratedLicenses().ifEmpty { fallbackLicenses }

        return AppResult.Success(
            licenses.map { detail ->
                LicenseInfo(
                    id = detail.id,
                    name = detail.name,
                    licenseName = detail.licenseName,
                    copyright = detail.copyright
                )
            }
        )
    }

    /**
     * 指定された ID の OSS ライセンス詳細を取得する。
     *
     * 生成済み raw resource から読み取ったライセンスを優先し、
     * 見つからない場合は fallbackLicenses から検索する。
     *
     * 対象 ID が存在しない場合は AppError.LocalStorage を返す。
     *
     * @param id 取得対象のライセンス ID。
     * @return ライセンス詳細の取得結果。
     *         成功時は LicenseDetail、対象が存在しない場合は AppError.LocalStorage を返す。
     */
    override fun getLicenseDetail(id: String): AppResult<LicenseDetail> {
        val licenses = loadGeneratedLicenses().ifEmpty { fallbackLicenses }
        val license = licenses.firstOrNull { it.id == id }
            ?: fallbackLicenses.firstOrNull { it.id == id }
            ?: return AppResult.Failure(
                AppError.LocalStorage(code = "LICENSE_DETAIL_NOT_FOUND")
            )

        return AppResult.Success(license)
    }

    /**
     * 生成済み raw resource から OSS ライセンス詳細一覧を読み取る。
     *
     * third_party_license_metadata と third_party_licenses が存在する場合のみ読み取る。
     * どちらかが存在しない場合は、生成済みライセンスなしとして空リストを返す。
     *
     * 読み取りや解析に失敗した場合も例外を外へ投げず、
     * fallback 利用へ切り替えられるよう空リストを返す。
     *
     * @return 生成済み raw resource から復元した LicenseDetail 一覧。
     */
    private fun loadGeneratedLicenses(): List<LicenseDetail> {
        val metadataResId = rawResourceId(GENERATED_METADATA_RESOURCE_NAME)
        val licensesResId = rawResourceId(GENERATED_LICENSES_RESOURCE_NAME)
        if (metadataResId == 0 || licensesResId == 0) {
            return emptyList()
        }

        return runCatching {
            val metadata = context.resources.openRawResource(metadataResId)
                .bufferedReader()
                .use { it.readText() }
            val licenseText = context.resources.openRawResource(licensesResId)
                .bufferedReader()
                .use { it.readText() }

            metadata.lineSequence()
                .mapNotNull { line -> line.toLicenseDetailOrNull(licenseText) }
                .toList()
        }.getOrDefault(emptyList())
    }

    /**
     * 生成済みメタデータの1行を LicenseDetail に変換する。
     *
     * メタデータは `offset:length name` 形式であることを前提とする。
     * offset と length を使用してライセンス全文リソースから該当部分を切り出し、
     * LicenseDetail として復元する。
     *
     * 解析できない行は不正データとして null を返す。
     *
     * @param licenseText 生成済みライセンス全文。
     * @return 変換できた場合は LicenseDetail、解析できない場合は null。
     */
    private fun String.toLicenseDetailOrNull(licenseText: String): LicenseDetail? {
        val firstSpaceIndex = indexOf(' ')
        if (firstSpaceIndex <= 0) {
            return null
        }

        val rangeText = substring(startIndex = 0, endIndex = firstSpaceIndex)
        val name = substring(startIndex = firstSpaceIndex + 1).trim()
        val offsetAndLength = rangeText.split(':')
        if (offsetAndLength.size != 2) {
            return null
        }

        val offset = offsetAndLength[0].toIntOrNull() ?: return null
        val length = offsetAndLength[1].toIntOrNull() ?: return null
        val text = licenseText.substringOrNull(offset, length) ?: return null

        return LicenseDetail(
            id = name.toRouteSafeId(),
            name = name,
            licenseName = text.firstMeaningfulLine() ?: GENERATED_LICENSE_NAME,
            copyright = GENERATED_COPYRIGHT_TEXT,
            licenseText = text
        )
    }

    /**
     * 指定された raw resource 名に対応する resource ID を取得する。
     *
     * @param name raw resource 名。
     * @return resource ID。存在しない場合は 0。
     */
    private fun rawResourceId(name: String): Int {
        return context.resources.getIdentifier(
            name,
            "raw",
            context.packageName
        )
    }

    /**
     * 指定された offset / count で文字列を切り出す。
     *
     * 範囲外指定の場合は例外を投げず null を返す。
     * count が文字列末尾を超える場合は、末尾までを切り出す。
     *
     * @param offset 切り出し開始位置。
     * @param count 切り出す文字数。
     * @return 切り出した文字列。指定が不正な場合は null。
     */
    private fun String.substringOrNull(offset: Int, count: Int): String? {
        if (offset < 0 || count <= 0 || offset >= this.length) {
            return null
        }

        val endIndex = (offset + count).coerceAtMost(this.length)
        return substring(offset, endIndex)
    }

    /**
     * 文字列内の最初の有効行を取得する。
     *
     * 空白行を除外し、最初に見つかった非空行を返す。
     * ライセンス名の推定など、表示上の代表行を取り出す目的で使用する。
     *
     * @return 最初の非空行。存在しない場合は null。
     */
    private fun String.firstMeaningfulLine(): String? {
        return lineSequence()
            .map { it.trim() }
            .firstOrNull { it.isNotBlank() }
    }

    /**
     * ライセンス名から Navigation route に利用しやすい ID を生成する。
     *
     * 英数字以外をハイフンに置換し、前後のハイフンを除去する。
     * 変換後に空文字になる場合は hashCode を文字列化した値を使用する。
     *
     * @return route parameter として扱いやすい ID。
     */
    private fun String.toRouteSafeId(): String {
        return lowercase()
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')
            .ifBlank { hashCode().toString() }
    }

    private companion object {
        private const val GENERATED_METADATA_RESOURCE_NAME = "third_party_license_metadata"
        private const val GENERATED_LICENSES_RESOURCE_NAME = "third_party_licenses"
        private const val GENERATED_LICENSE_NAME = "OSS License"
        private const val GENERATED_COPYRIGHT_TEXT = "Generated from dependency POM"
        private const val APACHE_LICENSE_NAME = "Apache License 2.0"
        private const val ANDROID_OPEN_SOURCE_COPYRIGHT = "Copyright The Android Open Source Project"

        private val APACHE_LICENSE_TEXT = """
            Apache License
            Version 2.0, January 2004
            http://www.apache.org/licenses/

            TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

            1. Definitions.

            "License" shall mean the terms and conditions for use, reproduction,
            and distribution as defined by Sections 1 through 9 of this document.

            "Licensor" shall mean the copyright owner or entity authorized by
            the copyright owner that is granting the License.

            "Legal Entity" shall mean the union of the acting entity and all
            other entities that control, are controlled by, or are under common
            control with that entity. For the purposes of this definition,
            "control" means (i) the power, direct or indirect, to cause the
            direction or management of such entity, whether by contract or
            otherwise, or (ii) ownership of fifty percent (50%) or more of the
            outstanding shares, or (iii) beneficial ownership of such entity.

            "You" (or "Your") shall mean an individual or Legal Entity
            exercising permissions granted by this License.

            "Source" form shall mean the preferred form for making modifications,
            including but not limited to software source code, documentation
            source, and configuration files.

            "Object" form shall mean any form resulting from mechanical
            transformation or translation of a Source form, including but not
            limited to compiled object code, generated documentation, and
            conversions to other media types.
        """.trimIndent()
    }
}
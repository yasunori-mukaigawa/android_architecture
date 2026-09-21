package jp.co.nsco.basearchitecture.feature.versioninfo.domain

import jp.co.nsco.basearchitecture.core.external.ExternalUri

/**
 * アプリ情報画面から開く外部リンク情報。
 *
 * AppExternalLinks は、VersionInfo 画面で利用する
 * ストアページやアプリ情報ページなどの外部リンクを表す Domain Model。
 *
 * ■ 提供する責務
 *   ストアページURIの保持
 *   アプリ情報ページURIの保持
 *
 * ■ 設計上の意図
 *   画面側が URI の組み立て方法を知らなくてもよいように、
 *   VersionInfo に必要な外部リンクを Domain Model としてまとめる。
 *
 *   実際の URI 生成方法は AppExternalLinksProvider の実装に委譲する。
 *
 * @property storeUri ストアページを開くための URI。
 * @property appInfoPageUri アプリ情報ページを開くための URI。
 */
data class AppExternalLinks(
    val storeUri: ExternalUri,
    val appInfoPageUri: ExternalUri
)
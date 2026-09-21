package jp.co.nsco.basearchitecture.feature.versioninfo.data

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionInfo
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionRepository

/**
 * 最新バージョン情報を固定値で返す Repository 実装。
 *
 * FakeLatestVersionRepository は、API 通信を行わずに
 * サンプル用の最新バージョン情報を返す。
 *
 * ■ 提供する責務
 *   固定値による最新バージョン情報の返却
 *   VersionInfo 画面のサンプル表示
 *   API 未接続状態での動作確認
 *
 * ■ 設計上の意図
 *   ベースアーキテクチャのサンプルでは、
 *   実APIが未準備でも VersionInfo 画面の構成や
 *   UseCase / Repository の接続を確認できるようにする。
 *
 *   LatestVersionRepository 契約に対する実装であるため、
 *   UseCase は Fake 実装か Retrofit 実装かを意識しない。
 */
class FakeLatestVersionRepository @Inject constructor() : LatestVersionRepository {

    /**
     * サンプル用の最新バージョン情報を取得する。
     *
     * @return 固定値の最新バージョン情報。
     */
    override suspend fun getLatestVersion(): AppResult<LatestVersionInfo> {
        return AppResult.Success(
            LatestVersionInfo(
                latestVersionName = "v1.2.4",
                latestVersionCode = 10204L
            )
        )
    }
}
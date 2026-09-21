package jp.co.nsco.basearchitecture.feature.versioninfo.data

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.network.ApiResponseHandler
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.result.map
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionInfo
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionRepository

/**
 * Retrofit を利用して最新バージョン情報を取得する Repository 実装。
 *
 * RetrofitLatestVersionRepository は、LatestVersionApi を呼び出し、
 * API response を LatestVersionInfo へ変換して返す。
 *
 * ■ 提供する責務
 *   最新バージョン情報 API の呼び出し
 *   HTTP response の AppResult 変換
 *   DTO から Domain Model への変換
 *
 * ■ 設計上の意図
 *   Retrofit や HTTP response の扱いを Data 層に閉じ込める。
 *
 *   GetVersionInfoUseCase は LatestVersionRepository 契約に依存するだけでよく、
 *   最新バージョン情報の取得元が API なのか Fake なのかを意識しない。
 *
 *   HTTP ステータスや通信例外の AppResult 変換は ApiResponseHandler に委譲し、
 *   本 Repository は API 呼び出しと Domain Model 変換に集中する。
 *
 * @param api 最新バージョン情報取得 API。
 * @param responseHandler API response を AppResult に変換する Handler。
 * @param mapper DTO を Domain Model へ変換する Mapper。
 */
class RetrofitLatestVersionRepository @Inject constructor(
    private val api: LatestVersionApi,
    private val responseHandler: ApiResponseHandler,
    private val mapper: LatestVersionMapper
) : LatestVersionRepository {

    /**
     * 最新バージョン情報を取得する。
     *
     * @return 最新バージョン情報の取得結果。
     */
    override suspend fun getLatestVersion(): AppResult<LatestVersionInfo> {
        return responseHandler.handle { api.getLatestVersion() }
            .map(mapper::map)
    }
}
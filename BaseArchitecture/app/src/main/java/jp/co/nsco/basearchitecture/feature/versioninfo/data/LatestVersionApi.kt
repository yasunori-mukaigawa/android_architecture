package jp.co.nsco.basearchitecture.feature.versioninfo.data

import retrofit2.Response
import retrofit2.http.GET

/**
 * 最新バージョン情報取得 API。
 *
 * LatestVersionApi は、Retrofit を利用して
 * サーバー上の最新バージョン情報を取得するための API 契約を表す。
 *
 * ■ 提供する責務
 *   最新バージョン情報取得エンドポイントの定義
 *
 * ■ 設計上の意図
 *   Retrofit 固有の API 定義は Data 層に閉じ込める。
 *
 *   Application / Domain 層は Retrofit や HTTP response を直接扱わず、
 *   Repository 実装を通して LatestVersionInfo を取得する。
 */
interface LatestVersionApi {

    /**
     * 最新バージョン情報を取得する。
     *
     * @return 最新バージョン情報 DTO を含む HTTP response。
     */
    @GET("apps/base-architecture/version")
    suspend fun getLatestVersion(): Response<LatestVersionDto>
}
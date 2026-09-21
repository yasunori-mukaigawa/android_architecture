package jp.co.nsco.basearchitecture.feature.versioninfo.data

import javax.inject.Inject
import jp.co.nsco.basearchitecture.feature.versioninfo.domain.LatestVersionInfo

/**
 * 最新バージョン情報 DTO を Domain Model へ変換する Mapper。
 *
 * LatestVersionMapper は、Data 層の LatestVersionDto を
 * Domain 層の LatestVersionInfo へ変換する。
 *
 * ■ 提供する責務
 *   DTO から Domain Model への変換
 *
 * ■ 設計上の意図
 *   API response の構造を Domain 層へ直接漏らさないようにする。
 *
 *   Data 層では DTO を扱い、Application / Domain 層では
 *   業務上の意味を持つ LatestVersionInfo を扱う。
 */
class LatestVersionMapper @Inject constructor() {

    /**
     * DTO を Domain Model へ変換する。
     *
     * @param dto 最新バージョン情報 DTO。
     * @return 最新バージョン情報 Domain Model。
     */
    fun map(dto: LatestVersionDto): LatestVersionInfo {
        return LatestVersionInfo(
            latestVersionName = dto.latestVersionName,
            latestVersionCode = dto.latestVersionCode
        )
    }
}
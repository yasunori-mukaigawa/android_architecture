package jp.co.nsco.basearchitecture.feature.sample.presentation.history

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import jp.co.nsco.basearchitecture.R
import jp.co.nsco.basearchitecture.core.resource.StringProvider
import jp.co.nsco.basearchitecture.core.time.DateTimeProvider
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLog
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogResult
import jp.co.nsco.basearchitecture.feature.operationlog.domain.OperationLogType

/**
 * OperationLog を Sample History 画面表示用 ItemState へ変換する Mapper。
 *
 * SampleHistoryItemMapper は、Domain Model である OperationLog を、
 * Sample History 画面でそのまま描画できる SampleHistoryItemUiState へ変換する。
 *
 * ■ 提供する責務
 *   操作ログIDの表示用ID変換
 *   操作ログタイトル・概要の表示値設定
 *   発生時刻の表示文字列変換
 *   日付グループラベルの生成
 *   OperationLogType から SampleHistoryFilter への変換
 *   エラー表示対象かどうかの判定
 *
 * ■ 設計上の意図
 *   ViewModel や Composable が日時フォーマット、グルーピングラベル、
 *   エラー判定などの表示変換ロジックを直接持たないようにする。
 *
 *   Domain Model は操作ログの意味を表し、
 *   ItemUiState は画面表示に必要な形を表す。
 *
 *   その境界変換を Mapper に集約することで、
 *   Presentation 層内の責務を分離する。
 *
 * @param stringProvider 文字列リソース取得を行う Provider。
 * @param dateTimeProvider 現在日時を取得する Provider。
 */
class SampleHistoryItemMapper @Inject constructor(
    private val stringProvider: StringProvider,
    private val dateTimeProvider: DateTimeProvider
) {

    /**
     * OperationLog を SampleHistoryItemUiState へ変換する。
     *
     * @param log 変換対象の操作ログ。
     * @return Sample History 画面表示用の ItemState。
     */
    fun toItemUiState(log: OperationLog): SampleHistoryItemUiState {
        return SampleHistoryItemUiState(
            id = log.id.value,
            title = log.title,
            summary = log.summary.orEmpty(),
            timeText = formatTime(log.occurredAtMillis),
            groupLabel = formatGroupLabel(log.occurredAtMillis),
            filter = resolveFilter(log.type),
            isError = log.type == OperationLogType.Error || log.result == OperationLogResult.Failure
        )
    }

    /**
     * 操作ログ種別から History 画面用フィルタを解決する。
     *
     * OperationLogType は Domain の分類であり、
     * SampleHistoryFilter は UI の表示分類であるため、ここで変換する。
     */
    private fun resolveFilter(type: OperationLogType): SampleHistoryFilter {
        return when (type) {
            OperationLogType.Setting -> SampleHistoryFilter.Setting
            OperationLogType.Error -> SampleHistoryFilter.Error
            else -> SampleHistoryFilter.Action
        }
    }

    /**
     * epoch millis を時刻表示文字列へ変換する。
     */
    private fun formatTime(millis: Long): String {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern(TimeFormatPattern, Locale.JAPANESE))
    }

    /**
     * epoch millis を日付グループラベルへ変換する。
     *
     * 今日・昨日は文言リソースを利用し、それ以外は日付文字列として表示する。
     */
    private fun formatGroupLabel(millis: Long): String {
        val date = toLocalDate(millis)
        val today = dateTimeProvider.now().toLocalDate()
        val yesterday = today.minusDays(1)

        return when (date) {
            today -> stringProvider.getString(R.string.sample_history_group_today)
            yesterday -> stringProvider.getString(R.string.sample_history_group_yesterday)
            else -> date.format(DateTimeFormatter.ofPattern(DateFormatPattern, Locale.JAPANESE))
        }
    }

    /**
     * epoch millis を実行環境のタイムゾーンにおける LocalDate へ変換する。
     */
    private fun toLocalDate(millis: Long): LocalDate {
        return Instant.ofEpochMilli(millis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    private companion object {

        /**
         * 履歴時刻表示のフォーマット。
         */
        private const val TimeFormatPattern = "HH:mm"

        /**
         * 履歴日付グループ表示のフォーマット。
         */
        private const val DateFormatPattern = "yyyy/MM/dd"
    }
}
package jp.co.nsco.basearchitecture.feature.sample.application

import javax.inject.Inject
import jp.co.nsco.basearchitecture.core.result.AppResult
import jp.co.nsco.basearchitecture.core.time.DateTimeProvider
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleGreetingPolicy
import jp.co.nsco.basearchitecture.feature.sample.domain.SampleHomeHeader

/**
 * Sample Home 画面のヘッダー情報を取得する UseCase。
 *
 * 本 UseCase は、現在日時をもとに Sample Home 画面へ表示する
 * 挨拶種別と日付を組み立てるアプリケーション操作を表す。
 *
 * ■ 提供する責務
 *   現在日時の取得
 *   挨拶種別の判定
 *   SampleHomeHeader の生成
 *   取得結果の AppResult 返却
 *
 * ■ 設計上の意図
 *   ViewModel が ZonedDateTime.now() などを直接呼び出さないようにする。
 *
 *   現在日時の取得は DateTimeProvider に委譲し、
 *   挨拶種別の判定ルールは SampleGreetingPolicy に委譲する。
 *
 *   UseCase は、それらを組み合わせて
 *   Sample Home 画面に必要なヘッダー情報を作る処理手順に集中する。
 *
 * ■ 注意
 *   本 UseCase は表示文言そのものは生成しない。
 *   greetingType や date をどの文言で表示するかは Presentation 層の責務とする。
 *
 * @param dateTimeProvider 現在日時を取得する Provider。
 * @param greetingPolicy 現在日時から挨拶種別を判定する Policy。
 */
class GetSampleHomeHeaderUseCase @Inject constructor(
    private val dateTimeProvider: DateTimeProvider,
    private val greetingPolicy: SampleGreetingPolicy
) {

    /**
     * Sample Home 画面のヘッダー情報を取得する。
     *
     * @return Sample Home 画面のヘッダー情報。
     */
    operator fun invoke(): AppResult<SampleHomeHeader> {
        val now = dateTimeProvider.now()

        return AppResult.Success(
            SampleHomeHeader(
                greetingType = greetingPolicy.resolveGreetingType(now),
                date = now.toLocalDate()
            )
        )
    }
}
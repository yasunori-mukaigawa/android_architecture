package jp.co.nsco.basearchitecture.feature.sample.domain

/**
 * Sample Home で表示する挨拶種別。
 *
 * SampleGreetingType は、現在時刻に応じて表示する挨拶の分類を表す。
 *
 * ■ 提供する責務
 *   朝の挨拶種別
 *   昼の挨拶種別
 *   夜の挨拶種別
 *
 * ■ 設計上の意図
 *   表示文言そのものではなく、挨拶の意味を enum として保持する。
 *
 *   実際にどの文字列を表示するかは Presentation 層で解決することで、
 *   Domain 層が Android の string resource に依存しないようにする。
 */
enum class SampleGreetingType {

    /**
     * 朝の挨拶。
     */
    Morning,

    /**
     * 昼の挨拶。
     */
    Afternoon,

    /**
     * 夜の挨拶。
     */
    Evening
}
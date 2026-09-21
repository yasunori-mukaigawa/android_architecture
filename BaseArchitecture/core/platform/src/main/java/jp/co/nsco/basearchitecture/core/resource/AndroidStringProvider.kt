package jp.co.nsco.basearchitecture.core.resource

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * AndroidStringProvider。
 *
 * この状態または処理は、所属する層の責務を表す。
 * 呼び出し側へ実装詳細を漏らさないための境界として扱う。
 */
class AndroidStringProvider @Inject constructor(
    @param:ApplicationContext private val context: Context
) : StringProvider {

    override fun getString(@StringRes resId: Int): String {
        return context.getString(resId)
    }

    override fun getString(@StringRes resId: Int, vararg args: Any): String {
        return context.getString(resId, *args)
    }
}







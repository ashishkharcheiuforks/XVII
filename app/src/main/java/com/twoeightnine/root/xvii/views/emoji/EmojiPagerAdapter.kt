package com.twoeightnine.root.xvii.views.emoji

import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import com.twoeightnine.root.xvii.App
import com.twoeightnine.root.xvii.R
import com.twoeightnine.root.xvii.managers.Prefs
import com.twoeightnine.root.xvii.utils.EmojiHelper

class EmojiPagerAdapter(private var listener: (Emoji) -> Unit) : androidx.viewpager.widget.PagerAdapter() {

    val views = mutableListOf<View>()

    init {
        views.add(getView(Prefs.recentEmojis))
        views.add(getView(EmojiHelper.facesRange))
        views.add(getView(EmojiHelper.animalsRange))
        views.add(getView(EmojiHelper.foodRange))
        views.add(getView(EmojiHelper.natureRange))
        views.add(getView(EmojiHelper.peopleRange))
        views.add(getView(EmojiHelper.symbolsRange))
        views.add(getView(EmojiHelper.otherRange))
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = views[position]
        container.addView(view)
        return view
    }

    override fun isViewFromObject(view: View, obj: Any) = view == obj

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }

    override fun getPageTitle(position: Int) =
            with(App.context) {
                when (position) {
                    1 -> getString(R.string.faces)
                    2 -> getString(R.string.animals)
                    3 -> getString(R.string.food)
                    4 -> getString(R.string.nature)
                    5 -> getString(R.string.people)
                    6 -> getString(R.string.symbols)
                    7 -> getString(R.string.other)
                    else -> getString(R.string.recent)
                }
            }


    override fun getCount() = views.size

    fun getEmojis(range: IntRange) = EmojiHelper.emojis
            .subList(range.first, range.endInclusive + 1)

    fun getView(range: IntRange) = getView(getEmojis(range))

    fun getView(emojis: MutableList<Emoji>): View {
        val view = View.inflate(App.context, R.layout.fragment_emoji, null)
        val gvEmojis = view.findViewById<GridView>(R.id.gvEmoji)
        val adapter = EmojiGridAdapter()
        adapter.add(emojis)
        gvEmojis.adapter = adapter
        gvEmojis.setOnItemClickListener {
            _, _, position, _ ->
            val emoji = adapter.items[position]
            listener.invoke(emoji)
        }
        return view
    }
}
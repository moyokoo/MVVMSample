package net.android.anko.ui.gank.adapter

import android.content.Context
import net.android.anko.R
import net.android.anko.model.model.MainDrawModel
import java.util.ArrayList

class GankHeaderHelper {
    companion object {
        fun provideDrawerList(mainActivity: Context): List<MainDrawModel> {
            val names = mainActivity.resources.getStringArray(R.array.drawer_names)
            val icons = mainActivity.resources.obtainTypedArray(R.array.drawer_icon)
            val mainDrawEntities = ArrayList<MainDrawModel>()
            for (i in names.indices) {
                val mainDrawEntity = MainDrawModel()
                mainDrawEntity.icon = icons.getResourceId(i, -1)
                mainDrawEntity.name = names[i]
                mainDrawEntities.add(mainDrawEntity)
            }
            icons.recycle()
            return mainDrawEntities
        }
    }
}
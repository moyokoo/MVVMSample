/*
 * Copyright (C) 2016 Bilibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.android.anko.helper;

import android.content.Context;
import android.content.SharedPreferences;

import net.android.anko.R;

import skin.support.SkinCompatManager;
import skin.support.content.res.ColorState;
import skin.support.content.res.SkinCompatUserThemeManager;


/**
 * miaoyongjun 如果自定义的背景图接近白色,则背景色设置为黑色
 */
public class ThemeHelper {
    private static final String CURRENT_THEME = "theme_current";

    public static final int CARD_SAKURA = 0x1;
    public static final int CARD_HOPE = 0x2;
    public static final int CARD_STORM = 0x3;
    public static final int CARD_WOOD = 0x4;
    public static final int CARD_LIGHT = 0x5;
    public static final int CARD_THUNDER = 0x6;
    public static final int CARD_SAND = 0x7;
    public static final int CARD_FIREY = 0x8;

    public static SharedPreferences getSharePreference(Context context) {
        return context.getSharedPreferences("multiple_theme", Context.MODE_PRIVATE);
    }


    public static int getTheme(Context context) {
        return getSharePreference(context).getInt(CURRENT_THEME, CARD_SAKURA);
    }

    public static void setGirlTheme(Context context) {
        SkinCompatUserThemeManager.get().clearDrawables();
        SkinCompatUserThemeManager.get().addColorState(R.color.colorAccent, "#ffffff");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color_default, "#ffffff");
        SkinCompatUserThemeManager.get().addColorState(R.color.textColorTertiary, "#ffffff");
        SkinCompatUserThemeManager.get().addColorState(R.color.textColorPrimary, "#ffffff");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color, "#00000000");
        applyTheme(context);
    }

    public static void setColorTheme(Context context) {
        SkinCompatUserThemeManager.get().clearDrawables();
        SkinCompatUserThemeManager.get().addColorState(R.color.colorAccent, "#FF0000");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color_default, "#00000000");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color, "#00000000");
        applyTheme(context);
    }

    public static void setDarkTheme(Context context) {
        SkinCompatUserThemeManager.get().clearDrawables();
        SkinCompatUserThemeManager.get().addColorState(R.color.colorAccent, "#000000");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color_default, "#ffffff");
        SkinCompatUserThemeManager.get().addColorState(R.color.textColorPrimary, "#ffffff");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color, "#eeeeee");
        applyTheme(context);

    }

    public static void setDrawableTheme(Context context) {
        SkinCompatUserThemeManager.get().clearColors();
        SkinCompatUserThemeManager.get().addDrawablePath(R.drawable.windowBackground, "/storage/emulated/0/DCIM/Camera/1514018701430.png");
        SkinCompatUserThemeManager.get().addColorState(R.color.colorAccent, "#00000000");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color_default, "#00000000");
        SkinCompatUserThemeManager.get().addColorState(R.color.navigation_color, "#00000000");
        applyTheme(context);
    }

    public static void applyTheme(Context context) {
        if (SkinCompatUserThemeManager.get().getColorState(R.color.colorAccent) != null
                && SkinCompatUserThemeManager.get().getColorState(R.color.navigation_color_default) != null) {
            SkinCompatUserThemeManager.get().addColorState(
                    R.color.navigation_item_tint, new ColorState.ColorBuilder()
                            .setColorSelected(context, R.color.colorAccent)
                            .setColorPressed(context, R.color.colorAccent)
                            .setColorChecked(context, R.color.colorAccent)
                            .setColorDefault(context, R.color.navigation_color_default)
                            .build());
        } else if (SkinCompatUserThemeManager.get().getColorState(R.color.navigation_item_tint) != null) {
            SkinCompatUserThemeManager.get().removeColorState(R.color.navigation_item_tint);
        }
        SkinCompatManager.getInstance().notifyUpdateSkin();
        SkinCompatUserThemeManager.get().apply();
    }

}

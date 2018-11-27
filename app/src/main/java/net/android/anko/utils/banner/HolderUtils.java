package net.android.anko.utils.banner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class HolderUtils {

    private static int mPagePadding = 15;
    private static int mShowLeftCardWidth = 15;

    public static void onCreateViewHolder(ViewGroup parent, View itemView) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        lp.width = parent.getWidth() - dip2px(itemView.getContext(), 2 * (mPagePadding + mShowLeftCardWidth));
        itemView.setLayoutParams(lp);
    }

    public static void onBindViewHolder(View itemView, final int position, int itemCount) {
        int padding = dip2px(itemView.getContext(), mPagePadding);
        itemView.setPadding(padding, 0, padding, 0);
        int leftMarin = position == 0 ? padding + dip2px(itemView.getContext(), mShowLeftCardWidth) : 0;
        int rightMarin = position == itemCount - 1 ? padding + dip2px(itemView.getContext(), mShowLeftCardWidth) : 0;
        setViewMargin(itemView, leftMarin, 0, rightMarin, 0);
    }

    private static void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
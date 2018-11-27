package net.android.anko.paging.base

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.util.MultiTypeDelegate
import miaoyongjun.autil.utils.LogUtils
import net.android.anko.R
import net.android.anko.paging.HeaderTransform
import net.android.anko.paging.NetworkState
import net.android.anko.paging.NetworkStateItemPagingViewHolder
import net.android.anko.utils.extensions.gone
import net.android.anko.utils.extensions.visible
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BasePagingAdapter<T, K : BasePagingViewHolder> constructor(@LayoutRes val mLayoutResId: Int, diff: DiffUtil.ItemCallback<T>)
    : PagedListAdapter<T, K>(diff) {
    private var mMultiTypeDelegate: MultiTypeDelegate<T>? = null
    val HEADER_VIEW = 0x00000111
    val LOADING_VIEW = 0x00000222
    val NET_VIEW = 0x00000333
    val ITEM_VIEW = 0x00000666
    protected var mLayoutInflater: LayoutInflater? = null
    lateinit var mContext: Context
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null
    private var mOnItemChildClickListener: OnItemChildClickListener? = null
    private var mOnItemChildLongClickListener: OnItemChildLongClickListener? = null

    var retryCallback: (() -> Unit)? = null
    private var networkState: NetworkState? = null
    var headerTransform: HeaderTransform? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): K {
        val baseViewHolder: K?
        this.mContext = parent.context
        this.mLayoutInflater = LayoutInflater.from(mContext)
        when (viewType) {
            HEADER_VIEW -> {
                baseViewHolder = createBaseViewHolder(headerTransform?.createView())
            }
            NET_VIEW -> {
                baseViewHolder = provideNetStatusViewHolder(parent, retryCallback)
            }
            else -> {
                baseViewHolder = onCreateDefViewHolder(parent, viewType)
                bindViewClickListener(baseViewHolder)
            }
        }
        baseViewHolder.setAdapter(this)
        return baseViewHolder
    }

    fun addHeader(headerTransform: HeaderTransform) {
        this.headerTransform = headerTransform
    }

    protected abstract fun provideNetStatusViewHolder(parent: ViewGroup, retryCallback: (() -> Unit)? = {}): K

    protected abstract fun convert(helper: K, item: T?)

    override fun onBindViewHolder(holder: K, position: Int) {
        val viewType = holder.itemViewType
        when (viewType) {
            ITEM_VIEW -> convert(holder, getItem(position - getHeaderLayoutCount()))
            HEADER_VIEW -> {
            }
            NET_VIEW -> (holder as BaseNetPagingViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED


    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NET_VIEW
        } else if (position == 0 && getHeaderLayoutCount() != 0) {
            HEADER_VIEW
        } else {
            ITEM_VIEW
        }
    }

    override fun getItemCount(): Int {
        return if (super.getItemCount() == 0) {
            if (hasExtraRow()) 1 else 0
        } else {
            return super.getItemCount() + getHeaderLayoutCount() + if (hasExtraRow()) 1 else 0
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        this.networkState = newNetworkState
        if (this.networkState == NetworkState.LOADED) {
            notifyItemRemoved(itemCount)
        } else {
            //TODO q:notifyItemChanged
//            if (super.getItemCount() + getHeaderLayoutCount() == itemCount - 1) {
//                notifyItemChanged(itemCount - 1,"notify")
//            } else {
//                notifyItemInserted(itemCount - 1)
//            }
            notifyItemInserted(itemCount - 1)
        }
    }

    protected fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): K {
        var layoutId = 0
        when (viewType) {
            NET_VIEW -> layoutId = R.layout.network_state_item
            ITEM_VIEW -> layoutId = mLayoutResId
        }
        return createBaseViewHolder(parent, layoutId)
    }

    fun createBaseViewHolder(parent: ViewGroup, layoutResId: Int): K {
        return createBaseViewHolder(getItemView(layoutResId, parent)!!)
    }


    fun createBaseViewHolder(view: View?): K {
        var temp: Class<*>? = javaClass
        var z: Class<*>? = null
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp)
            temp = temp.superclass
        }
        val k: K?
        // 泛型擦除会导致z为null
        k = if (z == null) {
            BasePagingViewHolder(view) as K
        } else {
            createGenericKInstance(z, view)
        }
        return k ?: BasePagingViewHolder(view) as K
    }

    private fun createGenericKInstance(z: Class<*>, view: View?): K? {
        try {
            val constructor: Constructor<*>
            // inner and unstatic class
            return if (z.isMemberClass && !Modifier.isStatic(z.modifiers)) {
                constructor = z.getDeclaredConstructor(javaClass, View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(this, view) as K
            } else {
                constructor = z.getDeclaredConstructor(View::class.java)
                constructor.isAccessible = true
                constructor.newInstance(view) as K
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        return null
    }

    private fun getInstancedGenericKClass(z: Class<*>): Class<*>? {
        val type = z.genericSuperclass
        if (type is ParameterizedType) {
            val types = type.actualTypeArguments
            for (temp in types) {
                if (temp is Class<*>) {
                    if (BasePagingViewHolder::class.java.isAssignableFrom(temp)) {
                        return temp
                    }
                } else if (temp is ParameterizedType) {
                    val rawType = temp.rawType
                    if (rawType is Class<*> && BasePagingViewHolder::class.java.isAssignableFrom(rawType)) {
                        return rawType
                    }
                }
            }
        }
        return null
    }

    protected fun getItemView(@LayoutRes layoutResId: Int, parent: ViewGroup): View? {
        return mLayoutInflater?.inflate(layoutResId, parent, false)
    }

    private fun getHeaderViewPosition(): Int {
        return 0
    }

    private fun bindViewClickListener(basePagingViewHolder: BasePagingViewHolder?) {
        if (basePagingViewHolder == null) {
            return
        }
        val view = basePagingViewHolder.itemView ?: return
        if (getOnItemClickListener() != null) {
            view.setOnClickListener { v -> setOnItemClick(v, basePagingViewHolder.layoutPosition - getHeaderLayoutCount()) }
        }
        if (getOnItemLongClickListener() != null) {
            view.setOnLongClickListener { v -> setOnItemLongClick(v, basePagingViewHolder.layoutPosition - getHeaderLayoutCount()) }
        }
    }


    fun setOnItemClick(v: View, position: Int) {
        getOnItemClickListener()?.onItemClick(this@BasePagingAdapter, v, position)
    }

    fun setOnItemLongClick(v: View, position: Int): Boolean {
        return getOnItemLongClickListener()?.onItemLongClick(this@BasePagingAdapter, v, position)
                ?: false
    }

    fun getHeaderLayoutCount(): Int {

        return if (headerTransform == null) {
            0
        } else 1
    }


    interface OnItemChildClickListener {
        fun onItemChildClick(adapter: BasePagingAdapter<*, *>, view: View, position: Int)
    }


    interface OnItemChildLongClickListener {
        fun onItemChildLongClick(adapter: BasePagingAdapter<*, *>, view: View, position: Int): Boolean
    }


    interface OnItemLongClickListener {
        fun onItemLongClick(adapter: BasePagingAdapter<*, *>, view: View, position: Int): Boolean
    }


    interface OnItemClickListener {
        fun onItemClick(adapter: BasePagingAdapter<*, *>, view: View, position: Int)
    }

    fun retry(r: (() -> Unit)?) {
        retryCallback = r
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    fun setOnItemChildClickListener(listener: OnItemChildClickListener) {
        mOnItemChildClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        mOnItemLongClickListener = listener
    }


    fun setOnItemChildLongClickListener(listener: OnItemChildLongClickListener) {
        mOnItemChildLongClickListener = listener
    }

    fun getOnItemLongClickListener(): OnItemLongClickListener? {
        return mOnItemLongClickListener
    }

    fun getOnItemClickListener(): OnItemClickListener? {
        return mOnItemClickListener
    }

    fun getOnItemChildClickListener(): OnItemChildClickListener? {
        return mOnItemChildClickListener
    }

    fun getOnItemChildLongClickListener(): OnItemChildLongClickListener? {
        return mOnItemChildLongClickListener
    }
}
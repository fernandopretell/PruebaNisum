package com.fernandopretell.componentes.button

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.TextViewCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import biz.belcorp.mobile.components.core.extensions.*
import com.fernandopretell.componentes.R
import kotlinx.android.synthetic.main.search_bar.view.*

class SearchBar @JvmOverloads constructor(
    context: Context?,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), AdapterView.OnItemClickListener {

    private val paddingEndWithIcon = resources.getDimensionPixelSize(R.dimen.search_bar_padding_end)
    private val paddingHorizontal = resources.getDimensionPixelSize(R.dimen.search_bar_padding_horizontal)
    private val paddingVertical = resources.getDimensionPixelSize(R.dimen.search_bar_padding_vertical)

    private var fontFamily: Typeface? = null
        set(value) {
            field = value
            applyFont()
        }

    var backgroundDrawable = R.drawable.background_searchbar
        set(value) {
            field = value
            applyBackgroundDrawable(value)
        }

    var iconSearch = R.drawable.ic_search

    var iconClose = R.drawable.ic_close

    var textAppearance = DEFAULT_TEXT_APPEARANCE_VALUE
        set(value) {
            field = value
            applyTextAppearance(value)
        }

    var hint = DEFAULT_HINT_TEXT_VALUE
        set(value) {
            field = value
            applyHint(value)
        }

    var hintColor = getColor(R.color.black)
        set(value) {
            field = value
            applyHintColor(value)
        }

    var textColor = getColor(R.color.black)
        set(value) {
            field = value
            applyTextColor(value)
        }

    var showIcon = false
        set(value) {
            field = value
            applyShowIcon(value)
            applyPadding()
        }

    private var iconType = IconType.NONE

    private var items: Array<CharSequence>? = null
    private var onTextChanged: OnTextChanged? = null
    private var onItemSelected: OnItemSelected? = null

    private var adapter: ArrayAdapter<CharSequence>? = null

    init {
        inflateView()
        initAttrs()
        applyFont()
        configureAdapter()
        createDefaultAdapter()
        listenSearchChanges()
        listenIconChanges()
        onActionGo()
    }

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.search_bar, this)
    }

    private fun initAttrs() {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.SearchBar, 0, 0)

        try {
            backgroundDrawable = attributes.getResourceId(R.styleable.SearchBar_searchBar_backgroundDrawable, backgroundDrawable)
            iconSearch = attributes.getResourceId(R.styleable.SearchBar_searchBar_iconSearch, iconSearch)
            iconClose = attributes.getResourceId(R.styleable.SearchBar_searchBar_iconClose, iconClose)
            textAppearance = attributes.getResourceId(R.styleable.SearchBar_searchBar_textAppearance, textAppearance)
            hint = attributes.getString(R.styleable.SearchBar_searchBar_hint) ?: hint
            hintColor = attributes.getColor(R.styleable.SearchBar_searchBar_hintColor, hintColor)
            textColor = attributes.getColor(R.styleable.SearchBar_searchBar_textColor, textColor)
            showIcon = attributes.getBoolean(R.styleable.SearchBar_searchBar_showIcon, showIcon)
            items = attributes.getTextArray(R.styleable.SearchBar_searchBar_items) ?: items
            val fontId = attributes.getResourceId(R.styleable.SearchBar_searchBar_fontFamily, -1)
            if (fontId != -1) {
                fontFamily = getFont(fontId)
            }

        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        applyHeight()
        applyPadding()
    }

    private fun applyFont() {
        if (fontFamily == null) return
        tvSearch?.apply {
            typeface = fontFamily
        }
    }

    private fun applyBackgroundDrawable(drawable: Int) {
        tvSearch?.setBackgroundResource(drawable)
    }

    private fun applyIcon() {
        val icon = if (isTextEmpty()) iconSearch else iconClose
        val drawable = createVectorDrawable(icon)
        ivIcon?.background = drawable
    }

    private fun applyIconType() {
        iconType = when {
            !showIcon -> IconType.NONE
            isTextEmpty() -> IconType.SEARCH
            else -> IconType.CLOSE
        }
    }

    private fun applyShowIcon(show: Boolean) {
        ivIcon?.apply {
            if (show) {
                visible()
            } else gone()
            applyIcon()
        }
    }

    private fun applyTextColor(@ColorInt textColor: Int) {
        tvSearch?.setTextColor(textColor)
    }

    private fun applyTextAppearance(@StyleRes textAppearance: Int) {
        if (textAppearance == DEFAULT_TEXT_APPEARANCE_VALUE) return
        tvSearch?.apply {
            TextViewCompat.setTextAppearance(this, textAppearance)
        }
    }

    private fun applyHint(hint: String) {
        tvSearch?.hint = hint
    }

    private fun applyHintColor(@ColorInt hintColor: Int) {
        tvSearch?.setHintTextColor(hintColor)
    }

    private fun applyHeight() {
        val params = tvSearch?.layoutParams ?: return
        params.height = this.height
    }

    private fun applyPadding() {
        val paddingEnd = if (showIcon) paddingEndWithIcon else paddingHorizontal
        tvSearch?.setPadding(paddingHorizontal, paddingVertical, paddingEnd, paddingVertical)
    }

    private fun listenSearchChanges() {
        tvSearch?.onTextChanged {
            applyIcon()
            applyIconType()
            onTextChanged?.onTextChanged(it)
        }
    }

    private fun listenIconChanges() {
        ivIcon?.setOnClickListener {
            if (!isTextEmpty() && iconType == IconType.CLOSE) {
                hideKeyboard()
                tvSearch?.setText("")
            } else if (isTextEmpty()) showKeyboard()
        }
    }

    private fun listenAdapterChanges() {
        tvSearch?.onItemClickListener = this
    }

    fun onTextChanged(onTextChanged: OnTextChanged) {
        this.onTextChanged = onTextChanged
    }

    fun onItemSelected(onItemSelected: OnItemSelected) {
        this.onItemSelected = onItemSelected
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        hideKeyboard()
        onItemSelected?.onItemSelected(parent?.getItemAtPosition(position), position)
    }

    private fun configureAdapter() {
        tvSearch?.threshold = DEFAULT_THRESHOLD_VALUE
    }

    private fun onActionGo() {
        tvSearch?.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    onItemSelected?.onItemSelected(text.toString(), -1)
                    hideKeyboard()
                }
                false
            }
        }
    }

    private fun createDefaultAdapter() {
        items?.let {
            adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, it)
            tvSearch?.setAdapter(adapter)
            listenAdapterChanges()
        }
    }

    private fun createVectorDrawable(icon: Int): Drawable? {
        val drawable = AppCompatResources.getDrawable(context, icon) ?: return null
        return if (isBitmapDrawable(drawable)) drawable
        else VectorDrawableCompat.create(resources, icon, null)
    }

    private fun isBitmapDrawable(drawable: Drawable): Boolean {
        return drawable is BitmapDrawable
    }

    private fun isTextEmpty() = tvSearch?.text?.isEmpty() ?: true

    fun clearText(){
        ivIcon.performClick()
    }

    fun updateItems(items: List<String>) {
        this.items = items.toTypedArray()
        createDefaultAdapter()
    }

    companion object {
        private val DEFAULT_TEXT_APPEARANCE_VALUE = R.style.TextAppearance_SearchBar
        private const val DEFAULT_THRESHOLD_VALUE = 1
        private const val DEFAULT_HINT_TEXT_VALUE = ""
    }

    private object IconType {
        const val NONE = -1
        const val SEARCH = 0
        const val CLOSE = 1
    }

    interface OnTextChanged {
        fun onTextChanged(text: String)
    }

    interface OnItemSelected {
        fun onItemSelected(element: Any?, position: Int)
    }
}

package com.fernandopretell.pruebanisum.ui.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.fernandopretell.componentes.button.SearchBar
import com.fernandopretell.pruebanisum.R
import com.fernandopretell.pruebanisum.base.BaseActivity
import com.fernandopretell.pruebanisum.viewmodel.SearchSongsViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var viewModel: SearchSongsViewModel
    private var conneted: Boolean? = null
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        conneted = isNetworkVConnected(this)

        viewModel = ViewModelProviders.of(this).get(SearchSongsViewModel::class.java)

        initView()

    }

    override fun showNetworkMessage(isConnected: Boolean) {
        if (!isConnected) {
            snackBar = Snackbar.make(nsContainer, "", Snackbar.LENGTH_LONG)

            val layout = snackBar?.getView() as Snackbar.SnackbarLayout
            layout.setBackgroundColor(ContextCompat.getColor(layout.context, android.R.color.transparent))
            layout.setPadding(0, 0, 0, 0)

            val snackView = LayoutInflater.from(this@MainActivity).inflate(R.layout.snack_bar, null) as View

            layout.addView(snackView, 0)
            snackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
            snackBar?.show()

        } else {
            snackBar?.dismiss()
            //notaViewModel!!.getPeliculasRemoto(id?:1)
        }
    }

    private fun initView() {
        search_bar?.apply {
            textAppearance = R.style.TextAppearance_SearchBar
            showIcon = true
            hintColor = getColor(R.color.gray_4)
            textColor = getColor(R.color.black)
            hint = "Buscar canción"
            iconClose = R.drawable.ic_close
            iconSearch = R.drawable.ic_search
            //updateItems(listOf("C1", "C2", "C3", "C4"))
            onTextChanged(object : SearchBar.OnTextChanged {
                override fun onTextChanged(text: String) {
                    println("onTextChanged: $text")
                }
            })
            onItemSelected(object : SearchBar.OnItemSelected {
                override fun onItemSelected(element: Any?, position: Int) {
                    println("onItemSelected ${element as String}")
                }

            })
        }
    }
}
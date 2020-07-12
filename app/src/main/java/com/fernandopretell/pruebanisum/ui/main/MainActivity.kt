package com.fernandopretell.pruebanisum.ui.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.fernandopretell.componentes.button.SearchBar
import com.fernandopretell.pruebanisum.R
import com.fernandopretell.pruebanisum.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

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
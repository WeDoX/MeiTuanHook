package com.onedream.meituan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mei_tuan_list.*

class MeiTuanListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mei_tuan_list)
        //
        btn_refresh.setOnClickListener {
            fillView(DataProducer.getPage())
        }
        //
        fillView(DataProducer.getPage())
    }

    private fun fillView(pageBean: PageBean?) {
        layout_top.visibility = View.GONE
        layout_bottom.visibility = View.GONE
        if (null == pageBean) {
            return
        }
        pageBean.top?.apply {
            layout_top.visibility = View.VISIBLE
            tv_1_money.text = "¥${money}"
            tv_1_time.text="${time}分钟内"
            btn_1_buy.setOnClickListener {
                Toast.makeText(this@MeiTuanListActivity, "top  ¥${money} ${time}分钟内", Toast.LENGTH_SHORT).show()
            }
        }

        pageBean.bottom.apply {
            layout_bottom.visibility = View.VISIBLE
            tv_2_money.text = "¥${money}"
            tv_2_time.text="${time}分钟内"
            btn_2_buy.setOnClickListener {
                Toast.makeText(this@MeiTuanListActivity, "bottom  ¥${money} ${time}分钟内", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
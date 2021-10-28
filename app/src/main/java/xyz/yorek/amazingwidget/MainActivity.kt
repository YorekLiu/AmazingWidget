package xyz.yorek.amazingwidget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.yorek.amazingwidget.widget.FixedNumberEditTextFragment
import xyz.yorek.amazingwidget.widget.ScrollableRingFragment
import xyz.yorek.amazingwidget.widget.SpanFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainActivityAdapter(loadActivityEntry()) {
            val intent = Intent(this, PlaceholderActivity::class.java)
            intent.putExtra(PlaceholderActivity.EXTRA_TITLE, it.first)
            intent.putExtra(PlaceholderActivity.EXTRA_PAGE, it.second)
            startActivity(intent)
        }
    }

    private fun loadActivityEntry(): List<Pair<String, Class<out Fragment>>> {
        return listOf(
            "格子样式的密码框" to FixedNumberEditTextFragment::class.java,
            "头尾相连的自滚动的ImageView" to ScrollableRingFragment::class.java,
            "各种Span" to SpanFragment::class.java,
        )
    }

    class MainActivityAdapter(
        private val data: List<Pair<String, Class<out Fragment>>>,
        private val startActivityAction: (Pair<String, Class<out Fragment>>) -> Unit
    ): RecyclerView.Adapter<MainActivityAdapter.Holder>() {

        private val mOnClickListener = View.OnClickListener {
            startActivityAction(it.tag as Pair<String, Class<out Fragment>>)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val holder = Holder(LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false))
            holder.itemView.setOnClickListener(mOnClickListener)
            return holder
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val entry = data[position]
            holder.itemView.tag = entry
            (holder.itemView as TextView).text = entry.first
        }

        override fun getItemCount() = data.size

        class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
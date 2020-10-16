package com.altun.currencyapp_sweeftdigital

import android.app.ProgressDialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.SearchManager;
import android.widget.SearchView.OnQueryTextListener;
import org.jsoup.Jsoup
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val items = mutableListOf<CurrencyModel>()
    private lateinit var adapter: CurrencyRecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        initRecyclerView()
        initSwipeRefreshLayout()
        CoroutineScope(Dispatchers.IO).launch {
            getCurrencyInfo()
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }

    }

    private fun initRecyclerView() {
        currencyRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CurrencyRecyclerViewAdapter(items)
        currencyRecyclerView.adapter = adapter
    }

    private fun initSwipeRefreshLayout(){
        swipeRefresh.setOnRefreshListener {
            items.clear()
            CoroutineScope(Dispatchers.IO).launch {
                getCurrencyInfo()
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                    swipeRefresh.isRefreshing = false
                }
            }

        }
    }

    private suspend fun getCurrencyInfo() {

        val url = URL("http://www.nbg.ge/rss.php")
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = false
        val parser = factory.newPullParser()
        parser.setInput(getInputStream(url), "UTF_8")

        var name: String? = null
        var fullName: String? = null
        var rate: String? = null
        var img: String? = null
        var diff: String? = null

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG) {
                if (parser.name == "description") {
                    var token = parser.nextToken()
                    while (token != XmlPullParser.CDSECT) {
                        token = parser.nextToken()
                    }
                    val cdata = parser.text
                    val doc = Jsoup.parse(cdata)
                    val select = doc.select("tr")
                    select.forEach {
                        val item = it.select("td")
                        item.forEachIndexed { i, element ->
                            when (i) {
                                0 -> name = element.text()
                                1 -> fullName = element.text()
                                2 -> rate = element.text()
                                3 -> {
                                    val image = element.select("img")
                                    val element = image.attr("src")
                                    img = element
                                }
                                4 -> diff = element.text()
                            }
                        }
                        items.add(CurrencyModel(name, fullName, rate, img, diff))
                    }
                }
                if (parser.name == "pubDate")
                    currencyTimeTextView.text = parser.nextText().removeSuffix("+0400")
            }
            eventType = parser.next()
        }
    }
    private fun getInputStream(url: URL): InputStream? {
        return try {
            url.openConnection().getInputStream()
        } catch (e: IOException) {
            null
        }
    }
}
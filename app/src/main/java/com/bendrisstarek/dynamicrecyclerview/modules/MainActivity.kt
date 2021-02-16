package com.bendrisstarek.dynamicrecyclerview.modules

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bendrisstarek.dynamicrecyclerview.R
import com.bendrisstarek.dynamicrecyclerview.databinding.ActivityMainBinding
import com.bendrisstarek.dynamicrecyclerview.utils.StringUtils
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    private lateinit var csvContentAdapter: CsvContentAdapter
    private lateinit var csvContentRecycler: RecyclerView
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mModels: ArrayList<StandardModel>
    private var issuesBtn: Button? = null
    private var usersBtn: Button? = null
    private var appStatsBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initVars()
        ReadCsvAsyncTask(this,R.raw.issues).execute()
    }

    private fun initVars()
    {
        csvContentRecycler = mBinding.csvContentRecycler
        issuesBtn = mBinding.issuesBtn
        usersBtn = mBinding.usersAgeBtn
        appStatsBtn = mBinding.appStatsBtn

        mModels = ArrayList()

        issuesBtn?.setOnClickListener {
            ReadCsvAsyncTask(this, R.raw.issues).execute()
        }

        usersBtn?.setOnClickListener {
            ReadCsvAsyncTask(this, R.raw.users_age).execute()
        }

        appStatsBtn?.setOnClickListener {
            ReadCsvAsyncTask(this, R.raw.app_stats).execute()
        }
    }

    internal fun initRecyclerView(lst:ArrayList<ArrayList<String>>) {
        when {
            lst.isEmpty() -> {
                /**
                 * do whatever you want
                 */
            }
            lst.size==1 -> {
                /**
                 * do whatever you want
                 */
            }
            else -> {
                val listModel = ArrayList<StandardModel>()
                val topList = lst[0]

                for (i in lst.indices) {
                    if (i != 0)
                        listModel.add(StandardModel(lst[i]))
                }

                csvContentAdapter = CsvContentAdapter(listModel,topList)
                csvContentRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                csvContentRecycler.adapter = csvContentAdapter
            }
        }
    }

}

class ReadCsvAsyncTask(private var activity: MainActivity?, private var csvFile: Int) : AsyncTask<String, String, ArrayList<ArrayList<String>>>() {

    /**
     * in background, we will try to get the CSV file and parse it
     */
    override fun doInBackground(vararg p0: String?): ArrayList<ArrayList<String>> {

        val result:ArrayList<ArrayList<String>> =  ArrayList()
        try {
            val inputStream: InputStream? = activity?.resources?.openRawResource(csvFile)
            val reader = BufferedReader(InputStreamReader(inputStream, Charset.forName("UTF-8")))
            reader.readLines().forEach {
                /**
                 * get a string array of all items in this line
                 */
                val items = it.split(",")
                val list = ArrayList<String>()

                for (i in items.indices)
                    list.add(StringUtils.transformToValidString(items[i]))

                result.add(list)
            }
        }catch (e:Exception)
        {
            return result
        }
        return result
    }

    /**
     * when the thread is completed, we return the result to the activity to handle it
     */
    override fun onPostExecute(result: ArrayList<ArrayList<String>>?) {
        super.onPostExecute(result)
        activity?.initRecyclerView(result!!)
    }
}

package com.natasaandzic.hydratabletopgames.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.natasaandzic.hydratabletopgames.R
import com.natasaandzic.hydratabletopgames.activities.GamesActivity
import com.natasaandzic.hydratabletopgames.adapters.GamesAdapter
import com.natasaandzic.hydratabletopgames.model.GamesDataModel
import com.natasaandzic.hydratabletopgames.model.InternetConnection
import com.natasaandzic.hydratabletopgames.model.Keys
import com.natasaandzic.hydratabletopgames.parser.JSONParser
import kotlinx.android.synthetic.main.activity_games.*
import org.json.JSONException
import java.util.*

class GamesActivity : AppCompatActivity() {

    private lateinit var list: ArrayList<GamesDataModel>
    private lateinit var adapter: GamesAdapter

    private lateinit var lm: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_games)

        recyclerView.setHasFixedSize(true)
        lm = LinearLayoutManager(this)
        recyclerView.layoutManager = lm
        val mDividerItemDecoration = DividerItemDecoration(recyclerView.context, lm.orientation)
        recyclerView.addItemDecoration(mDividerItemDecoration)
        setSupportActionBar(toolbar)
        list = ArrayList()
        adapter = GamesAdapter(list)
        recyclerView.adapter = adapter

        if (InternetConnection.checkConnection(applicationContext)) GetDataTask().execute() else Toast.makeText(applicationContext, "Internet connection is not available", Toast.LENGTH_LONG).show()
    }

    /**
     * Getting JSON data from the internet,
     * converting it to strings,
     * filling our textviews with those strings.
     */
    internal inner class GetDataTask : AsyncTask<Void?, Void?, Void?>() {
        var dialog: ProgressDialog? = null
        var jIndex = 0
        override fun onPreExecute() {
            super.onPreExecute()
            jIndex = list.size
            dialog = ProgressDialog(this@GamesActivity)
            dialog!!.setTitle("Reading from database...")
            dialog!!.setMessage("Gamessss ^_^")
            dialog!!.show()
        }

        protected override fun doInBackground(vararg params: Void): Void? {
            val jsonObject = JSONParser.getDataFromWeb(GAMES_URL)
            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        val array = jsonObject.getJSONArray(Keys.KEY_GAMES)
                        val lenArray = array.length()
                        if (lenArray > 0) {
                            while (jIndex < lenArray) {
                                val model = GamesDataModel()
                                val innerObject = array.getJSONObject(jIndex)
                                val gameName = innerObject.getString(Keys.KEY_GAMENAME)
                                val gamePrice = innerObject.getString(Keys.KEY_GAMEPRICE)
                                val gameDescription = innerObject.getString(Keys.KEY_GAMEDESCRIPTION)
                                val gameGenre = innerObject.getString(Keys.KEY_GAMEGENRE)
                                model.gameName = gameName
                                model.gamePrice = gamePrice
                                model.gameDescription = gameDescription
                                model.gameGenre = gameGenre
                                list.add(model)
                                jIndex++
                            }
                        }
                    }
                }
            } catch (je: JSONException) {
                Log.i("Games url", "" + je.localizedMessage)
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            dialog!!.dismiss()
            if (list.size > 0) {
                Log.i("List size", list.size.toString())
                adapter.notifyDataSetChanged()
            } else {
                Snackbar.make(findViewById(R.id.parentLayout), "No Data Found", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun makeDialog(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this@GamesActivity)
        alertDialogBuilder.setTitle(list[position].gameName)
        alertDialogBuilder.setMessage(list[position].gameDescription)
        alertDialogBuilder.setNegativeButton("Go back") { arg0, arg1 ->
            //nazad
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        private const val GAMES_URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=1hJBfdExFLbJcbM1FT846GEeO6zNAuyvBKStB-9OcQEY&sheet=Sheet1"
    }
}
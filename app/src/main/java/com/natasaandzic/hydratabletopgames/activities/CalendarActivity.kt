package com.natasaandzic.hydratabletopgames.activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.natasaandzic.hydratabletopgames.R
import com.natasaandzic.hydratabletopgames.adapters.EventsArrayAdapter
import com.natasaandzic.hydratabletopgames.model.EventsDataModel
import com.natasaandzic.hydratabletopgames.model.InternetConnection
import com.natasaandzic.hydratabletopgames.model.Keys
import com.natasaandzic.hydratabletopgames.parser.JSONParser
import kotlinx.android.synthetic.main.activity_calendar.*
import org.json.JSONException
import kotlin.collections.ArrayList

class CalendarActivity : AppCompatActivity() {

    private lateinit var list : ArrayList<EventsDataModel>
    private lateinit var adapter: EventsArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)

        adapter = EventsArrayAdapter(this, list)
        listView!!.adapter = adapter

        listView!!.onItemClickListener = OnItemClickListener { parent, view, position, id -> makeDialog(position) }

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
            dialog = ProgressDialog(this@CalendarActivity)
            dialog!!.setTitle("Reading from database...")
            dialog!!.setMessage("Go make some coffee ^_^")
            dialog!!.show()
        }

        protected override fun doInBackground(vararg params: Void): Void? {
            val jsonObject = JSONParser.getDataFromWeb(EVENTS_URL)
            try {
                if (jsonObject != null) {
                    if (jsonObject.length() > 0) {
                        val array = jsonObject.getJSONArray(Keys.KEY_EVENTS)
                        val lenArray = array.length()
                        if (lenArray > 0) {
                            while (jIndex < lenArray) {
                                val model = EventsDataModel()
                                val innerObject = array.getJSONObject(jIndex)
                                val eventName = innerObject.getString(Keys.KEY_EVENTNAME)
                                val eventDate = innerObject.getString(Keys.KEY_EVENTDATE)
                                val eventTime = innerObject.getString(Keys.KEY_EVENTTIME)
                                val eventDay = innerObject.getString(Keys.KEY_EVENTDAY)
                                val eventDescription = innerObject.getString(Keys.KEY_EVENTDESCRIPTION)
                                model.eventName = eventName
                                model.eventDate = eventDate
                                model.eventTime = eventTime
                                model.eventDay = eventDay
                                model.eventDescription = eventDescription
                                list.add(model)
                                jIndex++
                            }
                        }
                    }
                }
            } catch (je: JSONException) {
                Log.i("Events url", "" + je.localizedMessage)
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            dialog!!.dismiss()
            if (list.size > 0) {
                adapter.notifyDataSetChanged()
            } else {
                Snackbar.make(findViewById(R.id.parentLayout), "No Data Found", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun makeDialog(position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(this@CalendarActivity)
        alertDialogBuilder.setTitle(list[position].eventName)
        alertDialogBuilder.setMessage(list[position].eventDescription)
        alertDialogBuilder.setPositiveButton("Notify me!") { arg0, arg1 ->
            //Upisi korisnika u Firebase Cloud Messaging bazu
        }
        alertDialogBuilder.setNegativeButton("Go back") { arg0, arg1 ->
            //nazad
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        private const val EVENTS_URL = "https://script.google.com/macros/s/AKfycbxOLElujQcy1-ZUer1KgEvK16gkTLUqYftApjNCM_IRTL3HSuDk/exec?id=16PtWcg_Ghha0rnLjOdO4RmoGqpu72LTKYCAsZUVT-6M&sheet=Sheet1"
    }
}
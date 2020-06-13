package com.natasaandzic.hydratabletopgames.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.natasaandzic.hydratabletopgames.R
import com.natasaandzic.hydratabletopgames.adapters.MyFragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var adapter: MyFragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        eventsBtn.setOnClickListener {
            val i = Intent(this@MainActivity, CalendarActivity::class.java)
            startActivity(i)
        }
        gamesBtn.setOnClickListener {
            val i = Intent(this@MainActivity, GamesActivity::class.java)
            startActivity(i)
        }

        //Fragments stuff
		tabLayout.addTab(tabLayout.newTab().setText("Calendar"));
		tabLayout.addTab(tabLayout.newTab().setText("Games"));
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL;

		adapter =  MyFragmentPagerAdapter(supportFragmentManager, tabLayout.getTabCount())
        viewPager.adapter = adapter
		tabLayout.setupWithViewPager(viewPager) // this will automatically bind tab clicks to viewpager fragments

    }
}
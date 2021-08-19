package com.spsoft.gamelist.ui.main

import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spsoft.gamelist.R
import com.spsoft.gamelist.data.models.gamelist.Game
import com.spsoft.gamelist.data.models.gamelist.Result
import com.spsoft.gamelist.databinding.ActivityMainBinding
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein: Kodein by closestKodein()
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: GameListAdapter
    private val viewModelFactory: MainViewModelFactory by instance()
    private lateinit var viewModel: MainViewModel

    private var loading = true
    private var pastVisiblesItems = 0
    private var visibleItemCount: Int = 0
    private var totalItemCount: kotlin.Int = 0
    private val listGames = mutableListOf<Result>()
    private var game: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        init()
        settingRecyclerView(1)
    }

    private fun settingRecyclerView(page:Int) {
        if (page==1)
            listGames.clear()
            viewModel.getListGames(page, 10, "ebe7f17643cd4c5e92176cc953c88700")
    }

    private fun init() {


        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        binding.recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = GameListAdapter()
        viewModel.gamesList.observe(this, {

            game = it
            listGames.addAll(it.results)
            adapter.submitList(listGames)
            adapter.notifyDataSetChanged()
            binding.recyclerview.adapter = adapter
            binding.progressBar.visibility = View.GONE
            binding.swiperefresh.isRefreshing=false

        })
        viewModel.error.observe(this, {
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
            binding.swiperefresh.isRefreshing=false
        })

        binding.recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val layoutManager = (binding.recyclerview.layoutManager as LinearLayoutManager)
                    Log.e(
                        "Hello",
                        "Visible Count :$visibleItemCount Total item count : $totalItemCount  pastVisiblesItems: $pastVisiblesItems"
                    )
                    visibleItemCount =
                        (binding.recyclerview.layoutManager as LinearLayoutManager).childCount
                    totalItemCount =
                        (binding.recyclerview.layoutManager as LinearLayoutManager).itemCount
                    pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if ((pastVisiblesItems + visibleItemCount) >= totalItemCount) {
                        binding.progressBar.visibility = View.VISIBLE
                        settingRecyclerView(game?.next!!.split("page=")[1].first().code)
                    }
                }
            }
        })

        binding.swiperefresh.setOnRefreshListener {
            settingRecyclerView(1)
        }

        /*binding.swiperefresh.setOnScrollChangeListener { nestesScrollview, _, scrollY, _, _ ->
            if (scrollY==nestesScrollview){

            }
        }*/
    }


}


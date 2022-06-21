package final_Project_Group_29.project.app.android.SteamInHand.menu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import final_Project_Group_29.project.app.android.SteamInHand.data.PlayerDatabase
import final_Project_Group_29.project.app.android.SteamInHand.data.PlayerItem
import final_Project_Group_29.project.app.android.SteamInHand.databinding.ActivityMenuBinding
import final_Project_Group_29.project.app.android.SteamInHand.detail.DetailActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MenuActivity : AppCompatActivity(), MenuAdapter.OnPlayerSelectedListener, CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    lateinit var binding: ActivityMenuBinding
    lateinit var adapter: MenuAdapter
    private lateinit var database: PlayerDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = PlayerDatabase.getDatabase(applicationContext)

        binding.toolbar.title = "SteamInHand"

        initFab()
        initRecyclerView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1) {
            launch {
                val player = data?.getStringExtra("player")
                adapter.addPlayer(player!!,this@MenuActivity)
            }
        }
    }

    private fun initFab() {
        binding.fab.setOnClickListener{
            val showAddPlayIntent = Intent()
            showAddPlayIntent.setClass(this@MenuActivity, AddPlayerActivity::class.java)
            startActivityForResult(showAddPlayIntent,1)
        }
    }

    private fun initRecyclerView() {
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        adapter = MenuAdapter(this)
        loadPlayersInBackground()
        binding.rvMenu.adapter = adapter
    }

    override fun onPlayerSelected(player: Long?) {
        val showDetailIntent = Intent()
        showDetailIntent.setClass(this@MenuActivity, DetailActivity::class.java)
        showDetailIntent.putExtra(DetailActivity.PLAYER_NAME, player.toString())
        startActivityForResult(showDetailIntent,1)
    }

    override fun onPlayerDeleted(player: PlayerItem) {
        deletePlayerInBackground(player)
        adapter.removePlayer(player)
    }

    private fun deletePlayerInBackground(player: PlayerItem) = launch {
        withContext(Dispatchers.IO) {
            database.shoppingItemDao().deleteItem(player)
        }
    }

    override fun onPlayerAdded(player: PlayerItem) {
        addPlayerInBackgound(player)
    }

    private fun addPlayerInBackgound(item: PlayerItem) = launch {
        withContext(Dispatchers.IO) {
            database.shoppingItemDao().insert(item)
        }
    }

    private fun loadPlayersInBackground() = launch {
        val items = withContext(Dispatchers.IO) {
            database.shoppingItemDao().getAll()
        }
        adapter.update(items)
    }

}
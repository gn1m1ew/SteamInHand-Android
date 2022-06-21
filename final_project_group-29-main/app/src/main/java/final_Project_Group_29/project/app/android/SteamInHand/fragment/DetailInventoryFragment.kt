package final_Project_Group_29.project.app.android.SteamInHand.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import final_Project_Group_29.project.app.android.SteamInHand.databinding.FragmentDetailInventoryBinding
import final_Project_Group_29.project.app.android.SteamInHand.detail.InspectActivity
import final_Project_Group_29.project.app.android.SteamInHand.detail.PlayerDataHolder
import final_Project_Group_29.project.app.android.SteamInHand.data.model.inventory.InventoryData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.inventory.InventoryFullItem
import final_Project_Group_29.project.app.android.SteamInHand.Utils.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext


class DetailInventoryFragment : Fragment(),CoroutineScope, InventoryAdapter.OnItemSelectedListener{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var _binding: FragmentDetailInventoryBinding? = null
    private val binding get() = _binding!!

    private val TAG = "InventoryActivity"

    lateinit var adapter: InventoryAdapter

    private var playerDataHolder: PlayerDataHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerDataHolder = if (activity is PlayerDataHolder) {
            activity as PlayerDataHolder?
        } else {
            throw RuntimeException(
                    "Activity must implement PlayerDataHolder interface!"
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        _binding = FragmentDetailInventoryBinding.inflate(LayoutInflater.from(context))
        initRecyclerView()

        loadInvData(playerDataHolder?.getProfileData()?.response?.players?.get(0)?.steamid!!)

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvInventory.layoutManager = LinearLayoutManager(binding.rvInventory.context)
        adapter = InventoryAdapter(this)

        binding.rvInventory.adapter = adapter
    }

    override fun onItemSelected(item: InventoryFullItem) {
        val picRegex = "https://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?".toRegex()
        val nameRegex = "(Sticker|Patch): (.*)</center>".toRegex()
        val showInspectIntent = Intent()
        showInspectIntent.setClass(binding.root.context,InspectActivity::class.java)
        showInspectIntent.putExtra("icon", item.decs?.icon_url)
        showInspectIntent.putExtra("name",item.decs?.market_hash_name)
        var color = ""
        item.decs?.tags?.forEach {
            if (it.color != null){
                color = it.color!!
            }
        }
        showInspectIntent.putExtra("color",color)

        var inspect = item.decs?.actions?.get(0)?.link
        if (inspect != null){
            inspect = inspect.replace("%owner_steamid%",playerDataHolder?.getProfileData()?.response?.players?.get(0)?.steamid.toString())
            inspect = inspect.replace("%assetid%", item.id.toString())
        }

        var stickerHtml = ""
        item.decs?.descriptions?.forEach {
            if (it.value!!.contains("sticker_info")){
                stickerHtml = it.value!!
            }
        }

        val stickerPics: ArrayList<String> = ArrayList()
        var stickerNames = ""
        if(stickerHtml.isNotEmpty()){
            picRegex.findAll(stickerHtml).forEach {
                stickerPics.add(it.value)
            }
            stickerNames = nameRegex.find(stickerHtml)!!.groupValues[2]

        }

        showInspectIntent.putExtra("inspect", inspect)
        showInspectIntent.putExtra("stickerPics",stickerPics)
        showInspectIntent.putExtra("stickerNames",stickerNames)
        startActivity(showInspectIntent)
    }

    private fun loadInvData(playerID: Long) = launch{
        NetworkManager.getInventory(playerID)!!.enqueue(object : Callback<InventoryData?> {

            override fun onResponse(call: Call<InventoryData?>, response: Response<InventoryData?>) {

                Log.d(TAG,"Inv onResponse: " + response.code() + " - " + response.message())
                if (response.isSuccessful) {
                    displayInvData(response.body())
                } else {
                    if(response.code() == 429){
                        Toast.makeText(binding.root.context,"Too Many Requests! Please Wait a bit!", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(binding.root.context,"Inventory Error: " + response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<InventoryData?>, throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(binding.root.context,"Network request error occurred, check LOG", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayInvData(receivedInvData: InventoryData?) {

        if(receivedInvData?.success.equals("true")){
            adapter.addItems(receivedInvData!!)
            binding.rvInventory.visibility = View.VISIBLE
        } else {
            binding.tvInfo.visibility = View.VISIBLE
        }
    }
}
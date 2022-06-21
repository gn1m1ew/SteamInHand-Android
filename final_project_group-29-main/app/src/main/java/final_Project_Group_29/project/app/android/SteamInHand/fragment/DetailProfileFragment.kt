package final_Project_Group_29.project.app.android.SteamInHand.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import final_Project_Group_29.project.app.android.SteamInHand.R
import final_Project_Group_29.project.app.android.SteamInHand.databinding.FragmentDetailProfileBinding
import final_Project_Group_29.project.app.android.SteamInHand.detail.PlayerDataHolder
import final_Project_Group_29.project.app.android.SteamInHand.data.model.games.RecentlyData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.Player
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.level.LevelData
import final_Project_Group_29.project.app.android.SteamInHand.Utils.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class DetailProfileFragment: Fragment(),CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private val TAG = "ProfileFragment"

    private var _binding: FragmentDetailProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: RecentlyAdapter

    private var playerDataHolder: PlayerDataHolder? = null
    private var steamID:Long? = null

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
        _binding = FragmentDetailProfileBinding.inflate(LayoutInflater.from(context))
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvRecently.layoutManager = LinearLayoutManager(binding.rvRecently.context)
        adapter = RecentlyAdapter()
        binding.rvRecently.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayProfileData()

    }

    private fun displayProfileData() {
        val profile = playerDataHolder?.getProfileData()?.response?.players?.get(0)

        binding.tvPlayerName.text = profile?.personaname
        binding.tvPlayerID.text = profile?.steamid.toString()
        binding.tvPlayerProfileLink.text = profile?.profileurl

        setVis(profile)
        setState(profile)
        Glide.with(this)
                .load(profile?.avatarfull)
                .transition(DrawableTransitionOptions().crossFade())
                .into(binding.ivPlayerImg)
        steamID = playerDataHolder?.getProfileData()?.response?.players?.get(0)?.steamid
        loadLevelData()
        loadRecentlyData()
    }

    private fun setState(profile: Player?){
        if(profile?.gameextrainfo == null) {
            binding.tvPlayerState.text = when (profile?.personastate) {
                0 -> "Offline"
                1 -> "Online"
                2 -> "Busy"
                3 -> "Away"
                4 -> "Snooze"
                else -> ""
            }
            val color: Int = when(profile?.personastate){
                0 -> Color.LTGRAY
                else -> Color.rgb(87,203,222)
            }
            binding.tvPlayerState.setTextColor(color)
            binding.llBorder.setBackgroundColor(color)
        } else {

            binding.tvPlayerState.text = getString(R.string.playingx, profile.gameextrainfo)
            binding.tvPlayerState.setTextColor(Color.rgb(144,200,60))
            binding.llBorder.setBackgroundColor(Color.rgb(0, 173, 238))
        }
    }

    private fun setVis(profile: Player?){

        binding.tvPlayerCommProfileState.text = when(profile?.communityvisibilitystate){
            1 -> "Private"
            2 -> "Friends Only"
            3 -> "Public"
            else -> ""
        }
        val color: Int = when(profile?.communityvisibilitystate){
            1 -> Color.RED
            2 -> Color.rgb(255,69,0)
            else -> Color.GREEN
        }
        binding.tvPlayerCommProfileState.setTextColor(color)
    }


    private fun loadRecentlyData() = launch{
        NetworkManager.getRecentlyGames(steamID)!!.enqueue(object : Callback<RecentlyData?> {

            override fun onResponse(call: Call<RecentlyData?>, response: Response<RecentlyData?>) {

                Log.d(TAG, "Recently onResponse: " + response.code())
                if (response.isSuccessful) {
                    sendRecently(response.body())
                }
            }

            override fun onFailure(call: Call<RecentlyData?>, throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(binding.root.context,"Network request error occurred, check LOG", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendRecently(receivedRecently: RecentlyData?){
        if(receivedRecently?.response?.games.isNullOrEmpty()){
            binding.rvRecently.visibility = View.GONE
            binding.tvRecently.visibility = View.GONE
        }else{
            adapter.addItems(receivedRecently!!)
        }
    }

    private fun loadLevelData() = launch{
        NetworkManager.getSteamLevel(steamID)!!.enqueue(object : Callback<LevelData?> {

            override fun onResponse(call: Call<LevelData?>, response: Response<LevelData?>) {

                Log.d(TAG, "Level onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayLevelData(response.body())
                }
            }

            override fun onFailure(call: Call<LevelData?>, throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(binding.root.context,"Network request error occurred, check LOG",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayLevelData(receivedLevelData: LevelData?){
        val level = receivedLevelData?.response?.player_level.toString()
        if (level == "null"){
            binding.tvPlayerProfileLevel.text = getString(R.string.privateTag)
        } else {
            binding.tvPlayerProfileLevel.text = "Lv. " + level
        }
    }
}
package final_Project_Group_29.project.app.android.SteamInHand.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import final_Project_Group_29.project.app.android.SteamInHand.R
import final_Project_Group_29.project.app.android.SteamInHand.databinding.ActivityDetailBinding
import final_Project_Group_29.project.app.android.SteamInHand.data.model.friends.FriendlistData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.ProfileData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.stats.PlayerStatsData
import final_Project_Group_29.project.app.android.SteamInHand.Utils.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.CoroutineContext

class DetailActivity : AppCompatActivity(),PlayerDataHolder, CoroutineScope {

    lateinit var binding: ActivityDetailBinding

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    companion object {
        private const val TAG = "DetailsActivity"
        const val PLAYER_NAME = "player_name"
    }

    private var playerID: Long? = null
    private var profileData: ProfileData? = null
    private var statsData: PlayerStatsData? = null
    private var friendlistData: FriendlistData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerID = intent.getStringExtra(PLAYER_NAME)?.toLong()

        runBlocking {
            loadProfileData()
            loadFriendlistData()
        }

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.mainViewPager.adapter = detailPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.mainViewPager) { tab, position ->
            tab.text = when(position) {
                0 -> getString(R.string.profile)
                1 -> getString(R.string.friendlist)
                2 -> getString(R.string.inventory)
                else -> ""
            }
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getProfileData(): ProfileData? = profileData
    override fun getStatsData(): PlayerStatsData? = statsData
    override fun getFriendlistData(): FriendlistData? = friendlistData

    private fun loadProfileData() = launch{
        NetworkManager.getProfile(playerID)!!.enqueue(object : Callback<ProfileData?> {

            override fun onResponse( call: Call<ProfileData?>, response: Response<ProfileData?>) {
                Log.d(TAG, "profile onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayProfileData(response.body())
                } else {
                    Toast.makeText(this@DetailActivity, "Profile Error: " + response.message(), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileData?>, throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(this@DetailActivity, "Network request error occurred, check LOG", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayProfileData(receivedProfileData: ProfileData?) {
        profileData = receivedProfileData

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.mainViewPager.adapter = detailPagerAdapter
    }

    private fun loadFriendlistData() = launch{
        NetworkManager.getFriends(playerID)!!.enqueue(object : Callback<FriendlistData?> {

            override fun onResponse(call: Call<FriendlistData?>,response: Response<FriendlistData?>) {

                Log.d(TAG, "Friends onResponse: " + response.code())
                if (response.isSuccessful) {
                    displayFriendsData(response.body())
                } else {
                    if(response.code() != 401){
                        Toast.makeText(this@DetailActivity,response.message(),Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<FriendlistData?>,throwable: Throwable) {
                throwable.printStackTrace()
                Toast.makeText(this@DetailActivity,"Network request error occurred, check LOG",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayFriendsData(receivedFriendData: FriendlistData?) {
        friendlistData = receivedFriendData

        val detailPagerAdapter = DetailPagerAdapter(this)
        binding.mainViewPager.adapter = detailPagerAdapter
    }

}
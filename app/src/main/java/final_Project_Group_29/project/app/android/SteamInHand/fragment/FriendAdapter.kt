package final_Project_Group_29.project.app.android.SteamInHand.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import final_Project_Group_29.project.app.android.SteamInHand.databinding.ItemFriendBinding
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.Player
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.ProfileData
import final_Project_Group_29.project.app.android.SteamInHand.Utils.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendAdapter (private val listener: OnFriendSelectedListener) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {

    private val TAG = "FriendActivity"
    private var friends: MutableList<Player?> = ArrayList()
    private var friendsAll: MutableList<Player?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FriendViewHolder(
            ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val item = friends[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = friends.size

    fun addFriend(ID: String) {
        loadFriendsProfilesData(ID)
    }

    inner class FriendViewHolder(val binding: ItemFriendBinding) :
            RecyclerView.ViewHolder(binding.root) {
        var item: Player? = null

        init {
            binding.ibAdd.setOnClickListener {
                listener.onFriendSelected(item?.steamid)
            }
        }

        fun bind(newFriend: Player?) {
            item = newFriend!!
            binding.tvFriendName.text = item?.personaname
            Glide.with(binding.root)
                    .load(item?.avatarfull)
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(binding.ivFriendImg)
        }
    }

    interface OnFriendSelectedListener {
        fun onFriendSelected(friend: Long?)
    }

    private fun loadFriendsProfilesData(playerIDorURL: String?){
        NetworkManager.getFriendsProfiles(playerIDorURL)!!.enqueue(object : Callback<ProfileData?> {

            override fun onResponse(call: Call<ProfileData?>, response: Response<ProfileData?>) {
                Log.d(TAG, "Friends profiles onResponse: " + response.code())
                if (response.isSuccessful) {
                    friendsAll = response.body()?.response?.players!!.toMutableList()
                    friends = friendsAll
                    name(true)
                } else {
                    Log.d(TAG, "Error: " + response.message())
                }
            }

            override fun onFailure(call: Call<ProfileData?>, throwable: Throwable) {
                throwable.printStackTrace()
                Log.d(TAG, "Network request error occurred")
            }
        })
    }

    fun name(desc: Boolean){
        val comparator = Comparator { g1: Player, g2: Player ->
            if (desc) {
                return@Comparator g1.personaname?.compareTo(g2.personaname!!,ignoreCase = true)!!
            }
            else{
                return@Comparator g2.personaname?.compareTo(g1.personaname!!,ignoreCase = true)!!
            }
        }
        friends = friends.sortedWith(comparator).toMutableList()
        friendsAll = friendsAll.sortedWith(comparator).toMutableList()
        notifyDataSetChanged()
    }

    fun search(searchText: String){
        Log.d("friendadapter","text: " + searchText)
        if (searchText.isEmpty()){
            friends.clear()
            friends.addAll(friendsAll)
            Log.d("adapter","friends: " + friends.size + " friendsAll: " + friendsAll.size)
            notifyDataSetChanged()
        } else {
            friends.clear()
            Log.d("adapter","after clear friends: " + friends.size + " friendsAll: " + friendsAll.size)
            friendsAll.forEach {
                if (it?.personaname!!.contains(searchText,ignoreCase = true)){
                    friends.add(it)
                }
            }
            Log.d("adapter","after add friends: " + friends.size + " friendsAll: " + friendsAll.size)
            notifyDataSetChanged()
        }
    }

}
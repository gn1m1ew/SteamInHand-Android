package final_Project_Group_29.project.app.android.SteamInHand.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import final_Project_Group_29.project.app.android.SteamInHand.R
import final_Project_Group_29.project.app.android.SteamInHand.databinding.FragmentDetailFriendlistBinding
import final_Project_Group_29.project.app.android.SteamInHand.detail.PlayerDataHolder

class DetailFriendlistFragment : Fragment(), FriendAdapter.OnFriendSelectedListener,AdapterView.OnItemSelectedListener {

    private var _binding: FragmentDetailFriendlistBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: FriendAdapter

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
        _binding = FragmentDetailFriendlistBinding.inflate(LayoutInflater.from(context))
        initRecyclerView()
        initSpinner()
        binding.SSort.onItemSelectedListener = this

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("friend","text: " + s.toString())
                adapter.search(s.toString())
            }
        })

        return binding.root
    }

    private fun initRecyclerView() {
        binding.rvFriends.layoutManager = LinearLayoutManager(binding.rvFriends.context)
        adapter = FriendAdapter(this)
        var IDs = ""
        playerDataHolder?.getFriendlistData()?.friendslist?.friends?.forEach {
            IDs += it.steamid.toString() + ","
        }
        if(IDs.isNotEmpty()) {
            adapter.addFriend(IDs)
        }else{
            binding.tvInfo.visibility = View.VISIBLE
            binding.rvFriends.visibility = View.GONE
        }
        binding.rvFriends.adapter = adapter
    }

    override fun onFriendSelected(friend: Long?) {
        val id = Intent()
        id.putExtra("player",friend.toString())
        activity?.setResult(1,id)
        activity?.finish()
    }

    private fun initSpinner(){
        ArrayAdapter.createFromResource(this.requireContext(), R.array.friendsSortArray, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.SSort.adapter = adapter
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        when(pos){
            0 -> adapter.name(true)
            1 -> adapter.name(false)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}
}
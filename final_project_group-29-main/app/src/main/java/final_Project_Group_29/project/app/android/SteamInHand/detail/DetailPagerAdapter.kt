package final_Project_Group_29.project.app.android.SteamInHand.detail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import final_Project_Group_29.project.app.android.SteamInHand.fragment.*

class DetailPagerAdapter (fa: FragmentActivity): FragmentStateAdapter(fa) {

    companion object{
        private const val NUM_PAGES: Int = 3
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DetailProfileFragment()
            1 -> DetailFriendlistFragment()
            2 -> DetailInventoryFragment()
            else -> DetailProfileFragment()
        }
    }
}
package final_Project_Group_29.project.app.android.SteamInHand.detail

import final_Project_Group_29.project.app.android.SteamInHand.data.model.friends.FriendlistData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.ProfileData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.stats.PlayerStatsData

interface PlayerDataHolder {
    fun getProfileData(): ProfileData?
    fun getStatsData(): PlayerStatsData?
    fun getFriendlistData(): FriendlistData?
}
package final_Project_Group_29.project.app.android.SteamInHand.Utils

import final_Project_Group_29.project.app.android.SteamInHand.data.model.ItemPrice.ItemPriceData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.friends.FriendlistData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.games.GamesData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.games.RecentlyData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.inventory.InventoryData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.itemInfo.ItemInfoData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.ProfileData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.level.LevelData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.stats.PlayerStatsData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.url.UrlData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object NetworkManager {
    private val retrofit: Retrofit
    private val retrofitInv: Retrofit
    private val retrofitItem: Retrofit
    private val STEAM_WEB_API: SteamWebApi
    private val inventoryApi: InventoryApi
    private val ItemInfoApi: InventoryApi

    private const val SERVICE_URL = "https://api.steampowered.com"
    private const val INVENTORY_URL = "https://steamcommunity.com"
    private const val ITEM_INFO_URL = "https://api.csgofloat.com"
    private const val KEY = "EB2FB83D7A12205D5D3A6FAA499BA856" //EB2FB83D7A12205D5D3A6FAA499BA856

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(SERVICE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        STEAM_WEB_API = retrofit.create(SteamWebApi::class.java)

        retrofitInv = Retrofit.Builder()
                .baseUrl(INVENTORY_URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        inventoryApi = retrofitInv.create(InventoryApi::class.java)

        retrofitItem = Retrofit.Builder()
                .baseUrl(ITEM_INFO_URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        ItemInfoApi = retrofitItem.create(InventoryApi::class.java)
    }

    fun getStats(steamID: Long?): Call<PlayerStatsData?>? {
        return STEAM_WEB_API.getStats(KEY, steamID, 730)
    }

    fun getIDFromURL(url: String?): Call<UrlData?>?{
        return STEAM_WEB_API.getID(KEY,url)
    }

    fun getProfile(steamID: Long?): Call<ProfileData?>?{
        return STEAM_WEB_API.getProfile(KEY,steamID)
    }

    fun getFriendsProfiles(steamIDs: String?): Call<ProfileData?>?{
        return STEAM_WEB_API.getFriendsProfiles(KEY,steamIDs)
    }

    fun getFriends(steamID: Long?): Call<FriendlistData?>?{
        return STEAM_WEB_API.getFriends(KEY,steamID)
    }

    fun getInventory(steamID: Long?): Call<InventoryData?>?{
        return inventoryApi.getInventory(steamID)
    }

    fun getItemPrice(mhName: String?): Call<ItemPriceData?>?{
        return inventoryApi.getItemPrice(3,730,mhName)
    }

    fun getItemInfo(url: String?): Call<ItemInfoData?>?{
        return ItemInfoApi.getItemInfo(url)
    }

    fun getSteamLevel(steamid: Long?): Call<LevelData?>?{
        return STEAM_WEB_API.getSteamLevel(KEY,steamid)
    }

    fun getRecentlyGames(steamid: Long?): Call<RecentlyData?>?{
        return STEAM_WEB_API.getRecentlyGames(KEY,steamid,5)
    }

    fun getAllGames(steamid: Long?): Call<GamesData?>?{
        return STEAM_WEB_API.getAllGames(KEY,steamid,true,true)
    }
}
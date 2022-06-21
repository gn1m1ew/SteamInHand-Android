package final_Project_Group_29.project.app.android.SteamInHand.Utils

//import final_Project_Group_29.project.app.android.SteamInHand.data.model.ban.BanData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.friends.FriendlistData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.games.GamesData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.games.RecentlyData
//import final_Project_Group_29.project.app.android.SteamInHand.data.model.playercount.CountData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.ProfileData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.profile.level.LevelData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.stats.PlayerStatsData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.url.UrlData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamWebApi {
    @GET("/ISteamUser/ResolveVanityURL/v1/")
    fun getID(
            @Query("key") key: String?,
            @Query("vanityurl") vanityURL: String?
    ): Call<UrlData?>?

    @GET("/ISteamUserStats/GetUserStatsForGame/v2/")
    fun getStats(
            @Query("key") key: String?,
            @Query("steamid") steamid: Long?,
            @Query("appid") appId: Long?
    ): Call<PlayerStatsData?>?

    @GET("/ISteamUser/GetPlayerSummaries/v2/")
    fun getProfile(
            @Query("key") key: String?,
            @Query("steamids") steamids: Long?
    ): Call<ProfileData?>?

    @GET("/ISteamUser/GetPlayerSummaries/v2/")
    fun getFriendsProfiles(
            @Query("key") key: String?,
            @Query("steamids") steamids: String?
    ): Call<ProfileData?>?

    @GET("/ISteamUser/GetFriendList/v1/")
    fun getFriends(
            @Query("key") key: String?,
            @Query("steamid") steamid: Long?
    ): Call<FriendlistData?>?

    @GET("/IPlayerService/GetSteamLevel/v1/")
    fun getSteamLevel(
            @Query("key") key: String?,
            @Query("steamid") steamid: Long?
    ): Call<LevelData?>?

    @GET("/IPlayerService/GetRecentlyPlayedGames/v1/")
    fun getRecentlyGames(
            @Query("key") key: String?,
            @Query("steamid") steamid: Long?,
            @Query("count") count: Int?
    ): Call<RecentlyData?>?

    @GET("/IPlayerService/GetOwnedGames/v1/")
    fun getAllGames(
            @Query("key") key: String?,
            @Query("steamid") steamid: Long?,
            @Query("include_played_free_games") freeGames: Boolean?,
            @Query("include_appinfo") appinfo: Boolean?
    ): Call<GamesData?>?
}
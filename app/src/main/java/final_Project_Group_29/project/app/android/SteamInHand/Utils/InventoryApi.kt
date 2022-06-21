package final_Project_Group_29.project.app.android.SteamInHand.Utils

import final_Project_Group_29.project.app.android.SteamInHand.data.model.ItemPrice.ItemPriceData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.inventory.InventoryData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.itemInfo.ItemInfoData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InventoryApi {
    @GET("/profiles/{steamID}/inventory/json/730/2")
    fun getInventory(
            @Path("steamID") steamID: Long?
    ): Call<InventoryData?>?

    @GET("/market/priceoverview/")
    fun getItemPrice(
            @Query("currency") currency: Int?,
            @Query("appid") appid: Int?,
            @Query("market_hash_name") market_hash_name: String?
    ): Call<ItemPriceData?>?

    @GET(".")
    fun getItemInfo(
            @Query("url") url: String?
    ): Call<ItemInfoData?>?
}
package final_Project_Group_29.project.app.android.SteamInHand.data

import androidx.room.*

@Dao
interface PlayerItemDao {
    @Query("SELECT * FROM playeritem")
    suspend fun getAll(): List<PlayerItem>

    @Insert
    suspend fun insert(shoppingItems: PlayerItem): Long

    @Update
    suspend fun update(shoppingItem: PlayerItem)

    @Delete
    suspend fun deleteItem(shoppingItem: PlayerItem)
}
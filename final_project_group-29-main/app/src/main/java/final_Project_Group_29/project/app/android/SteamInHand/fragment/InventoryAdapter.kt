package final_Project_Group_29.project.app.android.SteamInHand.fragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import final_Project_Group_29.project.app.android.SteamInHand.R
import final_Project_Group_29.project.app.android.SteamInHand.databinding.ItemInventoryBinding
import final_Project_Group_29.project.app.android.SteamInHand.data.model.inventory.InventoryData
import final_Project_Group_29.project.app.android.SteamInHand.data.model.inventory.InventoryFullItem

class InventoryAdapter(private val listener: OnItemSelectedListener) : RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder>(){

    private var invenotry: MutableList<InventoryFullItem?> = ArrayList()
    private val imgURL = "https://steamcommunity-a.akamaihd.net/economy/image/"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = InventoryViewHolder(
            ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val item = invenotry[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = invenotry.size

    fun addItems(ID: InventoryData) {
        setupInventory(ID)
    }

    inner class InventoryViewHolder(val binding: ItemInventoryBinding): RecyclerView.ViewHolder(binding.root) {
        var item: InventoryFullItem? = null

        init {
            binding.root.setOnClickListener {
                listener.onItemSelected(item!!)
            }
        }

        fun bind(newItem: InventoryFullItem?) {
            item = newItem!!
            binding.tvItemName.text = item?.decs?.market_name
            binding.tvItemAmount.text = binding.root.context.resources.getString(R.string.amountx,item?.amount)
            item?.decs?.tags?.forEach {
                if (!it.color.isNullOrEmpty()){
                    binding.llBorder.setBackgroundColor(Color.parseColor("#${it.color}"))
                }
            }
            Glide.with(binding.root)
                    .load("${imgURL}${item?.decs?.icon_url}")
                    .transition(DrawableTransitionOptions().crossFade())
                    .into(binding.ivItemImg)
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(item: InventoryFullItem)
    }

    private fun setupInventory(inventoryData: InventoryData){
        val inv = inventoryData
        val itemList: MutableList<InventoryFullItem?> = ArrayList()
        var inIt = false

        for(entry in inv.rgInventory!!) {
            val decs = "${entry.value.classid}_${entry.value.instanceid}"
            val item = InventoryFullItem()
            item.id = entry.value.id
            item.amount = entry.value.amount
            item.decs = inv.rgDescriptions?.get(decs)
            if(itemList.size == 0){
                itemList.add(item)
            }else{
                for (it in itemList){
                    if(it?.decs?.market_name.equals(item.decs?.market_name)){
                        if (it?.decs?.type!!.contains("Container") || it.decs?.type!!.contains("Graffiti") || it.decs?.type!!.contains("Sticker")){
                            it.amount = it.amount?.toInt()?.plus(1).toString()
                            inIt = false
                            break
                        }
                    }else{
                        inIt = true
                    }
                }
                if (inIt){
                    itemList.add(item)
                }
            }
        }
        invenotry = sortInventory(itemList)
        notifyDataSetChanged()
    }

    private fun sortInventory(itemList: MutableList<InventoryFullItem?>): MutableList<InventoryFullItem?> {

        for (i in 0 until itemList.size-1){
            var min:Int = i
            for (j in i+1 until itemList.size){
                var color = ""
                var colorMin = ""
                itemList[j]?.decs?.tags?.forEach {
                    if (!it.color.isNullOrEmpty()){
                        color = it.color!!
                    }
                }
                itemList[min]?.decs?.tags?.forEach {
                    if (!it.color.isNullOrEmpty()){
                        colorMin = it.color!!
                    }
                }
                if (colorToInt(color) > colorToInt(colorMin)){
                    min = j
                }
            }
            if(min != i){
                itemList[i] = itemList[min].also { itemList[min] = itemList[i] }
            }
        }

        return itemList
    }

    private fun colorToInt(color:String): Int{
        when(color){
            "b0c3d9" -> return 1
            "5e98d9" -> return 2
            "4b69ff" -> return 3
            "8847ff" -> return 4
            "d32ce6" -> return 5
            "eb4b4b" -> return 6
            "e4ae39" -> return 7
        }
        return 0
    }
}
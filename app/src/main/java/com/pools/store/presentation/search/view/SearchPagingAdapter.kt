package com.pools.store.presentation.search.view
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pools.store.base.BaseAdapterPaging
import com.pools.store.databinding.ItemAppStoreBinding
import com.pools.store.domain.model.AppDomain
import javax.inject.Inject

class SearchPagingAdapter @Inject constructor() :
    BaseAdapterPaging<AppDomain>(diffCallbackTip) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchSwapTokenViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAppStoreBinding.inflate(inflater, parent, false)
        return SearchSwapTokenViewHolder(binding)
    }


    inner class SearchSwapTokenViewHolder(private val binding: ItemAppStoreBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<AppDomain> {
        override fun bind(item: AppDomain) {
            // Bind your data to the views in the ViewHolder
            binding.apply {
                tvApp.text = item.name
                tvAuthor.text = item.name
//                ivApp.setImageResource( R.drawable.ic_app_example)
                Glide.with(binding.ivApp.context).load(item.imageContentUrls[0]).into(ivApp)
                tvPriceReduce.paintFlags = tvPriceReduce.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                if (item.pricing > 0){
                    binding.apply {
                        tvPrice.visibility = View.VISIBLE
                        tvPriceReduce.visibility = View.VISIBLE
                        tvPercentReduce.visibility = View.VISIBLE
                    }
                }else {
                    binding.apply {
                        tvPrice.visibility = View.GONE
                        tvPriceReduce.visibility = View.GONE
                        tvPercentReduce.visibility = View.GONE
                    }
                }

                rBar.rating = item.averageStar.toFloat()
            }
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }


        }

    }


    companion object {
        val diffCallbackTip = object : DiffUtil.ItemCallback<AppDomain>() {
            override fun areItemsTheSame(oldItem: AppDomain, newItem: AppDomain): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AppDomain, newItem: AppDomain): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }


}
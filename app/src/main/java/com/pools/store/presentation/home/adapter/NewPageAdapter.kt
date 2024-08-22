package com.pools.store.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.pools.store.R


class NewPageAdapter(slidePhotoList: List<String>?, context: Context) : PagerAdapter() {
    private val slidePhotoList: List<String>?
    private val context: Context
    init {
        this.slidePhotoList = slidePhotoList
        this.context = context
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View =
            LayoutInflater.from(container.context).inflate(R.layout.item_new_marketplace, null)
        val imageView: ImageView = view.findViewById(R.id.ivPhoto)
//        val slidePhoto: String = slidePhotoList!![position].banner
//        Glide.with(context).load(slidePhoto).into(imageView)
//        imageView.setImageResource(slidePhotoList!![position])
        Glide.with(context).load(slidePhotoList!![position]).into(imageView)
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return slidePhotoList?.size ?: 0
    }

    override fun isViewFromObject(view: View, objects: Any): Boolean {
        return view === objects
    }

    override fun destroyItem(container: ViewGroup, position: Int, objects: Any) {
        container.removeView(objects as View)
    }
}
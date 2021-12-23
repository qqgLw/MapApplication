package com.example.map_app.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.map_app.R
import com.example.map_app.database.tables.Article
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    inner class EmptyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList.isEmpty()){
            true -> {R.layout.item_empty_preview}
            false -> R.layout.item_article_preview
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_article_preview -> {
                ArticleViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_article_preview,
                        parent,
                        false
                    )
                )
            }
            else -> {
                EmptyViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.item_empty_preview,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ArticleViewHolder -> {
                val article = differ.currentList[position]
                holder.itemView.apply {
                    Glide.with(this).load(article.urlToImage).into(ivArticleImage)
                    tvSource.text = article.source?.name
                    tvTitle.text = article.title
                    tvDescription.text = article.description
                    tvPublishedAt.text = article.publishedAt
                    setOnClickListener {
                        onItemClickListener?.let { it(article) }
                    }
                }
            }
            is EmptyViewHolder->{}
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) { //initialize article handling on elem onclick outside the adapter
        onItemClickListener = listener
    }
}









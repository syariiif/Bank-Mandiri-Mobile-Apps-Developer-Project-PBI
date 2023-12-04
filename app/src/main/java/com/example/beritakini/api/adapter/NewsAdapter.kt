package com.example.beritakini.api.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.beritakini.R
import com.example.beritakini.api.model.Article

class NewsAdapter(private val onClick: (Article) -> Unit) :
    ListAdapter<Article, NewsAdapter.NewsViewHolder>(ArticleCallBack), Filterable {

    var originalList: List<Article> = emptyList()

    class NewsViewHolder(itemView: View, val onClick: (Article) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val thumbnail: ImageView = itemView.findViewById(R.id.thumbnail)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val author: TextView = itemView.findViewById(R.id.author)
        private val date: TextView = itemView.findViewById(R.id.date)

        private var currentArticle: Article? = null

        init {
            itemView.setOnClickListener {
                currentArticle?.let {
                    onClick(it)
                }
            }
        }

        fun bind(article: Article) {
            currentArticle = article

            title.text = article.title
            author.text = article.author
            date.text = article.publishedAt

            Glide.with(itemView).load(article.urlToImage).centerCrop().into(thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false)
        return NewsViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = mutableListOf<Article>()

                if (constraint.isNullOrBlank()) {
                    filteredList.addAll(originalList)
                } else {
                    val filterPattern = constraint.toString().toLowerCase().trim()

                    for (item in originalList) {
                        val title = item.title?.toLowerCase()
                        val author = item.author?.toLowerCase()

                        if (!title.isNullOrBlank() && title.contains(filterPattern) ||
                            !author.isNullOrBlank() && author.contains(filterPattern)
                        ) {
                            filteredList.add(item)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }


            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.d("NewsAdapter", "Filtered List: ${results?.values}")
                submitList(results?.values as List<Article>?)
                notifyDataSetChanged()
            }
        }
    }
}

object ArticleCallBack : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
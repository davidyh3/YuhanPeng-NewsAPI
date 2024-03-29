package com.example.newsapi

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapi.databinding.ListItemNewsBinding


class NewsHolder(
    private val binding: ListItemNewsBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(news: News) {
        binding.newsTitle.text = news.title
        binding.newsAuthor.text = news.author
        binding.newsDate.text = news.publishedAt

        binding.root.setOnClickListener {
            Toast.makeText(
                binding.root.context,
                "${news.title} clicked!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

class NewsListAdapter(
    private var newsList: List<News>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<NewsHolder>() {

    interface OnItemClickListener {
        fun onItemClick(news: News)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemNewsBinding.inflate(inflater, parent, false)
        return NewsHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val news = newsList[position]
        holder.bind(news)

        holder.itemView.setOnClickListener {
            listener.onItemClick(news)
        }
    }

    override fun getItemCount() = newsList.size

    fun updateNewsList(newList: List<News>) {
        newsList = newList
        notifyDataSetChanged()
    }
}
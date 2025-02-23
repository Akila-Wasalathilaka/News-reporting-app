import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsreportingapp.Fragments.Post
import com.example.newsreportingapp.R
import java.security.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsAdapter(private val newsList: MutableList<Post>) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newsImage: ImageView = view.findViewById(R.id.newsImage)
        val newsTitle: TextView = view.findViewById(R.id.newsTitle)
        val newsDescription: TextView = view.findViewById(R.id.newsDescription)
        val newsReporter: TextView = view.findViewById(R.id.newsReporter)
        val newsTimestamp:TextView = view.findViewById(R.id.newsTimestampBottom)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news_list, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val post = newsList[position]

        // Decode Base64 image if available
        if (post.image != null) {
            val imageBytes = Base64.decode(post.image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            Glide.with(holder.itemView.context)
                .load(bitmap)
                .into(holder.newsImage)
        } else {
            // If no image is available, set a default placeholder
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_image)
                .into(holder.newsImage)
        }

        holder.newsTitle.text = post.topic
        holder.newsDescription.text = post.caption
        holder.newsReporter.text = "Reporter: ${post.reporter ?: "Unknown"}"
        try {
            val timestampMillis = post.timestamp.toString().toLongOrNull() ?: 0L
            if (timestampMillis > 0) {
                val date = Date(timestampMillis)
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                holder.newsTimestamp.text = sdf.format(date)
            } else {
                holder.newsTimestamp.text = "Time Unavailable"
            }
        } catch (e: Exception) {
            holder.newsTimestamp.text = "Time Error"
        }
    }

    override fun getItemCount() = newsList.size

    fun addNews(post: Post) {
        newsList.add(0, post)
        notifyItemInserted(0)
    }
}

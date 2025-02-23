import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsreportingapp.Fragments.Post
import com.example.newsreportingapp.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


class HomeFragment : Fragment(R.layout.home_activity) {
    private lateinit var postGrid: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private val newsList = mutableListOf<Post>()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postGrid = view.findViewById(R.id.postGrid)
        postGrid.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(newsList)
        postGrid.adapter = newsAdapter

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)

        // Set up SwipeRefreshLayout listener
        swipeRefreshLayout.setOnRefreshListener {
            // Fetch news again when pulled down
            fetchNews()
        }

        // Fetch news initially
        fetchNews()
    }

    private fun fetchNews() {
        // Show the refreshing indicator
        swipeRefreshLayout.isRefreshing = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Fetch posts from Firestore
                firestore.collection("posts")
                    .get()
                    .addOnSuccessListener { result ->
                        val posts = mutableListOf<Post>()
                        for (document in result) {
                            val post = document.toObject(Post::class.java)
                            posts.add(post)
                        }
                        // Update UI on the main thread
                        activity?.runOnUiThread {
                            newsList.clear()
                            newsList.addAll(posts)
                            newsAdapter.notifyDataSetChanged()

                            // Stop the refreshing indicator
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
                    .addOnFailureListener { e ->
                        activity?.runOnUiThread {
                            Toast.makeText(requireContext(), "Failed to load news: ${e.message}", Toast.LENGTH_SHORT).show()

                            // Stop the refreshing indicator
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
            } catch (e: Exception) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()

                    // Stop the refreshing indicator
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }
}


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsreportingapp.AddNewsFragment
import com.example.newsreportingapp.Fragments.NewsApprovalFragment
import com.example.newsreportingapp.Fragments.ProfileFragment




class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 4  // Total number of tabs

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AddNewsFragment()
            2 -> NewsApprovalFragment()
            3 -> ProfileFragment()
            else -> HomeFragment()
        }
    }
}

package infinum.academy2019.shows_danijel_pecek.ui.shared

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Fragment", "${javaClass.simpleName} onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("Fragment", "${javaClass.simpleName} onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }


    override fun onPause() {
        super.onPause()
        Log.d("Fragment", "${javaClass.simpleName} onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Fragment", "${javaClass.simpleName} onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Fragment", "${javaClass.simpleName} onDestroy")
    }
    open fun onBackButton() = false
}
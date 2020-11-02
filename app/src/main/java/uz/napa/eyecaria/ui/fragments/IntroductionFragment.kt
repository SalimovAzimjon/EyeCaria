package uz.napa.eyecaria.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_introduction.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.util.isVisible

class IntroductionFragment : BaseFragment(R.layout.fragment_introduction, R.color.lightBlack) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = requireActivity().getSharedPreferences("Pref", Context.MODE_PRIVATE).edit()
        pref.putString("login","Logged in")
        pref.apply()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val string = getString(R.string.we_aim)
        btn_skip.setOnClickListener {
            val action = IntroductionFragmentDirections.actionIntroductionFragmentToHomeFragment("Yes")
            findNavController().navigate(action)
        }
        btn_next.setOnClickListener {
            if (tv_explanation.text == string){
                val action = IntroductionFragmentDirections.actionIntroductionFragmentToHomeFragment("Yes")
                findNavController().navigate(action)
            }
            else{
                tv_welcome.isVisible(false)
                tv_explanation.text = string
            }
        }
    }
}
package uz.napa.eyecaria.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_info.*
import uz.napa.eyecaria.R

class InfoFragment : BaseFragment(R.layout.fragment_info, R.color.colorAccent) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_back.setOnClickListener { findNavController().popBackStack() }
    }
}
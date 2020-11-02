package uz.napa.eyecaria.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import kotlinx.android.synthetic.main.fragment_clinics.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.model.Clinics
import uz.napa.eyecaria.ui.adapters.ClinicsAdapter

class ClinicsFragment : BaseFragment(R.layout.fragment_clinics, R.color.colorAccent) {

    private val clinicsList = ArrayList<Clinics>()
    private val clinicsAdapter by lazy { ClinicsAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addClinics()
        setUpRv()
        btn_back.setOnClickListener { findNavController().popBackStack() }
        clinicsAdapter.onItemClickListener {
            findNavController().navigate(R.id.action_clinicsFragment_to_clinicsDetailFragment)
        }
    }

    private fun setUpRv() {
        rv_clinics.apply {
            adapter = clinicsAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun addClinics() {
        clinicsList.clear()
        clinicsList.add(
            Clinics(
                R.drawable.shifo_nur_logo,
                "Shifo Nur",
                "Opened in 2001"
            )
        )
        clinicsList.add(
            Clinics(
                R.drawable.salus_vita_logo,
                "Salus Vita",
                "Opened in 2002"
            )
        )
        clinicsList.add(
            Clinics(
                R.drawable.sihat_koz_logo,
                "Sihat Ko'z",
                "Opened in 2002"
            )
        )
        clinicsList.add(
            Clinics(
                R.drawable.saif_optima_logo,
                "Saif Optima",
                "Opened in 2002"
            )
        )

        clinicsList.add(
            Clinics(
                R.drawable.akfa_medline_logo,
                "Akfa Medline",
                "Opened in 2018"
            )
        )

        clinicsAdapter.submitList(clinicsList)
    }
}
package uz.napa.eyecaria.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_clinic.view.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.model.Clinics

class ClinicsAdapter : ListAdapter<Clinics, ClinicsAdapter.ClinicViewHolder>(DiffClinics()) {
    private var itemClickListener: ((Clinics) -> Unit)? = null

    fun onItemClickListener(listener: (Clinics) -> Unit) {
        itemClickListener = listener
    }

    inner class ClinicViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicViewHolder {
        return ClinicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_clinic, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ClinicViewHolder, position: Int) {
        val clinic = getItem(position)
        holder.itemView.apply {
            tv_clinic_name.text = clinic.clinicName
            tv_founded.text = clinic.openedYear
            img_clinic.setImageResource(clinic.img)
            setOnClickListener {
                itemClickListener?.let { it(clinic) }
            }
        }
    }
}

class DiffClinics : DiffUtil.ItemCallback<Clinics>() {
    override fun areItemsTheSame(oldItem: Clinics, newItem: Clinics) =
        oldItem.hashCode() == newItem.hashCode()

    override fun areContentsTheSame(oldItem: Clinics, newItem: Clinics) =
        oldItem.clinicName == newItem.clinicName

}
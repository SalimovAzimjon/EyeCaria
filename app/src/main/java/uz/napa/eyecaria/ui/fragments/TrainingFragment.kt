package uz.napa.eyecaria.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import kotlinx.android.synthetic.main.fragment_training.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.model.Training
import uz.napa.eyecaria.ui.activity.VideoActivity
import uz.napa.eyecaria.ui.adapters.TrainingAdapter


class TrainingFragment : BaseFragment(R.layout.fragment_training, R.color.colorAccent) {
    private val trainingAdapter by lazy(LazyThreadSafetyMode.NONE) { TrainingAdapter() }
    private val trainingList = ArrayList<Training>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTraining()
        setUpRv()
        trainingAdapter.setOnItemClickListener {
            val intent = Intent(activity, VideoActivity::class.java)
            intent.putExtra("data",it)
            startActivity(intent)
        }
        btn_back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setUpTraining() {
        trainingList.clear()
        trainingList.add(
            Training(
                R.drawable.vid_1,
                "Blink for a minute",
                "file:///android_asset/video1.mp4"
            )
        )
        trainingList.add(
            Training(
                R.drawable.vid_2,
                "Rotate your head while staring ahead",
                "file:///android_asset/video2.mp4"
            )
        )
        trainingAdapter.submitList(trainingList)
    }

    private fun setUpRv() {
        rv_training.apply {
            adapter = trainingAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }
}
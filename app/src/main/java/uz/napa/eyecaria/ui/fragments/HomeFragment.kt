package uz.napa.eyecaria.ui.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import kotlinx.android.synthetic.main.drawer_layout.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.post_app_bar.*
import uz.napa.eyecaria.R
import uz.napa.eyecaria.model.Post
import uz.napa.eyecaria.ui.adapters.PostAdapter

class HomeFragment : BaseFragment(R.layout.fragment_home, R.color.colorPrimaryDark) {
    private lateinit var mDrawerToggle: ActionBarDrawerToggle
    private val postsAdapter by lazy(LazyThreadSafetyMode.NONE) { PostAdapter() }
    private val postsList = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = requireActivity().getSharedPreferences("Pref", Context.MODE_PRIVATE)
        if (!pref.contains("login")){
            findNavController().navigate(R.id.action_homeFragment_to_introductionFragment)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpDrawerToggle()
        setUpNavigationClick()
        setUpRv()
        addPosts()
        btn_camera.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_cameraFragment)
        }
    }

    private fun setUpNavigationClick() {
        tv_home.setOnClickListener { drawer_layout.close() }
        tv_camera.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.homeFragment)
                navigate(R.id.action_homeFragment_to_cameraFragment)
        }
        tv_training.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.homeFragment)
                navigate(R.id.action_homeFragment_to_trainingFragment)
        }
        tv_info.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.homeFragment)
                navigate(R.id.action_homeFragment_to_infoFragment)
        }
        tv_settings.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.homeFragment)
                navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        tv_add_clinics.setOnClickListener {
            if (findNavController().currentDestination?.id == R.id.homeFragment)
                navigate(R.id.action_homeFragment_to_clinicsFragment)
        }
    }

    private fun navigate(actionId: Int) {
        drawer_layout.close()
        if (findNavController().currentDestination?.id == R.id.homeFragment)
            findNavController().navigate(actionId)
    }

    private fun setUpRv() {
        rv_posts.apply {
            adapter = postsAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun setUpDrawerToggle() {
        val actionBarToggle: ActionBarDrawerToggle =
            object : ActionBarDrawerToggle(
                requireActivity(),
                drawer_layout,
                R.string.open,
                R.string.close
            ) {
                //                private val scaleFactor = 10f
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = drawerView.width * slideOffset
                    toolbar_home.translationX = slideX
//                    toolbar_home.scaleX = 1 - slideOffset / scaleFactor
                }
            }

        drawer_layout.setScrimColor(Color.TRANSPARENT)
        drawer_layout.drawerElevation = 0f
        drawer_layout.addDrawerListener(actionBarToggle)
        mDrawerToggle =
            ActionBarDrawerToggle(
                activity,
                drawer_layout,
                toolbar,
                R.string.home,
                R.string.settings
            )
        drawer_layout.addDrawerListener(mDrawerToggle)
        mDrawerToggle.syncState()
    }

    private fun addPosts() {
        postsList.clear()
        postsList.add(
            Post(
                imgUrl = R.drawable.retinoblasma,
                text = """
                    • Retinoblastoma is a rare type of intraocular eye cancer that usually develops in early childhood, typically before the age of 5
                """.trimIndent()
            )
        )

        postsList.add(
            Post(
                imgUrl = R.drawable.eyecaria2,
                text = """
                    • In children with retinoblastoma the disease often affects only one eye. However, one out of three children with retinoblastoma, develops cancer in both eyes
                """.trimIndent()
            )
        )

        postsList.add(
            Post(
                imgUrl = R.drawable.retiblasma2,
                text = """
                    • The most common first sign of retinoblastoma is a visible whitness in the pupil called 'cat's eye reflex' or leukocoria
                """.trimIndent()
            )
        )

        postsList.add(
            Post(
                imgUrl = R.drawable.human_dog,
                text = """
                    • Humans have three types of cones which perceive the presence of red, green and blue. These combine in different levels to create the full range of color we see. In case dogs only have 2 of these, yellow and blue, which means his vision is closer to human with it red-green color blindness just black and white
                """.trimIndent()
            )
        )

        postsList.add(
            Post(
                imgUrl = R.drawable.color_eye,
                text = """
                    • The human eye can differentiate approximately 10 million different colors
                """.trimIndent()
            )
        )

        postsList.add(
            Post(
                imgUrl = R.drawable.blink_eye,
                text = """
                    • The human eye blinks an average of 4,200,000 times a year. This means if you were given a nickel for everytime you blinked you would make ${'$'}210k anually
                """.trimIndent()
            )
        )
        postsList.add(
            Post(
                imgUrl = R.drawable.blue_brown_eye,
                text = """
                    • Brown eyes are blue eyes underneath. Consequently, a person can receive surgery in order to make their brown eyes blue
                """.trimIndent()
            )
        )

        postsList.add(
            Post(
                imgUrl = R.drawable.tiger,
                text = """
                    • The night vision of tigers is six times better than that of humans
                """.trimIndent()
            )
        )

        postsList.add(
            Post(
                imgUrl = R.drawable.newborn,
                text = """
                    • The reason why babies can't see more colors is because the cones in their eyes - the photoreceptor cells responsible for picking up colors - are too weak to detect them. Those cells quickly get stronger, though. After about two months, baby can distinguish between red and green, and a few weeks later they can tell them difference between blue and yellow
                """.trimIndent()
            )
        )

        postsAdapter.submitList(postsList)
    }

}
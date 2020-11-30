package edu.hkbu.comp4097.groupproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import edu.hkbu.comp4097.groupproject.data.AppDatabase
import edu.hkbu.comp4097.groupproject.data.Code
import edu.hkbu.comp4097.groupproject.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        var username: TextView = view.findViewById(R.id.textView1)
        var image: ImageView = view.findViewById(R.id.imageView)
        val logout: Button = view.findViewById(R.id.Logout)

        var user = FirebaseAuth.getInstance().currentUser

        // var name = user?.displayName

        username.text = user?.displayName
        if (image != null) {

            Picasso.get().load(user?.photoUrl).resize(250, 250)
                .centerCrop().into(image)

        }

        logout.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val intent = Intent(activity, LoginActivity::class.java)

                var user = FirebaseAuth.getInstance().currentUser
                FirebaseAuth.getInstance().signOut()

                var name = user?.displayName
                val Bye = getString(R.string.Bye)
                Snackbar.make(
                    view,
                    "$Bye $name",
                    Snackbar.LENGTH_LONG
                ).show()

                AppDatabase.deleteInstance()

                if (FirebaseAuth.getInstance().currentUser == null)
                    startActivityForResult(
                        intent,
                        Code.LOGIN_RESULT
                    )
            }

        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
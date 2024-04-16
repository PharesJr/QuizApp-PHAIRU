package com.example.quizonline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizonline.databinding.ActivityMainBinding
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var quizModelList: MutableList<QuizModel>
    lateinit var adapter: QuizListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)


        enableEdgeToEdge()
        setContentView(binding.root)

        quizModelList = mutableListOf()
        getDataFromFirebase()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView(){
        binding.progressBar.visibility= View.GONE
    adapter = QuizListAdapter(quizModelList)
    binding.rvQuiz.layoutManager = LinearLayoutManager(this)
    binding.rvQuiz.adapter = adapter
    }

    private fun getDataFromFirebase(){

        binding.progressBar.visibility = View.VISIBLE


        FirebaseDatabase.getInstance().reference
            .get()
            .addOnSuccessListener {datasnapshot ->
                if(datasnapshot.exists()){
                    for (snapshot in datasnapshot.children){
                        val quizModel = snapshot.getValue(QuizModel::class.java)
                        if (quizModel != null) {
                            quizModelList.add(quizModel)
                        }
                    }
                }
                setupRecyclerView()
            }


    }
}
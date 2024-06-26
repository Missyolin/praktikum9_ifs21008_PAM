package com.ifs21008.lostfound.presentation.lostfound

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs18005.delcomtodo.data.remote.response.TodoResponse
import com.ifs21008.lostfound.R
import com.ifs21008.lostfound.data.model.LostFound
import com.ifs21008.lostfound.data.remote.MyResult
import com.ifs21008.lostfound.databinding.ActivityLostfoundDetailBinding
import com.ifs21008.lostfound.helper.Utils.Companion.observeOnce
import com.ifs21008.lostfound.presentation.ViewModelFactory

class LostFoundDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLostfoundDetailBinding
    private val viewModel by viewModels<LostFoundViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var isChanged: Boolean = false

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == LostFoundManageActivity.RESULT_CODE) {
            recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLostfoundDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
    }

    private fun setupView() {
        showComponent(false)
        showLoading(false)
    }

    private fun setupAction() {
        val todoId = intent.getIntExtra(KEY_TODO_ID, 0)
        if (todoId == 0) {
            finish()
            return
        }

        observeGetLostFound(todoId)

        binding.appbarLostFoundDetail.setNavigationOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(KEY_IS_CHANGED, isChanged)
            setResult(RESULT_CODE, resultIntent)
            finishAfterTransition()
        }
    }

    private fun observeGetLostFound(todoId: Int) {
        viewModel.getLostFound(todoId).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }

                is MyResult.Success -> {
                    showLoading(false)
                    loadTodo(result.data.data.todo)
                }

                is MyResult.Error -> {
                    Toast.makeText(
                        this@LostFoundDetailActivity,
                        result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                    showLoading(false)
                    finishAfterTransition()
                }
            }
        }
    }

    private fun loadTodo(todo: TodoResponse?) {
        if (todo != null) {
            showComponent(true)

            binding.apply {
                tvLostFoundDetailTitle.text = todo.title
                tvLostFoundDetailDate.text = "Diposting pada: ${todo.createdAt}"
                tvLostFoundDetailDesc.text = todo.description

                cbLostFoundDetailIsFinished.isChecked = todo.isCompleted == 1

                val statusText = if (todo.status.equals("found", ignoreCase = true)) {
                    // Jika status "found", maka gunakan warna hijau
                    highlightText("Found", Color.GREEN)
                } else {
                    // Jika status "lost", maka gunakan warna kuning
                    highlightText("Lost", Color.YELLOW)
                }
                // Menetapkan teks status yang sudah disorot ke TextView
                tvStatusDetail.text = statusText

                cbLostFoundDetailIsFinished.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.putLostFound(
                        todo.id,
                        todo.title,
                        todo.description,
                        todo.status,
                        isChecked
                    ).observeOnce {
                        when (it) {
                            is MyResult.Error -> {
                                if (isChecked) {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Gagal menyelesaikan todo: " + todo.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Gagal batal menyelesaikan todo: " + todo.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            is MyResult.Success -> {
                                if (isChecked) {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Item Lost and Found " + todo.title + "berhasil diselesaikan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Batal menyelesaikan item" + todo.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                if ((todo.isCompleted == 1) != isChecked) {
                                    isChanged = true
                                }
                            }

                            else -> {}
                        }
                    }
                }

                var isFavorite = false

                binding.ivLostFoundDetailActionFavorite.setOnClickListener {
                    isFavorite = !isFavorite
                    if (isFavorite) {
                        binding.ivLostFoundDetailActionFavorite.setImageResource(R.drawable.ic_favorite_24)
                        Toast.makeText(
                            this@LostFoundDetailActivity,
                            "Item berhasil ditambahkan ke favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        binding.ivLostFoundDetailActionFavorite.setImageResource(R.drawable.ic_favorite_border_24)
                        Toast.makeText(
                            this@LostFoundDetailActivity,
                            "Item berhasil dikeluarkan dari favorite",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                cbLostFoundDetailIsFinished.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.putLostFound(
                        todo.id,
                        todo.title,
                        todo.description,
                        todo.status,
                        isChecked
                    ).observeOnce {
                        when (it) {
                            is MyResult.Error -> {
                                if (isChecked) {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Gagal menyelesaikan todo: " + todo.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Gagal batal menyelesaikan todo: " + todo.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            is MyResult.Success -> {
                                if (isChecked) {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Item Lost and Found " + todo.title + "berhasil diselesaikan",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@LostFoundDetailActivity,
                                        "Batal menyelesaikan item" + todo.title,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                                if ((todo.isCompleted == 1) != isChecked) {
                                    isChanged = true
                                }
                            }

                            else -> {}
                        }
                    }
                }


                ivLostFoundDetailActionDelete.setOnClickListener {
                    val builder = AlertDialog.Builder(this@LostFoundDetailActivity)

                    builder.setTitle("Konfirmasi Hapus Item Lost & Found")
                        .setMessage("Anda yakin ingin menghapus Item ini?")

                    builder.setPositiveButton("Ya") { _, _ ->
                        observeDelete(todo.id)
                    }

                    builder.setNegativeButton("Tidak") { dialog, _ ->
                        dialog.dismiss() // Menutup dialog
                    }

                    val dialog = builder.create()
                    dialog.show()
                }

                ivLostFoundDetailActionEdit.setOnClickListener {
                    val lostFound = LostFound(
                        todo.id,
                        todo.title,
                        todo.description,
                        todo.status,
                        todo.isCompleted == 1,
                        todo.cover
                    )

                    val intent = Intent(
                        this@LostFoundDetailActivity,
                        LostFoundManageActivity::class.java
                    )
                    intent.putExtra(LostFoundManageActivity.KEY_IS_ADD, false)
                    intent.putExtra(LostFoundManageActivity.KEY_TODO, lostFound)
                    launcher.launch(intent)
                }
            }
        }else {
            // Tampilkan pesan atau lakukan tindakan lainnya jika objek todo null
            Toast.makeText(
                this@LostFoundDetailActivity,
                "Tidak ditemukan item yang dicari",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun highlightText(text: String, color: Int): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(ForegroundColorSpan(color), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannableString
    }

    private fun observeDelete(todoId: Int) {
        showComponent(false)
        showLoading(true)
        viewModel.delete(todoId).observeOnce {
            when (it) {
                is MyResult.Error -> {
                    showComponent(true)
                    showLoading(false)
                    Toast.makeText(
                        this@LostFoundDetailActivity,
                        "Gagal menghapus item: ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is MyResult.Success -> {
                    showLoading(false)

                    Toast.makeText(
                        this@LostFoundDetailActivity,
                        "Berhasil menghapus item",
                        Toast.LENGTH_SHORT
                    ).show()

                    val resultIntent = Intent()
                    resultIntent.putExtra(KEY_IS_CHANGED, true)
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }

                else -> {}
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbLostFoundDetail.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showComponent(status: Boolean) {
        binding.llLostFoundDetail.visibility =
            if (status) View.VISIBLE else View.GONE
    }

    companion object {
        const val KEY_TODO_ID = "todo_id"
        const val KEY_IS_CHANGED = "is_changed"
        const val RESULT_CODE = 1001
    }
}
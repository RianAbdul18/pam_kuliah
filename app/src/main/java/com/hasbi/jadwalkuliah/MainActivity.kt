package com.hasbi.jadwalkuliah

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hasbi.jadwalkuliah.adapter.JadwalAdapter
import com.hasbi.jadwalkuliah.database.DatabaseHelper
import com.hasbi.jadwalkuliah.model.Jadwal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var jadwalAdapter: JadwalAdapter
    private lateinit var db: DatabaseHelper
    private lateinit var btnTambah: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnTambah = findViewById(R.id.btnTambah)

        recyclerView.layoutManager = LinearLayoutManager(this)

        db = DatabaseHelper()

        val emptyList = mutableListOf<Jadwal>()
        jadwalAdapter = JadwalAdapter(emptyList, this) {
            loadJadwal()
        }
        recyclerView.adapter = jadwalAdapter

        loadJadwal()

        btnTambah.setOnClickListener {
            val intent = Intent(this, TambahJadwalActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        loadJadwal()
    }

    private fun loadJadwal() {
        db.getAllJadwal { jadwalList ->
            runOnUiThread {
                if (jadwalList.isNotEmpty()) {
                    jadwalAdapter.refreshData(jadwalList)
                } else {
                    Toast.makeText(this, "Tidak ada data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
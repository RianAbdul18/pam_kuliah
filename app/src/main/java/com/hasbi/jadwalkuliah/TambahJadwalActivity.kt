package com.hasbi.jadwalkuliah

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hasbi.jadwalkuliah.database.DatabaseHelper
import com.hasbi.jadwalkuliah.model.Jadwal
import com.google.android.material.textfield.TextInputEditText

class TambahJadwalActivity : AppCompatActivity() {

    private lateinit var etMataKuliah: TextInputEditText
    private lateinit var etDosen: TextInputEditText
    private lateinit var etHari: TextInputEditText
    private lateinit var etJam: TextInputEditText
    private lateinit var etRuangan: TextInputEditText
    private lateinit var etSks: TextInputEditText
    private lateinit var btnSimpan: Button
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_jadwal)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Tambah Jadwal"

        etMataKuliah = findViewById(R.id.etMataKuliah)
        etDosen = findViewById(R.id.etDosen)
        etHari = findViewById(R.id.etHari)
        etJam = findViewById(R.id.etJam)
        etRuangan = findViewById(R.id.etRuangan)
        etSks = findViewById(R.id.etSks)
        btnSimpan = findViewById(R.id.btnSimpan)

        db = DatabaseHelper()

        btnSimpan.setOnClickListener {
            simpanJadwal()
        }
    }

    private fun simpanJadwal() {
        val mataKuliah = etMataKuliah.text.toString().trim()
        val dosen = etDosen.text.toString().trim()
        val hari = etHari.text.toString().trim()
        val jam = etJam.text.toString().trim()
        val ruangan = etRuangan.text.toString().trim()
        val sks = etSks.text.toString().trim()

        if (mataKuliah.isEmpty()) {
            etMataKuliah.error = "Mata kuliah harus diisi"
            etMataKuliah.requestFocus()
            return
        }

        if (dosen.isEmpty()) {
            etDosen.error = "Nama dosen harus diisi"
            etDosen.requestFocus()
            return
        }

        if (hari.isEmpty()) {
            etHari.error = "Hari harus diisi"
            etHari.requestFocus()
            return
        }

        if (jam.isEmpty()) {
            etJam.error = "Jam harus diisi"
            etJam.requestFocus()
            return
        }

        if (sks.isEmpty()) {
            etSks.error = "SKS harus diisi"
            etSks.requestFocus()
            return
        }

        val jadwal = Jadwal(
            id = "",
            mata_kuliah = mataKuliah,
            dosen = dosen,
            hari = hari,
            jam = jam,
            ruangan = ruangan,
            sks = sks
        )

        db.insertJadwal(jadwal) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
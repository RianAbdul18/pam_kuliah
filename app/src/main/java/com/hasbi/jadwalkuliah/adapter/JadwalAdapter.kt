package com.hasbi.jadwalkuliah.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.hasbi.jadwalkuliah.EditJadwalActivity
import com.hasbi.jadwalkuliah.R
import com.hasbi.jadwalkuliah.database.DatabaseHelper
import com.hasbi.jadwalkuliah.model.Jadwal

class JadwalAdapter(
    private var jadwalList: MutableList<Jadwal>,
    private val context: Context,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<JadwalAdapter.JadwalViewHolder>() {

    private val db = DatabaseHelper()

    class JadwalViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMataKuliah: TextView = view.findViewById(R.id.tvMataKuliah)
        val tvDosen: TextView = view.findViewById(R.id.tvDosen)
        val tvHariJam: TextView = view.findViewById(R.id.tvHariJam)
        val tvRuangan: TextView = view.findViewById(R.id.tvRuangan)
        val tvSks: TextView = view.findViewById(R.id.tvSks)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JadwalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jadwal, parent, false)
        return JadwalViewHolder(view)
    }

    override fun onBindViewHolder(holder: JadwalViewHolder, position: Int) {
        val jadwal = jadwalList[position]

        holder.tvMataKuliah.text = jadwal.mata_kuliah
        holder.tvDosen.text = jadwal.dosen
        holder.tvHariJam.text = "${jadwal.hari}, ${jadwal.jam}"
        holder.tvRuangan.text = jadwal.ruangan
        holder.tvSks.text = "${jadwal.sks} SKS"

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, EditJadwalActivity::class.java)
            intent.putExtra("JADWAL_ID", jadwal.id)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            showDeleteConfirmation(jadwal, position)
        }
    }

    override fun getItemCount(): Int = jadwalList.size

    private fun showDeleteConfirmation(jadwal: Jadwal, position: Int) {
        AlertDialog.Builder(context)
            .setTitle("Hapus Jadwal")
            .setMessage("Yakin ingin menghapus ${jadwal.mata_kuliah}?")
            .setPositiveButton("Hapus") { dialog, _ ->
                deleteJadwal(jadwal.id, position)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteJadwal(id: String, position: Int) {
        db.deleteJadwal(id) { success ->
            (context as? Activity)?.runOnUiThread {
                if (success) {
                    jadwalList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, jadwalList.size)
                    Toast.makeText(context, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun refreshData(newData: List<Jadwal>) {
        jadwalList.clear()
        jadwalList.addAll(newData)
        notifyDataSetChanged()
    }
}
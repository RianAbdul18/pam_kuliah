package com.hasbi.jadwalkuliah.database

import android.util.Log
import com.hasbi.jadwalkuliah.model.Jadwal
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class DatabaseHelper {

    private val BASE_URL = "https://appocalypse.my.id/jadwal_api.php"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getAllJadwal(callback: (List<Jadwal>) -> Unit) {
        val jadwalList = mutableListOf<Jadwal>()
        val url = "$BASE_URL?proc=getdata"

        val request = Request.Builder().url(url).get().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("DatabaseHelper", "Error: ${e.message}")
                callback(emptyList())
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        try {
                            val jsonData = responseBody.string()
                            if (jsonData.isEmpty() || jsonData == "[]") {
                                callback(emptyList())
                                return
                            }

                            val jsonArray = JSONArray(jsonData)

                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val jadwal = Jadwal(
                                    id = jsonObject.optString("id", ""),
                                    mata_kuliah = jsonObject.optString("mata_kuliah", ""),
                                    dosen = jsonObject.optString("dosen", ""),
                                    hari = jsonObject.optString("hari", ""),
                                    jam = jsonObject.optString("jam", ""),
                                    ruangan = jsonObject.optString("ruangan", ""),
                                    sks = jsonObject.optString("sks", "0")
                                )
                                jadwalList.add(jadwal)
                            }
                            callback(jadwalList)
                        } catch (e: Exception) {
                            Log.e("DatabaseHelper", "JSON error: ${e.message}")
                            callback(emptyList())
                        }
                    } ?: callback(emptyList())
                } else {
                    callback(emptyList())
                }
            }
        })
    }

    fun getJadwalById(id: String, callback: (Jadwal?) -> Unit) {
        val url = "$BASE_URL?proc=getdata&id=$id"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.let { responseBody ->
                        try {
                            val jsonData = responseBody.string()
                            val jsonArray = JSONArray(jsonData)

                            if (jsonArray.length() > 0) {
                                val jsonObject = jsonArray.getJSONObject(0)
                                val jadwal = Jadwal(
                                    id = jsonObject.optString("id", ""),
                                    mata_kuliah = jsonObject.optString("mata_kuliah", ""),
                                    dosen = jsonObject.optString("dosen", ""),
                                    hari = jsonObject.optString("hari", ""),
                                    jam = jsonObject.optString("jam", ""),
                                    ruangan = jsonObject.optString("ruangan", ""),
                                    sks = jsonObject.optString("sks", "0")
                                )
                                callback(jadwal)
                            } else {
                                callback(null)
                            }
                        } catch (e: Exception) {
                            callback(null)
                        }
                    } ?: callback(null)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun insertJadwal(jadwal: Jadwal, callback: (Boolean) -> Unit) {
        try {
            val url = "$BASE_URL?proc=insert" +
                    "&mata_kuliah=${URLEncoder.encode(jadwal.mata_kuliah, "UTF-8")}" +
                    "&dosen=${URLEncoder.encode(jadwal.dosen, "UTF-8")}" +
                    "&hari=${URLEncoder.encode(jadwal.hari, "UTF-8")}" +
                    "&jam=${URLEncoder.encode(jadwal.jam, "UTF-8")}" +
                    "&ruangan=${URLEncoder.encode(jadwal.ruangan, "UTF-8")}" +
                    "&sks=${jadwal.sks}"

            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback(response.isSuccessful)
                }
            })
        } catch (e: Exception) {
            callback(false)
        }
    }

    fun updateJadwal(jadwal: Jadwal, callback: (Boolean) -> Unit) {
        try {
            val url = "$BASE_URL?proc=update" +
                    "&id=${jadwal.id}" +
                    "&mata_kuliah=${URLEncoder.encode(jadwal.mata_kuliah, "UTF-8")}" +
                    "&dosen=${URLEncoder.encode(jadwal.dosen, "UTF-8")}" +
                    "&hari=${URLEncoder.encode(jadwal.hari, "UTF-8")}" +
                    "&jam=${URLEncoder.encode(jadwal.jam, "UTF-8")}" +
                    "&ruangan=${URLEncoder.encode(jadwal.ruangan, "UTF-8")}" +
                    "&sks=${jadwal.sks}"

            val request = Request.Builder().url(url).build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback(false)
                }

                override fun onResponse(call: Call, response: Response) {
                    callback(response.isSuccessful)
                }
            })
        } catch (e: Exception) {
            callback(false)
        }
    }

    fun deleteJadwal(id: String, callback: (Boolean) -> Unit) {
        val url = "$BASE_URL?proc=delete&id=$id"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }
}
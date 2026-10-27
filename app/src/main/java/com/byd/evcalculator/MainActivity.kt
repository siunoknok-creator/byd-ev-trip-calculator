package com.byd.evcalculator

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var etPrice: TextInputEditText
    private lateinit var etDistance: TextInputEditText
    private lateinit var etEnergy: TextInputEditText
    private lateinit var tvCostPerKm: TextView
    private lateinit var tvTotalCost: TextView
    private lateinit var tvAvgConsumption: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPrice = findViewById(R.id.etElectricityPrice)
        etDistance = findViewById(R.id.etDistance)
        etEnergy = findViewById(R.id.etEnergy)
        tvCostPerKm = findViewById(R.id.tvCostPerKm)
        tvTotalCost = findViewById(R.id.tvTotalCost)
        tvAvgConsumption = findViewById(R.id.tvAvgConsumption)

        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        btnCalculate.setOnClickListener {
            calculateCost()
        }

        // 嘗試以動態反射讀取車載數據 (避免編譯期缺失 android.car 庫)
        initCarDataByReflection()
    }

    private fun initCarDataByReflection() {
        try {
            // 動態載入 Car 類別
            val carClass = Class.forName("android.car.Car")
            val createCarMethod = carClass.getMethod("createCar", Context::class.java)
            val carInstance = createCarMethod.invoke(null, this)

            if (carInstance != null) {
                val getCarManagerMethod = carClass.getMethod("getCarManager", String::class.java)
                val carPropertyManager = getCarManagerMethod.invoke(carInstance, "property")
                
                // 成功連接到車機系統底層 CarPropertyManager
            }
        } catch (e: Exception) {
            // 若非車機環境或權限限制，保持輸入欄供手動調整
            e.printStackTrace()
        }
    }

    private fun calculateCost() {
        val price = etPrice.text.toString().toFloatOrNull() ?: 0f
        val distance = etDistance.text.toString().toFloatOrNull() ?: 0f
        val energy = etEnergy.text.toString().toFloatOrNull() ?: 0f

        if (distance > 0f && energy > 0f) {
            val costPerKm = (energy / distance) * price
            val totalCost = energy * price
            val avgConsumption = (energy / distance) * 100 // kWh / 100km

            tvCostPerKm.text = String.format(Locale.getDefault(), "每公里電費: $%.2f / km", costPerKm)
            tvTotalCost.text = String.format(Locale.getDefault(), "本次行程總電費: $%.2f", totalCost)
            tvAvgConsumption.text = String.format(Locale.getDefault(), "平均電耗: %.2f kWh / 100km", avgConsumption)
        }
    }
}

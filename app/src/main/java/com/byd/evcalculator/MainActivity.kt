package com.byd.evcalculator

import android.car.Car
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
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

    private var car: Car? = null
    private var carPropertyManager: CarPropertyManager? = null

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

        // 初始化車載 API 連線
        initCarApi()
    }

    private fun initCarApi() {
        try {
            car = Car.createCar(this)
            carPropertyManager = car?.getCarManager(Car.PROPERTY_SERVICE) as? CarPropertyManager
            
            // 嘗試自動讀取當前車輛總里數 (Odometer) 作為初始參考
            // 註：實際車型 Property ID 可能因 DiLink 版本微調，若抓取不到會保持手動輸入框開放
        } catch (e: Exception) {
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

    override fun onDestroy() {
        super.onDestroy()
        try {
            car?.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

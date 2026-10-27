package com.byd.evcalculator

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
        etEnergy = findViewById(R.id.etEnergy) // 修正這裡的 ID 與 XML 對齊
        tvCostPerKm = findViewById(R.id.tvCostPerKm)
        tvTotalCost = findViewById(R.id.tvTotalCost)
        tvAvgConsumption = findViewById(R.id.tvAvgConsumption)

        val btnCalculate = findViewById<Button>(R.id.btnCalculate)
        btnCalculate.setOnClickListener {
            calculateCost()
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

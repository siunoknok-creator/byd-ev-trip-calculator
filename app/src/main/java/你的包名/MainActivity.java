package com.example.cartripcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// 假設這是車載 SDK 的相關匯入 (根據實際廠商 SDK 調整)
// import cc.omycar.magiccore.IMagicCoreInterface;

public class MainActivity extends AppCompatActivity {

    private EditText etPrice;
    private Button btnStartTrip, btnEndTrip;
    private TextView tvResult;

    // 車載介面物件
    private Object mMagicCore; // 實際開發時請換成 IMagicCoreInterface
    private static final double BATTERY_TOTAL_CAPACITY = 60.0; // 假設電池總容量 60 kWh

    // 紀錄變數
    private double startOdometer = 0.0;
    private int startSoC = 0;
    private boolean isTripStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etPrice = findViewById(R.id.et_price);
        btnStartTrip = findViewById(R.id.btn_start_trip);
        btnEndTrip = findViewById(R.id.btn_end_trip);
        tvResult = findViewById(R.id.tv_result);

        // 初始化車載連線服務
        initCarService();

        // 1. 開始行程按鈕事件
        btnStartTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 從車載介面讀取數據 (示範碼，需依實際 SDK 方法修改)
                    // startOdometer = ((IMagicCoreInterface) mMagicCore).getOdometer();
                    // startSoC = ((IMagicCoreInterface) mMagicCore).getBatterySoC();
                    
                    // 模擬測試數據 (實際開發時請解註解上面兩行)
                    startOdometer = 12500.5; 
                    startSoC = 90;

                    isTripStarted = true;
                    tvResult.setText(String.format("行程已開始\n起點里數：%.1f km\n起點電量：%d%%", startOdometer, startSoC));
                    Toast.makeText(MainActivity.this, "已成功記錄起點數據", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "讀取車載數據失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 2. 結束行程按鈕事件
        btnEndTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTripStarted) {
                    Toast.makeText(MainActivity.this, "請先點擊「開始行程」！", Toast.LENGTH_SHORT).show();
                    return;
                }

                String priceStr = etPrice.getText().toString().trim();
                if (priceStr.isEmpty()) {
                    Toast.makeText(MainActivity.this, "請輸入每度電價格！", Toast.LENGTH_SHORT).show();
                    return;
                }

                double pricePerKWh = Double.parseDouble(priceStr);

                try {
                    // 讀取結束時的數據 (示範碼)
                    // double endOdometer = ((IMagicCoreInterface) mMagicCore).getOdometer();
                    // int endSoC = ((IMagicCoreInterface) mMagicCore).getBatterySoC();

                    // 模擬測試數據
                    double endOdometer = 12535.5;
                    int endSoC = 75;

                    // 計算數據
                    double tripDistance = endOdometer - startOdometer;
                    int consumedSoCPercent = startSoC - endSoC;
                    if (consumedSoCPercent < 0) consumedSoCPercent = 0;

                    double consumedEnergyKWh = (consumedSoCPercent / 100.0) * BATTERY_TOTAL_CAPACITY;
                    double totalCost = consumedEnergyKWh * pricePerKWh;

                    // 顯示結果
                    String resultText = String.format(
                            "--- 行程結算報告 ---\n" +
                            "行駛里數：%.1f km\n" +
                            "消耗電量：%.2f kWh (SoC 消耗 %d%%)\n" +
                            "每度電價：$%.2f\n" +
                            "預估充電費：$%.2f",
                            tripDistance, consumedEnergyKWh, consumedSoCPercent, pricePerKWh, totalCost
                    );
                    tvResult.setText(resultText);
                    isTripStarted = false; // 重設狀態

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "計算失敗：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initCarService() {
        // 這裡填寫連接車載 MagicCore 服務的程式碼
        // 例如：mMagicCore = MagicCoreManager.get().getService();
    }
}

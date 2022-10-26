package orhan.uckulac.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import orhan.uckulac.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val startBtn: FrameLayout = binding.flStart
        startBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "First commit.", Toast.LENGTH_LONG).show()
        }

    }
}
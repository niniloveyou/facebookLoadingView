package deadline.child

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.setOnClickListener { toast("我来了") }
        textView.text = "fffff"

        var list = listOf<String>("cat", "dog", "waf", "chiken", "1", "2", "3", "4", "5", "6")
        list = list.filter { it.length > 2 }
        for (index in 0..3 step 2){
            Log.e("=============", "$index")
        }

        var index = 0
        while (index in list.indices) {
            Log.e("--------------", list[index])
            index++
        }

        var map : HashMap<String, String> = HashMap();
        map.put("df", "fd")
        for ((k, v) in map){
            Log.e("--------------", "$k -> $v")
        }
    }
}

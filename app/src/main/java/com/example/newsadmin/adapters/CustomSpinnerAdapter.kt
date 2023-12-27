import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.newsadmin.R

class CustomSpinnerAdapter(context: Context, private val categories: Array<String>) :
    ArrayAdapter<String>(context, R.layout.item_custom_spinner, categories) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_custom_spinner, parent, false)

        val categoryTextView: TextView = view.findViewById(R.id.customCategoryTextView)
        categoryTextView.text = categories[position]

        return view
    }
}
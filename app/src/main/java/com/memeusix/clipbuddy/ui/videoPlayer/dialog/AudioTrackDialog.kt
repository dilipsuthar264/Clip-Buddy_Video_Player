package com.memeusix.clipbuddy.ui.videoPlayer.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import com.memeusix.clipbuddy.R

class AudioTrackDialog(
    val context: Context,
    val selectedItem: String?,
    val list: List<Tracks.Group>,
    val title: String,
    val onItemSelected: (Tracks.Group?) -> Unit // Callback for selected item
) {

    private val builder = AlertDialog.Builder(context)
    private val adapter =
        object : ArrayAdapter<Tracks.Group>(context, android.R.layout.simple_list_item_1, list) {
            @OptIn(UnstableApi::class)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val textView = view.findViewById<TextView>(android.R.id.text1)
                val group = getItem(position)
                textView.text = group?.mediaTrackGroup?.getFormat(0)?.label ?: "None"
                if (selectedItem == group?.mediaTrackGroup?.id) {
                    view.setBackgroundColor(context.getColor(R.color.colorBackgroundSecondary))
                } else {
                    view.setBackgroundColor(0)
                }
                return view
            }
        }

    init {
        builder.setTitle(title) // Set title using the provided title parameter
        builder.setAdapter(adapter) { _, position ->
            val selected = list[position]
            onItemSelected(selected)
        }
        builder.setNeutralButton("Disable"){_,_->
            onItemSelected(null)
        }
        builder.setCancelable(true)

        // Cancel button
        builder.setNegativeButton("Cancel", null)

        // Show the dialog
        builder.show()
    }
}

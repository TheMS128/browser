package de.baumann.browser.view;

import static android.view.View.GONE;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

import de.baumann.browser.R;

public class AdapterSettingsMenu extends RecyclerView.Adapter<AdapterSettingsMenu.ViewHolder> {

    private final List<MenuItem> itemList;

    public AdapterSettingsMenu(List<MenuItem> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = itemList.get(position);
        holder.textView.setText(item.getTitle());
        holder.imageView.setImageResource(item.getIconResId());
        holder.checkBox.setChecked(item.isSelected());

        if (item.isSelected()) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E3F2FD")); // Sanftes Blau
        } else {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(v -> {
            item.setSelected(!item.isSelected());
            notifyItemChanged(holder.getAbsoluteAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(itemList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;
        final ImageView imageView;
        final CheckBox checkBox;
        final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.titleView);
            imageView = itemView.findViewById(R.id.item_icon);
            checkBox = itemView.findViewById(R.id.item_checkBox);
            cardView = itemView.findViewById(R.id.item_cardView);

            TextView tv = itemView.findViewById(R.id.dateView);
            tv.setVisibility(GONE);
        }
    }
}

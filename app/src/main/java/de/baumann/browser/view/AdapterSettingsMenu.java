package de.baumann.browser.view;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_menu, parent, false);
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
            notifyItemChanged(holder.getAdapterPosition());
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
        TextView textView;
        ImageView imageView;
        CheckBox checkBox;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.itemTitle);
            imageView = itemView.findViewById(R.id.itemIcon);
            checkBox = itemView.findViewById(R.id.itemCheckBox);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}

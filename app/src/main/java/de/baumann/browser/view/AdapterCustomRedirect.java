package de.baumann.browser.view;

import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import de.baumann.browser.R;
import de.baumann.browser.objects.CustomRedirect;
import de.baumann.browser.objects.CustomRedirectsHelper;
import de.baumann.browser.unit.HelperUnit;

public class AdapterCustomRedirect extends RecyclerView.Adapter<RedirectsViewHolder> {
    final private ArrayList<CustomRedirect> redirects;
    private final Context context;

    public AdapterCustomRedirect(ArrayList<CustomRedirect> redirects, Context context) {
        super();
        this.redirects = redirects;
        this.context = context;
    }

    @NonNull
    @Override
    public RedirectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new RedirectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedirectsViewHolder holder, int position) {
        CustomRedirect current = redirects.get(position);
        TextView source = holder.itemView.findViewById(R.id.titleView);
        TextView target = holder.itemView.findViewById(R.id.dateView);
        ImageView remove = holder.itemView.findViewById(R.id.iconView);
        ImageView edit = holder.itemView.findViewById(R.id.faviconView);
        edit.setVisibility(View.VISIBLE);

        remove.setVisibility(VISIBLE);
        CardView cardView = holder.itemView.findViewById(R.id.cardView);
        //cardView.setVisibility(View.GONE);
        source.setText(current.getSource());
        target.setText(current.getTarget());

        remove.setOnClickListener((iV) -> {
            MaterialAlertDialogBuilder builderSubMenu = new MaterialAlertDialogBuilder(context);
            builderSubMenu.setTitle(R.string.menu_delete);
            builderSubMenu.setMessage(R.string.hint_database);
            builderSubMenu.setIcon(R.drawable.icon_delete);
            builderSubMenu.setPositiveButton(R.string.app_ok, (dialog2, whichButton) -> {
                redirects.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                try {
                    CustomRedirectsHelper.saveRedirects(redirects);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            builderSubMenu.setNegativeButton(R.string.app_cancel, (dialog2, whichButton) -> builderSubMenu.setCancelable(true));
            Dialog dialogSubMenu = builderSubMenu.create();
            dialogSubMenu.show();
            HelperUnit.setupDialog(context, dialogSubMenu);
        });

        edit.setOnClickListener((iV) -> {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
            View dialogView = View.inflate(context, R.layout.create_new_redirect, null);
            TextInputEditText source2 = dialogView.findViewById(R.id.source);
            TextInputEditText target2 = dialogView.findViewById(R.id.target);
            source2.setText(source.getText());
            target2.setText(target.getText());

            builder.setTitle(R.string.privacy_redirect);
            builder.setIcon(R.drawable.icon_redirect);
            builder.setNegativeButton(R.string.app_cancel, null);
            builder.setPositiveButton(R.string.app_ok, ((dialogInterface, i) -> {
                String sourceText = Objects.requireNonNull(source2.getText()).toString();
                String targetText = Objects.requireNonNull(target2.getText()).toString();
                if (targetText.isEmpty() && sourceText.isEmpty()) {
                    NinjaToast.show(context, R.string.toast_input_empty);
                    return;
                }
                this.addRedirect(new CustomRedirect(sourceText, targetText));
                try {
                    CustomRedirectsHelper.saveRedirects(this.getRedirects());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }));
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();
            dialog.show();
            HelperUnit.setupDialog(context, dialog);
        });

    }

    @Override
    public int getItemCount() {
        return redirects.size();
    }

    public ArrayList<CustomRedirect> getRedirects() {
        return redirects;
    }

    public void addRedirect(CustomRedirect redirect) {
        redirects.add(redirect);
        notifyItemInserted(getItemCount() - 1);
    }
}


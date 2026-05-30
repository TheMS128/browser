package de.baumann.browser.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.baumann.browser.R;
import de.baumann.browser.unit.BrowserUnit;
import de.baumann.browser.unit.HelperUnit;
import de.baumann.browser.view.AdapterSettingsMenu;
import de.baumann.browser.view.MenuItem;

public class Settings_Menu extends AppCompatActivity {

    private List<MenuItem> masterList;
    private AdapterSettingsMenu adapter;
    private SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "AppPreferences";
    public static final String KEY_LIST = "SettingsList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        HelperUnit.initTheme(this);
        setContentView(R.layout.activity_settings_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadList();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSettings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AdapterSettingsMenu(masterList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {

                // Hier getBindingAdapterPosition() nutzen statt getAdapterPosition()
                int fromPosition = viewHolder.getBindingAdapterPosition();
                int toPosition = target.getBindingAdapterPosition();
                // Sicherstellen, dass beide Positionen gültig sind
                if (fromPosition != RecyclerView.NO_POSITION && toPosition != RecyclerView.NO_POSITION) {
                    adapter.onItemMove(fromPosition, toPosition);
                    saveList(); // Speichert die neue Reihenfolge sofort
                    return true;
                }
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {}
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }
    private void saveList() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LIST, new Gson().toJson(masterList));
        editor.apply();
    }
    private void loadList() {
        String json = sharedPreferences.getString(KEY_LIST, null);
        Type type = new TypeToken<ArrayList<MenuItem>>() {}.getType();
        masterList = new Gson().fromJson(json, type);

        if (masterList == null) {
            masterList = new ArrayList<>();

            masterList.add(new MenuItem(getString(R.string.menu_openFav), R.drawable.icon_fav, true));
            masterList.add(new MenuItem(getString(R.string.main_menu_new_tabOpen), R.drawable.icon_tab_plus, true));
            masterList.add(new MenuItem(getString(R.string.menu_reload), R.drawable.icon_refresh, true));
            masterList.add(new MenuItem(getString(R.string.menu_closeTab), R.drawable.icon_tab_remove, true));
            masterList.add(new MenuItem(getString(R.string.menu_quit), R.drawable.icon_close, true));

            masterList.add(new MenuItem(getString(R.string.menu_save_bookmark), R.drawable.icon_bookmark, true));
            masterList.add(new MenuItem(getString(R.string.menu_save_pdf), R.drawable.icon_file, true));
            masterList.add(new MenuItem(getString(R.string.menu_save_as), R.drawable.icon_menu_save, true));
            masterList.add(new MenuItem(getString(R.string.menu_fav), R.drawable.icon_fav_plus, true));

            masterList.add(new MenuItem(getString(R.string.menu_share_link), R.drawable.icon_link, true));
            masterList.add(new MenuItem(getString(R.string.dialog_postOnWebsite), R.drawable.icon_post, true));
            masterList.add(new MenuItem(getString(R.string.menu_shareClipboard), R.drawable.icon_clipboard, true));
            masterList.add(new MenuItem(getString(R.string.menu_shareOpenWith), R.drawable.icon_share_open_with, true));
            masterList.add(new MenuItem(getString(R.string.menu_sc), R.drawable.icon_home, true));

            masterList.add(new MenuItem(getString(R.string.menu_other_searchSite), R.drawable.icon_search_site, true));
            masterList.add(new MenuItem(getString(R.string.menu_download), R.drawable.icon_download, true));
            masterList.add(new MenuItem(getString(R.string.setting_label), R.drawable.icon_settings, true));
            masterList.add(new MenuItem(getString(R.string.menu_restart), R.drawable.icon_restart, true));masterList.add(new MenuItem(getString((R.string.app_help)), R.drawable.icon_help, true));

            masterList.add(new MenuItem(getString(R.string.main_menu_new_tab), R.drawable.icon_tab_background, true));
            masterList.add(new MenuItem(getString(R.string.menu_delete), R.drawable.icon_delete, true));
            masterList.add(new MenuItem(getString(R.string.menu_delete), R.drawable.icon_delete_alt, true));
            masterList.add(new MenuItem(getString(R.string.menu_edit), R.drawable.icon_edit, true));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        if (menuItem.getItemId() == R.id.menu_help) {
            Uri webpage = Uri.parse("https://codeberg.org/Gaukler_Faun/FOSS_Browser/wiki/Gestures");
            BrowserUnit.intentURL(this, webpage);
        }
        return true;
    }

    @Override
    public void finish() {
        saveList();
        super.finish();
    }
}
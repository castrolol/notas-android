package com.castrolol.hidra.notas;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SearchView;
import android.view.ContextMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.castrolol.hidra.notas.data.NotaShareService;
import com.castrolol.hidra.notas.notas.Nota;
import com.castrolol.hidra.notas.notas.NotaListItemAdapter;
import com.castrolol.hidra.notas.notas.NotaRepository;
import com.castrolol.hidra.notas.ui.NotaColorizer;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int DELETE = 1;
    private final int EDITAR = 0;
    private final int COMPARTILHAR = 2;

    NotaRepository repository;
    NotaListItemAdapter adapter;
    NotaShareService shareService;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, EditarNotaActivity.class);
                intent.putExtra("bundle", "value");
                startActivity(intent);

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initalizeUI();

    }

    private void initalizeUI() {
        repository = NotaRepository.getInstance(this);
        adapter = new NotaListItemAdapter(this, repository.getNotas());
        shareService = new NotaShareService(this);

        repository.keepSyncWith(adapter);

        repository.refresh();


        adapter.setRequestEditarNota(new NotaListItemAdapter.RequestEditarNota() {
            @Override
            public void editarNota(Nota n) {
                abrirEditarNota(n);
            }

        });

        ListView notasListView = (ListView) findViewById(R.id.lista_notas);
        notasListView.setAdapter(adapter);
        registerForContextMenu(notasListView);
    }

    private void abrirOpcoesNota(Nota n) {
        openOptionsMenu();
    }

    private void abrirEditarNota(Nota n) {

        searchView.clearFocus();
        searchView.setQuery("", false);
        searchView.setSelected(false);
        (menu.findItem(R.id.menu_search)).collapseActionView();

        Intent intent = new Intent(MainActivity.this, EditarNotaActivity.class);
        intent.putExtra("notaId", n.getId().toString());
        startActivity(intent);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo adapterMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Nota nota = adapter.getItem(adapterMenuInfo.position);

        menu.setHeaderTitle("Opções");
        menu.add(Menu.NONE, COMPARTILHAR, Menu.NONE, "Compartilhar...").setIcon(R.drawable.ic_menu_edit);
        menu.add(Menu.NONE, EDITAR, Menu.NONE, "Editar").setIcon(R.drawable.ic_menu_edit);
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Excluir").setIcon(R.drawable.ic_menu_remove);

    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Nota nota = adapter.getItem((int) info.id);
        switch (item.getItemId()) {
            case EDITAR:
                abrirEditarNota(nota);
                break;
            case COMPARTILHAR:
                shareService.share(nota);
                break;
            case DELETE:
                removerNota(nota);
                break;
        }

        return false;
    }

    private void removerNota(final Nota nota) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Remover Nota '" + nota.getTitulo() + "'");
        builder.setMessage("Deseja realmente remover esta nota?");
        builder.setPositiveButton("Remover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                repository.remove(nota);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    Menu menu;
    SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default
        searchView.setQueryHint("Buscar uma nota...");

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {

                    fab.hide();
                } else {
                    fab.show();
                }

            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText == null || "".equals(newText)) {
                    repository.refresh();
                    return false;
                }

                repository.filter(newText);

                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.todas_cores:
                filtrarCor(-1, null);
                return true;
            case R.id.color_blue:
                filtrarCor(NotaColorizer.BLUE, item);
                return true;
            case R.id.color_green:
                filtrarCor(NotaColorizer.GREEN, item);
                return true;
            case R.id.color_purple:
                filtrarCor(NotaColorizer.PURPLE, item);
                return true;
            case R.id.color_red:
                filtrarCor(NotaColorizer.RED, item);
                return true;
            case R.id.color_white:
                filtrarCor(NotaColorizer.WHITE, item);
                return true;
            case R.id.color_yellow:
                filtrarCor(NotaColorizer.YELLOW, item);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void filtrarCor(int color, MenuItem itemN) {

        MenuItem item = menu.findItem(R.id.fff);
        if (color < 0) {
            item.setIcon(R.drawable.ic_filter);
        } else {
            item.setIcon(itemN.getIcon());
        }

        repository.filter(color);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_remove_all) {

            confirmarRemoverTodos();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void confirmarRemoverTodos() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Remover Todas as Notas");
        builder.setMessage("Deseja realmente remover todas as notas? esse processo é irreversivel");
        builder.setPositiveButton("Remover Todos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                repository.clear();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }
}

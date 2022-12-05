package algonquin.cst2335.ticketmaster;
import algonquin.cst2335.ticketmaster.MainActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.navigation.NavigationView;

import algonquin.cst2335.ticketmaster.ui.main.TicketMasterActivity;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_navigation_drawer, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId()) {
            case R.id.event_search:
                startActivity(new Intent(this, TicketMasterActivity.class));
                break;
            case R.id.movie_information:
                //startActivity(new Intent(this, MovieInfoActivity.class));
                break;
            case R.id.pexels:
                //startActivity(new Intent(this, PexelsActivity.class));
                break;
            case R.id.soccer_highlights:
                //startActivity(new Intent(this, SoccerHighlightsActivity.class));
                break;
            case R.id.homepage:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }

        return false;
    }

    protected void setActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }


}
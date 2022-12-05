package algonquin.cst2335.ticketmaster.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import algonquin.cst2335.ticketmaster.R;
import algonquin.cst2335.ticketmaster.data.Event;
import algonquin.cst2335.ticketmaster.data.EventDAO;
import algonquin.cst2335.ticketmaster.data.EventDatabase;
import algonquin.cst2335.ticketmaster.databinding.ActivityTicketMasterBinding;
import algonquin.cst2335.ticketmaster.databinding.EventBinding;
import algonquin.cst2335.ticketmaster.ui.main.EventDetailsFragment;
import algonquin.cst2335.ticketmaster.ui.main.EventViewModel;

public class TicketMasterActivity extends AppCompatActivity {

    private ActivityTicketMasterBinding binding;
    private EventViewModel eventModel;
    private RecyclerView.Adapter<MyRowHolder> myAdaptor;
    private ArrayList<Event> eventsList = new ArrayList<>();
    protected RequestQueue queue = null;
    protected String cityName;
    protected int radius;
    EventDetailsFragment eventFragment;
    EventDatabase db;
    EventDAO eventDAO;
    String lastSearchCity;
    int lastSearchRadius;
    String apiPrefix;
    String api;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketMasterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.ticket_master_name));
        actionBar.setDisplayHomeAsUpEnabled(true);



        db = Room.databaseBuilder(getApplicationContext(), EventDatabase.class, "EventDatabase").build();
        eventDAO = db.eventDAO();

        eventModel = new ViewModelProvider(this).get(EventViewModel.class);
        //myAdaptor.notifyItemInserted(eventsList.size() - 1);
        queue = Volley.newRequestQueue(this);

        binding.searchButton.setOnClickListener(click -> {
            String cityInput = binding.citySearch.getText().toString();
            String radiusInput = binding.radiusSearch.getText().toString();
            if (!cityInput.equals("") && !radiusInput.equals("") && !radiusInput.equals("0")) {
                cityName = cityInput;
                radius = Integer.parseInt(radiusInput);
                search(cityName, radius);
            } else if (cityInput.equals("") ) {
                //radius = Integer.parseInt(radiusInput);
                Toast.makeText(this, R.string.invalid_city, Toast.LENGTH_SHORT).show();
            } else if (radiusInput.equals("")) {
                //cityName = cityInput;
                Toast.makeText(this, R.string.no_radius, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.zero_radius, Toast.LENGTH_SHORT).show();
            }

//            try {
//                radius = Integer.parseInt(binding.radiusSearch.getText().toString());
//            } catch (NumberFormatException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Please enter a number for radius!", Toast.LENGTH_SHORT).show();
//            }
            if (!binding.radiusSearch.getText().toString().equals("")) {
                radius = Integer.parseInt(binding.radiusSearch.getText().toString());
            } else {
                radius = 0;
            }


//            eventsList.clear();
//            cityName = binding.citySearch.getText().toString();
//            radius = Integer.parseInt(binding.radiusSearch.getText().toString());
//            if (!cityName.equals("") && radius != 0) {
//                try {
//                    api = apiPrefix + "&city=" + URLEncoder.encode(cityName, "UTF-8") + "&radius=" + radius;
//                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null, response -> {
//                        try {
//                            JSONArray events = response.getJSONObject("_embedded").getJSONArray("events");
//
//                            for (int i = 0; i < events.length(); i++) {
//                                JSONObject event = events.getJSONObject(i);
//                                String eventName = event.getString("name");
//                                String eventId = event.getString("id");
//                                String buyUrl = event.getString("url");
//                                String imgUrl = event.getJSONArray("images").getJSONObject(3).getString("url");
//                                String eventDate = event.getJSONObject("dates").getJSONObject("start").getString("dateTime");
//                                String minPrice = event.getJSONArray("priceRanges").getJSONObject(0).getString("min");
//                                String maxPrice = event.getJSONArray("priceRanges").getJSONObject(0).getString("max");
//                                String currency = event.getJSONArray("priceRanges").getJSONObject(0).getString("currency");
//                                String venue = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
//                                String postalCode = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("postalCode");
//                                String city = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name");
//                                String state = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("stateCode");
//                                String country = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("country").getString("name");
//                                String address = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1");
//                                Event eventDTO = new Event();
//                                eventDTO.setId(eventId);
//                                eventDTO.setName(eventName);
//                                eventDTO.setBuyUrl(buyUrl);
//                                eventDTO.setImgUrl(imgUrl);
//                                eventDTO.setDate(eventDate);
//                                eventDTO.setMinPrice(minPrice);
//                                eventDTO.setMaxPrice(maxPrice);
//                                eventDTO.setCurrency(currency);
//                                eventDTO.setVenue(venue);
//                                eventDTO.setPostalCode(postalCode);
//                                eventDTO.setCity(city);
//                                eventDTO.setState(state);
//                                eventDTO.setCountry(country);
//                                eventDTO.setAddress(address);
//                                eventDTO.setSaved(false);
//                                eventsList.add(eventDTO);
//                            }
//                            myAdaptor.notifyDataSetChanged();
//                            //myAdaptor.notifyItemRangeInserted(0,eventsList.size());
//                            eventModel.events.setValue(eventsList);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    },  (error) -> {});
//
//                    queue.add(request);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                //TODO: add message for not enter city or radius
//            }


        });

        myAdaptor = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                EventBinding eventBinding = EventBinding.inflate(getLayoutInflater(),parent, false);
                return new MyRowHolder((eventBinding.getRoot()));
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                holder.eventDateText.setText("");
                holder.eventNameText.setText("");
                Event obj = eventsList.get(position);
                holder.eventDateText.setText(obj.getDate());
                holder.eventNameText.setText(obj.getName());
            }

            @Override
            public int getItemCount() {
                return eventsList.size();
            }
        };

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            runOnUiThread(() -> binding.eventsRecyclerView.setAdapter(myAdaptor));
        });

        eventModel.selectedEvent.observe(this, newEvent -> {
            eventFragment = new EventDetailsFragment(newEvent);
            hideKeyboard(binding.getRoot());
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack("")
                    .replace(R.id.fragmentLocation, eventFragment)
                    .commit();
        });
        binding.eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.citySearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        binding.radiusSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        //toronsuper.onDestroy();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("apiPrefix", apiPrefix);
        editor.putString("city", cityName);
        editor.putInt("radius", radius);
        editor.apply();
        //finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        apiPrefix = prefs.getString("apiPrefix", "https://app.ticketmaster.com/discovery/v2/events.json?apikey=P92vWxEOv7rnD5pM2xn6XjbVzGP8fvM4");
        cityName = prefs.getString("city", "");
        radius = prefs.getInt("radius", 0);
        search(cityName, radius);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"NotifyDataSetChanged", "NonConstantResourceId", "SetTextI18n"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.favoriteList:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0 ) {
                    super.onBackPressed();
                }
                binding.resultText.setText(R.string.show_favorite);
                Executor thread = Executors.newSingleThreadExecutor();
                AtomicReference<List<Event>> events = new AtomicReference<>();
                thread.execute(() -> {
                    events.set(eventDAO.getAllEvent());
                    if( events.get().size() != eventsList.size()) {
                        eventsList.clear();
                        eventsList.addAll(events.get());

                        runOnUiThread(() -> {
                            myAdaptor.notifyDataSetChanged();
                            eventModel.events.setValue(eventsList);
                        });

                    }
                });

                //myAdaptor.notifyItemRangeInserted(0, eventsList.size());

                break;
        }

        return true;


    }
//    @Override
//    public void onBackPressed() {
//        if (getFragmentManager().getBackStackEntryCount() == 0) {
//            this.finish();
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void search(String cityName, int radius) {
        eventsList.clear();
//        cityName = binding.citySearch.getText().toString();
//        radius = Integer.parseInt(binding.radiusSearch.getText().toString());
//        if (!cityName.equals("") && radius != 0) {
            try {
                api = apiPrefix + "&city=" + URLEncoder.encode(cityName, "UTF-8") + "&radius=" + radius;
                @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"}) JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, api, null, response -> {
                    try {
                        JSONArray events = response.getJSONObject("_embedded").getJSONArray("events");

                        for (int i = 0; i < events.length(); i++) {
                            JSONObject event = events.getJSONObject(i);
                            String eventName = event.getString("name");
                            String eventId = event.getString("id");
                            String buyUrl = event.getString("url");
                            String imgUrl = event.getJSONArray("images").getJSONObject(3).getString("url");
                            String eventDate = "";
                            if (event.getJSONObject("dates").getJSONObject("start").getString("noSpecificTime").equals("false")){
                                eventDate = event.getJSONObject("dates").getJSONObject("start").getString("dateTime");
                            } else {
                                eventDate = event.getJSONObject("dates").getJSONObject("start").getString("localDate");
                                String timezone = event.getJSONObject("dates").getString("timezone");
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                format.setTimeZone(TimeZone.getTimeZone(timezone));
                                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(eventDate);
                                eventDate = format.format(date);
                                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                                newFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                                //format.setTimeZone(TimeZone.getTimeZone("GMT"));
                                eventDate = newFormat.format(format.parse(eventDate));
                            }
                            String minPrice;
                            String maxPrice;
                            String currency;
                            if (event.has("priceRanges")) {
                                minPrice = event.getJSONArray("priceRanges").getJSONObject(0).getString("min");
                                maxPrice = event.getJSONArray("priceRanges").getJSONObject(0).getString("max") ;
                                currency = event.getJSONArray("priceRanges").getJSONObject(0).getString("currency");
                            } else {
                                minPrice = "";
                                maxPrice = "";
                                currency = "";
                            }
//                            String minPrice = event.has("priceRanges") ? event.getJSONArray("priceRanges").getJSONObject(0).getString("min") : "null";
//                            String maxPrice = event.has("priceRanges") ? event.getJSONArray("priceRanges").getJSONObject(0).getString("max") : "null";
//                            String currency = event.getJSONArray("priceRanges").getJSONObject(0).getString("currency");
                            String venue = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                            String postalCode = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("postalCode");
                            String city = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name");
                            String state = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("stateCode");
                            String country = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("country").getString("name");
                            String address = event.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1");
                            Event eventDTO = new Event();
                            eventDTO.setId(eventId);
                            eventDTO.setName(eventName);
                            eventDTO.setBuyUrl(buyUrl);
                            eventDTO.setImgUrl(imgUrl);
                            eventDTO.setDate(eventDate);
                            eventDTO.setMinPrice(minPrice);
                            eventDTO.setMaxPrice(maxPrice);
                            eventDTO.setCurrency(currency);
                            eventDTO.setVenue(venue);
                            eventDTO.setPostalCode(postalCode);
                            eventDTO.setCity(city);
                            eventDTO.setState(state);
                            eventDTO.setCountry(country);
                            eventDTO.setAddress(address);
                            eventDTO.setSaved(false);
                            eventsList.add(eventDTO);
                        }
                        myAdaptor.notifyDataSetChanged();
                        //myAdaptor.notifyItemRangeInserted(0,eventsList.size());
                        eventModel.events.setValue(eventsList);
                        binding.resultText.setText(String.format(getResources().getString(R.string.show_result),cityName.toUpperCase(), radius));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        binding.resultText.setText(String.format(getResources().getString(R.string.no_result),cityName.toUpperCase(), radius));
                        eventsList.clear();
                        myAdaptor.notifyDataSetChanged();
                        eventModel.events.setValue(null);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                },  (error) -> {});

                queue.add(request);
            } catch (Exception e) {
                e.printStackTrace();
            }
//        } else if (radius == 0) {
//            Toast.makeText(this, "Radius has to be greater than 0!", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            //TODO: add message for not enter city or radius
//            Toast.makeText(this, "Please enter a city or a radius", Toast.LENGTH_SHORT).show();
//        }
    }

    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView eventDateText;
        TextView eventNameText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(click -> {
                int position = getAbsoluteAdapterPosition();
                Event selected = eventsList.get(position);
                eventModel.selectedEvent.postValue(selected);
            });

            eventDateText = itemView.findViewById(R.id.datePreview);
            eventNameText = itemView.findViewById(R.id.namePreview);
        }

    }

}
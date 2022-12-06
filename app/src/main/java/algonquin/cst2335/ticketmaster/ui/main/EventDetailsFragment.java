package algonquin.cst2335.ticketmaster.ui.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.ticketmaster.R;
import algonquin.cst2335.ticketmaster.data.Event;
import algonquin.cst2335.ticketmaster.data.EventDAO;
import algonquin.cst2335.ticketmaster.data.EventDatabase;
import algonquin.cst2335.ticketmaster.databinding.EventDetailsBinding;

public class EventDetailsFragment extends Fragment {

    Event selected;
    RequestQueue queue = null;
    ImageRequest imgReq= null;
    Bitmap image;

    public EventDetailsFragment(Event event) {
        selected = event;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventDetailsBinding binding = EventDetailsBinding.inflate(inflater);

        String imgUrl = selected.getImgUrl();
        binding.eventName.setText(selected.getName());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat newFormat = new SimpleDateFormat("h:mm a EEE    MMM dd, yyyy");
        newFormat.setTimeZone(TimeZone.getDefault());
        try {
            binding.eventDate.setText(newFormat.format(format.parse(selected.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.eventVenue.setText(selected.getVenue());
        binding.eventAddress.setText(selected.getAddress() + ", " + selected.getCity() + ", " + selected.getState() + ", " + selected.getCountry() + " " + selected.getPostalCode());
        binding.ticketPrice.setText(selected.getCurrency() + " " + selected.getMinPrice() + " - " + selected.getMaxPrice() );

        binding.buyTicketButton.setOnClickListener(click -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selected.getBuyUrl()));
            startActivity(browserIntent);
        });

        if (selected.isSaved()) {
            image = BitmapFactory.decodeFile(getActivity().getFilesDir() +"/" + selected.getId() + ".png");
            binding.eventImg.setImageBitmap(image);
            binding.saveButton.setText(R.string.remove);
            binding.saveButton.setOnClickListener(click -> {
                EventDatabase db = Room.databaseBuilder(this.getActivity().getApplicationContext(), EventDatabase.class, "EventDatabase").build();
                EventDAO eventDAO = db.eventDAO();
                Executor thread = Executors.newSingleThreadExecutor();
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity(), R.style.AlertDialogTheme);
                builder.setMessage(R.string.delete_warning)
                        .setTitle(R.string.question)
                        .setPositiveButton(R.string.yes, (dialog, cl) -> {
                            String deletedMessage = String.format(getResources().getString(R.string.deleted_message), selected.getName());
                            Snackbar.make( binding.getRoot(), deletedMessage, Snackbar.LENGTH_LONG)
                                    .setAction(R.string.undo, clik ->{
                                        Executor thread2 = Executors.newSingleThreadExecutor();
                                        thread2.execute(() -> {
                                            eventDAO.insertEvent(selected);
                                            FileOutputStream fOUt = null;
                                            try {
                                                fOUt = getActivity().openFileOutput(selected.getId() + ".png", Context.MODE_PRIVATE);
                                                image.compress(Bitmap.CompressFormat.PNG, 100, fOUt);
                                                fOUt.flush();
                                                fOUt.close();
                                            } catch (FileNotFoundException e) {
                                                e.printStackTrace();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                        });
                                    })
                                    .show();
                            thread.execute(() -> {
                                eventDAO.deleteEvent(selected);
                                getActivity().deleteFile(selected.getName() + ".png");
                            });

                        })
                        .setNegativeButton(R.string.no, (dialog, cl) -> { })
                        .create()
                        .show();

            });
        } else {
            queue = Volley.newRequestQueue(this.getContext());
            imgReq = new ImageRequest(imgUrl, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap bitmap) {
                    image = bitmap;
                    binding.eventImg.setImageBitmap(image);
                }
            }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {});
            queue.add(imgReq);
            binding.saveButton.setText(R.string.save);
            binding.saveButton.setOnClickListener(click -> {
                EventDatabase db = Room.databaseBuilder(this.getActivity().getApplicationContext(), EventDatabase.class, "EventDatabase").build();
                EventDAO eventDAO = db.eventDAO();
                Executor thread = Executors.newSingleThreadExecutor();
                thread.execute(() -> {
                    selected.setSaved(true);
                    try {
                        int rowAffected = (int)eventDAO.insertEvent(selected);
                        FileOutputStream fOUt = null;
                        fOUt = getActivity().openFileOutput(selected.getId() + ".png", Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, fOUt);
                        fOUt.flush();
                        fOUt.close();
                        getActivity().runOnUiThread(() -> {
                            if (rowAffected != 0) {
                                String message = selected.getName() + getResources().getString(R.string.saved_to_favorite);
                                Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this.getActivity(), R.string.failed_to_save, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (SQLiteConstraintException e) {
                        getActivity().runOnUiThread(() -> {
                            Toast.makeText(this.getActivity(), R.string.event_exists, Toast.LENGTH_SHORT).show();
                        });
                    }catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                });
            });
        }

        return binding.getRoot();
    }


}
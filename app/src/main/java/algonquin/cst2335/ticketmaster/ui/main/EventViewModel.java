package algonquin.cst2335.ticketmaster.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.ticketmaster.data.Event;

public class EventViewModel extends ViewModel {
    public MutableLiveData<ArrayList<Event>> events = new MutableLiveData<>();
    public MutableLiveData<Event> selectedEvent = new MutableLiveData<>();
}
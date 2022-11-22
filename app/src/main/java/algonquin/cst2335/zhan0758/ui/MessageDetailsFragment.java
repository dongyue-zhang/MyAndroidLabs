package algonquin.cst2335.zhan0758.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.zhan0758.R;
import algonquin.cst2335.zhan0758.data.ChatMessage;
import algonquin.cst2335.zhan0758.databinding.DetailLayoutBinding;

public class MessageDetailsFragment extends Fragment {
    ChatMessage selected;

    public MessageDetailsFragment(ChatMessage m) {
        selected = m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        DetailLayoutBinding binding = DetailLayoutBinding.inflate(inflater);
        binding.messageView.setText(selected.getMessage());
        binding.timeView.setText(selected.getTimeSent());
        binding.databaseIdView.setText("Id = " + selected.id);
        return binding.getRoot();
    }
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId())
//        {
//            case R.id.item_1:
//                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
//                ChatMessage m = chatModel.selectedMessage.getValue();
//                int position = chatModel.messages.getValue().indexOf(m);
//                builder.setMessage("Do you want to delete the message: " + m)
//                        .setTitle("Question:")
//                        .setPositiveButton("Yes", (dialog, cl) -> {
////                        ChatMessage m = messages.get(position);
////                        Snackbar.make(itemView.findViewById(R.id.messageText), "You deleted message #" + position, Snackbar.LENGTH_LONG)
////                                .setAction("Undo", clik ->{
////                                    Executor thread = Executors.newSingleThreadExecutor();
////                                    thread.execute(() -> {
////                                        mDAO.insertMessage(m);
////                                    });
////                                    chatModel.messages.getValue().add(m);
////                                    myAdapter.notifyItemInserted(position);
////                                })
////                                .show();
//                            Executor thread = Executors.newSingleThreadExecutor();
//                            thread.execute(() -> {
//                                mDAO.deleteMessage(m);
//                            });
//                            myAdapter.notifyItemRemoved(position);
//                            chatModel.messages.getValue().remove(position);
//                        })
//                        .setNegativeButton("No", (dialog, cl) -> { })
//                        .create()
//                        .show();
//
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.my_menu, menu);
//        return true;
//    }
}

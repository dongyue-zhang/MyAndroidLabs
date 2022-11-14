package algonquin.cst2335.zhan0758.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {
    @Insert
    public void insertMessage(ChatMessage m);

    @Query("SELECT * FROM ChatMessage")
    List<ChatMessage> getAllMessage();

    @Delete
    void deleteMessage(ChatMessage m);

}

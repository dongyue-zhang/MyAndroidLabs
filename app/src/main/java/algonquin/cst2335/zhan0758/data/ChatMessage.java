package algonquin.cst2335.zhan0758.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey
    public int id;

    @ColumnInfo(name = "message")
    protected String message;

    @ColumnInfo(name = "TimeSent")
    protected String timeSent;

    @ColumnInfo(name = "SendOrReceive")
    protected boolean isSentButton;

    public ChatMessage(){}
    public ChatMessage(String m, String t, boolean sent) {
        message = m;
        timeSent = t;
        isSentButton = sent;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}

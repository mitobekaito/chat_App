package jp.ac.shibaura_it.infolab1.chat;

import java.util.ArrayList;
import java.util.List;

// チャンネルクラス：チャットチャンネルを表す
public class Channel {
    private String name; // チャンネル名
    private List<Message> messages; // チャンネル内のメッセージリスト
    private int nextMessageId = 1; // 次に割り当てるメッセージID

    // チャンネル名を取得するメソッド
    public String getName() {
        return name;
    }

    // チャンネル内のメッセージリストを取得するメソッド
    public List<Message> getMessages() {
        return messages;
    }

    // チャンネルオブジェクトを生成するコンストラクタ
    Channel(String name) {
        this.name = name;
        this.messages = new ArrayList<>();
    }

    // チャンネルにメッセージを追加するメソッド
    void addMessage(Message message) {
        message.setId(nextMessageId++); // メッセージにIDを割り当てて、次のID値を増やす
        messages.add(message);
    }
}
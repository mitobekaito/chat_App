package jp.ac.shibaura_it.infolab1.chat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// メッセージクラス：チャットメッセージを表す
public class Message {
    private int id; // メッセージのユニークID
    private String content; // メッセージの内容
    private User sender; // メッセージの送信者
    private LocalDateTime timestamp; // メッセージの送信時刻
    private List<Message> replies; // メッセージへの返信リスト
    private int indent; // メッセージのインデントレベル（返信の深さを表す）
    private Map<String, Integer> reactions = new HashMap<>(); // メッセージのリアクション

    // メッセージオブジェクトを生成するコンストラクタ
    public Message(String content, User sender) {
        this.content = content;
        this.sender = sender;
        this.timestamp = LocalDateTime.now(); // メッセージの送信時刻を現在時刻で設定
        this.replies = new ArrayList<>();
        this.indent = 0; // デフォルトのインデントレベルは0 初期値に設定
    }

    // メッセージIDを取得するメソッド
    public int getId() {
        return id;
    }

    // メッセージIDを設定するメソッド
    public void setId(int id) {
        this.id = id;
    }

    // メッセージの内容を取得するメソッド
    public String getContent() {
        return content;
    }

    // メッセージの送信者を取得するメソッド
    public User getSender() {
        return sender;
    }

    // メッセージの送信時刻を取得するメソッド
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // メッセージへの返信リストを取得するメソッド
    public List<Message> getReplies() {
        return replies;
    }

    // メッセージのインデントレベルを取得するメソッド
    public int getIndent() {
        return indent;
    }

    // メッセージのインデントレベルを設定するメソッド
    public void setIndent(int indent) {
        this.indent = indent;
    }

    // メッセージに返信を追加するメソッド
    public void addReply(Message reply) {
        replies.add(reply);
    }

    // メッセージにリアクションを追加するメソッド
    public void addReaction(String emoji) {
        reactions.put(emoji, reactions.getOrDefault(emoji, 0) + 1);
    }

    // メッセージのリアクションを取得するメソッド
    public Map<String, Integer> getReactions() {
        return reactions;
    }
}

package jp.ac.shibaura_it.infolab1.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatSystem {
    // ユーザー名とユーザーオブジェクトをマッピングするハッシュマップ
    Map<String, User> users;
    // チャネルのリスト
    private List<Channel> channels;
    // メッセージIDのカウンター
    private int messageIdCounter;

    // チャネルのリストを返すメソッド
    public List<Channel> getChannels() {
        return channels;
    }

    // コンストラクタでオブジェクトを初期化
    public ChatSystem() {
        this.users = new HashMap<>();
        this.channels = new ArrayList<>();
        this.messageIdCounter = 1;
    }

    // ユーザー登録メソッド
    boolean registerUser(String username, String password) {
        if (!users.containsKey(username)) {
            users.put(username, new User(username, password));
            return true;
        }
        return false;
    }

    // ログインメソッド
    boolean login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            user.setLoggedIn(true);
            return true;
        }
        return false;
    }

    // ログアウトメソッド
    boolean logout(String username) {
        User user = users.get(username);
        if (user != null && user.isLoggedIn()) {
            user.setLoggedIn(false);
            return true;
        }
        return false;
    }

    // 新しいチャネルを作成するメソッド
    public Channel createChannel(String name) {
        Channel newChannel = new Channel(name);
        channels.add(newChannel);
        return newChannel;
    }

    // チャネルにメッセージを追加するメソッド
    public boolean addMessageToChannel(String channelName, String username, String content) {
        User user = users.get(username);
        if (user == null || !user.isLoggedIn()) {
            return false;
        }

        Channel channel = findChannelByName(channelName);
        if (channel == null) {
            return false;
        }

        jp.ac.shibaura_it.infolab1.chat.Message message = new jp.ac.shibaura_it.infolab1.chat.Message(content, user);
        message.setId(getNextMessageId());
        channel.addMessage(message);
        return true;
    }

    // メッセージに対して返信を追加するメソッド
    public boolean addReplyToMessage(String channelName, int parentId, String username, String content) {
        User user = users.get(username);
        if (user == null || !user.isLoggedIn()) {
            return false;
        }

        Channel channel = findChannelByName(channelName);
        if (channel == null) {
            return false;
        }

        Message parentMessage = findMessageById(channel, parentId);
        if (parentMessage == null) {
            return false;
        }

        Message reply = new Message(content, user);
        reply.setId(getNextMessageId());
        reply.setIndent(parentMessage.getIndent() + 1);

        // 親メッセージの返信リストに追加
        parentMessage.addReply(reply);
        return true;
    }

    // メッセージIDでメッセージを検索するメソッド
    public Message findMessageById(Channel channel, int id) {
        for (Message message : channel.getMessages()) {
            if (message.getId() == id) {
                return message;
            }
            Message found = findMessageInReplies(message, id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    // メッセージの返信からメッセージを再帰的に検索するメソッド
    private Message findMessageInReplies(Message message, int id) {
        for (Message reply : message.getReplies()) {
            if (reply.getId() == id) {
                return reply;
            }
            Message found = findMessageInReplies(reply, id);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    // メッセージにリアクションを追加するメソッド
    public boolean addReactionToMessage(String channelName, int messageId, String username, String emoji) {
        User user = users.get(username);
        if (user == null || !user.isLoggedIn()) {
            return false;
        }

        Channel channel = findChannelByName(channelName);
        if (channel == null) {
            return false;
        }

        jp.ac.shibaura_it.infolab1.chat.Message message = findMessageById(channel, messageId);
        if (message == null) {
            return false;
        }

        message.addReaction(emoji);
        return true;
    }

    // チャネル名でチャネルを検索するメソッド
    public Channel findChannelByName(String name) {
        for (Channel channel : channels) {
            if (channel.getName().equals(name)) {
                return channel;
            }
        }
        return null;
    }

    // 次のメッセージIDを取得するメソッド
    private int getNextMessageId() {
        return messageIdCounter++;
    }
}

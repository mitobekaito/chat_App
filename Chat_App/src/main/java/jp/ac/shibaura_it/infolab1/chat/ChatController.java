package jp.ac.shibaura_it.infolab1.chat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ChatController {

    private ChatSystem chatSystem = new ChatSystem(); // チャットシステムのインスタンス
    private List<String> emojiList = Arrays.asList("👍", "💛", "😊", "🎉", "🌟", "👏", "🤔", "😂", "＼(^o^)／"); // 利用可能な絵文字のリスト

    // ホームページへのルーティング
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "login"; // ログインページへリダイレクト
        }
        return "redirect:/chat"; // チャットページへリダイレクト
    }

    // ユーザー登録ページの表示
    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    // ユーザー登録処理
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        if (chatSystem.registerUser(username, password)) {
            return "redirect:/"; // 登録成功後、ホームページへリダイレクト
        } else {
            model.addAttribute("error", "そのユーザ名は既に登録されています。");
            return "register"; // エラーメッセージとともに登録ページへ戻る
        }
    }

    // ログイン処理
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        if (chatSystem.login(username, password)) {
            session.setAttribute("username", username);
            return "redirect:/chat"; // ログイン成功後、チャットページへリダイレクト
        } else {
            model.addAttribute("error", "ユーザ名またはパスワードが違います。");
            return "login"; // エラーメッセージとともにログインページへ戻る
        }
    }

    // チャットページの表示
    @GetMapping("/jp/ac/shibaura_it/infolab1/chat")
    public String chat(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/"; // ログインしていない場合、ホームページへリダイレクト
        }
        model.addAttribute("channels", chatSystem.getChannels()); // 全チャネルのリストをモデルに追加
        return "channelList"; // チャネルリストページを表示
    }

    // ログアウト処理
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("username"); // セッションからユーザ名を削除
        return "redirect:/"; // ホームページへリダイレクト
    }

    // 新しいチャネルの作成
    @PostMapping("/createChannel")
    public String createChannel(@RequestParam String channelName) {
        chatSystem.createChannel(channelName); // 新しいチャネルを作成
        return "redirect:/chat"; // チャットページへリダイレクト
    }

    // チャットルームページの表示
    @GetMapping("/jp/ac/shibaura_it/infolab1/chat/{channelName}")
    public String chatRoom(@PathVariable String channelName, HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/"; // ログインしていない場合、ホームページへリダイレクト
        }
        Channel channel = chatSystem.findChannelByName(channelName);
        if (channel == null) {
            return "redirect:/chat"; // チャネルが見つからない場合、チャットページへリダイレクト
        }
        List<jp.ac.shibaura_it.infolab1.chat.Message> flattenedMessages = flattenMessages(channel.getMessages()); // メッセージを平坦化
        model.addAttribute("messages", flattenedMessages); // メッセージをモデルに追加
        model.addAttribute("currentChannel", channel); // 現在のチャネルをモデルに追加
        model.addAttribute("channels", chatSystem.getChannels()); // 全チャネルのリストをモデルに追加
        model.addAttribute("emojiList", emojiList); // 利用可能な絵文字のリストをモデルに追加
        return "chatroom"; // チャットルームページを表示
    }

    // メッセージの送信
    @PostMapping("/jp/ac/shibaura_it/infolab1/chat/{channelName}/send")
    public String sendMessage(@PathVariable String channelName, @RequestParam String content,
                              @RequestParam(required = false) Integer parentId, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }
        if (parentId == null) {
            chatSystem.addMessageToChannel(channelName, username, content);
        } else {
            chatSystem.addReplyToMessage(channelName, parentId, username, content);
        }
        String encodedChannelName = URLEncoder.encode(channelName, StandardCharsets.UTF_8);
        //チャンネル名に特殊文字が含まれている可能性があるため、URLエンコーディングを使用してリダイレクト先のURLを構築
        //これをしないとhttp://localhost:8080/chat/?????のURLに飛んでしまう。
        return "redirect:/chat/" + encodedChannelName;
    }

    // メッセージにリアクションを追加
    @PostMapping("/jp/ac/shibaura_it/infolab1/chat/{channelName}/react")
    public String addReaction(@PathVariable String channelName,
                              @RequestParam int messageId,
                              @RequestParam String emoji,
                              HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/"; // ログインしていない場合、ホームページへリダイレクト
        }
        chatSystem.addReactionToMessage(channelName, messageId, username, emoji); // メッセージにリアクションを追加
        String encodedChannelName = URLEncoder.encode(channelName, StandardCharsets.UTF_8);
        return  "redirect:/chat/" + encodedChannelName;
    }
    // メッセージのリストを平坦化するメソッド
    private List<Message> flattenMessages(List<Message> messages) {
        List<Message> flattened = new ArrayList<>();
        for (Message message : messages) {
            flattened.add(message); // メッセージを追加
            // 返信を平坦化してリストに追加
            flattened.addAll(flattenReplies(message.getReplies(), message.getIndent() + 1));
        }
        return flattened;
    }

    // 返信のリストを平坦化するメソッド
    private List<Message> flattenReplies(List<jp.ac.shibaura_it.infolab1.chat.Message> replies, int indent) {
        List<Message> flattened = new ArrayList<>();
        for (Message reply : replies) {
            reply.setIndent(indent); // 返信のインデントを設定
            flattened.add(reply); // 返信をリストに追加
            // ネストされた返信（返信の返信など）も同様に処理して平坦化し、リストに追加
            flattened.addAll(flattenReplies(reply.getReplies(), indent + 1));
        }
        return flattened;
    }

}

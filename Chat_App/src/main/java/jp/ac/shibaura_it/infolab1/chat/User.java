package jp.ac.shibaura_it.infolab1.chat;

// ユーザークラス：チャットシステムのユーザーを表す
public class User {
    private String username; // ユーザー名
    private String password; // パスワード
    public boolean isLoggedIn; // ログイン状態（true: ログイン中、false: ログアウト中）

    // ユーザーオブジェクトを生成するコンストラクタ
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isLoggedIn = false; // 初期状態はログアウト
    }

    // ユーザー名を取得するメソッド
    public String getUsername() {
        return username;
    }

    // ユーザー名を設定するメソッド
    public void setUsername(String username) {
        this.username = username;
    }

    // パスワードを取得するメソッド
    public String getPassword() {
        return password;
    }

    // パスワードを設定するメソッド
    public void setPassword(String password) {
        this.password = password;
    }

    // ログイン状態を確認するメソッド
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    // ログイン状態を設定するメソッド
    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
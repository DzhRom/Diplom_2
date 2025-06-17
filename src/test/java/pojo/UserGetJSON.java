package pojo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserGetJSON {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private User user;

    @Override
    public String toString() {
       return "success:" + success + ", accessToken:" + accessToken + ", refreshToken:" + refreshToken + ", user:" + user;
    }
}

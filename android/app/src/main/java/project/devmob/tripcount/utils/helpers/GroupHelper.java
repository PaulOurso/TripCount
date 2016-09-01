package project.devmob.tripcount.utils.helpers;

import java.util.Random;

/**
 * Created by Tony on 01/09/2016.
 */
public class GroupHelper {

    private static final String CHARS = "abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ234567890";
    private static final int MAX_SIZE_TOKEN = 8;

    public static String generateToken() {
        StringBuilder token = new StringBuilder(MAX_SIZE_TOKEN);
        Random random = new Random();
        for (int i = 0; i < MAX_SIZE_TOKEN; i++) {
            token.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return token.toString();
    }
}

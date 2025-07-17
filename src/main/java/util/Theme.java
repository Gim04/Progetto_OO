package util;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Theme
{
    private static final String DEFAULT_FONT = "SansSerif";

    public static final Color actionColor = new Color(48, 198, 30);
    public static final Color actionColor2 = new Color(66, 209, 49);

    public static final Color secondaryColor = new Color(9, 28, 186);
    public static final Color secondaryColor2 = new Color(9, 34, 237);

    public static final Color backColor = new Color(200, 0, 0);
    public static final Color backColor2 = new Color(255, 77, 77);

    public static final Color gray = new Color(245, 245, 245);
    public static final Color gray2 = new Color(230, 230, 230);

    public static final Font header = new Font(DEFAULT_FONT, Font.BOLD, 24);
    public static final Font paragraph = new Font(DEFAULT_FONT, Font.PLAIN, 14);
    public static final Font hackathon_list_title = new Font(DEFAULT_FONT, Font.PLAIN, 16);
    public static final Font hackathon_list_p = new Font(DEFAULT_FONT, Font.PLAIN, 12);

    public static final ImageIcon ICON_ADD = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/add.png")));
    public static final ImageIcon ICON_COMMENT = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/comment.png")));
    public static final ImageIcon ICON_DOCS = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/docs.png")));
    public static final ImageIcon ICON_GROUP = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/group.png")));
    public static final ImageIcon ICON_PERSON_ADD = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/person_add.png")));
    public static final ImageIcon ICON_SUB = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/sub.png")));
    public static final ImageIcon ICON_UNLOCK = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/unlock.png")));
    public static final ImageIcon ICON_VOTE = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/vote.png")));
    public static final ImageIcon ICON_LEADERBOARD = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/leaderboard.png")));
    public static final ImageIcon ICON_UNDO = new ImageIcon(Objects.requireNonNull(Theme.class.getResource("/icons/undo.png")));
    
}

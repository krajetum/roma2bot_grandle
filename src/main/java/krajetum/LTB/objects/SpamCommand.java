package krajetum.LTB.objects;

/**
 *
 * This class is used to fetch the spam_commands.json file
 *
 *
 * @since 1.0
 */
public class SpamCommand {

    private String command;
    private String filepath;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}

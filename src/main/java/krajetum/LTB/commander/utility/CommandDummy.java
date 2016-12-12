package krajetum.LTB.commander.utility;

import krajetum.LTB.messagebuilder.exception.BadMessageBuildExeception;
import pro.zackpollard.telegrambot.api.chat.Chat;

import java.util.ArrayList;

public interface CommandDummy {
    void execute(Chat chat, ArrayList<Object> objects) throws BadMessageBuildExeception;
}

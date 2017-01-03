package krajetum.LTB.messagebuilder;

import krajetum.LTB.commander.utility.CommandDummy;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableAudioMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Function;

/**
 * Created by Lorenzo on 20/12/2016.
 */
public class AMessageBuilder implements CommandDummy{


    String _filepath;
    Function<ArrayList<Object>,String> _callable;

    public AMessageBuilder(){}

    public AMessageBuilder path(String path) {
        _filepath = path;
        return this;
    }

    public AMessageBuilder runnable(Function<ArrayList<Object>,String> callable){
        _callable = callable;
        return this;
    }

    public SendableAudioMessage buildWithRunnable(ArrayList<Object> parameters) {
        String result;
        if(_callable!=null){
            result = _callable.apply(parameters);
        }else{
            result = "404.jpg";
        }
        return SendableAudioMessage.builder().audio(new InputFile(new File(System.getProperty("user.dir") + "/audio/"+result))).build();
    }

    public SendableAudioMessage build() {
        return SendableAudioMessage.builder().audio(new InputFile(new File(System.getProperty("user.dir") + "/audio/"+_filepath))).build();
    }

    @Override
    public void execute(Chat chat, ArrayList<Object> objects) {
        if(_callable==null)
            chat.sendMessage(this.build());
        else
            chat.sendMessage(this.buildWithRunnable(objects));
    }
}

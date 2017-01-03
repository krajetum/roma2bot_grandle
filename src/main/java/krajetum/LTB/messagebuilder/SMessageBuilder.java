package krajetum.LTB.messagebuilder;

import krajetum.LTB.commander.utility.CommandDummy;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableStickerMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Function;

public class SMessageBuilder implements CommandDummy {


    String _filepath;
    Function<ArrayList<Object>,String> _callable;

    public SMessageBuilder(){}

    public SMessageBuilder path(String path) {
        _filepath = path;
        return this;
    }

    public SMessageBuilder runnable(Function<ArrayList<Object>,String> callable){
        _callable = callable;
        return this;
    }

    public SendableStickerMessage buildWithRunnable(ArrayList<Object> parameters) {
        String result;
        if(_callable!=null){
            result = _callable.apply(parameters);
        }else{
            result = "404.jpg";
        }
        return SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir") + "/stickers/"+result))).build();
    }

    public SendableStickerMessage build() {
        return SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir") + "/stickers/"+_filepath))).build();
    }

    @Override
    public void execute(Chat chat, ArrayList<Object> objects) {
        if(_callable==null)
            chat.sendMessage(this.build());
        else
            chat.sendMessage(this.buildWithRunnable(objects));
    }
}

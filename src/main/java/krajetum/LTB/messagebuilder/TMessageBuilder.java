package krajetum.LTB.messagebuilder;

import krajetum.LTB.commander.utility.CommandDummy;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.util.ArrayList;
import java.util.function.Function;

public class TMessageBuilder implements CommandDummy {

    StringBuilder _textBuilder;
    ParseMode _parseMode;
    Function<ArrayList<Object>, String> _callable;

    public TMessageBuilder(){_textBuilder = new StringBuilder();}

    public TMessageBuilder append(String text){
        this._textBuilder.append(text);
        return this;
    }
    public TMessageBuilder parseMode(ParseMode mode){
        this._parseMode = mode;
        return this;
    }

    public TMessageBuilder runnable(Function<ArrayList<Object>,String> callable){
        this._callable = callable;
        return this;
    }

    public SendableTextMessage buildWithRunnable(ArrayList<Object> parameters) {
        String result;
        if(this._callable!=null){
            result = this._callable.apply(parameters);
        }else{
            result = "404.jpg";
        }
        return SendableTextMessage.builder().message(result).parseMode(_parseMode).build();
    }

    public SendableTextMessage build(){
        if(_parseMode==null)_parseMode=ParseMode.NONE;
        return SendableTextMessage.builder().message(_textBuilder.toString()).parseMode(_parseMode).build();
    }

    @Override
    public void execute(Chat chat, ArrayList<Object> objects) {
        if(_callable==null)
            chat.sendMessage(this.build());
        else
            chat.sendMessage(this.buildWithRunnable(objects));
    }
}

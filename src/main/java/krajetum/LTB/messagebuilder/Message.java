package krajetum.LTB.messagebuilder;

import com.sun.istack.internal.NotNull;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableStickerMessage;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;

import java.util.ArrayList;
import java.util.function.Function;

public class Message {

    public Message(){}



    public static class SMessageBuilder extends Message {

        @NotNull
        String _filepath;
        Function<ArrayList<Object>,String> _callable;

        public SMessageBuilder(){}

        public SMessageBuilder path(@NotNull String path) {
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
            return SendableStickerMessage.builder().sticker(new InputFile(System.getProperty("user.id")+"/stickers/"+result)).build();
        }

        public SendableStickerMessage build() {
            return SendableStickerMessage.builder().sticker(new InputFile(System.getProperty("user.id")+"/stickers/"+_filepath)).build();
        }
    }

    public static class TMessageBuilder extends Message {

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
    }
}


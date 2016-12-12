package krajetum.LTB.commander;


import krajetum.LTB.commander.utility.LambdaCommand;
import krajetum.LTB.messagebuilder.Message;
import org.apache.commons.lang3.tuple.Pair;
import pro.zackpollard.telegrambot.api.chat.Chat;

import java.util.ArrayList;
import java.util.HashMap;


public class Commander{
    private HashMap<String, Pair<Object, Class<?>>> commandMap;



    public Commander(){
        commandMap = new HashMap<>();
    }

    public void register(String name, LambdaCommand lambdaCommand, Class<?> type){
        //noinspection ResultOfMethodCallIgnored
        commandMap.put(name, Pair.of(lambdaCommand.lambda(), type));
    }

    public void execute(String name, Chat chat,ArrayList<Object> arrayList){
        if(commandMap.containsKey(name)){
            Pair<Object, Class<?>> pair = commandMap.get(name);
            if(pair.getValue()== Message.TMessageBuilder.class) {
                Message.TMessageBuilder messageBuilder = (Message.TMessageBuilder) pair.getValue().cast(pair.getKey());
                messageBuilder.execute(chat, arrayList);
            }else if(pair.getValue()== Message.SMessageBuilder.class){
                Message.SMessageBuilder messageBuilder = (Message.SMessageBuilder) pair.getValue().cast(pair.getKey());
                messageBuilder.execute(chat, arrayList);
            }
        }
    }

    public String list(){
        StringBuilder builder = new StringBuilder();
        commandMap.forEach((s, objectClassPair) -> {
            builder.append(s +"\n");
        });
        return builder.toString();
    }

}

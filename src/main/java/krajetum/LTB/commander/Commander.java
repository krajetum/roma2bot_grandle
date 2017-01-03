package krajetum.LTB.commander;

import krajetum.LTB.commander.utility.LambdaCommand;
import krajetum.LTB.messagebuilder.AMessageBuilder;
import krajetum.LTB.messagebuilder.SMessageBuilder;
import krajetum.LTB.messagebuilder.TMessageBuilder;
import org.apache.commons.lang3.tuple.Pair;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.ArrayList;
import java.util.HashMap;


public class Commander{
    private HashMap<String, Pair<Object, Class<?>>> commandMap;
    //Cache<String, Integer> commandCache;
    public Commander(){
        commandMap = new HashMap<>();
        //commandCache = new Cache<>(10,60,100);

    }

    public void register(String name, LambdaCommand lambdaCommand, Class<?> type){
        //noinspection ResultOfMethodCallIgnored
        commandMap.put(name, Pair.of(lambdaCommand.lambda(), type));
    }

    public void execute(String name, Chat chat,ArrayList<Object> arrayList){
        if(arrayList.get(0) instanceof CommandMessageReceivedEvent) {
            CommandMessageReceivedEvent event = (CommandMessageReceivedEvent) arrayList.get(0);
            /*
            if (commandCache.get(event.getMessage().getSender().getFullName()) == null) {
                    commandCache.put(event.getMessage().getSender().getFullName(), 0);
            }
            if (commandCache.get(event.getMessage().getSender().getFullName()) <= 5) {
                int value = commandCache.get(event.getMessage().getSender().getFullName()) + 1;
                commandCache.remove(event.getMessage().getSender().getFullName());
                commandCache.put(event.getMessage().getSender().getFullName(), value);
                */
                if (commandMap.containsKey(name)) {
                    Pair<Object, Class<?>> pair = commandMap.get(name);
                    if (pair.getValue() == TMessageBuilder.class) {
                        TMessageBuilder messageBuilder = (TMessageBuilder) pair.getValue().cast(pair.getKey());
                        messageBuilder.execute(chat, arrayList);
                    } else if (pair.getValue() == SMessageBuilder.class) {
                        SMessageBuilder messageBuilder = (SMessageBuilder) pair.getValue().cast(pair.getKey());
                        messageBuilder.execute(chat, arrayList);
                    } else if (pair.getValue() == AMessageBuilder.class){
                        AMessageBuilder messageBuilder = (AMessageBuilder) pair.getValue().cast(pair.getKey());
                        messageBuilder.execute(chat, arrayList);
                    }
                }
                /*
            }else{
                event.getMessage().getBotInstance().kickChatMember(event.getChat().getId(), (int)event.getMessage().getSender().getId());
                event.getChat().sendMessage(new TMessageBuilder().append("User: ").append(event.getMessage().getSender().getFullName()).append(" Banned for 1 min. Reason Flooding").parseMode(ParseMode.MARKDOWN).build());
            }*/
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

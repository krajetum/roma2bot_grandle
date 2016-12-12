package krajetum.LTB;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import krajetum.LTB.commander.Commander;
import krajetum.LTB.messagebuilder.Message;
import krajetum.LTB.objects.LUGMember;
import krajetum.LTB.objects.SpamCommand;
import org.mortbay.log.Log;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableStickerMessage;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.CallbackQueryReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.ParticipantJoinGroupChatEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@SuppressWarnings("ALL")
public class Roma2LugCore implements Listener {

    private HashMap<String, String> spam_commands;
    private List<SpamCommand> commandList;
    private final TelegramBot telegramBot;

    private Commander commander;

    public Roma2LugCore(TelegramBot telegramBot) {

        this.telegramBot = telegramBot;
        spam_commands = new HashMap<>();
        commander = new Commander();

        File file = new File(System.getProperty("user.dir")+"/data/spam_commands.json");
        Gson gson = new Gson();
        try {
            Type type = new TypeToken<List<SpamCommand>>(){}.getType();
            commandList = gson.fromJson(new FileReader(file), type);
            Log.info("INIT SPAM COMMANDS");
            for(SpamCommand command:commandList){
                Log.info("Command "+command.getCommand()+" initialized");
                commander.register(command.getCommand(),() -> new Message.SMessageBuilder().path(command.getFilepath()), Message.SMessageBuilder.class);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        register();
    }

    public void register(){
        commander.register("whoami", ()->{
            Message.TMessageBuilder builder = new Message.TMessageBuilder();
            builder.runnable(objects -> {
               CommandMessageReceivedEvent event = ((CommandMessageReceivedEvent) objects.get(0));
               return  "Name: *" + event.getMessage().getSender().getFullName() + "*";
            });
            builder.parseMode(ParseMode.MARKDOWN);
            return builder;
        }, Message.TMessageBuilder.class);
        commander.register("k", ()->{
            Message.TMessageBuilder builder = new Message.TMessageBuilder();
            builder.runnable(objects -> {
                return  "K";
            });
            builder.parseMode(ParseMode.MARKDOWN);
            return builder;
        }, Message.TMessageBuilder.class);
        commander.register("quarantennetriste", ()->{
            Message.SMessageBuilder builder = new Message.SMessageBuilder();
            builder.runnable(objects -> {
                CommandMessageReceivedEvent event = ((CommandMessageReceivedEvent) objects.get(0));
                File file = new File(System.getProperty("user.dir")+"/stickers/quarantennitristi");
                //noinspection ConstantConditions
                int rand = ThreadLocalRandom.current().nextInt(0, file.listFiles().length);
                //noinspection ConstantConditions
                return "quarantennitristi/"+file.listFiles()[rand].getName();
            });
            return builder;
        }, Message.SMessageBuilder.class);

        commander.register("chatid", ()->{
            Message.TMessageBuilder builder = new Message.TMessageBuilder();
            builder.runnable(objects -> {
                CommandMessageReceivedEvent event = ((CommandMessageReceivedEvent) objects.get(0));
                return  event.getChat().getId();
            });
            builder.parseMode(ParseMode.MARKDOWN);
            return builder;
        }, Message.TMessageBuilder.class);

        commander.register("kloop", () -> {
            return new Message.TMessageBuilder().runnable(objects -> {
                CommandMessageReceivedEvent event = (CommandMessageReceivedEvent) objects.get(0);
                try{
                    String string = event.getArgsString();
                    int ripeti= Integer.parseInt(string);
                    if(ripeti>5){
                        return "No spam pls";
                    }else{
                        StringBuilder builder = new StringBuilder();
                        for(int i =0; i<ripeti;i++){
                            builder.append("k \n");
                        }
                        return builder.toString();
                    }
                }catch (NumberFormatException e){
                    Log.debug("Questo è flavio che fa lo stronzo xD");
                }
                return "Unexpected Error";

            }).parseMode(ParseMode.MARKDOWN);
        }, Message.TMessageBuilder.class);

        commander.register("cetriolotime", () -> {
            return new Message.TMessageBuilder().runnable(objects -> {
                CommandMessageReceivedEvent event = (CommandMessageReceivedEvent) objects.get(0);
                Gson gson = new Gson();
                try {
                    File file = new File(System.getProperty("user.dir")+"/data/lug_members.json");
                    Type listType = new TypeToken<ArrayList<LUGMember>>(){}.getType();
                    List<LUGMember> memeMembers= gson.fromJson(new JsonReader(new FileReader(file)), listType);
                    int rand = ThreadLocalRandom.current().nextInt(0, memeMembers.size());
                    return "Cetriolo a: *" + memeMembers.get(rand).getName() + "* ! Congratulazioni!";
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return "Unexpected Error";

            }).parseMode(ParseMode.MARKDOWN);
        }, Message.TMessageBuilder.class);

        commander.register("about", () -> {
            return new Message.TMessageBuilder().runnable(objects -> {
                return "Get this project on: https://krajetum.github.io/roma2lug_bot/";
            }).parseMode(ParseMode.MARKDOWN);
        }, Message.TMessageBuilder.class);

        commander.register("list", () -> {
            return new Message.TMessageBuilder().runnable(objects -> {
                return commander.list();
            }).parseMode(ParseMode.MARKDOWN);
        }, Message.TMessageBuilder.class);
    }


    @Override
    public void onParticipantJoinGroupChat(ParticipantJoinGroupChatEvent event) {
        SendableTextMessage welcomeMessage = SendableTextMessage.builder()
                                            .message("Diamo il benvenuto a "+event.getParticipant().getFullName()+"!")
                                            .parseMode(ParseMode.MARKDOWN)
                                            .build();
        event.getChat().sendMessage(welcomeMessage);
        SendableStickerMessage sendableStickerMessage = SendableStickerMessage.builder().sticker(new InputFile(new File(System.getProperty("user.dir")+"/stickers/linux_inside.png"))).build();
        event.getChat().sendMessage(sendableStickerMessage);
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event){
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(event);
        commander.execute(event.getCommand(), event.getChat(),objects);
    }

    @Override
    public void onCallbackQueryReceivedEvent(CallbackQueryReceivedEvent event) {
        event.getCallbackQuery().answer("MINCHIA VOI "+event.getCallbackQuery().getFrom().getFirstName()+" EH? SONO ANCORA IN FASE DI SVILUPPO OK?", false);
    }


}
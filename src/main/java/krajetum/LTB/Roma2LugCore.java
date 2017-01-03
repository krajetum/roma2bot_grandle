package krajetum.LTB;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import krajetum.LTB.commander.Commander;
import krajetum.LTB.messagebuilder.AMessageBuilder;
import krajetum.LTB.messagebuilder.SMessageBuilder;
import krajetum.LTB.messagebuilder.TMessageBuilder;
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
                commander.register(command.getCommand(),() -> new SMessageBuilder().path(command.getFilepath()), SMessageBuilder.class);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.info("FINISHED SPAM COMMANDS");
        file = new File(System.getProperty("user.dir")+"/data/audio_commands.json");
        try{
            Type type = new TypeToken<List<SpamCommand>>(){}.getType();
            commandList = gson.fromJson(new FileReader(file), type);
            Log.info("INIT AUDIO COMMANDS");
            for(SpamCommand command:commandList){
                Log.info("Command "+command.getCommand()+" initialized");
                commander.register(command.getCommand(),() -> new AMessageBuilder().path(command.getFilepath()), AMessageBuilder.class);
            }
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }
        Log.info("FINISHED AUDIO COMMANDS");

        register();
    }

    public void register(){
        commander.register("whoami", ()->{
            TMessageBuilder builder = new TMessageBuilder();
            builder.runnable(objects -> {
               CommandMessageReceivedEvent event = ((CommandMessageReceivedEvent) objects.get(0));
               return  "Name: *" + event.getMessage().getSender().getFullName() + "*";
            });
            builder.parseMode(ParseMode.MARKDOWN);
            return builder;
        }, TMessageBuilder.class);
        commander.register("k", ()->{
            TMessageBuilder builder = new TMessageBuilder();
            builder.runnable(objects -> {
                return  "K";
            });
            builder.parseMode(ParseMode.MARKDOWN);
            return builder;
        }, TMessageBuilder.class);
        commander.register("quarantennetriste", ()->{
            SMessageBuilder builder = new SMessageBuilder();
            builder.runnable(objects -> {
                CommandMessageReceivedEvent event = ((CommandMessageReceivedEvent) objects.get(0));
                File file = new File(System.getProperty("user.dir")+"/stickers/quarantennitristi");
                //noinspection ConstantConditions
                int rand = ThreadLocalRandom.current().nextInt(0, file.listFiles().length);
                //noinspection ConstantConditions
                return "quarantennitristi/"+file.listFiles()[rand].getName();
            });
            return builder;
        }, SMessageBuilder.class);

        commander.register("chatid", ()->{
            TMessageBuilder builder = new TMessageBuilder();
            builder.runnable(objects -> {
                CommandMessageReceivedEvent event = ((CommandMessageReceivedEvent) objects.get(0));
                return  event.getChat().getId();
            });
            builder.parseMode(ParseMode.MARKDOWN);
            return builder;
        }, TMessageBuilder.class);

        commander.register("kloop", () -> {
            return new TMessageBuilder().runnable(objects -> {
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
        }, TMessageBuilder.class);

        commander.register("cetriolotime", () -> {
            return new TMessageBuilder().runnable(objects -> {
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
        }, TMessageBuilder.class);

        commander.register("about", () -> {
            TMessageBuilder messageBuilder = new TMessageBuilder().append("Get this project on: https://krajetum.github.io/roma2lug_bot/").parseMode(ParseMode.MARKDOWN);
            return messageBuilder;
        }, TMessageBuilder.class);

        commander.register("list", () -> {
            return new TMessageBuilder().runnable(objects -> {
                return commander.list();
            }).parseMode(ParseMode.MARKDOWN);
        }, TMessageBuilder.class);

        commander.register("huehue", ()->{
            TMessageBuilder messageBuilder = new TMessageBuilder().append("BrrBrr").parseMode(ParseMode.MARKDOWN);
            return messageBuilder;
        }, TMessageBuilder.class);
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
package me.skipperguy12.autobroadcasterplus;

import com.google.common.collect.Lists;
import me.skipperguy12.autobroadcasterplus.runnables.MessagesRunnable;
import me.skipperguy12.autobroadcasterplus.utils.LiquidMetal;
import me.skipperguy12.autobroadcasterplus.utils.Log;
import me.skipperguy12.autobroadcasterplus.utils.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.List;

/**
 * Class to handle multiple MessageFile's
 */
public class Messenger {
    private List<MessageFile> messageFileList = Lists.newArrayList();
    private List<Integer> taskIds = Lists.newArrayList();

    public Messenger(File dataFolder) {
        Log.debug("Scanning directory " + dataFolder.getAbsolutePath() +":");
        for (File file : dataFolder.listFiles()) {
            Log.debug(file.getName());
            if (!FilenameUtils.getBaseName(file.getName()).contains("messages"))
                continue;
            messageFileList.add(new MessageFile(file));
        }
        Log.debug("Message Files detected(" + messageFileList.size() + "): " + StringUtils.listToEnglishCompound(messageFileList, new LiquidMetal.StringProvider<MessageFile>() {
            @Override
            public String get(MessageFile c) {
                return c.getFile().getName();
            }
        }));
    }

    public void reloadSchedulers() {
        Log.debug("Reloading schedulers...");
        for (Integer taskId : taskIds) {
            Log.debug("Cancelling " + taskId);
            Bukkit.getScheduler().cancelTask(taskId);
        }

        for (MessageFile messageFile : messageFileList)
            taskIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoBroadcasterPlus.getInstance(),
                    new MessagesRunnable(AutoBroadcasterPlus.getInstance(), messageFile)
                    , 0L, Config.Broadcaster.interval * 20L));

        Log.debug("Task ID's now running(" + taskIds.size() + "): " + StringUtils.listToEnglishCompound(taskIds, new LiquidMetal.StringProvider<Integer>() {
            @Override
            public String get(Integer c) {
                return c.toString();
            }
        }));
    }

    public List<MessageFile> getMessageFileList() {
        return this.messageFileList;
    }
}

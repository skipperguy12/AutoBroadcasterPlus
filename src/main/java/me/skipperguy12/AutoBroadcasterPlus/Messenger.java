package me.skipperguy12.autobroadcasterplus;

import com.google.common.collect.Lists;
import me.skipperguy12.autobroadcasterplus.runnables.MessagesRunnable;
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
        if (!dataFolder.isDirectory())
            return;

        for (File file : dataFolder.listFiles()) {
            if (FilenameUtils.getExtension(file.getName()) != "txt")
                continue;
            if (!FilenameUtils.getBaseName(file.getName()).contains("messages"))
                continue;
            messageFileList.add(new MessageFile(file));
        }
    }

    public void reloadSchedulers() {
        for (Integer taskId : taskIds)
            Bukkit.getScheduler().cancelTask(taskId);

        for (MessageFile messageFile : messageFileList)
            taskIds.add(Bukkit.getScheduler().scheduleSyncRepeatingTask(AutoBroadcasterPlus.getInstance(),
                    new MessagesRunnable(AutoBroadcasterPlus.getInstance(), messageFile)
                    , 0L, Config.Broadcaster.interval * 20L));
    }

    public List<MessageFile> getMessageFileList() {
        return this.messageFileList;
    }
}

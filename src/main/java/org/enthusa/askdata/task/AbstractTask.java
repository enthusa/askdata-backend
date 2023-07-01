package org.enthusa.askdata.task;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.enthusa.avatar.utils.task.Task;
import org.enthusa.avatar.utils.task.TaskModel;

import java.util.Date;

/**
 * @author henry
 * @date 2021/9/8
 */
@Slf4j
public abstract class AbstractTask implements Task {
    @Setter
    @Getter
    private String[] args = new String[]{};

    @Override
    public String getBiz() {
        return "askdata-task";
    }

    @Override
    public String getTaskName() {
        return getClass().getSimpleName();
    }

    @Override
    public void start() {
        TaskModel model = new TaskModel(getBiz(), getTaskName());
        log.info("Begin to run {} at {}", getTaskName(), new Date());
        run(model);
        log.info("Complete {} at {}", getTaskName(), new Date());
    }
}

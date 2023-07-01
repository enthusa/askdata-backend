package org.enthusa.askdata.task;

import org.enthusa.askdata.AbstractTest;
import org.enthusa.askdata.task.impl.FillMetaDataTask;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author henry
 * @date 2023/7/1
 */
public class TaskTest extends AbstractTest {
    @Resource
    private FillMetaDataTask fillMetaDataTask;

    @Test
    public void test() {
        fillMetaDataTask.start();
    }
}

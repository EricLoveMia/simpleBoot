package cn.eric.h2.interview.mode.delegate;

import java.util.List;
import java.util.Random;

/**
 * @ClassName Manager
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/15
 * @Version V1.0
 **/
public class Manager implements IEmployee {

    private List<IEmployee> employees;

    public Manager(List<IEmployee> employees) {
        this.employees = employees;
    }

    @Override
    public void work(String command) {
        // 找到一个工人分配工作
        employees.get(new Random().nextInt(employees.size())).work(command);
    }
}

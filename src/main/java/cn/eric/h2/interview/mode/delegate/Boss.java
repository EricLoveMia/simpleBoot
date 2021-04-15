package cn.eric.h2.interview.mode.delegate;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName Boss
 * @Description: TODO
 * @Author YCKJ2725
 * @Date 2021/4/15
 * @Version V1.0
 **/
public class Boss {
    public void startWork(String taskCommand, Manager manager) {
        manager.work(taskCommand);
    }

    public static void main(String[] args) {
        List<IEmployee> employees = new ArrayList<>();
        employees.add(new EmployeeA());
        employees.add(new EmployeeB());
        new Boss().startWork("部署项目", new Manager(employees));
    }
}
